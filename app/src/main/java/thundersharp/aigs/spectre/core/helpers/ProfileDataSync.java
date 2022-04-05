package thundersharp.aigs.spectre.core.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import thundersharp.aigs.spectre.core.interfaces.ProfileSync;
import thundersharp.aigs.spectre.core.models.ProfileData;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class ProfileDataSync {

    public ProfileDataSync(Activity activity){
        this.activity = activity;
    }

    private Activity activity;
    private ProfileSync profileSync;
    private SharedPreferences sharedPreferences;

    private String uid;

    private static ProfileDataSync profileDataSync;

    public static ProfileDataSync getInstance(Activity activity){
        if (profileDataSync == null) profileDataSync = new ProfileDataSync(activity);
        return profileDataSync;
    }

    public ProfileDataSync setProfileSyncListener(ProfileSync profileSyncListener){
        this.profileSync = profileSyncListener;
        fetchProfileData();
        return this;
    }

    public ProfileDataSync setUid(String uid){
        this.uid = uid;
        return this;
    }

    public ProfileDataSync initializeLocalStorage(){
        sharedPreferences = activity.getSharedPreferences(CONSTANTS.SHARED_PREF_PROFILE,Context.MODE_PRIVATE);
        return this;
    }

    public ProfileDataSync saveDataLocally(ProfileData profileData){
        if (sharedPreferences == null)
            Toast.makeText(activity, "Local storage not initialized, Have you called saveDataLocally() sAV? ", Toast.LENGTH_SHORT).show();
        else {
            SharedPreferences.Editor profileDataEditor = sharedPreferences.edit();
            profileDataEditor.clear();
            profileDataEditor.putBoolean("acharyan", profileData.acharyan);
            profileDataEditor.putString("FCM_TOKEN", profileData.FCM_TOKEN);
            profileDataEditor.putString("name", profileData.name);
            profileDataEditor.putString("email", profileData.email);
            profileDataEditor.putString("phone", profileData.phone);
            profileDataEditor.putString("uid",profileData.uid);
            profileDataEditor.apply();
        }
        return this;
    }

    public ProfileData pullDataBack(){
        return new ProfileData(sharedPreferences.getString("FCM_TOKEN",null),
                sharedPreferences.getBoolean("acharyan",false),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("phone",null),
                sharedPreferences.getString("uid",null));
    }

    public boolean doDataExists(){
        if (sharedPreferences == null) {
            Toast.makeText(activity, "Local storage not initialized, Have you called saveDataLocally() ?", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (sharedPreferences.getString("name",null) == null)
                return false;
            else return true;
        }
    }

    private void fetchProfileData(){
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NODE_ALL_USERS)
                .child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) profileSync.onProfileDataSyncSuccess(snapshot);
                        else profileSync.onProfileDataSyncFailure(new Exception("Data unavailable"));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        profileSync.onProfileDataSyncFailure(error.toException());
                    }
                });
    }
}
