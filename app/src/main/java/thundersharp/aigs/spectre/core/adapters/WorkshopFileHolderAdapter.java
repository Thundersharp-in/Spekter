package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import thundersharp.aigs.pdfviwer.PdfLoader;
import thundersharp.aigs.pdfviwer.PdfModel;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.RELATED_FILES;

public class WorkshopFileHolderAdapter extends RecyclerView.Adapter<WorkshopFileHolderAdapter.ViewHolder>{

    private List<RELATED_FILES> projectShortDescription;
    private Context context;

    public WorkshopFileHolderAdapter(){}

    public WorkshopFileHolderAdapter(List<RELATED_FILES> projectShortDescription, Context context) {
        this.projectShortDescription = projectShortDescription;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_files,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RELATED_FILES projectBasicInfo = projectShortDescription.get(position);

        //holder.catHolder.setImageResource(R.drawable.iot);

        holder.name.setText(projectBasicInfo.TITTLE);
        holder.description.setText(projectBasicInfo.SHORT_DESC);
    }

    @Override
    public int getItemCount() {
        if (projectShortDescription != null)return projectShortDescription.size();
        else return 0;
    }

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
            RELATED_FILES RELATEDFILES = projectShortDescription.get(getAdapterPosition());
            PdfLoader
                    .getInstance(context)
                    .setSecureMode(false)
                    .setPdfData(new PdfModel(RELATEDFILES.ID, RELATEDFILES.URL, RELATEDFILES.TITTLE, RELATEDFILES.SHORT_DESC))
                    .loadPdf();
        }
    }
}
