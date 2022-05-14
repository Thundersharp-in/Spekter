package thundersharp.aigs.spectre.ui.activities.lectures;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.AboutCourseAdapter;
import thundersharp.aigs.spectre.core.models.ServiceItemModel;

public class CourseDetailActivity extends AppCompatActivity {

    private ServiceItemModel model;
    private CircleImageView s_img;
    private TextView s_name, subscribe, s_chat, s_creator, s_desc,
            s_lang, s_level,p_desc,hashtags, duration, viewCourseBtn;
    private RecyclerView s_will_learn, to_know_before;
    private String Ctype;
    private Intent intent;
    BottomSheetDialog bottomSheetDialog;
    //private PurchaseData paymentdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);


        initViews();

        if (getIntent().getSerializableExtra("course_detail")!=null){
            model = (ServiceItemModel) getIntent().getSerializableExtra("course_detail");
            Ctype = getIntent().getStringExtra("Ctype");
        }else {
            Toast.makeText(this, "Currently Course unavailable!", Toast.LENGTH_SHORT).show();
            finish();
        }

        subscribe.setText("SUBSCRIBE");

       /* if (getIntent().getSerializableExtra("payment_status") != null){
            //paymentdata = (PurchaseData) getIntent().getSerializableExtra("payment_status");
            if (paymentdata.PAYMENT.PAYMENT_STATUS.equals("1")) subscribe.setText("VIEW COURSE");
            else subscribe.setText("GET COURSE");
        }else {
            subscribe.setText("SUBSCRIBE");
        }*/

        Glide.with(this).load(model.COURSE_ICON).into(s_img);
        s_name.setText(model.COURSE_NAME);
        s_creator.setText(model.COARSE_BY);

        s_desc.setText(model.COARSE_DESCRIPTION);
        makeTextViewResizable(s_desc,6,"See more",true);

        s_lang.setText("Coarse Language: "+model.LANGUAGE);
        s_level.setText("Difficulty level: "+model.LEVEL);
        duration.setText(model.COARSE_DURATION.substring(0,model.COARSE_DURATION.indexOf("&"))
                +", total "+model.COARSE_DURATION.substring(model.COARSE_DURATION.indexOf("&")+2));
        p_desc.setText("This coarse is priced ₹"+model.PRICE+"/- only");
        hashtags.setText(model.HASHTAGS);

        String[] list_items = model.MUST_KNOW_TOPICS.split("\\.");
        List<String> list = new ArrayList<>(Arrays.asList(list_items));
        to_know_before.setAdapter(new AboutCourseAdapter(this,list));

        String[] list_items1 = model.WILL_LEARN.split("\\.");
        List<String> list1= new ArrayList<>(Arrays.asList(list_items1));
        s_will_learn.setAdapter(new AboutCourseAdapter(this,list1));


        subscribe.setOnClickListener(view-> {


        });


        viewCourseBtn.setOnClickListener(view->{
            startActivity(new Intent(this,CoarseContents.class)
                    .putExtra("coarse_id",model.COARSE_ID)
                    .putExtra("coarse_name",model.COURSE_NAME)
                    .putExtra("by",model.COARSE_BY));
        });
    }

    private void initViews() {
        s_img = findViewById(R.id.s_img);
        s_name = findViewById(R.id.s_name);
        subscribe = findViewById(R.id.subscribe);
        s_chat = findViewById(R.id.s_chat);
        s_creator = findViewById(R.id.s_creator);
        s_desc = findViewById(R.id.s_desc);
        s_lang = findViewById(R.id.s_lang);
        s_level = findViewById(R.id.s_level);
        duration = findViewById(R.id.duration);
        viewCourseBtn = findViewById(R.id.viewCourseBtn);
        s_will_learn = findViewById(R.id.s_will_learn);
        to_know_before = findViewById(R.id.to_know_before);
        p_desc = findViewById(R.id.p_desc);
        hashtags = findViewById(R.id.hashtags);
    }

    public static void makeTextViewResizable(final TextView tv,
                                             final int maxLine,
                                             final String expandText,
                                             final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned,
                                                                            final TextView tv,
                                                                            final int maxLine,
                                                                            final String spanableText,
                                                                            final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, ".. See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#1b76d3"));
        }

        @Override
        public void onClick(View widget) {


        }
    }

}