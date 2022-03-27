package thundersharp.aigs.spectre.core.serviceLoader;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.core.exceptions.DataRenderError;
import thundersharp.aigs.spectre.core.models.ServiceTopicModel;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class PaidService implements ServiceLoader{

    private Context context;
    private int count;
    private OnServiceLoaded onServiceLoaded;

    public static PaidService getInstance(Context context){
        return new PaidService(context);
    }

    public PaidService(Context context){
        this.context = context;
    }


    public PaidService setDataCallbackCount(int count){
        this.count = count;
        return this;
    }

    public void addDataWatcher(OnServiceLoaded onServiceLoaded){
        this.onServiceLoaded = onServiceLoaded;
        OndataCallback();
    }


    @Override
    public void OndataCallback() {
        if (onServiceLoaded != null) {

            List<ServiceTopicModel> modelList1 = new ArrayList<>();
            List<ServiceTopicModel> modelList2 = new ArrayList<>();
            List<ServiceTopicModel> modelList3 = new ArrayList<>();

            FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.DATABASE_SERVICES_HOME)
                    .child(CONSTANTS.DATABASE_COURSES)
                    .limitToFirst(count)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    modelList3.add(snapshot1.getValue(ServiceTopicModel.class));
                                }

                                onServiceLoaded.courseLoaded(modelList3);

                            } else {
                                onServiceLoaded.dataRenderException(0,new DataRenderError("No data found"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            onServiceLoaded.dataRenderException(0,new DataRenderError(error.getMessage()));
                        }
                    });


            FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.DATABASE_SERVICES_HOME)
                    .child(CONSTANTS.DATABASE_COURSES_LIVE)
                    .limitToFirst(count)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    modelList1.add(snapshot1.getValue(ServiceTopicModel.class));
                                }

                                onServiceLoaded.liveCoursesLoaded(modelList1);
                            } else {
                                onServiceLoaded.dataRenderException(1,new DataRenderError("No data found"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            onServiceLoaded.dataRenderException(1,new DataRenderError(error.getMessage()));
                        }
                    });


            FirebaseDatabase
                    .getInstance()
                    .getReference(CONSTANTS.DATABASE_SERVICES_HOME)
                    .child(CONSTANTS.DATABASE_COURSES_FREE)
                    .limitToFirst(count)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                    modelList2.add(snapshot1.getValue(ServiceTopicModel.class));
                                }

                                onServiceLoaded.freeCourcesLoaded(modelList2);
                            } else {
                                onServiceLoaded.dataRenderException(2,new DataRenderError("No data found"));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            onServiceLoaded.dataRenderException(2,new DataRenderError(error.getMessage()));
                        }
                    });
        }else
            Toast.makeText(context, "Data watcher not attached or is null", Toast.LENGTH_SHORT).show();
    }
}
