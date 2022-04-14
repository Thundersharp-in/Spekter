package thundersharp.aigs.spectre.ui.activities.feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import thundersharp.aigs.spectre.R;

public class FacultyFeedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_feedback);
        findViewById(R.id.close).setOnClickListener(f->finish());

        findViewById(R.id.notification).setOnClickListener(n->new AlertDialog.Builder(this)
                .setMessage("Welcome to Acharya institutes of graduate studies's online faculty complaint/improvement feedback/positive feedback section, this section is managed by Thundersharp and your submission will be completely anonymous at any cost except any miss use of this portal.\n\n" +
                        "Your identity can be revelled to the college only if some vulgar/spam/harassment message is sent by this portal.\n\n" +
                        "Your feedback is directly sent to HOD'S And Principal for quick resolution.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show());

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

