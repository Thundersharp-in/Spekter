package thundersharp.aigs.spectre.ui.activities.exhibition;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;


public class ScannerProjectInfo extends AppCompatActivity {

    private TextView category;
    private ProjectBasicInfo projectBasicInfo;

    private SeekBar seekBar;
    private EditText amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_project_info);
        projectBasicInfo = (ProjectBasicInfo) getIntent().getSerializableExtra("projects_basic_info");

        if (projectBasicInfo == null) {
            finish();
            Toast.makeText(this, "Cannot go to the requested page !", Toast.LENGTH_SHORT).show();
        }

        category = findViewById(R.id.by);
        ((TextView) findViewById(R.id.tittle)).setText(projectBasicInfo.NAME);
        ((TextView) findViewById(R.id.short_desc)).setText(projectBasicInfo.SHORT_DESCRIPTION);

        if (projectBasicInfo.TYPE.equalsIgnoreCase("0")) {
            category.setText("Category : IOT");
        } else if (projectBasicInfo.TYPE.equalsIgnoreCase("1")) {
            category.setText("Category : Artificial Intelligence");
        } else if (projectBasicInfo.TYPE.equalsIgnoreCase("2")) {
            category.setText("Category : Cyber Security");
        } else {
            category.setText("Category : Cyber Security");
        }

        findViewById(R.id.go_four).setOnClickListener(t -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.bottom_sheet_donate);
            seekBar = bottomSheetDialog.findViewById(R.id.seekbar);
            amount = bottomSheetDialog.findViewById(R.id.amount);

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    amount.setText("₹" + i);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequenceM, int i, int i1, int i2) {
                    String charSequence = charSequenceM.toString();
                    if (charSequence.contains("₹")) {
                        charSequence = charSequence.replace("₹", "");
                    }

                    if (!charSequence.toString().trim().isEmpty()) {
                        try {
                            int num = Integer.parseInt(charSequence.toString());
                            seekBar.setProgress(num);
                            //Toast.makeText(ScannerProjectInfo.this,charSequence.toString(),Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(ScannerProjectInfo.this, "Numbers only !", Toast.LENGTH_SHORT).show();
                            amount.setText(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            bottomSheetDialog.show();
        });

    }

}