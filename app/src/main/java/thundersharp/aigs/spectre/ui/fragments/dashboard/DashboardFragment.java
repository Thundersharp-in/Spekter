package thundersharp.aigs.spectre.ui.fragments.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.databinding.FragmentDashboardBinding;


public class DashboardFragment extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}