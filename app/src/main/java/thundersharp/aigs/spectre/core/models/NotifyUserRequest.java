package thundersharp.aigs.spectre.core.models;

import android.content.Context;

public class NotifyUserRequest {

    public String fcm_token;
    public Context context;
    public String tittle;
    public String message;

    public NotifyUserRequest(String fcm_token, Context context, String tittle, String message) {
        this.fcm_token = fcm_token;
        this.context = context;
        this.tittle = tittle;
        this.message = message;
    }

    public String getFcm_token() {
        return fcm_token;
    }

    public String getTittle() {
        return tittle;
    }


    public String getMessage() {
        return message;
    }


    public void setFcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public Context getContext() {
        return context;
    }
}
