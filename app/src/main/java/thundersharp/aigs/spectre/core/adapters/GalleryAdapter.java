package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jsibbold.zoomage.ZoomageView;

import java.net.URI;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.Committee;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

    private List<String> projectShortDescription;
    private Context context;

    public GalleryAdapter(List<String> projectShortDescription, Context context) {
        this.projectShortDescription = projectShortDescription;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallary_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(projectShortDescription.get(position)). into(holder.aavatar);
    }

    @Override
    public int getItemCount() {
        if (projectShortDescription != null)return projectShortDescription.size();
        else return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView aavatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            aavatar = itemView.findViewById(R.id.image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            View view1 = LayoutInflater.from(context).inflate(R.layout.view_one_image,null,false);
            ZoomageView zoomageView = view1.findViewById(R.id.myZoomageView);
            //zoomageView.setImageURI(Uri.parse(projectShortDescription.get(getAdapterPosition())));
            Glide.with(context).load(projectShortDescription.get(getAdapterPosition())).into(zoomageView);

            new AlertDialog.Builder(context)
                    .setView(view1)
                    .show();
        }


    }
}
