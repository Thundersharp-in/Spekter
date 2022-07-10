package thundersharp.aigs.spectre.core.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.List;

import thundersharp.aigs.newsletter.core.utils.TimeUtils;
import thundersharp.aigs.pdfviwer.PdfLoader;
import thundersharp.aigs.pdfviwer.PdfModel;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.Initiative;

public class InitiativeAdapter extends RecyclerView.Adapter<InitiativeAdapter.ViewHolder>{

    Activity context;
    List<Initiative> list;


    public InitiativeAdapter(Activity context, List<Initiative> list) {
        this.context = context;
        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InitiativeAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_aigs_initiatives,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {

            Initiative initiative = list.get(position);
            holder.tittle.setText(initiative.NAME);
            holder.started_by.setText(initiative.STARTED_BY);
            holder.started_on.setText(TimeUtils.getTimeInStringFromTimeStamp(initiative.ID));
            if (!initiative.COVER_PIC.isEmpty()){
                Glide.with(holder.itemView).load(initiative.COVER_PIC).into(holder.imageView);
            }
        }catch (Exception e){
            Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int getItemCount() {
        if (list != null ) return list.size(); else return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView pinItem;
        TextView tittle,started_by,started_on;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pinItem = itemView.findViewById(R.id.pinner);
            tittle = itemView.findViewById(R.id.tittle);
            started_by = itemView.findViewById(R.id.started_by);
            started_on = itemView.findViewById(R.id.started_on);
            imageView = itemView.findViewById(R.id.profile_pic);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            try {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_initative);
                LinearLayout pledge = bottomSheetDialog.findViewById(R.id.pledge);
                LinearLayout readMore = bottomSheetDialog.findViewById(R.id.readMore);
                LinearLayout share = bottomSheetDialog.findViewById(R.id.share);
                TextView plegge = bottomSheetDialog.findViewById(R.id.pledgeT);
                TextView top = bottomSheetDialog.findViewById(R.id.top);
                top.setText(list.get(getAdapterPosition()).NAME);
                plegge.setText(list.get(getAdapterPosition()).PLEDGE);

                pledge.setOnClickListener(h -> {
                    if (list.get(getAdapterPosition()).PLEDGE_URL != null) {
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(Color.parseColor("#262626"));
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.launchUrl(context, Uri.parse(list.get(getAdapterPosition()).PLEDGE_URL));
                    } else {
                        Toast.makeText(context, "Error cannot proceed !", Toast.LENGTH_SHORT).show();
                    }
                });

                readMore.setOnClickListener(k -> {
                    PdfLoader.getInstance(view.getContext())
                            .setPdfData(new PdfModel("", list.get(getAdapterPosition()).READ_URL, "External Link", ""))
                            .setSecureMode(false)
                            .loadPdf();

                });

                share.setOnClickListener(k->generateShareLink());

                bottomSheetDialog.show();
            }catch (Exception e){
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        private synchronized void generateShareLink() {
            String url = "https://spekteraigs.page.link/initiative/?"+list.get(getAdapterPosition()).ID;
            FirebaseDynamicLinks.getInstance().createDynamicLink()
                    .setLink(Uri.parse(url))
                    .setDomainUriPrefix("https://spekteraigs.page.link")
                    .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                    .buildShortDynamicLink()
                    .addOnCompleteListener(context, new OnCompleteListener<ShortDynamicLink>() {
                        @Override
                        public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                            if (task.isSuccessful()) {
                                // Short link created
                                Uri shortLink = task.getResult().getShortLink();
                                Uri flowchartLink = task.getResult().getPreviewLink();

                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT,"Hey,\n\nLook at this initiative taken by AIGS,"+

                                        "\n\nTo view more about this Initiative download the Spekter app."+
                                        "\nInitiative link : "+shortLink+
                                        "\n\nSpekter App link : https://play.google.com/store/apps/details?id=thundersharp.aigs.spectre");
                                sendIntent.setType("text/plain");
                                context.startActivity(sendIntent);
                            } else {
                                Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                // Error
                                // ...
                            }
                        }
                    });

        }
    }


}
