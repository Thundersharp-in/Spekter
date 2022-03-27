package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import thundersharp.aigs.spectre.R;

public class AboutCourseAdapter extends RecyclerView.Adapter<AboutCourseAdapter.ViewHolder> {

    private Context context;
    private List<String> stringList;

    public AboutCourseAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.about_course,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt.setText(stringList.get(position).trim());
    }

    @Override
    public int getItemCount() {
        if (stringList != null) return stringList.size(); else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt = itemView.findViewById(R.id.txt);

        }
    }
}
