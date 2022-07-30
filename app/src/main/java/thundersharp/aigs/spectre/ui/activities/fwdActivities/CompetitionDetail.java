package thundersharp.aigs.spectre.ui.activities.fwdActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.List;

import thundersharp.aigs.newsletter.core.utils.TimeUtils;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.CompetitionFileHolderAdapter;
import thundersharp.aigs.spectre.core.adapters.WorkShopDetailsAdapter;
import thundersharp.aigs.spectre.core.adapters.WorkshopFileHolderAdapter;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.interfaces.GeneralEventsLoader;
import thundersharp.aigs.spectre.core.models.CompetitionFiles;
import thundersharp.aigs.spectre.core.models.Competitions;
import thundersharp.aigs.spectre.core.models.GeneralEventsDetails;
import thundersharp.aigs.spectre.core.models.RELATED_FILES;
import thundersharp.aigs.spectre.core.models.WorkshopDetail;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.Progressbars;
import thundersharp.aigs.spectre.ui.activities.auth.IntroActivity;

public class CompetitionDetail extends AppCompatActivity {

    private Competitions GENERAL_EVENTS;
    ChipGroup highlights;
    RecyclerView details, extras, files;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_competition_detail);
        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);
        alertDialog.show();

        GENERAL_EVENTS = (Competitions) getIntent().getSerializableExtra(CONSTANTS.GENERAL_EVENTS_INFO);

        if (GENERAL_EVENTS == null){
            finish();
            Toast.makeText(this, "Internal error.", Toast.LENGTH_SHORT).show();
        }

        ((Toolbar)findViewById(R.id.toolbar)).setOnClickListener(view -> finish());
        Glide.with(this).load(GENERAL_EVENTS.COVER).into((ImageView)findViewById(R.id.topImage));

        highlights = findViewById(R.id.heighlights);
        details = findViewById(R.id.details);
        extras = findViewById(R.id.extras);
        files = findViewById(R.id.files);
        //recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        TextView textView = findViewById(R.id.tittle);
        TextView by = findViewById(R.id.by);
        TextView date = findViewById(R.id.month);
        TextView day = findViewById(R.id.date);
        TextView timeFull = findViewById(R.id.time_full);
        TextView event_start = findViewById(R.id.event_start);

        findViewById(R.id.share).setOnClickListener(t->{
            generateShareLink(GENERAL_EVENTS);
        });

        try {
            timeFull.setText(TimeUtils.getTimeInStringFromTimeStamp(GENERAL_EVENTS.ID));
            event_start.setText("Starts from : "+TimeUtils.getClockFromTimeStamp(GENERAL_EVENTS.ID));
            day.setText(TimeUtils.getDayFromTimeStamp(GENERAL_EVENTS.ID));
            date.setText(TimeUtils.getMonthNameSecond(Integer.parseInt(TimeUtils.getMonthFromTimeStamp(GENERAL_EVENTS.ID))));
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        textView.setText(GENERAL_EVENTS.TITTLE);
        by.setText("By "+GENERAL_EVENTS.ORGANISED_BY.trim());

        DatabaseHelpers
                .getInstance()
                .setEventId(GENERAL_EVENTS.ID)
                .fetchGeneralEvents(new GeneralEventsLoader() {
                    @Override
                    public void onGeneralEventsFetchSuccess(GeneralEventsDetails workshopDetails) {
                        updateUI(workshopDetails);
                        alertDialog.dismiss();
                    }

                    @Override
                    public void onDataError(Exception e) {
                        alertDialog.dismiss();
                        Toast.makeText(CompetitionDetail.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void generateShareLink(Competitions general_events) {
        String url = "https://spekteraigs.page.link/general_events/?eventId="+general_events.ID+"&type%1";
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(url))
                .setDomainUriPrefix("https://spekteraigs.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .buildShortDynamicLink()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Short link created
                        Uri shortLink = task.getResult().getShortLink();
                        Uri flowchartLink = task.getResult().getPreviewLink();

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,"Hey,\n\nLook at this awesome event which is hosted by "+general_events.ORGANISED_BY+
                                "\n\nWorkshop name : "+general_events.TITTLE+
                                "\nOrganiser : "+general_events.ORGANISED_BY+
                                "\nDuration : "+general_events.DURATION+
                                "\nMode : "+general_events.MODE+
                                "\n\nTo view more about this event and to register download the Spekter app."+
                                "\nWorkshop link : "+shortLink+
                                "\n\nSpekter App link : https://play.google.com/store/apps/details?id=thundersharp.aigs.spectre");
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    } else {
                        Toast.makeText(CompetitionDetail.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        // Error
                        // ...
                    }
                });
    }

    private void updateUI(GeneralEventsDetails workshopDetails) {
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
                .getReference(CONSTANTS.GENERAL_EVENTS)
                .child("GENERAL_EVENTS_FILES")
                .child(workshopDetails.ID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<RELATED_FILES> related_files = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                related_files.add(dataSnapshot.getValue(RELATED_FILES.class));
                            }
                            files.setAdapter(new WorkshopFileHolderAdapter(related_files,CompetitionDetail.this));
                        }else
                            Toast.makeText(CompetitionDetail.this, "Files not found.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CompetitionDetail.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        findViewById(R.id.register).setOnClickListener(n->{
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("Payment for this event (If required) will be collected later after submission of this form.")
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
                                customTabsIntent.launchUrl(CompetitionDetail.this, Uri.parse(workshopDetails.REG_LINK));
                                alertDialog.dismiss();
                            }else{
                                Toast.makeText(CompetitionDetail.this, "Please log in ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CompetitionDetail.this, IntroActivity.class));
                            }

                        }
                    }).show();

        });

    }


}