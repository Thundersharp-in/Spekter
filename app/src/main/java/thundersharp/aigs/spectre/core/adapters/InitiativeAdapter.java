package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import thundersharp.aigs.spectre.R;

public class InitiativeAdapter extends RecyclerView.Adapter<InitiativeAdapter.ViewHolder>{

    Context context;
    List<Object> list;


    public InitiativeAdapter(Context context, List<Object> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InitiativeAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_aigs_initiatives,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (list != null ) return list.size(); else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView pinItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pinItem = itemView.findViewById(R.id.pinner);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

        }
    }
}
