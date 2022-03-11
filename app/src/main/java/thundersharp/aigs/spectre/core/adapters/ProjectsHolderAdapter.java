package thundersharp.aigs.spectre.core.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.ProjectShortDescription;

public class ProjectsHolderAdapter extends RecyclerView.Adapter<ProjectsHolderAdapter.ViewHolder> {

    private List<ProjectShortDescription> projectShortDescription;

    public ProjectsHolderAdapter(){}

    public ProjectsHolderAdapter(List<ProjectShortDescription> projectShortDescription) {
        this.projectShortDescription = projectShortDescription;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_projects_actions);
            bottomSheetDialog.show();
        }
    }
}
