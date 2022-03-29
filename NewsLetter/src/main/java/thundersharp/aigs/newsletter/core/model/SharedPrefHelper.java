package thundersharp.aigs.newsletter.core.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Type;
import java.util.List;

public class SharedPrefHelper implements SharedPrefUpdater{

    private Context context;
    private SharedPreferences sharedPreferences,namePhoneData,sharedTab;
    static SharedPreferences sharedPreferencesSettings;
    //private switchAvailabilityListener switchAvailabilityListener;

    public static SharedPrefHelper getInstanceSettings(Context context){
        sharedPreferencesSettings = context.getSharedPreferences("Settings",Context.MODE_PRIVATE);
        return new SharedPrefHelper(context);
    }


    public SharedPrefHelper(Context context) {
        this.context = context;
        sharedTab = context.getSharedPreferences("TAB_DATA",Context.MODE_PRIVATE);
        sharedPreferences = context.getSharedPreferences("Homelocation",Context.MODE_PRIVATE);
        namePhoneData = context.getSharedPreferences("NamePhoneData",Context.MODE_PRIVATE);
    }

    public void saveLastNewsData(int selection){
        SharedPreferences.Editor editor = sharedPreferencesSettings.edit();

        editor.putInt("selection_interval",selection);
        editor.apply();
    }

    public void savelastNewsTime(long timeStamp){
        SharedPreferences.Editor editor = sharedPreferencesSettings.edit();

        editor.putLong("timestamp",timeStamp);
        editor.apply();
    }


    public long getTimeOfNews(){
        return sharedPreferencesSettings.getLong("timestamp",0);
    }

    public Integer getUpadateMinutes(){return sharedPreferencesSettings.getInt("selection_interval",720);}

    @Override
    public void saveNamePhoneData(String name, String phone) {
        if (namePhoneData != null){
            SharedPreferences.Editor editor = namePhoneData.edit();
            editor.putString("NAME",name);
            editor.putString("PHONE",phone);
            editor.apply();
        }
    }

    /*
    public NamePhone getNamePhoneData(){
        NamePhone namePhone = new NamePhone();
        if (namePhoneData != null){

            namePhone.setName(namePhoneData.getString("NAME", FirebaseAuth.getInstance().getCurrentUser().getDisplayName()));
            namePhone.setPhone(namePhoneData.getString("PHONE",""));
        }else {
            namePhone.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            namePhone.setPhone("");
        }

        return namePhone;
    }
     */

    public void clearSavedHomeProfileData() {
        if (namePhoneData != null) {
            SharedPreferences.Editor editor = namePhoneData.edit();
            editor.clear();
            editor.apply();
        }
    }

    public void clearSavedHomeLocationData() {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }


}
