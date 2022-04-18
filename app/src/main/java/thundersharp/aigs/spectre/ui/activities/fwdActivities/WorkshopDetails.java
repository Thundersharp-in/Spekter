package thundersharp.aigs.spectre.ui.activities.fwdActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.WorkShopHeilightsAdapter;
import thundersharp.aigs.spectre.core.models.Workshops;

public class WorkshopDetails extends AppCompatActivity {

    private Workshops workshops;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_details);
        workshops = (Workshops) getIntent().getSerializableExtra("workshop_info");

        if (workshops == null){
            finish();
            Toast.makeText(this, "Internal error.", Toast.LENGTH_SHORT).show();
        }

        Glide.with(this).load(workshops.COVER).into((ImageView)findViewById(R.id.topImage));

        recyclerView = findViewById(R.id.heighlights);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(new WorkShopHeilightsAdapter(getData()));
    }

    private List<String> getData(){
        return new ArrayList<>(Arrays.asList("Project Discussion","Interactive learning","Hands on to real world projects","Teamwork","Stability","Joyful"));
    }
}