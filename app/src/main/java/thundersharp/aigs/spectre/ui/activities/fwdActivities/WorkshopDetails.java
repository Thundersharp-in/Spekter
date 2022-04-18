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
import thundersharp.aigs.spectre.core.adapters.WorkshopFileHolderAdapter;
import thundersharp.aigs.spectre.core.models.WorkshopFiles;
import thundersharp.aigs.spectre.core.models.Workshops;

public class WorkshopDetails extends AppCompatActivity {

    private Workshops workshops;
    ChipGroup highlights;
    RecyclerView details, extras, files;

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
        files = findViewById(R.id.files);
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));


        for (String s:getData()) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip, highlights, false);
            chip.setText(s);
            highlights.addView(chip);
        }


        details.setAdapter(new WorkShopDetailsAdapter(getDetails(),this));
        extras.setAdapter(new WorkShopDetailsAdapter(getExtras(),this));
        files.setAdapter(new WorkshopFileHolderAdapter(getFileList(),this));
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

    private List<WorkshopFiles> getFileList(){
        List<WorkshopFiles> files = new ArrayList<>();
        files.add(new WorkshopFiles("12345","https://res-3.cloudinary.com/fieldfisher/image/upload/c_lfill,g_auto/f_auto,q_auto/v1/sectors/technology/tech_neoncircuitboard_857021704_medium_lc5h05","IOT file","Should be stable, neat and clean, and suited to the objects on display"));
        files.add(new WorkshopFiles("12345","https://res-2.cloudinary.com/fieldfisher/image/upload/c_lfill,dpr_1,g_auto,h_470,w_760/f_auto,q_auto/v1/sectors/technology/tech_silhouette-woman-globe_889231052_medium_ifjvbc","IOT file","Can be a good way of influencing people traffic"));
        files.add(new WorkshopFiles("12345","https://res-4.cloudinary.com/fieldfisher/image/upload/c_lfill,dpr_1,g_auto,h_340,w_280/f_auto,q_auto/v1/pdfs/codes_of_practice_on_network_security_under_the_telecoms_security_bill_oanba2","IOT file","Should be insect and rodent proof, and lit so as not to cast the exhibits in shadow"));
        files.add(new WorkshopFiles("12345","https://ak.picdn.net/shutterstock/videos/9597380/thumb/1.jpg","IOT file","Case lining can be fabric, but avoid wool."));
        files.add(new WorkshopFiles("12345","https://economictimes.indiatimes.com/thumb/msid-70906384,width-1200,height-900,resizemode-4,imgsize-743396/quantum-computing.jpg?from=mdr","IOT file","If painting use low VOC water based paints, and air the cases for several weeks."));
        files.add(new WorkshopFiles("12345","","IOT file","A heavy weight in the base of a plinth can ensure it is not top heavy."));
        files.add(new WorkshopFiles("12345","","IOT file","Mylar (inert plastic) can be cut to size to provide a barrier underneath sensitive display items, to protect them from dye transfer or other damage from fabric or painted surfaces"));
        files.add(new WorkshopFiles("12345","","IOT file","Items can be secured using wire wrapped in silicone tubing, or with museum wax (for example at the bottom of a vase)."));
        files.add(new WorkshopFiles("12345","","IOT file","Title panel – name of exhibition, can be something catchy followed by actual description, e.g. Firebrands, WWI Anti-Conscriptionists in Marlborough. The title sets the tone through words, colour and font. Approx. 400pt (10cm high)"));
        files.add(new WorkshopFiles("12345","","IOT file","Extended labels – presents story/context for highlighted items as well as explaining what an item is. Approx. 24pt, 50-100 words\n" +
                "Object labels - explains what a particular item is and when it was made. Approx. 18-22 pt., 15-25 words"));
        return files;
    }
}