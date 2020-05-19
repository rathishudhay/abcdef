package com.steelkiwi.cropiwa.FreeHandCrop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.steelkiwi.cropiwa.PhotoEditor.MainCode.EditImageActivity;
import com.steelkiwi.cropiwa.R;

import java.io.ByteArrayOutputStream;
import java.util.List;


public class FreeHandCropActivity extends AppCompatActivity {
    private Bitmap mBitmap;
    private SomeView mSomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_hand_crop);

        getIntentData();

        if(mBitmap==null){
            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.employee);
        }
        mSomeView = new SomeView(this, mBitmap);
        LinearLayout layout = findViewById(R.id.layout);
        LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.addView(mSomeView, lp);
    }

    void getIntentData(){
        Intent intent = getIntent();
        Uri imageUri=null;
        if (intent != null) {
            imageUri = intent.getParcelableExtra("imgUri");
            if (imageUri != null) {
                Log.d("EditImageActivity","Success");
//                    source.setImageURI(imageUri);
            }
        }
        try {
            mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//            mPhotoEditor.addImage(bitmap);
        }catch(Exception e){
            Log.d("EditImageActivity",e.toString());
        }
    }

    public void cropImage() {
        setContentView(R.layout.activity_picture_preview);
        ImageView imageView = findViewById(R.id.image);

        Bitmap fullScreenBitmap =
                Bitmap.createBitmap(mSomeView.getWidth(), mSomeView.getHeight(), mBitmap.getConfig());

        Canvas canvas = new Canvas(fullScreenBitmap);

        Path path = new Path();
        List<Point> points = mSomeView.getPoints();
        for (int i = 0; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }

        // Cut out the selected portion of the image...
        Paint paint = new Paint();
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        // Frame the cut out portion...
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(0.001f);
        canvas.drawPath(path, paint);

        // Create a bitmap with just the cropped area.
        Region region = new Region();
        Region clip = new Region(0, 0, fullScreenBitmap.getWidth(), fullScreenBitmap.getHeight());
        region.setPath(path, clip);
        Rect bounds = region.getBounds();
        Bitmap croppedBitmap =
                Bitmap.createBitmap(fullScreenBitmap, bounds.left, bounds.top,
                        bounds.width(), bounds.height());

//        imageView.setImageBitmap(croppedBitmap);
        Intent intent = new Intent(getApplicationContext(), EditImageActivity.class);
        intent.putExtra("extra_uri", getImageUri(getApplicationContext(),croppedBitmap));
        Log.d("Intent","Intent");
        startActivity(intent);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}

