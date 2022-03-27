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


import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.CourseData;
import thundersharp.aigs.spectre.core.utils.TimeUtils;
import thundersharp.aigs.spectre.ui.activities.VideoPlayer;

public class CoursesItemAdapter extends RecyclerView.Adapter<CoursesItemAdapter.ViewHolder>{
    List<CourseData> modelList;
    Context context;
    String course_by;
    Boolean accessible;
    Boolean restricted;

    public CoursesItemAdapter(List<CourseData> modelList, String course_by, Context context, Boolean accessible, Boolean restricted) {
        this.modelList = modelList;
        this.course_by = course_by;
        this.context = context;
        this.accessible = accessible;
        this.restricted = restricted;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_main_coarse_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseData model = modelList.get(position);

        holder.CS_name.setText(model.TOPIC_NAME);
        holder.CS_by.setText("By "+course_by);
        holder.CS_desc.setText(model.TOPIC_DESCRIPTION);

        if (accessible){
            holder.iv_lock.setImageResource(R.drawable.ic_baseline_lock_open_24);
            //Glide.with(context).load(R.drawable.ic_baseline_lock_open_24).into(holder.iv_lock);
        }else {
            holder.iv_lock.setImageResource(R.drawable.ic_outline_lock_24);
            //Glide.with(context).load(R.drawable.ic_outline_lock_24).into(holder.iv_lock);
        }

        holder.hashtags.setText("Updated on "+ TimeUtils.getDateFromTimeStamp(model.ID));

        Glide.with(context).load("https://img.youtube.com/vi/"+model.VIDEO_ID+"/0.jpg").into(holder.c_s_img);


    }

    @Override
    public int getItemCount() {
        if (modelList!=null)
        return modelList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView c_s_img,iv_lock;
        TextView CS_name , CS_by, CS_desc, hashtags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CS_name = itemView.findViewById(R.id.CS_name);
            CS_by = itemView.findViewById(R.id.CS_by);
            CS_desc = itemView.findViewById(R.id.CS_desc);
            iv_lock = itemView.findViewById(R.id.iv_lock);

            c_s_img = itemView.findViewById(R.id.c_s_img);
            hashtags = itemView.findViewById(R.id.hashtags);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (accessible) {
                if (!restricted) {
                    context.startActivity(new Intent(context, VideoPlayer.class).putExtra("video_id", modelList.get(getAdapterPosition()).VIDEO_ID));
                    Toast.makeText(context, "Not restricted!", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(context, "This has been restricted for you", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(context, "Please do subscribe this course first!", Toast.LENGTH_SHORT).show();
        }
    }
}
