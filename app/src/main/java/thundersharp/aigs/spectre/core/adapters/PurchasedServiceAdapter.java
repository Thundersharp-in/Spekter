package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.PurchaseData;
import thundersharp.aigs.spectre.core.models.ServiceItemModel;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.ui.activities.lectures.CourseDetailActivity;

public class PurchasedServiceAdapter extends RecyclerView.Adapter<PurchasedServiceAdapter.ViewHolder>{

    Context context;
    List<PurchaseData> purchaseDataList;
    Integer type;

    public PurchasedServiceAdapter(Context context, List<PurchaseData> purchaseDataList, Integer type) {
        this.context = context;
        this.purchaseDataList = purchaseDataList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_purchased_services,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchaseData purchaseData = purchaseDataList.get(position);

        holder.course_name.setText(purchaseData.NAME);
        switch (purchaseData.PAYMENT.PAYMENT_STATUS){
            case "0" :
                holder.payment_status.setText("Payment for this course is unsuccessful!. Try again to make payment to get course lifetime access with great benefit.");
                Glide.with(context).load(R.drawable.error).into(holder.c_s_img);
                holder.amount.setTextColor(Color.RED);
                holder.payment_status.setTextColor(Color.RED);
                break;
            case "1" :
                holder.payment_status.setText("Lifetime course access is available for this course.");
                Glide.with(context).load(R.drawable.goodtogo).into(holder.c_s_img);
                break;
            case "4" :
                holder.payment_status.setText("Payment for this course is failed!. Try again to make payment to get course lifetime access with great benefit.");
                Glide.with(context).load(R.drawable.error).into(holder.c_s_img);
                holder.amount.setTextColor(Color.RED);
                holder.payment_status.setTextColor(Color.RED);
                break;
            default:
                holder.payment_status.setText("Payment for this course is unavailable!. Try again to make payment or contact customer support.");
                holder.amount.setTextColor(Color.RED);
                holder.payment_status.setTextColor(Color.RED);
                Glide.with(context).load(R.drawable.error).into(holder.c_s_img);
                break;
        }
        holder.payid.setText("Pay id: "+purchaseData.PAYMENT.PAYMENT_ID);
        holder.course_id.setText("Product id:"+purchaseData.COURSE_ID);
        holder.amount.setText("@ ₹ "+purchaseData.AMOUNT);
    }

    @Override
    public int getItemCount() {
        if (purchaseDataList != null ) return purchaseDataList.size(); else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView course_name,payment_status, amount,course_id,payid;
        private ImageView c_s_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            course_name = itemView.findViewById(R.id.course_name);
            payment_status = itemView.findViewById(R.id.payment_status);
            amount = itemView.findViewById(R.id.amount);
            c_s_img = itemView.findViewById(R.id.c_s_img);
            course_id = itemView.findViewById(R.id.course_id);
            payid = itemView.findViewById(R.id.CS_by);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (type == 0){
                Toast.makeText(context, "Navigate to course section and avail the benefit of this service", Toast.LENGTH_SHORT).show();
            }else if (type == 1){
                if (purchaseDataList.get(getAdapterPosition()).COURSE_TYPE != null) {
                    //if (!purchaseDataList.get(getAdapterPosition()).COURSE_TYPE.equals("")) {
                       // if (purchaseDataList.get(getAdapterPosition()).COURSE_TYPE.equalsIgnoreCase("COURSES")) {
                            FirebaseDatabase
                                    .getInstance()
                                    .getReference(CONSTANTS.DATABASE_COURSES_ALL)
                                    .child(purchaseDataList.get(getAdapterPosition()).COURSE_TYPE)
                                    .child(purchaseDataList.get(getAdapterPosition()).COURSE_ID)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if (snapshot.exists()) {
                                                context.startActivity(new Intent(context, CourseDetailActivity.class).putExtra("course_detail",
                                                        snapshot.getValue(ServiceItemModel.class))
                                                        .putExtra("Ctype", purchaseDataList.get(getAdapterPosition()).COURSE_TYPE)
                                                        .putExtra("payment_status",purchaseDataList.get(getAdapterPosition())));
                                            } else {
                                                Toast.makeText(context, "Data Not found", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                            Toast.makeText(context, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                       // } else if (purchaseDataList.get(getAdapterPosition()).COURSE_TYPE.equalsIgnoreCase("COURSES_LIVE")) {

                        //}
                   /*
                    }else {
                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
                    }
                    */
                }
            }

            //TODO get category type as of course,free tip or course_live
            //context.startActivity(new Intent(context, CourseDetailActivity.class).putExtra("course_detail",modelList.get(getAdapterPosition())).putExtra("Ctype",course_type));

        }
    }
}
