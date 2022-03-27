package thundersharp.aigs.newsletter.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import thundersharp.aigs.newsletter.R;

/**
 * @author hrishikeshprateek
 * This View Handles all the newsletters and displays them as vertical scroll views
 * @see android.graphics.drawable.Drawable.Callback
 * @see android.view.View
 * @see android.view.ViewGroup
 * @see android.widget.RelativeLayout
 */
public class NewsLetterView extends RecyclerView {

    private View view;


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

    private void initViews(Context context, AttributeSet attrs) {
        view = inflate(context, R.layout.news_letter_view,this);
    }
}
