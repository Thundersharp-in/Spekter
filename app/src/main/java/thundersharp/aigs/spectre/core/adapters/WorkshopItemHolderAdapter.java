package thundersharp.aigs.spectre.core.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import thundersharp.aigs.expandablecardview.ExpandableCardView;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.Notifications;
import thundersharp.aigs.spectre.core.models.Workshops;

public class WorkshopItemHolderAdapter extends RecyclerView.Adapter<WorkshopItemHolderAdapter.ViewHolder> {

    private List<Workshops> notificationsList;

    public WorkshopItemHolderAdapter(){}

    public WorkshopItemHolderAdapter(List<Workshops> notificationsList) {
        this.notificationsList = notificationsList;
    }

    public WorkshopItemHolderAdapter setData(List<Workshops> notifications){
        this.notificationsList = notifications;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workshop_holder,null,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Workshops notifications = notificationsList.get(position);

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
