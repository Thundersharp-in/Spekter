package thundersharp.aigs.spectre.ui.activities.lectures;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
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
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.AboutCourseAdapter;
import thundersharp.aigs.spectre.core.exceptions.InternalException;
import thundersharp.aigs.spectre.core.helpers.ProfileDataSync;
import thundersharp.aigs.spectre.core.models.Payment_Data;
import thundersharp.aigs.spectre.core.models.ProfileData;
import thundersharp.aigs.spectre.core.models.PurchaseData;
import thundersharp.aigs.spectre.core.models.ServiceItemModel;
import thundersharp.aigs.spectre.core.models.SubscriptionDetails;
import thundersharp.aigs.spectre.core.models.TransactionModel;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.Progressbars;
import thundersharp.aigs.spectre.core.utils.ServiceChecker;

public class CourseDetailActivity extends AppCompatActivity {

    private ServiceItemModel model;
    private CircleImageView s_img;
    private TextView s_name, subscribe, s_chat, s_creator, s_desc,
            s_lang, s_level, p_desc, hashtags, duration, viewCourseBtn;
    private RecyclerView s_will_learn, to_know_before;
    private String Ctype;
    private Intent intent;
    BottomSheetDialog bottomSheetDialog;
    private AlertDialog alertDialog;
    private ProfileData profileDataSync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);
        profileDataSync = ProfileDataSync.getInstance(this).initializeLocalStorage().pullDataBack();

        ((Toolbar) findViewById(R.id.tool)).setNavigationOnClickListener(t -> finish());
        initViews();

        if (getIntent().getSerializableExtra("course_detail") != null && profileDataSync != null) {
            model = (ServiceItemModel) getIntent().getSerializableExtra("course_detail");
            Ctype = getIntent().getStringExtra("Ctype");
        } else {
            Toast.makeText(this, "Currently Course unavailable for you !", Toast.LENGTH_SHORT).show();
            finish();
        }

        ServiceChecker
                .getInstance()
                .setServiceId(model.COARSE_ID)
                .build()
                .setListner(new ServiceChecker.listner() {
                    @Override
                    public void onDataPathOk() {
                        //showSubSheet();
                        subscribe.setText("SUBSCRIBE");
                        alertDialog.dismiss();
                    }

                    @Override
                    public void dataExists(Payment_Data payment_data) {
                        if (profileDataSync.acharyan) {

                            if (payment_data.PAYMENT_STATUS.equals("1")) {
                                subscribe.setText("UNSUBSCRIBE");
                                alertDialog.dismiss();

                            } else {
                                Toast.makeText(CourseDetailActivity.this, "Previous subscription failed retry", Toast.LENGTH_SHORT).show();
                                //showSubSheet();
                                subscribe.setText("SUBSCRIBE");

                            }
                        }else {
                            Toast.makeText(CourseDetailActivity.this, "Accessible only by acharyans.", Toast.LENGTH_SHORT).show();
                            subscribe.setText("UNAVAILABLE");
                        }
                    }

                    @Override
                    public void dataError(Exception e) {
                        subscribe.setText("ERROR");
                        if (e instanceof InternalException) {

                            Toast.makeText(CourseDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        } else
                            Toast.makeText(CourseDetailActivity.this, "Filed error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


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
        makeTextViewResizable(s_desc, 6, "See more", true);

        s_lang.setText("Coarse Language: " + model.LANGUAGE);
        s_level.setText("Difficulty level: " + model.LEVEL);
        duration.setText(model.COARSE_DURATION.substring(0, model.COARSE_DURATION.indexOf("&"))
                + ", total " + model.COARSE_DURATION.substring(model.COARSE_DURATION.indexOf("&") + 2));
        p_desc.setText("This coarse is priced ₹" + model.PRICE + "/- only");
        hashtags.setText(model.HASHTAGS);

        String[] list_items = model.MUST_KNOW_TOPICS.split("\\.");
        List<String> list = new ArrayList<>(Arrays.asList(list_items));
        to_know_before.setAdapter(new AboutCourseAdapter(this, list));

        String[] list_items1 = model.WILL_LEARN.split("\\.");
        List<String> list1 = new ArrayList<>(Arrays.asList(list_items1));
        s_will_learn.setAdapter(new AboutCourseAdapter(this, list1));


        subscribe.setOnClickListener(view -> {
            alertDialog.show();
            if (profileDataSync.acharyan) {
                if (subscribe.getText().toString().equalsIgnoreCase("SUBSCRIBE")) {
                    showSubSheet();
                } else if (subscribe.getText().toString().equalsIgnoreCase("UNSUBSCRIBE")) {
                    showUnSubSheet();
                }
            }else {
                Toast.makeText(this, "Action restricted for you !", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        s_chat.setOnClickListener(t -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("email/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact_spekter@acharya.ac.in"});
            intent.putExtra(Intent.EXTRA_SUBJECT,"Support on "+model.COURSE_NAME+" course.");
            intent.putExtra(Intent.EXTRA_TEXT, new String("__________________________________________________________\n"));
            intent.putExtra(Intent.EXTRA_TEXT, new String(
                    "DEVICE: "+ Build.DEVICE + "\nMANUFACTURER: "
                            + Build.MANUFACTURER + "\nBOOTLOADER: "
                            + Build.BOOTLOADER + "\nPRODUCT: "
                            + Build.PRODUCT + "\nUSER: "
                            + Build.USER + "\nDEVICE ID: "
                            + Build.ID + "\nCPU_ABI: "
                            + Build.CPU_ABI + "\n\n"
                            +"COURSE NAME : "+model.COURSE_NAME+"\n"
                            +"COURSE ID : "+model.COARSE_ID+"\n\n"
                            + "________Your message after here _______\n"));
            Intent mailer = Intent.createChooser(intent, "Choose a email app to send support message for courses.");
            startActivity(mailer);
        });

        viewCourseBtn.setOnClickListener(view -> {
            if (profileDataSync.acharyan) {
                startActivity(new Intent(this, CoarseContents.class)
                        .putExtra("coarse_id", model.COARSE_ID)
                        .putExtra("coarse_name", model.COURSE_NAME)
                        .putExtra("by", model.COARSE_BY));
            }else {
                Toast.makeText(this, "Action restricted for you !", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }

    private void showSubSheet() {
        alertDialog.dismiss();
        new AlertDialog.Builder(this)
                .setMessage("Subscribe to " + model.COURSE_NAME + "\nby " + model.COARSE_BY)
                .setCancelable(false)
                .setPositiveButton("SUBSCRIBE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setSubscribe("pay_1234567");
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }

    private void showUnSubSheet() {
        alertDialog.dismiss();
        new AlertDialog.Builder(this)
                .setMessage("Unsubscribe from " + model.COURSE_NAME + "\nby " + model.COARSE_BY)
                .setCancelable(false)
                .setPositiveButton("UNSUBSCRIBE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.show();
                        setUnSubscribe();
                        alertDialog.dismiss();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
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


            ssb.setSpan(new MySpannable(false) {
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

    private void setSubscribe(String s){
        alertDialog.show();
        Integer type = 0;
        switch (Ctype){
            case CONSTANTS.DATABASE_COURSES:
                type = 1;
                break;
            case CONSTANTS.DATABASE_COURSES_FREE:
                type = 2;
                break;
            case CONSTANTS.DATABASE_COURSES_LIVE:
                type = 3;
                break;
            default:
                type = 1;
                break;
        }
        HashMap<String, Object> updateDataRequest = new HashMap<>();
        Payment_Data payment_data = new Payment_Data(s,"1","");

        PurchaseData purchaseData = new PurchaseData(model.COARSE_ID,
                model.PRICE,
                model.COURSE_NAME,
                Ctype,
                "false",
                payment_data);
        TransactionModel transactionModel = new TransactionModel(String.valueOf(type),
                s,
                "1",
                model.COARSE_ID);

        updateDataRequest.put(CONSTANTS.DATABASE_USER_DATA
                        +"/"+FirebaseAuth.getInstance().getUid()
                        +"/"+CONSTANTS.DATABASE_PURCHASED_SERVICES
                        +"/"+model.COARSE_ID,
                purchaseData);

        updateDataRequest.put(CONSTANTS.DATABASE_PLAN_COURSE_ENROLLMENT_DATA
                        +"/"+Ctype
                        +"/"+model.COARSE_ID
                        +"/"+FirebaseAuth.getInstance().getUid(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
                        +"Ѱ"+ FirebaseAuth.getInstance().getCurrentUser().getEmail()
                        +"π"+ FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()
                        +"Ω1ηfalse");

        updateDataRequest.put(CONSTANTS.DATABASE_USER_DATA
                        +"/"+FirebaseAuth.getInstance().getUid()
                        +"/"+CONSTANTS.DATABASE_TRANSACTIONS
                        +"/"+model.COARSE_ID,
                transactionModel);

        FirebaseDatabase
                .getInstance()
                .getReference()
                .updateChildren(updateDataRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CourseDetailActivity.this, "Subscription completed!", Toast.LENGTH_LONG).show();
                        subscribe.setText("UNSUBSCRIBE");
                        alertDialog.dismiss();
                        startActivity(new Intent(CourseDetailActivity.this,CoarseContents.class)
                                .putExtra("coarse_id",model.COARSE_ID)
                                .putExtra("coarse_name",model.COURSE_NAME)
                                .putExtra("by",model.COARSE_BY));
                    } else {
                        subscribe.setText("GET COURSE");
                        Toast.makeText(CourseDetailActivity.this,
                                "Could not complete subscription contact support for your refund if not generated automatically",
                                Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                    }
                });
    }

    private synchronized void setUnSubscribe(){
        alertDialog.show();
        Integer type = 0;
        switch (Ctype){
            case CONSTANTS.DATABASE_COURSES:
                type = 1;
                break;
            case CONSTANTS.DATABASE_COURSES_FREE:
                type = 2;
                break;
            case CONSTANTS.DATABASE_COURSES_LIVE:
                type = 3;
                break;
            default:
                type = 1;
                break;
        }
        HashMap<String, Object> updateDataRequest = new HashMap<>();

        updateDataRequest.put(CONSTANTS.DATABASE_USER_DATA
                        +"/"+FirebaseAuth.getInstance().getUid()
                        +"/"+CONSTANTS.DATABASE_PURCHASED_SERVICES
                        +"/"+model.COARSE_ID,
                null);

        updateDataRequest.put(CONSTANTS.DATABASE_PLAN_COURSE_ENROLLMENT_DATA
                        +"/"+Ctype
                        +"/"+model.COARSE_ID
                        +"/"+FirebaseAuth.getInstance().getUid(),
                null);

        updateDataRequest.put(CONSTANTS.DATABASE_USER_DATA
                        +"/"+FirebaseAuth.getInstance().getUid()
                        +"/"+CONSTANTS.DATABASE_TRANSACTIONS
                        +"/"+model.COARSE_ID,
                null);

        FirebaseDatabase
                .getInstance()
                .getReference()
                .updateChildren(updateDataRequest)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(CourseDetailActivity.this, "Unsubscribed!", Toast.LENGTH_LONG).show();
                        subscribe.setText("SUBSCRIBE");

                    } else {
                        subscribe.setText("UNSUBSCRIBE");
                        Toast.makeText(CourseDetailActivity.this,
                                "Could not complete action contact support for your refund if not generated automatically",
                                Toast.LENGTH_LONG).show();
                    }
                    alertDialog.dismiss();
                });
    }
}