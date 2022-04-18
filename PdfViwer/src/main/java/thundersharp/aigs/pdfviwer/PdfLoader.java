package thundersharp.aigs.pdfviwer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class PdfLoader {

    private Context activity;
    private PdfModel pdfUri;
    private boolean secure_mode;

    public static PdfLoader getInstance(Context activity){
        return new PdfLoader(activity);
    }

    public PdfLoader(Context activity) {
        this.activity = activity;
    }

    public PdfLoader setPdfData(PdfModel pdfUri){
        this.pdfUri = pdfUri;
        return this;
    }

    public PdfLoader setSecureMode(boolean secure_mode){
        this.secure_mode = secure_mode;
        return this;
    }

    public void loadPdf(){
        activity.startActivity(new Intent(activity,PdfViwer.class).putExtra("data",pdfUri).putExtra("secure",secure_mode));
    }
}
