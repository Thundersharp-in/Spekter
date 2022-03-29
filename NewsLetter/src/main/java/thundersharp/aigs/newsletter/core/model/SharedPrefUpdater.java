package thundersharp.aigs.newsletter.core.model;

import java.util.HashMap;

public interface SharedPrefUpdater {


    void saveNamePhoneData(String name, String phone);



    interface namePhoneUpdate{
        void namePhoneUpdated(String name, String phone);
    }

    interface AccountSwitch{

        void saveSwitchedUser(HashMap<String , String> data,boolean saveSwitchedUser);

        interface lisetner{
            void onSaveSuccess(String employeeCode,String name,String type, String passcode);
            void onSaveFailure(Exception e);
        }

    }
}
