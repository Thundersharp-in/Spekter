package thundersharp.aigs.spectre.ui.activities.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.UpcomingHolderAdapter;
import thundersharp.aigs.spectre.core.models.Upcomming;

public class UpcommingEventsHome extends AppCompatActivity {

    private RelativeLayout contents,loader;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcomming_events_home);

        contents = findViewById(R.id.contents);
        loader = findViewById(R.id.loader);
        recyclerView = findViewById(R.id.event_holder);

        findViewById(R.id.exit).setOnClickListener(k->finish());


        setPreAnimation(true);

        FirebaseDatabase
                .getInstance()
                .getReference("EVENTS")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<Upcomming> upcommings = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                upcommings.add(dataSnapshot.getValue(Upcomming.class));
                            }

                            recyclerView.setAdapter(new UpcomingHolderAdapter(upcommings,getSupportFragmentManager()));
                            setPreAnimation(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UpcommingEventsHome.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    private synchronized void setPreAnimation(boolean animation) {
        if (animation) {
            contents.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        } else {
            contents.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }
}