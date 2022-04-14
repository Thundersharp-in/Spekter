package thundersharp.aigs.spectre.core.notification;

import android.os.AsyncTask;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import thundersharp.aigs.spectre.core.models.NotifyUserRequest;

public class NotifiAll  extends AsyncTask<NotifyUserRequest, Integer, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public String doInBackground(NotifyUserRequest... notifyUserRequests) {
        NotifyUserRequest notifyUserRequest = notifyUserRequests[0];
        RequestQueue requestQueue = Volley.newRequestQueue(notifyUserRequest.getContext());
        String URL = "https://spekter-aigs.herokuapp.com/feedback/notify_admins";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new com.android.volley.Response.Listener<String>() {
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

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("tittle", notifyUserRequest.getTittle());
                params.put("message", notifyUserRequest.getMessage());
                return params;
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
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
