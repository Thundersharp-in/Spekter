package thundersharp.aigs.newsletter.core.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.Browser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import thundersharp.aigs.newsletter.R;
import thundersharp.aigs.newsletter.core.helper.OfflineNewsHelper;
import thundersharp.aigs.newsletter.core.model.NewsLetters;
import thundersharp.aigs.newsletter.core.model.SharedPrefHelper;
import thundersharp.aigs.newsletter.core.utils.TimeUtils;
import thundersharp.aigs.newsletter.views.NewsLetterView;

public class HeadlinesAdapter extends RecyclerView.Adapter<HeadlinesAdapter.ViewHolder> {

    private Context context;
    private List<NewsLetters> articles;
    private Integer selected;

    public HeadlinesAdapter(Context context, List<NewsLetters> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_news,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsLetters model = articles.get(position);
        //Article a = articles.get(position);

        holder.title.setText(model.TITLE);
        Log.e("TITTLE"," "+model.TITLE);
        holder.description.setText(model.DESCRIPTION);
        holder.source.setText(model.SOURCE_NAME);

        String date = model.PUBLISHED_AT;
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        Date parsedDate = null;
        try {
            parsedDate = inputFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (parsedDate!=null) {
            String formattedDate = outputFormat.format(parsedDate);

            //System.out.println(formattedDate);

            holder.date.setText(formattedDate);
        }else {
            holder.date.setText(model.PUBLISHED_AT);
        }

        holder.view_source.setOnClickListener(b->{
            //Browser.loadUrl(context,model.getNews_url());
        });
        //String imageUrl = model.getNews_url();
        Glide.with(context)
                .load(model.URL_TO_IMAGE)
                .into(holder.imageView);

        Log.e("DATA",model.toString());


    }

    @Override
    public int getItemCount() {
        if (articles != null)
        return articles.size();
        else return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title,date,source, description;
        private ImageView imageView;
        private ImageView share_news,update_frequency;
        CheckBox chk_now;
        FloatingActionButton view_source;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            date = itemView.findViewById(R.id.tvDate);
            source = itemView.findViewById(R.id.tvSource);
            description = itemView.findViewById(R.id.tvDescription);
            share_news = itemView.findViewById(R.id.share_news);
            //chk_now = itemView.findViewById(R.id.chk_now);

            update_frequency = itemView.findViewById(R.id.update_frequency);

            imageView = itemView.findViewById(R.id.image);

            view_source = itemView.findViewById(R.id.view_source);

            share_news.setOnClickListener(this);
            update_frequency.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }

        @Override
        public void onClick(View view) {

            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
            View bottomview = LayoutInflater.from(context).inflate(R.layout.bottomsheet_share,itemView.findViewById(R.id.botomcontainer));

            TextView ddd_news = bottomview.findViewById(R.id.ddd_news);
            ddd_news.setText(articles.get(getAdapterPosition()).DESCRIPTION);

            ((ImageView)bottomview.findViewById(R.id.whatsapp)).setOnClickListener(n->{
                /*StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(articles.get(getAdapterPosition()).getNews_title()).append("\n");
                stringBuilder.append("By"+articles.get(getAdapterPosition()).getNews_author()).append("\n\n");
                stringBuilder.append(""+articles.get(getAdapterPosition()).getNews_description()).append("\n\n");
                stringBuilder.append("Published on"+articles.get(getAdapterPosition()).getNews_publishedAt()).append("\n\n\n");
                stringBuilder.append("Full article link: "+articles.get(getAdapterPosition()).getNews_url()).append("\n\n");
                stringBuilder.append("Sent from Warrior bulls application, for more stock related stuffs and news download now from playstore.").append("\n\n");
                stringBuilder.append("https://warriorbulls.page.link/download");*/

                shareExternally(new StringBuilder(),"com.whatsapp");

            });

            bottomSheetDialog.setContentView(bottomview);
            bottomSheetDialog.show();

        }
    }

    private List<String> getFoodTypeList() {
        ArrayList<String> customers = new ArrayList<>();
        customers.add("10 minutes");
        customers.add("30 Minutes");
        customers.add("5 hours");
        customers.add("12 hours");
        customers.add("24 hours");
        customers.add("48 hours");
        return customers;
    }

    protected void shareExternally(StringBuilder stringBuilder,String packageName){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString());
        sendIntent.setType("text/plain");
        sendIntent.setPackage(packageName);
        Intent shareIntent = Intent.createChooser(sendIntent, null);
        context.startActivity(shareIntent);
    }
}
