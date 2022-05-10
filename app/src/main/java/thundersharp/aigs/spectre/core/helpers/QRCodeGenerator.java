package thundersharp.aigs.spectre.core.helpers;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;

public class QRCodeGenerator {

    public static Bitmap getQrCodeFromData(String data) throws IOException, WriterException {
        return new QRCodeGenerator().generateQRCodeImage(data,350,350);
    }

    public static Image getQrCodeFromDataItext(String data) throws BadElementException {
        return new QRCodeGenerator().generateQr(data);
    }

    private Image generateQr(String data) throws BadElementException {
        BarcodeQRCode barcodeQrcode = new BarcodeQRCode(data, 10, 10, null);
        Image qrcodeImage = barcodeQrcode.getImage();
        qrcodeImage.setAbsolutePosition(20, 500);
        qrcodeImage.scalePercent(100);
        return qrcodeImage;
    }

    private Bitmap generateQRCodeImage(String text, int w, int h)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE,w,h);
        int height = bitMatrix.getHeight();
        int width = bitMatrix.getWidth();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                bmp.setPixel(x, y, bitMatrix.get(x,y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bmp;
    }

    private Bitmap generateBarCode(String data){
        EAN13Writer multiFormatWriter = new EAN13Writer();
        try {

            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.EAN_13,600,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }

    }

}
