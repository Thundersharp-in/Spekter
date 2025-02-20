package thundersharp.aigs.spectre.core.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.interfaces.ProjectListner;
import thundersharp.aigs.spectre.core.models.Participants;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.models.ProjectShortDescription;
import thundersharp.aigs.spectre.ui.activities.barcode.BarCodeScanner;
import thundersharp.aigs.spectre.ui.activities.exhibition.ProjectsInfo;

public class ProjectsHolderAdapter extends RecyclerView.Adapter<ProjectsHolderAdapter.ViewHolder> implements Filterable {

    private List<ProjectBasicInfo> projectShortDescription,projectShortDescriptionListCopy;

    public ProjectsHolderAdapter(){}

    public ProjectsHolderAdapter(List<ProjectBasicInfo> projectShortDescription) {
        this.projectShortDescription = projectShortDescription;
        projectShortDescriptionListCopy = new ArrayList<>(projectShortDescription);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_projects,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProjectBasicInfo projectBasicInfo = projectShortDescription.get(position);
        if (projectBasicInfo.TYPE.equalsIgnoreCase("0")){
            holder.catHolder.setImageResource(R.drawable.iot);
        }else if (projectBasicInfo.TYPE.equalsIgnoreCase("1")){
            holder.catHolder.setImageResource(R.drawable.ai);
        }else if (projectBasicInfo.TYPE.equalsIgnoreCase("2")){
            holder.catHolder.setImageResource(R.drawable.cyber_sec);
        }else{
            holder.catHolder.setImageResource(R.drawable.cyber_sec);
        }

        holder.name.setText(projectBasicInfo.NAME);
        holder.description.setText(projectBasicInfo.SHORT_DESCRIPTION);
    }

    @Override
    public int getItemCount() {
        if (projectShortDescription != null)return projectShortDescription.size();
        else return 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ProjectBasicInfo> filterList = new ArrayList<>();
            if (constraint == null|| constraint.length() == 0){
                filterList.addAll(projectShortDescriptionListCopy);
            }else {
                String text = constraint.toString().toLowerCase().trim();
                for (ProjectBasicInfo data : projectShortDescriptionListCopy){
                    if (data.NAME.toLowerCase().contains(text)||
                            data.TYPE.toLowerCase().contains(text)||
                            data.SHORT_DESCRIPTION.toLowerCase().contains(text)){
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
            projectShortDescription.clear();
            projectShortDescription.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView catHolder;
        TextView name,description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catHolder = itemView.findViewById(R.id.top_type);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.address);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_projects_actions);

            ImageView scanner = bottomSheetDialog.findViewById(R.id.scanner);
            scanner.setOnClickListener(j->view.getContext().startActivity(new Intent(view.getContext(), BarCodeScanner.class)));

            ((TextView)bottomSheetDialog.findViewById(R.id.top)).setText(projectShortDescription.get(getAdapterPosition()).NAME);
            bottomSheetDialog.findViewById(R.id.participants).setOnClickListener(n -> ShowOtherBottomSheet(view));
            bottomSheetDialog.findViewById(R.id.info)
                    .setOnClickListener(n -> view.getContext().startActivity(new Intent(view.getContext(), ProjectsInfo.class).putExtra("projects_basic_info",projectShortDescription.get(getAdapterPosition()))));

            bottomSheetDialog.show();
        }

        private void ShowOtherBottomSheet(View view){
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
            bottomSheetDialog.setContentView(R.layout.item_participants);

            DatabaseHelpers
                    .getInstance()
                    .setProjectId(Long.parseLong(projectShortDescription.get(getAdapterPosition()).ID))
                    .setFetchParticipantsListeners(new ProjectListner.fetchParticipants() {
                        @Override
                        public void onParticipantsFetchSuccess(List<Participants> participantsList) {
                            ((RecyclerView)bottomSheetDialog.findViewById(R.id.recycler)).setAdapter(new ParticipantsListAdapter(participantsList));
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(view.getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            bottomSheetDialog.show();
        }
    }
}
