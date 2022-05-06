package thundersharp.aigs.spectre.core.interfaces;

import com.google.firebase.database.DataSnapshot;

public interface BasicDataInterface {

    void dataFetchSuccess(DataSnapshot snapshot);
    void onFetchError(Exception e);

}
