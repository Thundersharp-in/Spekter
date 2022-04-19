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

public class WorkShopDetailsAdapter extends RecyclerView.Adapter<WorkShopDetailsAdapter.ViewHoldr> {

    String[] time;
    Integer selectedPos;
    Context context;

    public WorkShopDetailsAdapter(String[] time, Context context) {
        this.time = time;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHoldr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHoldr(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workshop_heighlights,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoldr holder, int position) {
        holder.text.setText(time[position]);
    }

    @Override
    public int getItemCount() {
        if (time != null) return time.length; else return 0;
    }

    class ViewHoldr extends RecyclerView.ViewHolder {

        private TextView text;
        //private RelativeLayout container;


        public ViewHoldr(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.timeHOlder);
            //container = itemView.findViewById(R.id.container);

        }

    }

}
