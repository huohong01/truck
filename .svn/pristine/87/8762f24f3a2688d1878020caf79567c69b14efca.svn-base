package com.hsdi.MinitPay;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.hsdi.NetMe.R;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.EnumMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ShowCodeActivity extends AppCompatActivity {

    @Bind(R.id.iv_barCode)
    ImageView ivBarCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_code);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String userId = intent.getStringExtra("userId");

        Bitmap bitmap = encodeAsBitmap(userId, 500);
        ivBarCode.setImageBitmap(bitmap);
        ivBarCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ivBarCode.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(ivBarCode.getDrawingCache());
                ivBarCode.setDrawingCacheEnabled(false);
                decodeQRCode(bitmap);
                return true;
            }
        });
    }

    public Bitmap encodeAsBitmap(String str, int size) {
        Bitmap bitmap = null;
        BitMatrix result = null;
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            result = multiFormatWriter.encode(str, BarcodeFormat.QR_CODE, size, size);
            // 使用 ZXing Android Embedded 要写的代码
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.createBitmap(result);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException iae) { // ?
            return null;
        }

        // 如果不使用 ZXing Android Embedded 的话，要写的代码
//        int w = result.getWidth();
//        int h = result.getHeight();
//        int[] pixels = new int[w * h];
//        for (int y = 0; y < h; y++) {
//            int offset = y * w;
//            for (int x = 0; x < w; x++) {
//                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
//            }
//        }
//        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(pixels,0,100,0,0,w,h);

        return bitmap;
    }
    //长按识别二维码
    public final Map<DecodeHintType,Object> HINTS = new EnumMap<>(DecodeHintType.class);
    public void decodeQRCode(final Bitmap bitmap){

        new AsyncTask<Void,Void,String>(){

            @Override
            protected String doInBackground(Void... params) {
                try {
                    int width = bitmap.getWidth();
                    int height = bitmap.getHeight();
                    int[] pixels = new int[width*height];
                    bitmap.getPixels(pixels,0,width,0,0,width,height);
                    RGBLuminanceSource source = new RGBLuminanceSource(width,height,pixels);
                    Result result = new MultiFormatReader().decode(new BinaryBitmap(new HybridBinarizer(source)),HINTS);
                    return result.getText();
                } catch (NotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String s) {
                Log.e("yi", "onPostExecute: result = " + s );
                Toast.makeText(ShowCodeActivity.this, s , Toast.LENGTH_SHORT).show();
            }
        }.execute();
    }
}
