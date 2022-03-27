package thundersharp.aigs.spectre.ui.fragments.lectures;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.chip.Chip;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.FilterTagRecyclerAdapter;
import thundersharp.aigs.spectre.core.adapters.ServiceItemAdapter;
import thundersharp.aigs.spectre.core.models.FilterData;
import thundersharp.aigs.spectre.core.models.ServiceItemModel;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.ui.activities.lectures.FilterActivity;

public class Courses extends Fragment {

    private RecyclerView rv_CS;
    private List<ServiceItemModel> modelList;
    private Chip filter;
    private ArrayList<String> hashes,level,languages;
    private ShimmerFrameLayout shimmer;
    private ImageView img_not_found;
    private Intent intent;
    private RecyclerView tagsh,tagsd,tagsl;
    private ServiceItemAdapter adapter;
    //public String data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_courses, container, false);
        rv_CS = root.findViewById(R.id.rv_CS);
        filter = root.findViewById(R.id.filter);
        shimmer = root.findViewById(R.id.shimmer);
        tagsh = root.findViewById(R.id.tagsh);
        tagsd = root.findViewById(R.id.diffh);
        tagsl = root.findViewById(R.id.langh);
        img_not_found = root.findViewById(R.id.img_not_found);
        img_not_found.setVisibility(View.GONE);

        hashes = new ArrayList<>();
        level = new ArrayList<>();
        languages = new ArrayList<>();

        modelList = new ArrayList<>();

        loadData();
        filter.setOnClickListener(n->{
            if (modelList.isEmpty()){
                Toast.makeText(getActivity(), "No data loaded or not found", Toast.LENGTH_SHORT).show();
            }else
            startActivityForResult(new Intent(getContext(), FilterActivity.class)
                    .putStringArrayListExtra("level",level)
                    .putStringArrayListExtra("language",languages)
                    .putStringArrayListExtra("hashes",hashes),7890);
        });

        //if (getArguments()!=null){
          //  Bundle bundle = getArguments();
            //CharSequence sequence = bundle.getCharSequence("filter_s");}
        return root;
    }

    public void applyFilter(String data) {
        if (adapter != null){
            adapter.getFilter().filter(data);
        }
    }

    private synchronized void loadData() {
        img_not_found.setVisibility(View.GONE);
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_COURSES_ALL)
                .child(CONSTANTS.DATABASE_COURSES)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            img_not_found.setVisibility(View.GONE);
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                ServiceItemModel serviceItemModel = snapshot1.getValue(ServiceItemModel.class);
                                modelList.add(serviceItemModel);
                                languages.add(serviceItemModel.LANGUAGE);
                                level.add(serviceItemModel.LEVEL);
                                String[] hash_item = serviceItemModel.HASHTAGS.split(" ");
                                List<String> list = new ArrayList<>(Arrays.asList(hash_item));
                                hashes.addAll(list);
                            }
                            adapter = new ServiceItemAdapter(modelList,getActivity(),CONSTANTS.DATABASE_COURSES);
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            rv_CS.setAdapter(adapter);
                            rv_CS.setHasFixedSize(true);
                        }else {
                            shimmer.stopShimmer();
                            shimmer.setVisibility(View.GONE);
                            img_not_found.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Not found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        shimmer.stopShimmer();
                        shimmer.setVisibility(View.GONE);
                        img_not_found.setVisibility(View.VISIBLE);

                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 7890 && resultCode == RESULT_OK){

            if (intent!=null){
                if (intent.getAction().equals("filter_data")){
                    FilterData mainDataSet = (FilterData) intent.getSerializableExtra("data");

                    /*Object[] filterData = (Object[]) intent.getSerializableExtra("tags");
                    Object[] difficulty = (Object[]) intent.getSerializableExtra("difficulty");
                    Object[] language = (Object[]) intent.getSerializableExtra("language");*/

                    if (mainDataSet.getTags() != null){
                        tagsh.setAdapter(new FilterTagRecyclerAdapter(getActivity(),mainDataSet.getTags()));

                    }

                    if (mainDataSet.getDifficulty() != null){
                        tagsd.setAdapter(new FilterTagRecyclerAdapter(getActivity(),mainDataSet.getDifficulty()));
                    }

                    if (mainDataSet.getLanguage() != null){
                        tagsl.setAdapter(new FilterTagRecyclerAdapter(getActivity(),mainDataSet.getLanguage()));
                    }

                    Filter filter = new Filter();
                    filter.doInBackground(mainDataSet);

                }
            }

        }
    }

    public class Filter extends AsyncTask<FilterData,String,List<ServiceItemModel>> {

        public Filter() {
        }

        @Override
        protected List<ServiceItemModel> doInBackground(FilterData... filterData) {

            List<String> tags = filterData[0].getTags();
            List<String> difficulty = filterData[0].getDifficulty();
            List<String> language = filterData[0].getLanguage();

            List<ServiceItemModel> modelListCopy = new ArrayList<>(modelList);
            List<ServiceItemModel> finalList = new ArrayList<>();
            int languageCounter = 0;


            if (tags!=null){

                if (!tags.isEmpty()){

                    for (ServiceItemModel serviceItemModel : modelListCopy){
                        for (String string :tags){
                            if (serviceItemModel.HASHTAGS.contains(string)){
                                if (!finalList.contains(serviceItemModel))
                                    finalList.add(serviceItemModel);
                            }
                        }
                    }

                }else {
                    finalList.addAll(modelListCopy);
                }
            }else {
                finalList.addAll(modelListCopy);
            }

            if (difficulty!=null){

                if (!difficulty.isEmpty()){

                    for (ServiceItemModel serviceItemModel : finalList){

                        for (String string : difficulty){

                            if (!serviceItemModel.LEVEL.contains(string)){
                                finalList.remove(serviceItemModel);
                            }
                        }
                    }

                }
            }

            if (language!=null){
                if (!language.isEmpty()){

                    for (ServiceItemModel serviceItemModel : finalList){

                        for (String string : language){

                            if (!serviceItemModel.LANGUAGE.contains(string)){
                                    finalList.remove(serviceItemModel);

                            }
                        }
                    }


                }
            }

            ServiceItemAdapter adapter = new ServiceItemAdapter(finalList,getActivity(),CONSTANTS.DATABASE_COURSES);
            shimmer.stopShimmer();
            shimmer.setVisibility(View.GONE);
            rv_CS.setAdapter(adapter);
            rv_CS.setHasFixedSize(true);

            return finalList;
        }

        @Override
        protected void onPostExecute(List<ServiceItemModel> objects) {
            super.onPostExecute(objects);
            Toast.makeText(getActivity(), "FFFF", Toast.LENGTH_SHORT).show();

        }
    }
}