package thundersharp.aigs.spectre.core.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.Testimonials;

public class CustomPagerAdapter extends RecyclerView.Adapter<CustomPagerAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Testimonials[] mResources;

    public CustomPagerAdapter(Context context, Testimonials[] resources) {
        mContext = context;
        mResources = resources;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pager_item,parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Glide.with(holder.itemView.getContext()).load(mResources[position].photoUri).into(holder.photo);
            holder.data.setText(mResources[position].message);
            holder.name.setText(mResources[position].name);
            holder.design.setText(mResources[position].designation);
        }catch (Exception exception){
            Toast.makeText(mContext, exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        if (mResources != null) return mResources.length;else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        private TextView data,name,design;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            data = itemView.findViewById(R.id.data);
            name = itemView.findViewById(R.id.name);
            design = itemView.findViewById(R.id.design);
            photo = itemView.findViewById(R.id.photo);
        }
    }


}