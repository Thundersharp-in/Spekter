package thundersharp.aigs.spectre.core.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.Participants;

public class ParticipantsListAdapter extends RecyclerView.Adapter<ParticipantsListAdapter.ViewHolder>{

    private List<Participants> projectShortDescription;

    public ParticipantsListAdapter(){}

    public ParticipantsListAdapter(List<Participants> projectShortDescription) {
        this.projectShortDescription = projectShortDescription;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_participants_holder,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Participants participants = projectShortDescription.get(position);
        if (participants.PROFILE_PIC == null || participants.PROFILE_PIC.isEmpty())
            Glide.with(holder.itemView).load(R.mipmap.ic_launcher_round).into(holder.aavatar);
        else Glide.with(holder.itemView).load(participants.PROFILE_PIC).into(holder.aavatar);
        holder.name.setText(participants.NAME);
        holder.role.setText(participants.ROLE);
        holder.sem.setText(participants.SEM_YEAR);
    }

    @Override
    public int getItemCount() {
        if (projectShortDescription != null)return projectShortDescription.size();
        else return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView aavatar;
        TextView name,role,sem;
        FloatingActionButton floatingActionButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            aavatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            role = itemView.findViewById(R.id.role);
            sem = itemView.findViewById(R.id.year_sem);

            floatingActionButton = itemView.findViewById(R.id.mail);

            floatingActionButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{projectShortDescription.get(getAdapterPosition()).EMAIL});
            //emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Not able to start the app.");

            emailIntent.setType("message/rfc822");
            try {
                view.getContext().startActivity(Intent.createChooser(emailIntent,
                        "Send email using..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(view.getContext(),
                        "No email clients installed.",
                        Toast.LENGTH_SHORT).show();
                }
            }


        }
}
