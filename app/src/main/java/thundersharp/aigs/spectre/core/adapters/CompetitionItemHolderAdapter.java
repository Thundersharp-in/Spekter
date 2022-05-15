package thundersharp.aigs.spectre.core.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.newsletter.core.utils.TimeUtils;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.Competitions;
import thundersharp.aigs.spectre.ui.activities.fwdActivities.CompetitionDetail;
import thundersharp.aigs.spectre.ui.activities.fwdActivities.WorkshopDetails;

public class CompetitionItemHolderAdapter extends RecyclerView.Adapter<CompetitionItemHolderAdapter.ViewHolder> implements Filterable {

    private List<Competitions> notificationsList,getNotificationsListCopy;

    public CompetitionItemHolderAdapter(){}

    public CompetitionItemHolderAdapter(List<Competitions> notificationsList) {
        this.notificationsList = notificationsList;
        getNotificationsListCopy = new ArrayList<>(notificationsList);
    }

    public CompetitionItemHolderAdapter setData(List<Competitions> notifications){
        this.notificationsList = notifications;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_competition_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Competitions notifications = notificationsList.get(position);
        Glide.with(holder.itemView.getContext()).load(notifications.COVER).into(holder.background);

        holder.duramtion.setText("Duration : "+notifications.DURATION);
        holder.by.setText("By "+notifications.ORGANISED_BY);
        holder.tittle.setText(notifications.TITTLE);
        holder.mode.setText("This is a "+notifications.MODE.toUpperCase()+" Event");

        holder.itemView.findViewById(R.id.bottom_w).setOnClickListener(n->{
            holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), CompetitionDetail.class).putExtra("workshop_info",notifications));
        });

        try {
            holder.day.setText(TimeUtils.getDayFromTimeStamp(notifications.ID));
            holder.month.setText(TimeUtils.getMonthName(Integer.parseInt(TimeUtils.getMonthFromTimeStamp(notifications.ID))).substring(0, 3).toUpperCase());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        if (notificationsList != null)
            return notificationsList.size();
        else
            return 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Competitions> filterList = new ArrayList<>();
            if (constraint == null|| constraint.length() == 0){
                filterList.addAll(getNotificationsListCopy);
            }else {
                String text = constraint.toString().toLowerCase().trim();
                for (Competitions data : getNotificationsListCopy){
                    if (data.TITTLE.toLowerCase().contains(text)||
                            data.ORGANISED_BY.toLowerCase().contains(text)||
                            data.MODE.toLowerCase().contains(text)){
                        filterList.add(data);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            notificationsList.clear();
            notificationsList.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

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
