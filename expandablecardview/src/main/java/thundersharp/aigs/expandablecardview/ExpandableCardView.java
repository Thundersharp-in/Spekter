package thundersharp.aigs.expandablecardview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * @author hrishikeshprateek
 * @see android.view.ViewGroup
 * @see android.view.ViewGroup
 * @see androidx.cardview.widget.CardView
 */
public class ExpandableCardView extends RelativeLayout {

    private View view;

    public ExpandableCardView(@NonNull Context context) {
        super(context);
        inflateViews(context,null);
    }

    public ExpandableCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateViews(context, attrs);
    }

    public ExpandableCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateViews(context, attrs);
    }


    private void inflateViews(@NonNull Context context, @Nullable AttributeSet attrs){
        view = inflate(context, R.layout.expandable_card_view,this);
    }

}