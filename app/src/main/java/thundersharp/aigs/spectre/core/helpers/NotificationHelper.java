package thundersharp.aigs.spectre.core.helpers;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.core.adapters.NotificationHolder;
import thundersharp.aigs.spectre.core.exceptions.NotificationException;
import thundersharp.aigs.spectre.core.interfaces.Notifier;
import thundersharp.aigs.spectre.core.models.Notifications;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;

public class NotificationHelper {

    int notificationCount = 0;
    RecyclerView recyclerView;
    private Notifier.NotificationListener notificationListener;

    private NotificationHolder adapter;

    public static NotificationHelper getInstance(){
        return new NotificationHelper();
    }

    public NotificationHelper setNoOfNotifications(int number){
        this.notificationCount = number;
        return this;
    }

    public NotificationHelper setRecyclerView(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        return this;
    }

    public NotificationHelper setCustomAdapter(NotificationHolder customAdapter){
        this.adapter = customAdapter;
        return this;
    }

    public NotificationHelper setNotifier(Notifier.NotificationListener notificationListener){
        this.notificationListener = notificationListener;
        getNotifications();
        return this;
    }

    private NotificationHelper getNotifications(){
        List<Notifications> notificationsList = new ArrayList<>();
        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.DATABASE_NOTIFICATIONS)
                .limitToLast(notificationCount)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                                notificationsList.add(dataSnapshot.getValue(Notifications.class));
                            }

                            adapter.setData(notificationsList);
                            recyclerView.setAdapter(adapter);
                            notificationListener.notificationFetchSuccess(notificationsList,adapter);
                        }else notificationListener.notificationFetchError(new NotificationException("Error 404 !! No data found."));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        notificationListener.notificationFetchError(new NotificationException(error.getMessage()));
                    }
                });

        return this;
    }

}
