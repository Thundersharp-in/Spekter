package thundersharp.aigs.ratingbar.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import thundersharp.aigs.ratingbar.R;

/**
 * @author hrishikeshprateek
 */
public class FacultyFeedbackProgressView extends RelativeLayout {

    private View view;

    public FacultyFeedbackProgressView(Context context) {
        super(context);
        initializeViews(context,null);
    }

    public FacultyFeedbackProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
    }

    public FacultyFeedbackProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context,attrs);
    }

    protected synchronized void initializeViews(Context context,@Nullable AttributeSet attributeSet){
        view = inflate(context,R.layout.fac_prog_view,null);

    }
}
