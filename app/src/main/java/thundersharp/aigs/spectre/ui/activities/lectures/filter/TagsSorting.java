package thundersharp.aigs.spectre.ui.activities.lectures.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.TagsSortAdapter;


public class TagsSorting extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> tags_i,tags_f;
    ImageView img_not_found;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_tags_sorting, container, false);
        tags_f = new ArrayList<>();

        Set<String> tag = new HashSet<>();
        tag.addAll(getArguments().getStringArrayList("hash"));
        tags_i = new ArrayList<>(tag);
        for (int i=0; i<tags_i.size();i++){
            tags_f.add(tags_i.get(i).replace("#",""));
        }

        img_not_found = view.findViewById(R.id.img_not_found);
        img_not_found.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (tags_f.size() >0){
            img_not_found.setVisibility(View.GONE);
            recyclerView.setAdapter(new TagsSortAdapter((List<String>) tags_f,getContext()));
        } else {
            img_not_found.setVisibility(View.VISIBLE);
        }

        return view;
    }
}