package thundersharp.aigs.spectre.core.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.ProjectShortDescription;
import thundersharp.aigs.spectre.ui.activities.barcode.BarCodeScanner;
import thundersharp.aigs.spectre.ui.activities.exhibition.ProjectsInfo;

public class ProjectsHolderAdapter extends RecyclerView.Adapter<ProjectsHolderAdapter.ViewHolder> implements Filterable {

    private List<ProjectShortDescription> projectShortDescription,projectShortDescriptionListCopy;

    public ProjectsHolderAdapter(){}

    public ProjectsHolderAdapter(List<ProjectShortDescription> projectShortDescription) {
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
            List<ProjectShortDescription> filterList = new ArrayList<>();
            if (constraint == null|| constraint.length() == 0){
                filterList.addAll(projectShortDescriptionListCopy);
            }else {
                String text = constraint.toString().toLowerCase().trim();
                for (ProjectShortDescription data : projectShortDescriptionListCopy){
                    if (data.name.toLowerCase().contains(text)||
                            data.category.toLowerCase().contains(text)||
                            data.description.toLowerCase().contains(text)){
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_projects_actions);

            ImageView scanner = bottomSheetDialog.findViewById(R.id.scanner);
            scanner.setOnClickListener(j->view.getContext().startActivity(new Intent(view.getContext(), BarCodeScanner.class)));

            bottomSheetDialog.findViewById(R.id.participants).setOnClickListener(n -> ShowOtherBottomSheet(view));
            bottomSheetDialog.findViewById(R.id.info).setOnClickListener(n -> view.getContext().startActivity(new Intent(view.getContext(), ProjectsInfo.class)));

            bottomSheetDialog.show();
        }

        private void ShowOtherBottomSheet(View view){
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
            bottomSheetDialog.setTitle("Participants details.");
            bottomSheetDialog.show();
        }
    }
}
