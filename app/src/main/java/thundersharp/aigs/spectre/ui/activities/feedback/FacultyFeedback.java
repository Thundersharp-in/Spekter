package thundersharp.aigs.spectre.ui.activities.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.NotifyUserRequest;
import thundersharp.aigs.spectre.core.notification.ExternalNotifiers;

public class FacultyFeedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_feedback);
        findViewById(R.id.close).setOnClickListener(f->finish());

        findViewById(R.id.lock).setOnClickListener(g->shoWInfo());
        findViewById(R.id.notification).setOnClickListener(n->shoWInfo());

        AutoCompleteTextView cat_text = findViewById(R.id.cat_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getFacultyList());
        cat_text.setAdapter(adapter);
        cat_text.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });


        AutoCompleteTextView sel_Sub = findViewById(R.id.selSub);
        ArrayAdapter<String> adapterSub = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getSubList());
        sel_Sub.setAdapter(adapterSub);
        sel_Sub.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        AutoCompleteTextView sel_Sem = findViewById(R.id.selSem);
        ArrayAdapter<String> adapterSem = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getSemList());
        sel_Sem.setAdapter(adapterSem);
        sel_Sem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        TextView values = findViewById(R.id.values);

        ((Slider)findViewById(R.id.range_slider)).addOnChangeListener(new Slider.OnChangeListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                switch ((int) value) {
                    case 0:
                        values.setText("Horrible");
                        break;
                    case 20:
                        values.setText("Below Average");
                        break;
                    case 40:
                        values.setText("Fine");
                        break;
                    case 60:
                        values.setText("Good");
                        break;
                    case 80:
                        values.setText("Excellent");
                        break;
                    case 100:
                        values.setText("Outstanding");
                        break;
                }
            }
        });

        findViewById(R.id.book).setOnClickListener(n->{
            NotifyUserRequest notifyUserRequest = new NotifyUserRequest("decnbH8AT4WCKqyKKLKyCd:APA91bHxyg5iKlyfXt1v3PnKkTGNeXLUk1STlMYV4BkZWFa-4p3F99mB4M8_bH_Vn5moIN9IUA-XMq0wBaydsxuOPa16ra_ssJoAUbjXGZUJYq__-WOv7BT1mbcI6b_I4AQMXCfSPc6q",
                    this,
                    "Notification request",
                    "First notification request sent from spekter client application.");
            new ExternalNotifiers().doInBackground(notifyUserRequest);
        });

    }

    private void shoWInfo() {
        new AlertDialog.Builder(this)
                .setMessage("Welcome to Acharya institutes of graduate studies's online faculty complaint/improvement feedback/positive feedback section, this section is managed by Thundersharp and your submission will be completely anonymous at any cost except any miss use of this portal.\n\n" +
                        "Your identity can be revelled to the college only if some vulgar/spam/harassment message is sent by this portal.\n\n" +
                        "Your feedback will sent directly to the concerned authorities quick resolution.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    private List<String> getFacultyList() {
        return new ArrayList<String>(Arrays.asList("Prof Yamuna P",
                "Prof Archana Bhaskar",
                "Prof Ranjana K K",
                "Prof Shweta",
                "Prof Pavitra",
                "Prof Ramakrishna Reddy",
                "Prof Rajesh Rao",
                "Prof Ravikiran R K",
                "Prof Shruti H K"));
    }

    private List<String> getSubList() {
        return new ArrayList<String>(Arrays.asList(
                "PST",
                "Banking and finance",
                "Java Programming",
                "Web Programming",
                "C language",
                "C++",
                "Operating System",
                "Discrete Mathematics",
                "Additional English",
                "General English",
                "Additional English",
                "Functional Kannada",
                "ADA",
                "TOC"));
    }

    private List<String> getSemList() {
        return new ArrayList<String>(Arrays.asList(
                "I SEMESTER",
                "II SEMESTER",
                "III SEMESTER",
                "IV SEMESTER",
                "V SEMESTER",
                "VI SEMESTER",
                "VII SEMESTER",
                "VIII SEMESTER"));
    }
}

