package thundersharp.aigs.pdfviwer;

import java.io.Serializable;

public class PdfModel implements Serializable {
    public String ID, URL, TITLE, SHORT_DESCRIPTION;

    public PdfModel(){}

    public PdfModel(String ID, String URL, String TITLE, String SHORT_DESCRIPTION) {
        this.ID = ID;
        this.URL = URL;
        this.TITLE = TITLE;
        this.SHORT_DESCRIPTION = SHORT_DESCRIPTION;
    }
}
