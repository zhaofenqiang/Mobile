package com.example.zfq.myndk;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.input);
        tv.setText("input image");
        TextView tv2 = (TextView) findViewById(R.id.output);
        tv2.setText("output image");

        ImageView iv = (ImageView) findViewById(R.id.imageView);
        ImageView iv2 = (ImageView) findViewById(R.id.imageView2);

        Bitmap bm = getImageFromAssetsFile(getBaseContext(), "img/dog.jpg");
        iv.setImageBitmap(bm);

        int width = 300;
        int height = 300;
        int[] colors = new int[width * height];
        for (int i = 0; i < 90000; i++) {
            colors[i] = Color.rgb(i/350, i/600, i/1000);
        }
        Bitmap bm2 = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
        iv2.setImageBitmap(bm2);


        String str = stringFromJNI();


        /*
        try{
            Log.d("zfq", "zfq: load ppm image");
            Bitmap bm2 = ReadBitmapFromPPM2("img/dog.ppm");
            iv2.setImageBitmap(bm);
        } catch(java.io.IOException e){
            System.out.println("zfq load ppm failed:" + e);
        }
        */
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    public static Bitmap ReadBitmapFromPPM2(String file) throws IOException {
        //FileInputStream fs = new FileInputStream(file);
        BufferedInputStream reader = new BufferedInputStream(new FileInputStream(new File(file)));
        if (reader.read() != 'P' || reader.read() != '6')
            return null;

        reader.read(); //Eat newline
        String widths = "" , heights = "";
        char temp;
        while ((temp = (char) reader.read()) != ' ') {
            widths += temp;
        }
        while ((temp = (char) reader.read()) >= '0' && temp <= '9')
            heights += temp;
        if (reader.read() != '2' || reader.read() != '5' || reader.read() != '5')
            return null;
        reader.read();

        int width = Integer.valueOf(widths);
        int height = Integer.valueOf(heights);
        int[] colors = new int[width * height];

        byte [] pixel = new byte[3];
        int len = 0;
        int cnt = 0;
        int total = 0;
        int[] rgb = new int[3];
        while ((len = reader.read(pixel)) > 0) {
            for (int i = 0; i < len; i ++) {
                rgb[cnt] = pixel[i]>=0?pixel[i]:(pixel[i] + 255);
                if ((++cnt) == 3) {
                    cnt = 0;
                    colors[total++] = Color.rgb(rgb[0], rgb[1], rgb[2]);
                }
            }
        }

        Bitmap bmp = Bitmap.createBitmap(colors, width, height, Bitmap.Config.ARGB_8888);
        return bmp;
    }

}
