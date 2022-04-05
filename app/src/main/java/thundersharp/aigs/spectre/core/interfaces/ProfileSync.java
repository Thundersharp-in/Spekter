package thundersharp.aigs.spectre.core.interfaces;

import com.google.firebase.database.DataSnapshot;

public interface ProfileSync {
    void onProfileDataSyncSuccess(DataSnapshot dataSnapshot);
    void onProfileDataSyncFailure(Exception exception);
}
