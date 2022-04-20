package thundersharp.aigs.spectre.ui.activities.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import thundersharp.aigs.spectre.core.interfaces.ChallengeLoader;
import thundersharp.aigs.spectre.core.models.InovativeChallangeDetails;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.models.RELATED_FILES;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.Progressbars;
import thundersharp.aigs.spectre.ui.activities.auth.IntroActivity;

public class InnovativeChallengeHome extends AppCompatActivity {

    RelativeLayout loader,mainContents;

    private ChipGroup highlights;
    private RecyclerView extras, files;
    private android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_innovative_challenge_home);
        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);

        loader = findViewById(R.id.loader);
        mainContents = findViewById(R.id.container);
        ((Toolbar)findViewById(R.id.toolbar)).setNavigationOnClickListener(view -> finish());
        ((ImageView)findViewById(R.id.share)).setOnClickListener(n->{
            generateShareLink(null);
        });

        setPreAnimation(true);
        //alertDialog.show();
        DatabaseHelpers
                .getInstance()
                .setChallengeLoader(new ChallengeLoader() {
                    @Override
                    public void OnChallengeLoadSuccess(InovativeChallangeDetails inovativeChallangeDetails) {
                        updateUI(inovativeChallangeDetails);

                    }

                    @Override
                    public void OnLoadError(Exception e) {
                        //alertDialog.dismiss();
                        finish();
                        Toast.makeText(InnovativeChallengeHome.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void updateUI(InovativeChallangeDetails workshopDetails) {
        TextView textView = findViewById(R.id.tittle);
        TextView by = findViewById(R.id.by);
        TextView timeFull = findViewById(R.id.time_full);
        //TextView event_start = findViewById(R.id.event_start);
        Glide.with(this).load(workshopDetails.COVER).into((ImageView)findViewById(R.id.topImage));

        try {
            timeFull.setText(TimeUtils.getTimeInStringFromTimeStamp(workshopDetails.ID));
            ///event_start.setText("Starts from : "+TimeUtils.getClockFromTimeStamp(workshopDetails.ID));
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        textView.setText(workshopDetails.TITTLE);
        by.setText("By "+workshopDetails.LOCATION.trim());

        highlights = findViewById(R.id.heighlights);
        extras = findViewById(R.id.extras);
        files = findViewById(R.id.files);
        //String[] detailsO = workshopDetails..split("\\.");
        String[] extrasD = workshopDetails.RULES_MORE.split("\\.");
        String[] chipD = workshopDetails.HIGHLIGHTS.split("\\.");

        for (String s:chipD) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_chip,highlights, false);
            chip.setText(s);
            highlights.addView(chip);
        }

        ((TextView)findViewById(R.id.loc)).setText(workshopDetails.LOCATION);
        ((TextView)findViewById(R.id.price)).setText(TimeUtils.getDateFromTimeStamp(workshopDetails.SUBMISSION_DATE));
        ((TextView)findViewById(R.id.details)).setText(workshopDetails.DESCRIPTION);
        extras.setAdapter(new WorkShopDetailsAdapter(extrasD,this));

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.CHALLENGES)
                .child("FILES")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<RELATED_FILES> related_files = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                related_files.add(dataSnapshot.getValue(RELATED_FILES.class));
                            }
                            files.setAdapter(new WorkshopFileHolderAdapter(related_files, InnovativeChallengeHome.this));
                        }else
                            Toast.makeText(InnovativeChallengeHome.this, "Files not found.", Toast.LENGTH_SHORT).show();
                        setPreAnimation(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        setPreAnimation(false);
                        Toast.makeText(InnovativeChallengeHome.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        findViewById(R.id.register).setOnClickListener(n->{
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage("One account can submit a solution for the problem statement only once.")
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
                                try {
                                    if (Long.parseLong(workshopDetails.SUBMISSION_DATE) > System.currentTimeMillis()) {
                                        alertDialog.show();
                                        dialogInterface.dismiss();
                                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                                        builder.setToolbarColor(Color.parseColor("#262626"));
                                        CustomTabsIntent customTabsIntent = builder.build();
                                        customTabsIntent.launchUrl(InnovativeChallengeHome.this, Uri.parse(workshopDetails.SUBMISSION_LINK));
                                        alertDialog.dismiss();
                                    } else
                                        Toast.makeText(InnovativeChallengeHome.this, "Last date has passed", Toast.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    Toast.makeText(InnovativeChallengeHome.this, "INTERNAL ERROR ", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(InnovativeChallengeHome.this, "Please log in ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(InnovativeChallengeHome.this, IntroActivity.class));
                            }

                        }
                    }).show();

        });

    }

    protected synchronized void generateShareLink(ProjectBasicInfo projectsInfo) {
        String url = "https://spekteraigs.page.link/ic/";
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
                            sendIntent.putExtra(Intent.EXTRA_TEXT,"Hey,\n\nLook at this awesome Innovative challenge which is given by Spekter"+

                                    "\n\nTo view more about this challenge and to register download the Spekter app."+
                                    "\nWorkshop link : "+shortLink+
                                    "\n\nSpekter App link : https://play.google.com/store/apps/details?id=thundersharp.aigs.spectre");
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        } else {
                            Toast.makeText(InnovativeChallengeHome.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            // Error
                            // ...
                        }
                    }
                });

    }

    private synchronized void setPreAnimation(boolean animation) {
        if (animation) {
            mainContents.setVisibility(View.GONE);
            loader.setVisibility(View.VISIBLE);
        } else {
            mainContents.setVisibility(View.VISIBLE);
            loader.setVisibility(View.GONE);
        }
    }
}