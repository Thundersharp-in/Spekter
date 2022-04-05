package thundersharp.aigs.spectre.core.models;

import java.io.Serializable;

public class ProfileData implements Serializable {

    public ProfileData(){}

    public String FCM_TOKEN;
    public boolean acharyan;
    public String email;
    public String name;
    public String phone;
    public String uid;

    public ProfileData(String FCM_TOKEN, boolean acharyan, String email, String name, String phone, String uid) {
        this.FCM_TOKEN = FCM_TOKEN;
        this.acharyan = acharyan;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.uid = uid;
    }
}
