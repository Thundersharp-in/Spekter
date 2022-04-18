package thundersharp.aigs.spectre.core.helpers;

import android.app.Activity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import thundersharp.aigs.spectre.core.interfaces.EventFeedbackObserver;
import thundersharp.aigs.spectre.core.interfaces.FeedbackObserver;
import thundersharp.aigs.spectre.core.models.EventFeedbacks;
import thundersharp.aigs.spectre.core.models.FacultyFeedback;
import thundersharp.aigs.spectre.core.models.StudentsDetails;

public class ExternalServerHelpers {

    private static ExternalServerHelpers externalServerHelpers = null;
    private FeedbackObserver feedbackObserver;
    private FacultyFeedback facultyFeedback;
    private StudentsDetails studentsDetails;

    private EventFeedbackObserver eventFeedbackObserver;
    private EventFeedbacks eventFeedbacks;

    private Activity activity;

    public static ExternalServerHelpers main(){
        if (externalServerHelpers == null) externalServerHelpers = new ExternalServerHelpers();
        return externalServerHelpers;
    }

    public ExternalServerHelpers(){}

    public void setFeedbackObserver(FeedbackObserver feedbackObserver){
        this.feedbackObserver = feedbackObserver;
        updateToServerver(feedbackObserver,studentsDetails,facultyFeedback);
    }

    public void setEventFeedbackObserver(EventFeedbackObserver eventFeedbackObserver){
        this.eventFeedbackObserver = eventFeedbackObserver;
        updateEventFeedbackToServer(studentsDetails, eventFeedbacks);
    }

    public ExternalServerHelpers setStudentDetails(StudentsDetails studentsDetails){
        this.studentsDetails = studentsDetails;
        return this;
    }

    public ExternalServerHelpers setFacultyFeedback(FacultyFeedback facultyFeedback){
        this.facultyFeedback = facultyFeedback;
        return this;
    }

    public ExternalServerHelpers setEventFeedback(EventFeedbacks eventFeedbacks){
        this.eventFeedbacks = eventFeedbacks;
        return this;
    }

    public ExternalServerHelpers setActivity(Activity activity){
        this.activity = activity;
        return this;
    }

    private void updateToServerver(FeedbackObserver feedbackObserver, StudentsDetails studentsDetails, FacultyFeedback facultyFeedback) {
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
                    feedbackObserver.OnError(new Exception(error.networkResponse.statusCode+""));
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
        } catch (Exception e) {
            e.printStackTrace();
            feedbackObserver.OnError(e);
        }
    }


    private void updateEventFeedbackToServer(StudentsDetails studentsDetails, EventFeedbacks facultyFeedback) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(activity);
            String URL = "https://spekter-aigs.herokuapp.com/event/feedback/post";
            JSONObject jsonBody = new JSONObject();

            JSONObject facFeedback = new JSONObject();
            facFeedback.put("EVENT",facultyFeedback.EVENT);
            facFeedback.put("EVENT_RATING",facultyFeedback.EVENT_RATING);
            facFeedback.put("ID",facultyFeedback.ID);
            facFeedback.put("MESSAGE",facultyFeedback.MESSAGE);
            facFeedback.put("DATE",facultyFeedback.DATE);
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
                            eventFeedbackObserver.OnFeedbackSent(jsonObject);
                        }else eventFeedbackObserver.OnError(new Exception("ERROR IN SETTING DATA TO EXTERNAL SERVERS"));
                    } catch (JSONException e) {
                        eventFeedbackObserver.OnError(e);
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    eventFeedbackObserver.OnError(new Exception(error.networkResponse.statusCode+""));
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
        } catch (Exception e) {
            e.printStackTrace();
            eventFeedbackObserver.OnError(e);
        }

    }


}
