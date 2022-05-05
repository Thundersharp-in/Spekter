package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.Committee;

public class CommitteeListAdapter extends RecyclerView.Adapter<CommitteeListAdapter.ViewHolder>{

    private List<Committee> projectShortDescription;
    private Context context;

    public CommitteeListAdapter(List<Committee> projectShortDescription, Context context) {
        this.projectShortDescription = projectShortDescription;
        this.context = context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Committee participants = projectShortDescription.get(position);
        holder.name.setText(""+participants.NAME);
        if (participants.PIC.trim().isEmpty())
            Glide.with(context).load(R.mipmap.ic_launcher_round).into(holder.aavatar);
        else
        Glide.with(context).load(participants.PIC).into(holder.aavatar);
        holder.role.setText(participants.DESC);

    }

    @Override
    public int getItemCount() {
        if (projectShortDescription != null)return projectShortDescription.size();
        else return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView aavatar;
        TextView name,role;
        FloatingActionButton floatingActionButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            aavatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            role = itemView.findViewById(R.id.role);

            floatingActionButton = itemView.findViewById(R.id.mail);
            floatingActionButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }


    }
}
