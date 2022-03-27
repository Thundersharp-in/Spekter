package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.ui.activities.lectures.FilterActivity;

public class TagsSortAdapter extends RecyclerView.Adapter<TagsSortAdapter.ViewHolder> {

    List<String> hashttags;
    Context context;

    public TagsSortAdapter(List<String> hashttags, Context context) {
        this.hashttags = hashttags;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_filter_tags,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tags_items.setText(hashttags.get(position));
        holder.tags_items.setOnCheckedChangeListener((compoundButton, b) ->{

            List<String> dataList = FilterActivity.hash_selected;
            int counter = 0;
            boolean matchFound = false;

            if (dataList.isEmpty()){
                FilterActivity.hash_selected.add(hashttags.get(position));
            }else {
                for (String filterData : dataList){
                    if (filterData.equals(hashttags.get(position))){
                        if (!b)
                            FilterActivity.hash_selected.remove(counter);
                        matchFound = true;
                        break;
                    }
                    counter++;
                }

                if (!matchFound){
                    FilterActivity.hash_selected.add( hashttags.get(position));
                }
            }

        });

    }

    @Override
    public int getItemCount() {
        if (hashttags != null)
            return hashttags.size();
        else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatCheckBox tags_items;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tags_items = itemView.findViewById(R.id.tags_items);
        }
    }
}
