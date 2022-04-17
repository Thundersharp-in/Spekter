package thundersharp.aigs.spectre.core.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

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
        Glide.with(holder.itemView.getContext()).load(notifications.COVER).into(holder.background);


    }

    @Override
    public int getItemCount() {
        if (notificationsList != null)
            return notificationsList.size();
        else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView background;
        private TextView month,day,tittle,duramtion,mode,by;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.event_poster);
            month = itemView.findViewById(R.id.month);
            day = itemView.findViewById(R.id.day);

            by = itemView.findViewById(R.id.by);
            mode = itemView.findViewById(R.id.mode);
            tittle = itemView.findViewById(R.id.tittle);
            duramtion = itemView.findViewById(R.id.duration);

        }
    }
}
