package thundersharp.aigs.spectre.ui.activities.barcode;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;

import thundersharp.aigs.spectre.R;
import thundersharp.aigs.spectre.core.models.ProjectBasicInfo;
import thundersharp.aigs.spectre.core.utils.CONSTANTS;
import thundersharp.aigs.spectre.core.utils.Progressbars;
import thundersharp.aigs.spectre.ui.activities.deeplinking.DeeplinkingActivity;
import thundersharp.aigs.spectre.ui.activities.exhibition.ExhibitionHome;
import thundersharp.aigs.spectre.ui.activities.exhibition.ProjectsInfo;
import thundersharp.aigs.spectre.ui.activities.exhibition.ScannerProjectInfo;

public class BarCodeScanner extends AppCompatActivity {

    private SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private boolean isFlashAvailable = false;
    private boolean flashStatus = false;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private ImageView flashLight;
    private boolean dialog = true;

    private String scanValue = null;

    private CameraManager mCameraManager;
    private String mCameraId;

    private BottomSheetDialog bottomSheetDialog;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code_scanner);
        isFlashAvailable = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        alertDialog = Progressbars.getInstance().createDefaultProgressBar(BarCodeScanner.this);

        surfaceView = findViewById(R.id.surfaceView);
        flashLight = findViewById(R.id.flash_toogle);
        bottomSheetDialog = new BottomSheetDialog(this);


        flashLight.setOnClickListener(i -> {
            if (flashStatus) {
                flashLight.setImageDrawable(getDrawable(R.drawable.ic_outline_flashlight_on_24));
                //toggleFlash(false);
                flashStatus = false;
            } else {
                flashLight.setImageDrawable(getDrawable(R.drawable.ic_outline_flashlight_off_24));
                // toggleFlash(true);
                flashStatus = true;
            }
        });
        //initialiseDetectorsAndSources();

    }

    public void toggleFlash(boolean status) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager.setTorchMode(mCameraId, status);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void initialiseDetectorsAndSources() {
        //Toast.makeText(getApplicationContext(), "Scanner started", Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(300, 300)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();


        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(BarCodeScanner.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(BarCodeScanner.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                //Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if (dialog) {
                                dialog = false;
                                alertDialog.show();
                                scanValue = barcodes.valueAt(0).displayValue;
                                if (scanValue.startsWith("https://spekteraigs.page.link/QR/")) {
                                    String stallId = scanValue.substring(scanValue.indexOf("=") + 1);

                                    FirebaseDatabase
                                            .getInstance()
                                            .getReference(CONSTANTS.EXHIBITION)
                                            .child(CONSTANTS.PROJECTS)
                                            .child(stallId)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    if (snapshot.exists()) {
                                                        FirebaseDatabase
                                                                .getInstance()
                                                                .getReference(CONSTANTS.PASSES)
                                                                .child(FirebaseAuth.getInstance().getUid())
                                                                .child("ID")
                                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapsho) {
                                                                        if (snapsho.exists()){
                                                                            startActivity(new Intent(BarCodeScanner.this, ScannerProjectInfo.class)
                                                                                    .putExtra("type", true)
                                                                                    .putExtra("projects_basic_info", snapshot.getValue(ProjectBasicInfo.class)));
                                                                            alertDialog.dismiss();
                                                                            dialog = true;
                                                                        }else {
                                                                            new AlertDialog.Builder(BarCodeScanner.this)
                                                                                    .setMessage("Pass has not been generated for your account yet please generate your pass first from Exhibition section.")
                                                                                    .setPositiveButton("GENERATE", ((dialogInterface, i) -> {
                                                                                        alertDialog.dismiss();
                                                                                        startActivity(new Intent(BarCodeScanner.this, ExhibitionHome.class));
                                                                                    })).setNegativeButton("CANCEL",(a,s)->{
                                                                                        a.dismiss();
                                                                                        alertDialog.dismiss();
                                                                                    }).setCancelable(false)
                                                                                    .show();

                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                                        alertDialog.dismiss();
                                                                        Toast.makeText(BarCodeScanner.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });


                                                    } else {
                                                        new AlertDialog.Builder(BarCodeScanner.this)
                                                                .setMessage("Not a valid Spekter events QR Code!!")
                                                                .setPositiveButton("OK", ((dialogInterface, i) -> {
                                                                    alertDialog.dismiss();
                                                                    dialog = true;
                                                                })).setCancelable(false)
                                                                .show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(BarCodeScanner.this, "ERROR : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                    alertDialog.dismiss();
                                                    dialog = true;
                                                }
                                            });

                                } else {
                                    new AlertDialog.Builder(BarCodeScanner.this)
                                            .setMessage("Not a valid Spekter events QR Code!!")
                                            .setPositiveButton("OK", ((dialogInterface, i) -> {
                                                alertDialog.dismiss();
                                                dialog = true;
                                            })).setCancelable(false)
                                            .show();
                                }

                            }
                        }
                    });

                    Log.d("TAG", barcodes.valueAt(0).rawValue);
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        //toggleFlash(false);
        flashStatus = false;
        flashLight.setImageDrawable(getDrawable(R.drawable.ic_outline_flashlight_off_24));
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}