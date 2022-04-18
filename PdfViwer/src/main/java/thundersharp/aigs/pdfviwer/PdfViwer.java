package thundersharp.aigs.pdfviwer;

import android.app.DownloadManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shockwave.pdfium.PdfDocument;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class PdfViwer extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener {

    PDFView pdfView;
    ProgressBar progressbokview;
    long ONE_MEGABYTE = 1024 * 1024;
    PdfModel booksDisplayModel;
    StorageReference mmFirebaseStorageRef;
    int position=-1;
    String pdfFileNameUrl, pdfFileUrl;
    int pageNumber =0;
    Toolbar toolbar;
    public static String bookkeys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_viwer);
        toolbar = findViewById(R.id.toolbarpdf);
        setSupportActionBar(toolbar);

        if (getIntent().getBooleanExtra("secure",false))
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressbokview=findViewById(R.id.progressbokview);
        pdfView= findViewById(R.id.pdfViewone);
        booksDisplayModel = (PdfModel) getIntent().getSerializableExtra("data");
        bookkeys = booksDisplayModel.ID;
        pdfFileNameUrl = booksDisplayModel.SHORT_DESCRIPTION;
        pdfFileUrl = booksDisplayModel.URL;

        if (dofileexistinStorage(bookkeys)){
            File mybook = new File(getExternalFilesDir(null), bookkeys+".pdf");
            pdfView.fromFile(mybook)
                    .defaultPage(pageNumber)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .onPageChange(PdfViwer.this)
                    .enableAnnotationRendering(true)
                    .onLoad(PdfViwer.this)
                    .scrollHandle(new DefaultScrollHandle(this))
                    .load();

            progressbokview.setVisibility(View.GONE);

        }else {
            //loadbookfromserver();

            pdfView.fromUri(Uri.parse(pdfFileUrl));
            new RetrivePDFfromUrl().execute(pdfFileUrl);

           // BookDisplayAdapter.dialog.dismiss();


        }

    }

    private void downloadPdf(Context context, String fileName, String fileExtension, String destinationPath, String data_url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(pdfFileUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context,destinationPath,fileName+fileExtension);

        downloadManager.enqueue(request);

    }

    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            pdfView.fromStream(inputStream)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {

                        }
                    })
                    .enableAnnotationRendering(true)
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {
                            PdfDocument.Meta meta = pdfView.getDocumentMeta();
                            printBookmarksTree(pdfView.getTableOfContents(), "-");
                        }
                    })
                    .scrollHandle(new DefaultScrollHandle(PdfViwer.this))
                    .load();
        }
    }

    private void loadbookfromserver(){

        mmFirebaseStorageRef=
                FirebaseStorage
                        .getInstance()
                        .getReference()
                        .child(booksDisplayModel.URL);

        mmFirebaseStorageRef
                .getBytes(Long.MAX_VALUE)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {

                     /*   if ((settingsData.loadPdfsize()*1000000) > bytes.length ){
                            new SavePhotoTask().doInBackground(bytes);
                        }else {
                            Toast.makeText(BookViwer.this,"Error Saving This Book in Offline Storage as the book size is more than the set limit you can increase this limit is settings",Toast.LENGTH_LONG).show();
                        }
*/

                        progressbokview.setVisibility(View.GONE);
                        pdfView.fromBytes(bytes)
                                .defaultPage(pageNumber)
                                .enableSwipe(true)
                                .swipeHorizontal(false)
                                .onPageChange(PdfViwer.this)
                                .enableAnnotationRendering(true)
                                .onLoad(PdfViwer.this)
                                .scrollHandle(new DefaultScrollHandle(PdfViwer.this))
                                .load();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getCause().getMessage(), Toast.LENGTH_LONG).show();
                progressbokview.setVisibility(View.GONE);
                Toast.makeText(PdfViwer.this,
                        "download unsuccessful",
                        Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    public boolean dofileexistinStorage(String bookkey){
        File mybook = new File(getExternalFilesDir(null), bookkey+".pdf");
        return mybook.exists();
    }

    private void savetoStorage(byte[] bytesArray,@NonNull String bookkey) throws IOException {

        File myPdf=new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/Android/data/com.thundersharp.learninghub/files", bookkey+".pdf");

        if (myPdf.exists()){
            Toast.makeText(PdfViwer.this,"Exists",Toast.LENGTH_LONG).show();
        }else {
            myPdf.mkdirs();
            Toast.makeText(PdfViwer.this,"created ",Toast.LENGTH_LONG).show();
        }

        FileOutputStream fos=new FileOutputStream(myPdf.getPath());
        fos.write(bytesArray);
        fos.close();
    }

    class SavePhotoTask extends AsyncTask<byte[], String, String> {

        @Override
        protected String doInBackground(byte[]... pdf) {
            File photo=new File(getExternalFilesDir(null), bookkeys+".pdf");


            if (photo.exists()) {
                photo.delete();
            }


            try {
                FileOutputStream fos=new FileOutputStream(photo.getPath());

                fos.write(pdf[0]);
                fos.close();
            }
            catch (IOException e) {
                Log.e("Pdf error", e.getCause().getMessage(), e.getCause());
            }

            return(null);
        }
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        toolbar.setSubtitle(String.format("%s %s / %s", booksDisplayModel.SHORT_DESCRIPTION, page + 1, pageCount));

    }

    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e("TAG", String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
}
