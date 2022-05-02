package thundersharp.aigs.spectre.ui.fragments.knowUs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import thundersharp.aigs.spectre.R;


public class Commitee extends Fragment {


    public Commitee() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_commitee, container, false);

        return view;
    }
}