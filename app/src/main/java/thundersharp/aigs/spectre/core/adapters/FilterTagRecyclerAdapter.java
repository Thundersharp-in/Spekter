package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

import thundersharp.aigs.spectre.R;

public class FilterTagRecyclerAdapter extends RecyclerView.Adapter<FilterTagRecyclerAdapter.ViewHolder>{

    Context context;
    List<String> commentModelList;

    public FilterTagRecyclerAdapter(Context context, List<String> commentModelList) {
        this.context = context;
        this.commentModelList = commentModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_filter_tags_recycler,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String commentModel = (String) commentModelList.get(position);
        holder.chip.setText(commentModel);
    }

    @Override
    public int getItemCount() {
        if (commentModelList != null ) return commentModelList.size(); else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private Chip chip;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.office);
        }
    }
}
