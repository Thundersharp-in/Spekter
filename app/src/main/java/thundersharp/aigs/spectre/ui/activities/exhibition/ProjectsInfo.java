package thundersharp.aigs.spectre.ui.activities.exhibition;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.ParticipantsListAdapter;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.interfaces.ProjectListner;
import thundersharp.aigs.spectre.core.models.Participants;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.progress.BrowseProgress;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.ui.activities.barcode.BarCodeScanner;

public class ProjectsInfo extends AppCompatActivity {

    private RecyclerView students_holder;
    private TextView name,project_cat,description,toolbar_name;
    private ProjectBasicInfo projectBasicInfo;
    private ImageView catHolder;
    boolean browsable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects_info);
        projectBasicInfo = (ProjectBasicInfo)getIntent().getSerializableExtra("projects_basic_info");
        browsable = getIntent().getBooleanExtra("type",false);

        if (!browsable)
            BrowseProgress.getInstance(this).setPageBrowsed(projectBasicInfo);

        findViewById(R.id.rrr).setOnClickListener(o->finish());

        students_holder = findViewById(R.id.students_holder);
        project_cat = findViewById(R.id.role);
        name = findViewById(R.id.name);
        description = findViewById(R.id.eventName);
        toolbar_name = findViewById(R.id.textName);
        catHolder = findViewById(R.id.project_type);

        findViewById(R.id.book).setOnClickListener(k->startActivity(new Intent(this, BarCodeScanner.class)));

        findViewById(R.id.share).setOnClickListener(i->{
            generateShareLink(projectBasicInfo.ID,projectBasicInfo);
        });

        if ( projectBasicInfo== null) {
            finish();
            Toast.makeText(this, "can't start activity", Toast.LENGTH_SHORT).show();
        }else {
            popagateFromBackData(projectBasicInfo);
        }

        DatabaseHelpers
                .getInstance()
                .setProjectId(Long.parseLong(projectBasicInfo.ID))
                .setFetchParticipantsListeners(new ProjectListner.fetchParticipants() {
                    @Override
                    public void onParticipantsFetchSuccess(List<Participants> participantsList) {
                        students_holder.setAdapter(new ParticipantsListAdapter(participantsList));
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(ProjectsInfo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.EXHIBITION)
                .child(CONSTANTS.PROJECT_DETAILS)
                .child(String.valueOf(projectBasicInfo.ID))
                .child("INFO")
                .child("DESCRIPTION")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) description.setText(snapshot.getValue(String.class));
                        else description.setText("Database error retry after some time again !! ERROR CODE: 459TH");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        description.setText("Database error retry after some time again !! ERROR CODE: 409TH "+error.getMessage());
                    }
                });


    }

    private void popagateFromBackData(ProjectBasicInfo projectBasicInfo) {
        if (projectBasicInfo.TYPE.equalsIgnoreCase("0")){
            catHolder.setImageResource(R.drawable.iot);
            project_cat.setText("IOT");
        }else if (projectBasicInfo.TYPE.equalsIgnoreCase("1")){
            catHolder.setImageResource(R.drawable.ai);
            project_cat.setText("ARTIFICIAL INT.");
        }else if (projectBasicInfo.TYPE.equalsIgnoreCase("2")){
            catHolder.setImageResource(R.drawable.cyber_sec);
            project_cat.setText("CYBER SECURITY");
        }else{
            catHolder.setImageResource(R.drawable.cyber_sec);
            project_cat.setText("CYBER SECURITY");
        }

        name.setText(projectBasicInfo.NAME);
        toolbar_name.setText(projectBasicInfo.NAME);

    }

    protected synchronized void generateShareLink(String projectId, ProjectBasicInfo projectsInfo) {
        String url = "https://spekteraigs.page.link/projects/?projectId="+projectId+"&type%1";
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
                            sendIntent.putExtra(Intent.EXTRA_TEXT,"Hey,\n\nLook at this awesome project which is hosted in the Spekter Tech Fest "+
                                    "\n\nProject name : "+projectsInfo.NAME+
                                    "\nDescription : "+projectsInfo.SHORT_DESCRIPTION+
                                    "\nCategory : "+projectsInfo.TYPE+
                                    "\n\nTo view more about this project download the Spekter app or visit the event and scan the stall QR codes."+
                                    "\nProject link : "+shortLink+
                                    "\n\nSpekter App link : https://play.google.com/store/apps/details?id=thundersharp.aigs.spectre");
                            sendIntent.setType("text/plain");
                            startActivity(sendIntent);
                        } else {
                            Toast.makeText(ProjectsInfo.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            // Error
                            // ...
                        }
                    }
                });

    }

}