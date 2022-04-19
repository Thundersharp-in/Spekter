package thundersharp.aigs.spectre.ui.activities.fwdActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import thundersharp.aigs.spectre.core.adapters.CompetitionFileHolderAdapter;
import thundersharp.aigs.spectre.core.adapters.WorkShopDetailsAdapter;
import thundersharp.aigs.spectre.core.models.CompetitionFiles;
import thundersharp.aigs.spectre.core.models.Competitions;

public class CompetitionDetail extends AppCompatActivity {

    private Competitions workshops;
    ChipGroup highlights;
    RecyclerView details, extras, files;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_detail);

        workshops = (Competitions) getIntent().getSerializableExtra("workshop_info");

        if (workshops == null){
            finish();
            Toast.makeText(this, "Internal error.", Toast.LENGTH_SHORT).show();
        }

        ((Toolbar)findViewById(R.id.toolbar)).setOnClickListener(view -> finish());
        Glide.with(this).load(workshops.COVER).into((ImageView)findViewById(R.id.topImage));

        highlights = findViewById(R.id.heighlights);
        details = findViewById(R.id.details);
        extras = findViewById(R.id.extras);
        files = findViewById(R.id.files);
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));


        for (String s:getData()) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip, highlights, false);
            chip.setText(s);
            highlights.addView(chip);
        }


        details.setAdapter(new WorkShopDetailsAdapter(getDetails(),this));
        extras.setAdapter(new WorkShopDetailsAdapter(getExtras(),this));
        files.setAdapter(new CompetitionFileHolderAdapter(getFileList(),this));
    }

    private String[] getDetails(){
        return new String[]{"On-Line Group leader trainings are highly interactive, dynamic small-group training experiences (no more than 15 participants per training).",
                "As more group leaders are exploring how to deliver groups and individual services to families and children online during the Covid-19",
                "Hands on to real world projects",
                " Home workshops typically contain a workbench, hand tools, power tools and other hardware. ",
                "The organisation and contents of laboratories are determined by the differing requirements of the specialists working within. ",
                "Joyful"};
    }

    private String[] getExtras(){
        return new String[]{"Project Discussion",
                "Have an eye to catch the amazing moments and a mind to turn a waste free world with the following events.",
                "Hands on to real world projects",
                "Teamwork",
                "CORONA The annual technical fest of NIT Patna has become one of the biggest technical fest of the country.",
                "Creativity is a wild mind and a disciplined eye. It is an art to explore your ideas to extraordinary way from the ordinary work."};
    }

    private List<String> getData(){
        return new ArrayList<>(Arrays.asList("Project Discussion","Interactive learning","Hands on to real world projects","Teamwork","Stability","Joyful"));
    }

    private List<CompetitionFiles> getFileList(){
        List<CompetitionFiles> files = new ArrayList<>();
        files.add(new CompetitionFiles("12345","https://docs.google.com/uc?export=download&id=0BxyMs1jY42NLd2RFSk51TXBRRzQ","Circular","Should be stable, neat and clean, and suited to the objects on display"));
        files.add(new CompetitionFiles("12345","https://res-2.cloudinary.com/fieldfisher/image/upload/c_lfill,dpr_1,g_auto,h_470,w_760/f_auto,q_auto/v1/sectors/technology/tech_silhouette-woman-globe_889231052_medium_ifjvbc","Time Table","Can be a good way of influencing people traffic"));
        files.add(new CompetitionFiles("12345","https://res-4.cloudinary.com/fieldfisher/image/upload/c_lfill,dpr_1,g_auto,h_340,w_280/f_auto,q_auto/v1/pdfs/codes_of_practice_on_network_security_under_the_telecoms_security_bill_oanba2","Study materials","Should be insect and rodent proof, and lit so as not to cast the exhibits in shadow"));
        files.add(new CompetitionFiles("12345","https://ak.picdn.net/shutterstock/videos/9597380/thumb/1.jpg","Rules and Regulations","Case lining can be fabric, but avoid wool."));

        return files;
    }
}