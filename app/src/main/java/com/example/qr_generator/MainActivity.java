package com.example.qr_generator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class MainActivity extends AppCompatActivity {
    private Button button,but;
    private ImageView image;
    private Bitmap grbits;
    private EditText upi, name;
    private AppCompatActivity activity;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        button = findViewById(R.id.button);
        image = findViewById(R.id.image);
        upi = (EditText) findViewById(R.id.upi);
        name = (EditText) findViewById(R.id.name);
        but = (Button) findViewById(R.id.but);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (upi.length() > 0) {
                    String up = upi.getText().toString();
                    String nam = name.getText().toString();
                    String data = "upi://pay?pa=" + up + "&pn=" + nam + "&mc=0000&mode=02&purpose=00";
                    Toast toast = Toast.makeText(getApplicationContext(), data, Toast.LENGTH_LONG);
                    toast.show();
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;
                    QRGEncoder grgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, smallerDimension);
                    try {
                        Bitmap grbits = grgEncoder.encodeAsBitmap();
                        image.setImageBitmap(grbits);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                } else {
                    upi.setError("Required");
                }
            }
        });
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    FileOutputStream foStream;
                    try {
                        /*boolean save = new QRGSaver().save(savePath, upi.getText().toString().trim(), grbits, QRGContents.ImageType.IMAGE_JPEG);
                        String result = save ? "Image Saved" : "Image Not Saved";
                        Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                        foStream = getApplication().openFileOutput(String.valueOf(name), Context.MODE_PRIVATE);
                        grbits.compress(Bitmap.CompressFormat.PNG, 100, foStream);
                        foStream.close();

                        upi.setText(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }*/
                BitmapDrawable bitmapDrawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                FileOutputStream outputStream = null;
                File file = Environment.getExternalStorageDirectory();
                File dir = new File(file.getAbsolutePath() + "/MyPics");
                dir.mkdirs();

                String filename = String.format("%d.png",System.currentTimeMillis());
                File outFile = new File(dir,filename);
                try{
                    outputStream = new FileOutputStream(outFile);
                }catch (Exception e){
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                try{
                    outputStream.flush();
                }catch (Exception e){
                    e.printStackTrace();
                }
                try{
                    outputStream.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
