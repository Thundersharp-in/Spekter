package thundersharp.aigs.spectre.core.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapInitOptions;
import com.mapbox.maps.MapOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.ResourceOptions;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import thundersharp.aigs.newsletter.core.utils.TimeUtils;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.Upcomming;
import thundersharp.aigs.spectre.core.models.Workshops;
import thundersharp.aigs.spectre.ui.activities.fwdActivities.WorkshopDetails;

public class UpcomingHolderAdapter extends RecyclerView.Adapter<UpcomingHolderAdapter.ViewHolder> implements Filterable {

    private List<Upcomming> notificationsList, getNotificationsListCopy;
    private FragmentManager activity;

    public UpcomingHolderAdapter() {
    }

    public UpcomingHolderAdapter(List<Upcomming> notificationsList, FragmentManager fragmentManager) {
        this.activity = fragmentManager;
        this.notificationsList = notificationsList;
        getNotificationsListCopy = new ArrayList<>(notificationsList);
    }

    public UpcomingHolderAdapter setData(List<Upcomming> notifications) {
        this.notificationsList = notifications;
        return this;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_upcomming_holder, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Upcomming notifications = notificationsList.get(position);

        try {
            holder.day.setText(TimeUtils.getDayFromTimeStamp(notifications.ID));
            holder.month.setText(TimeUtils.getMonthName(Integer.parseInt(TimeUtils.getMonthFromTimeStamp(notifications.ID))).substring(0, 3).toUpperCase());
        } catch (Exception e) {
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
            List<Upcomming> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterList.addAll(getNotificationsListCopy);
            } else {
                String text = constraint.toString().toLowerCase().trim();
                for (Upcomming data : getNotificationsListCopy) {
                    if (data.TITTLE.toLowerCase().contains(text) ||
                            data.DESCRIPTION.toLowerCase().contains(text)) {
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
            notificationsList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView background;
        private TextView month, day, tittle, duramtion, mode, by;
        private MapView mapView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //SupportMapFragment mapFragment = (SupportMapFragment)activity.findFragmentById(R.id.mapUpcoming);
            //mapFragment.getMapAsync(this);

            month = itemView.findViewById(R.id.month);
            day = itemView.findViewById(R.id.day);
            by = itemView.findViewById(R.id.by);
            tittle = itemView.findViewById(R.id.tittle);
            duramtion = itemView.findViewById(R.id.duration);

            mapView = itemView.findViewById(R.id.mapView);
            mapView.getMapboxMap().loadStyleUri(Style.DARK);
        }


    }
}
