package thundersharp.aigs.spectre.core.adapters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.ui.activities.passes.BookPasses;

public class WorkShopHeilightsAdapter extends RecyclerView.Adapter<WorkShopHeilightsAdapter.ViewHoldr> {

    List<String> time;
    Integer selectedPos;

    public WorkShopHeilightsAdapter(List<String> time) {
        this.time = time;
    }


    @NonNull
    @Override
    public ViewHoldr onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHoldr(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chip,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoldr holder, int position) {
        holder.chip.setText(time.get(position));
    }

    @Override
    public int getItemCount() {
        if (time != null) return time.size(); else return 0;
    }

    class ViewHoldr extends RecyclerView.ViewHolder {

        //private TextView timeH;
        //private RelativeLayout container;
        private Chip chip;

        public ViewHoldr(@NonNull View itemView) {
            super(itemView);
            chip = itemView.findViewById(R.id.chip_items);
            //timeH = itemView.findViewById(R.id.timeHOlder);
            //container = itemView.findViewById(R.id.container);

        }

    }

}
