package thundersharp.aigs.spectre.core.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import thundersharp.aigs.expandablecardview.ExpandableCardView;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.Notifications;

public class NotificationHolder extends RecyclerView.Adapter<NotificationHolder.ViewHolder> {

    private List<Notifications> notificationsList;

    public NotificationHolder(){}

    public NotificationHolder(List<Notifications> notificationsList) {
        this.notificationsList = notificationsList;
    }

    public NotificationHolder setData(List<Notifications> notifications){
        this.notificationsList = notifications;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notifications,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notifications notifications = notificationsList.get(position);

    }

    @Override
    public int getItemCount() {
        if (notificationsList != null)
            return notificationsList.size();
        else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ExpandableCardView expandableCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            expandableCardView = itemView.findViewById(R.id.expandable_card_view);
        }
    }
}
