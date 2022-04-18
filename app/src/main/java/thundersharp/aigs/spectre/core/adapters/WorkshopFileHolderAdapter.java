package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.pdfviwer.PdfLoader;
import thundersharp.aigs.pdfviwer.PdfModel;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.interfaces.ProjectListner;
import thundersharp.aigs.spectre.core.models.Participants;
import thundersharp.aigs.spectre.core.models.WorkshopFiles;
import thundersharp.aigs.spectre.ui.activities.barcode.BarCodeScanner;
import thundersharp.aigs.spectre.ui.activities.exhibition.ProjectsInfo;

public class WorkshopFileHolderAdapter extends RecyclerView.Adapter<WorkshopFileHolderAdapter.ViewHolder>{

    private List<WorkshopFiles> projectShortDescription;
    private Context context;

    public WorkshopFileHolderAdapter(){}

    public WorkshopFileHolderAdapter(List<WorkshopFiles> projectShortDescription, Context context) {
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
        WorkshopFiles projectBasicInfo = projectShortDescription.get(position);

        //holder.catHolder.setImageResource(R.drawable.iot);

        holder.name.setText(projectBasicInfo.TITLE);
        holder.description.setText(projectBasicInfo.SHORT_DESCRIPTION);
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
            WorkshopFiles workshopFiles = projectShortDescription.get(getAdapterPosition());
            PdfLoader
                    .getInstance(context)
                    .setSecureMode(false)
                    .setPdfData(new PdfModel(workshopFiles.ID, workshopFiles.URL, workshopFiles.TITLE, workshopFiles.SHORT_DESCRIPTION))
                    .loadPdf();
        }
    }
}
