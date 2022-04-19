package thundersharp.aigs.spectre.core.helpers;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import thundersharp.aigs.spectre.core.interfaces.ChallengeLoader;
import thundersharp.aigs.spectre.core.interfaces.ExhibitionInterface;
import thundersharp.aigs.spectre.core.interfaces.FeedbackObserver;
import thundersharp.aigs.spectre.core.interfaces.OnCompetitionFetchSuccess;
import thundersharp.aigs.spectre.core.interfaces.OnWorkshopFetchSuccess;
import thundersharp.aigs.spectre.core.interfaces.ProjectListner;
import thundersharp.aigs.spectre.core.interfaces.WorkshopDattaLoader;
import thundersharp.aigs.spectre.core.models.Competitions;
import thundersharp.aigs.spectre.core.models.FacultyFeedback;
import thundersharp.aigs.spectre.core.models.InovativeChallangeDetails;
import thundersharp.aigs.spectre.core.models.Participants;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.models.StudentsDetails;
import thundersharp.aigs.spectre.core.models.WorkshopDetail;
import thundersharp.aigs.spectre.core.models.Workshops;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

/**
 *
 */
public class DatabaseHelpers {

    /**
     *
     */
    private static DatabaseHelpers databaseHelpers = null;
    private ExhibitionInterface exhibitionInterface;
    private ProjectListner.fetchParticipants fetchParticipants;
    private OnWorkshopFetchSuccess onWorkshopFetchSuccess;
    private OnCompetitionFetchSuccess onCompetitionFetchSuccess;
    private WorkshopDattaLoader workshopDattaLoader;

    private Activity activity;


    /**
     *
     */
    private long projectId;
    private String workshopId;
    private ChallengeLoader challengeLoader;

    public static DatabaseHelpers getInstance(){
        if (databaseHelpers == null){
            databaseHelpers = new DatabaseHelpers();
        }
        return databaseHelpers;
    }

    public DatabaseHelpers setProjectId(long projectId){
        this.projectId = projectId;
        return this;
    }

    public DatabaseHelpers setWorkshopId(String workshopId){
        this.workshopId = workshopId;
        return this;
    }

    public DatabaseHelpers setActivity(Activity activity){
        this.activity = activity;
        return this;
    }

    public void setFetchParticipantsListeners(ProjectListner.fetchParticipants participantsListeners){
        this.fetchParticipants = participantsListeners;
        fetchParticipantsNow();
    }

    public void setChallengeLoader(ChallengeLoader challengeLoader){
        this.challengeLoader = challengeLoader;
        loadChallange(challengeLoader);
    }

    private void loadChallange(ChallengeLoader challengeLoader) {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.CHALLENGES)
                .child(CONSTANTS.INFO)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            challengeLoader.OnChallengeLoadSuccess(snapshot.getValue(InovativeChallangeDetails.class));
                        }else challengeLoader.OnLoadError(new Exception("No data found for workshop id "+workshopId));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        challengeLoader.OnLoadError(error.toException());
                    }
                });

    }

    public void setWorkshopDattaLoader(WorkshopDattaLoader workshopDattaLoader){
        this.workshopDattaLoader = workshopDattaLoader;
        fetchWorkShopDetails(workshopId);
    }

    private void fetchWorkShopDetails(String workshopId) {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.WORKSHOPS)
                .child(CONSTANTS.WORKSHOP_DETAILS)
                .child(workshopId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            workshopDattaLoader.onDataFetchSuccess(snapshot.getValue(WorkshopDetail.class));
                        }else workshopDattaLoader.onDataError(new Exception("No data found for workshop id "+workshopId));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        workshopDattaLoader.onDataError(error.toException());
                    }
                });

    }

    public DatabaseHelpers setOnWorkshopsFetchListener(OnWorkshopFetchSuccess onWorkshopFetchSuccess){
        this.onWorkshopFetchSuccess= onWorkshopFetchSuccess;
        loadWorkshops();
        return this;
    }

    public DatabaseHelpers setOnCompetitionFetchListener(OnCompetitionFetchSuccess onCompetitionFetchSuccess){
        this.onCompetitionFetchSuccess= onCompetitionFetchSuccess;
        loadCompetitions();
        return this;
    }

    private void loadCompetitions() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.WORKSHOPS)
                .child(CONSTANTS.WORKSHOP_INFO)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<Competitions> workshops = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                workshops.add(dataSnapshot.getValue(Competitions.class));
                            }
                            onCompetitionFetchSuccess.onFetchProjectsSuccess(workshops);

                        }else onCompetitionFetchSuccess.onFetchError(new Exception("ERROR 404 : NO DATA FOUND."));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onCompetitionFetchSuccess.onFetchError(error.toException());
                    }
                });
    }

    private void loadWorkshops() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.WORKSHOPS)
                .child(CONSTANTS.WORKSHOP_INFO)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<Workshops> workshops = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                workshops.add(dataSnapshot.getValue(Workshops.class));
                            }
                            onWorkshopFetchSuccess.onFetchProjectsSuccess(workshops);

                        }else onWorkshopFetchSuccess.onFetchError(new Exception("ERROR 404 : NO DATA FOUND."));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        onWorkshopFetchSuccess.onFetchError(error.toException());
                    }
                });
    }

    private void fetchParticipantsNow() {
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.EXHIBITION)
                .child(CONSTANTS.PROJECT_DETAILS)
                .child(String.valueOf(projectId))
                .child("PARTICIPANTS")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            List<Participants> participantsList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                participantsList.add(dataSnapshot.getValue(Participants.class));
                            }
                            fetchParticipants.onParticipantsFetchSuccess(participantsList);
                        }else fetchParticipants.onError(new Exception("Data not found."));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        fetchParticipants.onError(error.toException());
                    }
                });

    }

    public DatabaseHelpers setExhibitionListener(ExhibitionInterface exhibitionInterface){
        this.exhibitionInterface = exhibitionInterface;
        fetchExhibits();
        return this;
    }

    private void fetchExhibits(){
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.EXHIBITION)
                .child(CONSTANTS.PROJECTS)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            List<ProjectBasicInfo> projectBasicInfoList = new ArrayList<>();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                projectBasicInfoList.add(dataSnapshot.getValue(ProjectBasicInfo.class));
                            }
                            exhibitionInterface.onProjectsFetchSuccess(projectBasicInfoList);
                        }else exhibitionInterface.onProjectsFetchFailure(new Exception("No data Here !!"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        exhibitionInterface.onProjectsFetchFailure(error.toException());
                    }
                });
    }

}
