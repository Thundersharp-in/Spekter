package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.ServiceItemModel;
import thundersharp.aigs.spectre.ui.activities.lectures.CourseDetailActivity;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.ViewHolder> implements Filterable{

    List<ServiceItemModel> modelList, modelListCopy;
    Context context;
    String course_type;

    public ServiceItemAdapter(List<ServiceItemModel> modelList, Context context, String course_type) {
        this.modelList = modelList;
        this.context = context;
        this.course_type = course_type;
        modelListCopy = new ArrayList<>(modelList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_service_course,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceItemModel model = modelList.get(position);

        holder.CS_name.setText(model.COURSE_NAME);
        holder.CS_by.setText(model.COARSE_BY);
        holder.CS_desc.setText(model.COARSE_DESCRIPTION);
        holder.CS_lang.setText(model.LANGUAGE);
        holder.CS_level.setText(model.LEVEL);
        holder.CS_price.setText("₹ "+model.PRICE);

        holder.hashtags.setText(model.HASHTAGS);

        Glide.with(context).load(model.COURSE_ICON).into(holder.c_s_img);

    }

    @Override
    public int getItemCount() {
        if (modelList != null)
            return modelList.size();
        else return 0;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ServiceItemModel> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0){
                filterList.addAll(modelListCopy);
            }else {
                String data = constraint.toString().toLowerCase();
                for (ServiceItemModel model:modelListCopy){
                    if (model.COURSE_NAME.toLowerCase().contains(data)||
                            model.COARSE_DESCRIPTION.toLowerCase().contains(data)||
                            model.COARSE_BY.toLowerCase().contains(data)||
                            model.HASHTAGS.toLowerCase().contains(data)||
                            model.LANGUAGE.toLowerCase().contains(data)||
                            model.LEVEL.toLowerCase().contains(data)){
                        filterList.add(model);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            modelList.clear();
            modelList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView c_s_img;
        TextView CS_name , CS_by, CS_desc, CS_lang, CS_level, CS_price,hashtags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CS_name = itemView.findViewById(R.id.CS_name);
            CS_by = itemView.findViewById(R.id.CS_by);
            CS_desc = itemView.findViewById(R.id.CS_desc);
            CS_lang = itemView.findViewById(R.id.CS_lang);
            CS_level = itemView.findViewById(R.id.CS_level);
            CS_price = itemView.findViewById(R.id.CS_price);
            c_s_img = itemView.findViewById(R.id.c_s_img);
            hashtags = itemView.findViewById(R.id.hashtags);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(context, CourseDetailActivity.class).putExtra("course_detail",modelList.get(getAdapterPosition())).putExtra("Ctype",course_type));
        }
    }
}
