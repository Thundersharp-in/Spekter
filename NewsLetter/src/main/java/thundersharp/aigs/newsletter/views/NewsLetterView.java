package thundersharp.aigs.newsletter.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.List;

import thundersharp.aigs.newsletter.R;
import thundersharp.aigs.newsletter.core.adapter.HeadlinesAdapter;
import thundersharp.aigs.newsletter.core.helper.OfflineNewsHelper;
import thundersharp.aigs.newsletter.core.model.NewsLetters;

/**
 * @author hrishikeshprateek
 * This View Handles all the newsletters and displays them as vertical scroll views
 * @see android.graphics.drawable.Drawable.Callback
 * @see android.view.View
 * @see android.view.ViewGroup
 * @see android.widget.RelativeLayout
 */
public class NewsLetterView extends RelativeLayout {

    private View view;

    private HeadlinesAdapter adapter;
    private ViewPager2 viewPager2;
    private OfflineNewsHelper offlineNewsHelper;

    public NewsLetterView(@NonNull Context context) {
        super(context);
        initViews(context,null);
    }

    public NewsLetterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context,attrs);
    }

    public NewsLetterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs);
    }

    private void initViews(@NonNull Context context, @Nullable AttributeSet attrs) {
        view = inflate(context, R.layout.news_letter_view,this);
        offlineNewsHelper = new OfflineNewsHelper(context);

        viewPager2 = view.findViewById(R.id.pager);
        reload();
    }

    private void reload(){

        FirebaseDatabase
                .getInstance()
                .getReference("NEWS_LETTERS")
                .limitToLast(30)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            int i =0;
                            offlineNewsHelper.dropTable();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                NewsLetters newsLetters = dataSnapshot.getValue(NewsLetters.class);
                                offlineNewsHelper.addContact(newsLetters,i);
                                i++;
                            }
                            List<NewsLetters> data = offlineNewsHelper.getAllNews();
                            Collections.sort(data);
                            adapter = new HeadlinesAdapter(getContext(),data);
                            offlineNewsHelper.saveCurrentUpdateTime();
                            viewPager2.setAdapter(adapter);
                        }else Toast.makeText(getContext(), "Data not found", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
