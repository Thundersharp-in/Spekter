package thundersharp.aigs.spectre.ui.fragments.lectures;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
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

public class FreeCources extends Fragment {

    private RecyclerView rv_CS;
    private List<ServiceItemModel> modelList;
    private Chip filter;
    private ArrayList<String> hashes,level,languages;
    private ShimmerFrameLayout shimmer;
    private ImageView img_not_found;
    private Intent intent;
    private RecyclerView tagsh,tagsd,tagsl;
    private ServiceItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_information_services, container, false);

        rv_CS = root.findViewById(R.id.rv_IS);
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
            startActivityForResult(new Intent(getContext(), FilterActivity.class)
                    .putStringArrayListExtra("level",level)
                    .putStringArrayListExtra("language",languages)
                    .putStringArrayListExtra("hashes",hashes),7890);
        });

        return root;
    }

    public void applyFilter(String data) {
        if (adapter != null){
            adapter.getFilter().filter(data);
        }
    }

    private synchronized void loadData() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_COURSES_ALL)
                .child(CONSTANTS.DATABASE_COURSES_FREE)
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
                            adapter = new ServiceItemAdapter(modelList,getActivity(),CONSTANTS.DATABASE_COURSES_FREE);
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 7890 && resultCode == RESULT_OK){

            if (intent!=null){
                if (intent.getAction().equals("filter_data")){

                    FilterData filterData = (FilterData) intent.getSerializableExtra("data");
                   /* Object[] filterData = (Object[]) intent.getSerializableExtra("tags");
                    Object[] difficulty = (Object[]) intent.getSerializableExtra("difficulty");
                    Object[] language = (Object[]) intent.getSerializableExtra("language");*/

                    if (filterData.getTags() != null){
                        tagsh.setAdapter(new FilterTagRecyclerAdapter(getActivity(),filterData.getTags()));
                    }

                    if (filterData.getDifficulty() != null){
                        tagsd.setAdapter(new FilterTagRecyclerAdapter(getActivity(),filterData.getDifficulty()));
                    }

                    if (filterData.getLanguage() != null){
                        tagsl.setAdapter(new FilterTagRecyclerAdapter(getActivity(),filterData.getLanguage()));
                    }

                }
            }

        }
    }

    public int gen() {
        Random r = new Random( System.currentTimeMillis() );
        return ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
    }
}