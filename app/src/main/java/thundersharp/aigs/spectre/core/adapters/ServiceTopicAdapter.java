package thundersharp.aigs.spectre.core.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.annonations.ServiceType;
import thundersharp.aigs.spectre.core.models.ServiceItemModel;
import thundersharp.aigs.spectre.core.models.ServiceTopicModel;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.ui.activities.lectures.CourseDetailActivity;

public class ServiceTopicAdapter extends RecyclerView.Adapter<ServiceTopicAdapter.ViewHolder>{

    private List<ServiceTopicModel> modelList;
    private Context context;
    private int type;

    private AlertDialog.Builder builder;
    private Dialog dialog;


    public ServiceTopicAdapter(List<ServiceTopicModel> modelList, Context context, @ServiceType int type) {
        this.modelList = modelList;
        this.context = context;
        this.type = type;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_service_coarse_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ServiceTopicModel model = modelList.get(position);

        builder = new AlertDialog.Builder(context);
        View dialogview = LayoutInflater.from(context).inflate(R.layout.progress_dialog,null,false);
        builder.setView(dialogview);
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        if (type == ServiceType.TYPE_NORMAL_COURSE){

            holder.hashtags.setText("#WarriorCourse #"+model.COARSE_BY+" #trending");

        }else if (type == ServiceType.TYPE_FREE_COURSE){

            holder.hashtags.setText("#LearnForFree #WarriorCourse #"+model.COARSE_BY+" #trending");

        }else if (type == ServiceType.TYPE_LIVE_COURSE){
            holder.hashtags.setText("#LiveCourse #"+model.COARSE_BY+" #trending");
        }

        if (model.COURSE_ICON != null)
            Glide.with(context).load(model.COURSE_ICON).into(holder.c_s_img);
        holder.CS_by.setText("By "+model.COARSE_BY);
        holder.CS_desc.setText(model.COARSE_DESCRIPTION);
        holder.CS_name.setText(model.COURSE_NAME);
        holder.CS_price.setText("Get @ ₹"+model.PRICE);
       /* holder.cat_name.setText(model.getName());
        holder.cat_size.setText(String.valueOf(model.getTotal()));*/

    }

    @Override
    public int getItemCount() {
        if (modelList != null)
            return modelList.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView c_s_img;
        TextView CS_name,CS_by,hashtags,CS_desc,CS_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            c_s_img = itemView.findViewById(R.id.c_s_img);
            CS_name = itemView.findViewById(R.id.CS_name);
            CS_by = itemView.findViewById(R.id.CS_by);
            hashtags = itemView.findViewById(R.id.hashtags);
            CS_desc = itemView.findViewById(R.id.CS_desc);
            CS_price = itemView.findViewById(R.id.CS_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            dialog.show();
            if (type == ServiceType.TYPE_NORMAL_COURSE){

                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_COURSES_ALL)
                        .child(CONSTANTS.DATABASE_COURSES)
                        .child(modelList.get(getAdapterPosition()).COARSE_ID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    dialog.dismiss();
                                    context.startActivity(new Intent(context, CourseDetailActivity.class)
                                            .putExtra("course_detail",snapshot.getValue(ServiceItemModel.class))
                                            .putExtra("Ctype",CONSTANTS.DATABASE_COURSES));
                                }else {
                                    dialog.dismiss();
                                    Toast.makeText(context, "Data Not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                dialog.dismiss();
                                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }else if (type == ServiceType.TYPE_FREE_COURSE){

                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_COURSES_ALL)
                        .child(CONSTANTS.DATABASE_COURSES_FREE)
                        .child(modelList.get(getAdapterPosition()).COARSE_ID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    dialog.dismiss();
                                    context.startActivity(new Intent(context, CourseDetailActivity.class).putExtra("course_detail",snapshot.getValue(ServiceItemModel.class)).putExtra("Ctype",CONSTANTS.DATABASE_COURSES_FREE));
                                }else {
                                    dialog.dismiss();
                                    Toast.makeText(context, "Data Not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                dialog.dismiss();
                                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }else if (type == ServiceType.TYPE_LIVE_COURSE){

                FirebaseDatabase
                        .getInstance()
                        .getReference(CONSTANTS.DATABASE_COURSES_ALL)
                        .child(CONSTANTS.DATABASE_COURSES_LIVE)
                        .child(modelList.get(getAdapterPosition()).COARSE_ID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    dialog.dismiss();
                                    context.startActivity(new Intent(context, CourseDetailActivity.class)
                                            .putExtra("course_detail",snapshot.getValue(ServiceItemModel.class))
                                            .putExtra("Ctype",CONSTANTS.DATABASE_COURSES_LIVE));
                                }else {
                                    dialog.dismiss();
                                    Toast.makeText(context, "Data Not found", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                dialog.dismiss();
                                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        }
    }
}
