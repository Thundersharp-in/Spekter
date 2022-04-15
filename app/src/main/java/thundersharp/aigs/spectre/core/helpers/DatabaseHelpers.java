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

import thundersharp.aigs.spectre.core.interfaces.ExhibitionInterface;
import thundersharp.aigs.spectre.core.interfaces.FeedbackObserver;
import thundersharp.aigs.spectre.core.interfaces.ProjectListner;
import thundersharp.aigs.spectre.core.models.FacultyFeedback;
import thundersharp.aigs.spectre.core.models.Participants;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.models.StudentsDetails;
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

    private Activity activity;

    private FeedbackObserver feedbackObserver;
    private FacultyFeedback facultyFeedback;
    private StudentsDetails studentsDetails;

    /**
     *
     */
    private long projectId;

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

    public DatabaseHelpers setStudentDetails(StudentsDetails studentsDetails){
        this.studentsDetails = studentsDetails;
        return this;
    }

    public DatabaseHelpers setFacultyFeedback(FacultyFeedback facultyFeedback){
        this.facultyFeedback = facultyFeedback;
        return this;
    }

    public DatabaseHelpers setActivity(Activity activity){
        this.activity = activity;
        return this;
    }

    public void setFeedbackObserver(FeedbackObserver feedbackObserver){
        this.feedbackObserver = feedbackObserver;
        updateToServerver(feedbackObserver,studentsDetails,facultyFeedback);
    }

    private void updateToServerver(FeedbackObserver feedbackObserver, StudentsDetails studentsDetails,FacultyFeedback facultyFeedback) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            String URL = "https://spekter-aigs.herokuapp.com/faculty/feedback/post";
            JSONObject jsonBody = new JSONObject();

            JSONObject facFeedback = new JSONObject();
            facFeedback.put("FACULTY",facultyFeedback.FACULTY);
            facFeedback.put("FACULTY_RATING",facultyFeedback.FACULTY_RATING);
            facFeedback.put("ID",facultyFeedback.ID);
            facFeedback.put("MESSAGE",facultyFeedback.MESSAGE);
            facFeedback.put("SUBJECT",facultyFeedback.SUBJECT);
            facFeedback.put("SEMESTER",facultyFeedback.SEMESTER);

            JSONObject stuFeedback = new JSONObject();
            stuFeedback.put("EMAIL",studentsDetails.EMAIL);
            stuFeedback.put("ID",studentsDetails.ID);
            stuFeedback.put("NAME",studentsDetails.NAME);
            stuFeedback.put("PHONE",studentsDetails.PHONE);


            jsonBody.put("facultyFeedback",facFeedback);
            jsonBody.put("studentsDetails",stuFeedback);

            final String requestBody = jsonBody.toString();
            //Toast.makeText(activity,requestBody, Toast.LENGTH_LONG).show();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getBoolean("status")){
                            feedbackObserver.OnFeedbackSent(jsonObject);
                        }else feedbackObserver.OnError(new Exception("ERROR IN SETTING DATA TO EXTERNAL SERVERS"));
                    } catch (JSONException e) {
                        feedbackObserver.OnError(e);
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    feedbackObserver.OnError(error);
                }
            }){

                @Override
                public byte[] getBody() {
                    return requestBody.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                public String getPostBodyContentType() {
                    return "application/json;charset=UTF-8";
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String,String> hashMap = new HashMap<>();

                    hashMap.put("Content-Type","application/json;charset=UTF-8");
                    return hashMap;
                }
            };
            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
            feedbackObserver.OnError(e);
            Toast.makeText(activity, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setFetchParticipantsListeners(ProjectListner.fetchParticipants participantsListeners){
        this.fetchParticipants = participantsListeners;
        fetchParticipantsNow();
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
