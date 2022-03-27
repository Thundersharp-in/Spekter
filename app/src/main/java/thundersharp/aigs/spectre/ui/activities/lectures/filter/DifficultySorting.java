package thundersharp.aigs.spectre.ui.activities.lectures.filter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;


import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.ui.activities.lectures.FilterActivity;


public class DifficultySorting extends Fragment {

    AppCompatCheckBox basic,intermidiate,advanced;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_difficulty_sorting, container, false);
        basic = view.findViewById(R.id.basic);
        intermidiate = view.findViewById(R.id.intermidiate);
        advanced = view.findViewById(R.id.advanced);


        basic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    intermidiate.setChecked(false);
                    advanced.setChecked(false);
                    FilterActivity.level_selected.clear();
                    FilterActivity.level_selected.add("Basic");
                }
            }
        });


        intermidiate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    basic.setChecked(false);
                    advanced.setChecked(false);
                    FilterActivity.level_selected.clear();
                    FilterActivity.level_selected.add("Intermediate");
                }
            }
        });


        advanced.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    intermidiate.setChecked(false);
                    basic.setChecked(false);
                    FilterActivity.level_selected.clear();
                    FilterActivity.level_selected.add("Advanced");
                }
            }
        });
        return view;
    }
}