package thundersharp.aigs.spectre.ui.activities.fwdActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.WorkShopDetailsAdapter;
import thundersharp.aigs.spectre.core.models.Workshops;

public class WorkshopDetails extends AppCompatActivity {

    private Workshops workshops;
    ChipGroup highlights;
    RecyclerView details, extras;

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

        highlights = findViewById(R.id.heighlights);
        details = findViewById(R.id.details);
        extras = findViewById(R.id.extras);
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));


        for (String s:getData()) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip, highlights, false);
            chip.setText(s);
            highlights.addView(chip);
        }


        details.setAdapter(new WorkShopDetailsAdapter(getDetails(),this));
        extras.setAdapter(new WorkShopDetailsAdapter(getExtras(),this));
    }

    private List<String> getDetails(){
        return new ArrayList<>(Arrays.asList("Project Discussion","Interactive learning","Hands on to real world projects","Teamwork","Stability","Joyful"));
    }

    private List<String> getExtras(){
        return new ArrayList<>(Arrays.asList("Project Discussion","Interactive learning","Hands on to real world projects","Teamwork","Stability\nAnd Durability","Joyful"));
    }

    private List<String> getData(){
        return new ArrayList<>(Arrays.asList("Project Discussion","Interactive learning","Hands on to real world projects","Teamwork","Stability","Joyful"));
    }
}