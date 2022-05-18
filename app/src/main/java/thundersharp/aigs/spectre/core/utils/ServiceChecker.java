package thundersharp.aigs.spectre.core.utils;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import thundersharp.aigs.spectre.core.exceptions.InternalException;
import thundersharp.aigs.spectre.core.models.SubscriptionDetails;

public class ServiceChecker {

    private String serviceId;
    private listner listner;

    public ServiceChecker(){ }

    public static ServiceChecker getInstance(){
        return new ServiceChecker();
    }

    public ServiceChecker setServiceId(String serviceId){
        this.serviceId = serviceId;
        return this;
    }

    public ServiceChecker build(){

        if (serviceId == null) {
            listner.dataError(new InternalException("service id is null"));
        }
        return this;
    }

    public void setListner(listner listner){
        this.listner = listner;
        buildmain();
    }

    private void buildmain(){

            FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.DATABASE_USER_DATA)
                    .child(FirebaseAuth.getInstance().getUid())
                    .child(CONSTANTS.DATABASE_PURCHASED_SERVICES)
                    .child(serviceId)
                    .child(CONSTANTS.DATABASE_PAYMENT)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()){
                                listner.dataExists(snapshot.getValue(SubscriptionDetails.class));
                            }else listner.onDataPathOk();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            listner.dataError(error.toException());
                        }
                    });
    }

    public interface listner{

        void onDataPathOk();
        void dataExists(SubscriptionDetails subscriptionDetails);
        void dataError(Exception e);
    }
}
