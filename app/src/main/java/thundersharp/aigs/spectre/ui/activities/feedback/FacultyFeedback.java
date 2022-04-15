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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.helpers.DatabaseHelpers;
import thundersharp.aigs.spectre.core.helpers.ProfileDataSync;
import thundersharp.aigs.spectre.core.interfaces.FeedbackObserver;
import thundersharp.aigs.spectre.core.models.NotifyUserRequest;
import thundersharp.aigs.spectre.core.models.StudentsDetails;
import thundersharp.aigs.spectre.core.notification.ExternalNotifiers;
import thundersharp.aigs.spectre.core.notification.NotifiAll;
import thundersharp.aigs.spectre.core.utils.Progressbars;

public class FacultyFeedback extends AppCompatActivity {

    private AlertDialog alertDialog;
    private ProfileDataSync profileDataSync;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_feedback);
        findViewById(R.id.close).setOnClickListener(f->finish());
        alertDialog = Progressbars.getInstance().createDefaultProgressBar(this);
        profileDataSync = ProfileDataSync.getInstance(this).initializeLocalStorage();

        findViewById(R.id.lock).setOnClickListener(g->shoWInfo());
        findViewById(R.id.notification).setOnClickListener(n->shoWInfo());
        message = findViewById(R.id.message);

        AutoCompleteTextView cat_text = findViewById(R.id.cat_text);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getFacultyList());
        cat_text.setAdapter(adapter);


        AutoCompleteTextView sel_Sub = findViewById(R.id.selSub);
        ArrayAdapter<String> adapterSub = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, getSubList());
        sel_Sub.setAdapter(adapterSub);

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
            if (cat_text.getText().toString().isEmpty()){
                cat_text.setError("Faculty Selection required.");
                cat_text.requestFocus();
            }else if (sel_Sub.getText().toString().isEmpty()){
                sel_Sub.setError("Subject Selection required.");
                sel_Sub.requestFocus();
            }else if (sel_Sem.getText().toString().isEmpty()){
                sel_Sem.setError("Semester Selection required.");
                sel_Sem.requestFocus();
            }else if (message.getText().toString().isEmpty()){
                message.setError("Feedback required.");
                message.requestFocus();
            }else {
                StudentsDetails studentsDetails = new StudentsDetails(FirebaseAuth.getInstance().getCurrentUser().getEmail(),System.currentTimeMillis()+"",profileDataSync.pullDataBack().name+"",profileDataSync.pullDataBack().phone);
                alertDialog.show();
                DatabaseHelpers
                        .getInstance()
                        .setActivity(this)
                        .setFacultyFeedback(new thundersharp.aigs.spectre.core.models.FacultyFeedback(cat_text.getText().toString(),values.getText().toString(),studentsDetails.ID,message.getText().toString(),sel_Sub.getText().toString(),sel_Sem.getText().toString()))
                        .setStudentDetails(studentsDetails)
                        .setFeedbackObserver(new FeedbackObserver() {
                            @Override
                            public void OnFeedbackSent(JSONObject jsonObject) {
                                new NotifiAll().doInBackground(new NotifyUserRequest("",FacultyFeedback.this,"New Faculty feedback received","Feedback about "+cat_text.getText().toString()+": "+message.getText().toString()));
                                alertDialog.dismiss();
                                finish();
                                Toast.makeText(FacultyFeedback.this, "Sent feedback.\n\n"+jsonObject.toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void OnError(Exception e) {
                                alertDialog.dismiss();
                                Toast.makeText(FacultyFeedback.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
            }
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

