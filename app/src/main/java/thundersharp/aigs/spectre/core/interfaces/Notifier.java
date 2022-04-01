package thundersharp.aigs.spectre.core.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import thundersharp.aigs.spectre.core.adapters.NotificationHolder;
import thundersharp.aigs.spectre.core.exceptions.NotificationException;
import thundersharp.aigs.spectre.core.models.Notifications;

public interface Notifier {

    interface NotificationListener {
        void notificationFetchSuccess(List<Notifications> notifications, NotificationHolder adapter);
        void notificationFetchError(NotificationException notificationException);
    }
}
