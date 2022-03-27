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
import thundersharp.aigs.spectre.core.adapters.LanguageSortAdapter;


public class LanguageSorting extends Fragment {

    RecyclerView rv_lang;

    ImageView img_not_found;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_language_sorting, container, false);

        Set<String> lang = new HashSet<>();
        lang.addAll(getArguments().getStringArrayList("lang"));

        ArrayList<String> languages = new ArrayList<>(lang);
        img_not_found = view.findViewById(R.id.img_not_found);
        img_not_found.setVisibility(View.GONE);
        rv_lang = view.findViewById(R.id.rv_lang);
        rv_lang.setLayoutManager(new LinearLayoutManager(getContext()));
        if (languages.size() !=0){
            img_not_found.setVisibility(View.GONE);
            rv_lang.setAdapter(new LanguageSortAdapter((List<String>) languages,getContext()));
        } else img_not_found.setVisibility(View.VISIBLE);

        return view;
    }
}