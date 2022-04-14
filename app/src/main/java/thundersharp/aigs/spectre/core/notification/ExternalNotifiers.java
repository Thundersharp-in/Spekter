package thundersharp.aigs.spectre.core.notification;

import android.os.AsyncTask;
import android.widget.Toast;

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

import thundersharp.aigs.spectre.core.models.NotifyUserRequest;

public class ExternalNotifiers extends AsyncTask<NotifyUserRequest , Integer, String>{

    public ExternalNotifiers(){}

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public String doInBackground(NotifyUserRequest... notifyUserRequests) {
        NotifyUserRequest notifyUserRequest = notifyUserRequests[0];
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(notifyUserRequest.getContext());
            String URL = "https://spekter-aigs.herokuapp.com/notifyUser";
            JSONObject jsonBody = new JSONObject();

            jsonBody.put("fcm_token",notifyUserRequest.getFcm_token());
            jsonBody.put("auth_token","");
            jsonBody.put("tittle",notifyUserRequest.getTittle());
            jsonBody.put("message",notifyUserRequest.getMessage());

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (!jsonObject.getBoolean("sent")){
                            onPostExecute("ERROR");
                        }
                    } catch (JSONException e) {
                        onPostExecute("ERROR "+e.getMessage());
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    onPostExecute("ERROR "+error.getMessage());
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
            Toast.makeText(notifyUserRequest.getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }


}
