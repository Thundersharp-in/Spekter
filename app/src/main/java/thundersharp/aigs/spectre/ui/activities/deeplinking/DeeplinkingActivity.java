package thundersharp.aigs.spectre.ui.activities.deeplinking;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.Competitions;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.models.Workshops;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.ui.activities.exhibition.ProjectsInfo;
import thundersharp.aigs.spectre.ui.activities.fwdActivities.CompetitionDetail;
import thundersharp.aigs.spectre.ui.activities.fwdActivities.WorkshopDetails;
import thundersharp.aigs.spectre.ui.activities.home.InnovativeChallengeHome;

public class DeeplinkingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplinking);


        FirebaseDynamicLinks
                .getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();

                        if (deepLink.toString().startsWith("https://spekteraigs.page.link/projects/")) {

                            String projectId = deepLink.toString().substring(deepLink.toString().indexOf("=") + 1, deepLink.toString().indexOf("&"));
                            String actionType = deepLink.toString().substring(deepLink.toString().indexOf("%"));


                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.EXHIBITION)
                                    .child(CONSTANTS.PROJECTS)
                                    .child(projectId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                startActivity(new Intent(DeeplinkingActivity.this, ProjectsInfo.class)
                                                        .putExtra("type", true)
                                                        .putExtra("projects_basic_info", snapshot.getValue(ProjectBasicInfo.class)));
                                            } else {
                                                Toast.makeText(DeeplinkingActivity.this, "Cannot go to the project", Toast.LENGTH_SHORT).show();
                                            }
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(DeeplinkingActivity.this, "Cannot go to the project. " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                        }else if (deepLink.toString().startsWith("https://spekteraigs.page.link/workshops/")){
                            String projectId = deepLink.toString().substring(deepLink.toString().indexOf("=") + 1, deepLink.toString().indexOf("&"));

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.WORKSHOPS)
                                    .child(CONSTANTS.WORKSHOP_INFO)
                                    .child(projectId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                startActivity(new Intent(DeeplinkingActivity.this, WorkshopDetails.class)
                                                        .putExtra("workshop_info", snapshot.getValue(Workshops.class)));

                                            }else {
                                                Toast.makeText(DeeplinkingActivity.this, "Cannot go to the workshop", Toast.LENGTH_SHORT).show();
                                            }

                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(DeeplinkingActivity.this, "Cannot go to the workshop", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });


                        }else if (deepLink.toString().startsWith("https://spekteraigs.page.link/ic/")){
                            startActivity(new Intent(DeeplinkingActivity.this, InnovativeChallengeHome.class));
                            finish();
                        }else if (deepLink.toString().startsWith("https://spekteraigs.page.link/general_events/")){
                            String projectId = deepLink.toString().substring(deepLink.toString().indexOf("=") + 1, deepLink.toString().indexOf("&"));

                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.GENERAL_EVENTS)
                                    .child(CONSTANTS.GENERAL_EVENTS_INFO)
                                    .child(projectId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()){
                                                startActivity(new Intent(DeeplinkingActivity.this, CompetitionDetail.class)
                                                        .putExtra(CONSTANTS.GENERAL_EVENTS_INFO, snapshot.getValue(Competitions.class)));

                                            }else {
                                                Toast.makeText(DeeplinkingActivity.this, "Cannot go to the event", Toast.LENGTH_SHORT).show();
                                            }

                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(DeeplinkingActivity.this, "Cannot go to the workshop", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });


                        }else {
                            Toast.makeText(this, "This version of the app couldn't handle this link. ", Toast.LENGTH_SHORT).show();
                        }

                    }


                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "getDynamicLink:onFailure", e);
                    }
                });
    }
}