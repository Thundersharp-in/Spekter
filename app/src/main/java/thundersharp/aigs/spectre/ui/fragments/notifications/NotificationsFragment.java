package thundersharp.aigs.spectre.ui.fragments.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.NotificationHolder;
import thundersharp.aigs.spectre.core.exceptions.NotificationException;
import thundersharp.aigs.spectre.core.helpers.NotificationHelper;
import thundersharp.aigs.spectre.core.interfaces.Notifier;
import thundersharp.aigs.spectre.core.models.Notifications;
import thundersharp.aigs.spectre.databinding.FragmentNotificationsBinding;


public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = root.findViewById(R.id.recycler_notification);
        refreshLayout = root.findViewById(R.id.swipe);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateNotifications();
            }
        });

        updateNotifications();


        return root;
    }

    private synchronized void updateNotifications(){
        NotificationHelper
                .getInstance()
                .setNoOfNotifications(10)
                .setRecyclerView(recyclerView)
                .setCustomAdapter(new NotificationHolder())
                .setNotifier(new Notifier.NotificationListener() {
                    @Override
                    public void notificationFetchSuccess(List<Notifications> notifications, NotificationHolder adapter) {
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void notificationFetchError(NotificationException notificationException) {
                        refreshLayout.setRefreshing(false);
                    }
                });

    }

}