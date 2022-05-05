package thundersharp.aigs.spectre.ui.fragments.knowUs;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.CommitteeListAdapter;
import thundersharp.aigs.spectre.core.models.Committee;

public class Organisers extends Fragment {

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_organisers, container, false);
        recyclerView = view.findViewById(R.id.recyclerData);

        loadOrganisers();

        return view;
    }

    private void loadOrganisers() {
        FirebaseDatabase
                .getInstance()
                .getReference("KNOW_US")
                .child("ORGANISERS")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<Committee> data = new ArrayList<>();

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                data.add(dataSnapshot.getValue(Committee.class));
                            }
                            recyclerView.setAdapter(new CommitteeListAdapter(data,getContext()));

                        }else
                            Toast.makeText(getContext(), "Data cannot be rendered!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), "ERROR : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}