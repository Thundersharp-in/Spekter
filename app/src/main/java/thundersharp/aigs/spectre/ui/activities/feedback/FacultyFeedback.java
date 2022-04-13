package thundersharp.aigs.spectre.ui.activities.feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import thundersharp.aigs.spectre.R;

public class FacultyFeedback extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_feedback);
        findViewById(R.id.close).setOnClickListener(f->finish());

        findViewById(R.id.notification).setOnClickListener(n->new AlertDialog.Builder(this)
                .setMessage("Welcome to Acharya institutes of graduate studies's online faculty complaint/improvement feedback/positive feedback section, this section is managed by Thundersharp and your submission will be completely anonymous at any cost except any miss use of this portal.\n\nYour identity can be revelled to the college only if some vulgar/spam/harassment message is sent by this portal.\n\nYour feedback is directly sent to HOD'S And Principal for quick resolution.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show());

    }
}