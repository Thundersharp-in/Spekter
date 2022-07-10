package thundersharp.aigs.spectre.ui.activities.exhibition;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.GalleryAdapter;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class ProjectGallery extends AppCompatActivity {

    private ProjectBasicInfo projectBasicInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_gallery);
        projectBasicInfo = (ProjectBasicInfo) getIntent().getSerializableExtra("data");
        if (projectBasicInfo == null) finish();

        RecyclerView re = findViewById(R.id.recycler);

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.EXHIBITION)
                .child(CONSTANTS.PROJECT_DETAILS)
                .child(projectBasicInfo.ID)
                .child("IMAGE_GALLARY")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<String> data = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                data.add(dataSnapshot.getValue(String.class));
                            }

                            re.setLayoutManager(new GridLayoutManager(ProjectGallery.this,3));
                            re.setAdapter(new GalleryAdapter(data,ProjectGallery.this));

                        }else
                            Toast.makeText(ProjectGallery.this, "No image data found", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(ProjectGallery.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}