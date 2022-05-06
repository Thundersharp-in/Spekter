package thundersharp.aigs.spectre.ui.activities.passes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import thundersharp.aigs.newsletter.core.utils.TimeUtils;
import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.adapters.SlotTimeHolderAdapter;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.helpers.ProfileDataSync;
import thundersharp.aigs.spectre.core.interfaces.PassIssueWatcher;
import thundersharp.aigs.spectre.core.interfaces.ProfileSync;
import thundersharp.aigs.spectre.core.models.PassData;
import thundersharp.aigs.spectre.core.models.ProfileData;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.Progressbars;

public class BookPasses extends AppCompatActivity {

    private boolean toggle_cal, toggle_no_of_guest, toggle_time_slot;
    private CompactCalendarView compactCalendar_view;
    private TextView month_display,display_data;
    private RelativeLayout guest_container;
    private RecyclerView time_slots;
    private AlertDialog alertDialog;
    private boolean isSafe = false;
    private String bookingData,bookingTimestamp;
    private String timeSlot =null;

    public static Object time_slot;

    private ImageView slot_Icon;
    private List<String> timeSlotsda;

    private ProfileData profileData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_passes);

        profileData = ProfileDataSync.getInstance(this).initializeLocalStorage().pullDataBack();
        compactCalendar_view = findViewById(R.id.compactcalendar_view);
        guest_container = findViewById(R.id.guest_container);
        slot_Icon = findViewById(R.id.slot_icon);
        month_display = findViewById(R.id.months_display);
        compactCalendar_view.setFirstDayOfWeek(Calendar.MONDAY);
        time_slots = findViewById(R.id.time_slots);
        display_data = findViewById(R.id.display_data);
        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);
        alertDialog.show();

        timeSlotsda = new ArrayList<>();
        timeSlotsda.add("10-11");
        timeSlotsda.add("11-12");
        timeSlotsda.add("12-13");
        timeSlotsda.add("13-14");
        timeSlotsda.add("14-15");



        Date date = new Date();
        month_display.setText(TimeUtils.getMonthName(date.getMonth())+", "+(date.getYear()+1900));

        compactCalendar_view.setListener(new CompactCalendarView.CompactCalendarViewListener() {

            @Override
            public void onDayClick(Date dateClicked) {
                if (isSafe) {
                    if (dateClicked.getTime() < date.getTime() && dateClicked.getDate() != date.getDate()) {
                        Toast.makeText(BookPasses.this, "You cannot book for past dates select dates greater than or equal to today.", Toast.LENGTH_SHORT).show();
                        compactCalendar_view.setCurrentDate(date);
                        month_display.setText(TimeUtils.getMonthName(date.getMonth()) + ", " + (date.getYear() + 1900));

                    } else {
                        //bookingDate = dateClicked;
                        new AlertDialog.Builder(BookPasses.this)
                                .setMessage("Since this is a one day day event which will be held on "+bookingData+" therefore the date is automatically selected.")
                                .setTitle("Info")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();

                        display_data.setText("I will visit on  " + bookingData);
                        compactCalendar_view.hideCalendarWithAnimation();
                        toggle_cal = true;
                        month_display.setVisibility(View.GONE);


                    }
                }else {
                    new AlertDialog.Builder(BookPasses.this)
                            .setMessage("Error cant proceed for pass booking, contact organisers.")
                            .setTitle("ERROR CE454")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss()).show();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                month_display.setText(TimeUtils.getMonthName(firstDayOfNewMonth.getMonth())+", "+(firstDayOfNewMonth.getYear()+1900));
            }
        });


        findViewById(R.id.noOfGuest).setOnClickListener(b -> {
            Snackbar
                    .make(findViewById(R.id.container),"Currently entry is restricted to one person per account !!", BaseTransientBottomBar.LENGTH_LONG)
                    .setBackgroundTint(Color.GREEN)
                    .show();
          /*  if (toggle_no_of_guest){
                guest_container.setVisibility(View.VISIBLE);
                //drop_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_arrow_drop_up_24));
                toggle_no_of_guest = false;
            }else {
                guest_container.setVisibility(View.GONE);
                //drop_icon.setImageDrawable(getResources().getDrawable(R.drawable.ccp_ic_arrow_drop_down));
                toggle_no_of_guest = true;
            }*/
        });

        FirebaseDatabase
                .getInstance()
                .getReference(CONSTANTS.EXHIBITION)
                .child(CONSTANTS.COMMON_DATA)
                .child("EVENT_DATE")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            try {
                                String da = TimeUtils.getDateFromTimeStamp(snapshot.getValue(String.class));
                                bookingData = da;
                                //Toast.makeText(BookPasses.this, da+"", Toast.LENGTH_SHORT).show();
                                time_slots.setLayoutManager(new GridLayoutManager(BookPasses.this, 3));
                                time_slots.setAdapter(new SlotTimeHolderAdapter(timeSlotsda));
                                isSafe = true;
                            }catch (Exception e){
                                isSafe = false;
                                Toast.makeText(BookPasses.this, "Error cant proceed for pass booking, contact organisers", Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            isSafe=false;
                            Toast.makeText(BookPasses.this, "Error cant proceed for pass booking, contact organisers", Toast.LENGTH_SHORT).show();

                        }

                        alertDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        alertDialog.dismiss();
                        isSafe = false;
                        Toast.makeText(BookPasses.this, "Server Error cant proceed for pass booking, contact organisers", Toast.LENGTH_SHORT).show();
                    }
                });

        findViewById(R.id.relative_container_cal).setOnClickListener(b -> {
            if (!compactCalendar_view.isAnimating()) {
                if (toggle_cal) {
                    compactCalendar_view.showCalendarWithAnimation();
                    month_display.setVisibility(View.VISIBLE);
                    toggle_cal = false;
                } else {
                    compactCalendar_view.hideCalendarWithAnimation();
                    month_display.setVisibility(View.GONE);
                    toggle_cal = true;
                }
            }
        });

        slot_Icon.setOnClickListener(n->{
            if (toggle_time_slot){
                time_slots.setVisibility(View.VISIBLE);
                toggle_time_slot = false;
            }else {
                time_slots.setVisibility(View.GONE);
                toggle_time_slot = true;
            }
        });


        findViewById(R.id.time_container).setOnClickListener(n->{
            if (toggle_time_slot){
                time_slots.setVisibility(View.VISIBLE);
                toggle_time_slot = false;
            }else {
                time_slots.setVisibility(View.GONE);
                toggle_time_slot = true;
            }
        });

        findViewById(R.id.book_button).setOnClickListener(n->{
            if (!isSafe || timeSlot == null){
                Snackbar
                        .make(findViewById(R.id.container),"Select all parameters correctly.", BaseTransientBottomBar.LENGTH_LONG)
                        .setBackgroundTint(Color.RED)
                        .show();

            }else issuePass();
        });

        registerReceiver(broadcastReceiver,new IntentFilter("posSlot"));
    }

    private void issuePass() {
        if (!profileData.acharyan) {
            new AlertDialog.Builder(this)
                    .setTitle("Sorry :( you cant book passes for this event as this event is only for acharyan.")
                    .setPositiveButton("UNDERSTOOD", (dialogInterface, i) -> dialogInterface.dismiss()).show();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("If you have previous booking for the event it will be automatically canceled with this booking !!")
                    .setPositiveButton("PROCEED", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        issuePassDb();
                    }).setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.dismiss()).show();

        }
    }

    private void issuePassDb() {
        alertDialog.show();
        DatabaseHelpers
                .getInstance()
                .setPassData(new PassData(timeSlot,profileData.name, profileData.phone, FirebaseAuth.getInstance().getUid(),profileData.email))
                .setPassIssueWatcher(new PassIssueWatcher() {
                    @Override
                    public void OnPassIssued(PassData passData) {
                        Toast.makeText(BookPasses.this, "Pass generated view from top pass view icon.", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                    }

                    @Override
                    public void OnPassNotIssued(Exception e) {
                        alertDialog.dismiss();
                        Toast.makeText(BookPasses.this, "Pass not issued : "+e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                });
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            timeSlot = intent.getStringExtra("timeSlot");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}