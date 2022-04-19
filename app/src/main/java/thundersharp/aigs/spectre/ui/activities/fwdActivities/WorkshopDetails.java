package thundersharp.aigs.spectre.ui.activities.fwdActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.newsletter.core.utils.TimeUtils;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.WorkShopDetailsAdapter;
import thundersharp.aigs.spectre.core.adapters.WorkshopFileHolderAdapter;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.interfaces.WorkshopDattaLoader;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.models.RELATED_FILES;
import thundersharp.aigs.spectre.core.models.WorkshopDetail;
import thundersharp.aigs.spectre.core.models.Workshops;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.Progressbars;
import thundersharp.aigs.spectre.ui.activities.auth.IntroActivity;
import thundersharp.aigs.spectre.ui.activities.exhibition.ProjectsInfo;

public class WorkshopDetails extends AppCompatActivity {

    private Workshops workshops;
    private ChipGroup highlights;
    private RecyclerView details, extras, files;
    private android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop_details);
        workshops = (Workshops) getIntent().getSerializableExtra("workshop_info");
        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);

        if (workshops == null){
            finish();
            Toast.makeText(this, "Internal error.", Toast.LENGTH_SHORT).show();
        }

        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(view -> finish());
        ((ImageView)findViewById(R.id.share)).setOnClickListener(n->{
            generateShareLink(workshops);
        });
        Glide.with(this).load(workshops.COVER).into((ImageView)findViewById(R.id.topImage));
        TextView textView = findViewById(R.id.tittle);
        TextView by = findViewById(R.id.by);
        TextView date = findViewById(R.id.month);
        TextView day = findViewById(R.id.date);
        TextView timeFull = findViewById(R.id.time_full);
        TextView event_start = findViewById(R.id.event_start);

        try {
            timeFull.setText(TimeUtils.getTimeInStringFromTimeStamp(workshops.ID));
            event_start.setText("Starts from : "+TimeUtils.getClockFromTimeStamp(workshops.ID));
            day.setText(TimeUtils.getDayFromTimeStamp(workshops.ID));
            date.setText(TimeUtils.getMonthName(Integer.parseInt(TimeUtils.getMonthFromTimeStamp(workshops.ID))));
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        textView.setText(workshops.TITTLE);
        by.setText("By "+workshops.ORGANISED_BY.trim());

        highlights = findViewById(R.id.heighlights);
        details = findViewById(R.id.details);
        extras = findViewById(R.id.extras);
        files = findViewById(R.id.files);

        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        alertDialog.show();
        DatabaseHelpers
                .getInstance()
                .setWorkshopId(workshops.ID)
                .setWorkshopDattaLoader(new WorkshopDattaLoader() {
                    @Override
                    public void onDataFetchSuccess(WorkshopDetail workshopDetails) {
                        updateUI(workshopDetails);
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onDataError(Exception e) {
                        Toast.makeText(WorkshopDetails.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }
                });


    }

    private void updateUI(WorkshopDetail workshopDetails) {
        String[] detailsO = workshopDetails.DETAILS.split("\\.");
        String[] extrasD = workshopDetails.MORE.split("\\.");
        String[] chipD = workshopDetails.HIGHLIGHTS.split("\\.");

        for (String s:chipD) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip, highlights, false);
            chip.setText(s);
            highlights.addView(chip);
        }

        ((TextView)findViewById(R.id.loc)).setText(workshopDetails.LOCATION);
        ((TextView)findViewById(R.id.price)).setText("₹ "+workshopDetails.PRICE);
        details.setAdapter(new WorkShopDetailsAdapter(detailsO,this));
        extras.setAdapter(new WorkShopDetailsAdapter(extrasD,this));

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.WORKSHOPS)
                .child("WORKSHOP_FILES")
                .child(workshopDetails.ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<RELATED_FILES> related_files = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                related_files.add(dataSnapshot.getValue(RELATED_FILES.class));
                            }
                            files.setAdapter(new WorkshopFileHolderAdapter(related_files,WorkshopDetails.this));
                        }else
                            Toast.makeText(WorkshopDetails.this, "Files not found.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(WorkshopDetails.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        findViewById(R.id.register).setOnClickListener(n->{
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Payment for this workshop (If required) will be collected later after submission of this form.")
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                                alertDialog.show();
                                dialogInterface.dismiss();
                                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                builder.setToolbarColor(Color.parseColor("#262626"));
                                CustomTabsIntent customTabsIntent = builder.build();
                                customTabsIntent.launchUrl(WorkshopDetails.this, Uri.parse(workshopDetails.REG_LINK));
                                alertDialog.dismiss();
                            }else{
                                Toast.makeText(WorkshopDetails.this, "Please log in ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(WorkshopDetails.this, IntroActivity.class));
                            }

                        }
                    }).show();

        });

    }

    protected synchronized void generateShareLink(Workshops projectsInfo) {
        String url = "https://spekteraigs.page.link/workshops/?workshopId="+workshops.ID+"&type%1";
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(url))
                .setDomainUriPrefix("https://spekteraigs.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT,"Hey,\n\nLook at this awesome and Informative workshop which is hosted by "+workshops.ORGANISED_BY+
                                    "\n\nWorkshop name : "+projectsInfo.TITTLE+
                                    "\nOrganiser : "+projectsInfo.ORGANISED_BY+
                                    "\nDuration : "+projectsInfo.DURATION+
                                    "\nMode : "+projectsInfo.MODE+
                                    "\n\nTo view more about this workshop and to register download the Spekter app."+
                                    "\nWorkshop link : "+shortLink+
                                    "\n\nSpekter App link : https://play.google.com/store/apps/details?id=thundersharp.aigs.spectre");
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        } else {
                            Toast.makeText(WorkshopDetails.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            // Error
                            // ...
                        }
                    }
                });

    }

}