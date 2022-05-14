package thundersharp.aigs.spectre.core.helpers;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class AdsHelper {

    public static AdsHelper getInstance(){
        return new AdsHelper();
    }
    private AdsLoader adsLoader;


    public void setAdsLoader(AdsLoader adsLoader){
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.SPONSORS)
                .child(CONSTANTS.ADS)
                .child(CONSTANTS.URI)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            adsLoader.OnLoadSuccess(snapshot.getValue(String.class));
                        }else adsLoader.OnError(new Exception("404 : NO DATA FOUND."));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        adsLoader.OnError(error.toException());
                    }
                });
    }


    public interface AdsLoader{
        void OnLoadSuccess(String uri);
        void OnError(Exception e);
    }
}
