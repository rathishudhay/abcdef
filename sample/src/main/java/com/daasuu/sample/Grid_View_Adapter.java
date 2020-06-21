package com.daasuu.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Grid_View_Adapter extends BaseAdapter {


    private Context context;
    Typeface customFont;
    private final String[] FilesARR;

    public Grid_View_Adapter(Context context, String[] files) {
        this.context = context;
        this.FilesARR = files;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.grid_item, null);

            // set value into textview
//            TextView textView = (TextView) gridView
//                    .findViewById(R.id.grid_item_label);
//            textView.setText(FilesARR[position]);

            TextView count = (TextView) gridView
                    .findViewById(R.id.count);
//            customFont = Typeface.createFromAsset(context.getAssets(), "fonts/GOTHICB.TTF");
//            count.setTypeface(customFont);
            count.setText(String.valueOf(position+1));

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_thumbnail);

            String filepath = FilesARR[position];

            Bitmap thumb = ThumbnailUtils.createVideoThumbnail(filepath,
                    MediaStore.Images.Thumbnails.MINI_KIND);

            imageView.setImageBitmap(thumb);

//            if (mobile.equals("Windows")) {
//                imageView.setImageResource(R.drawable.windows_logo);
//            } else if (mobile.equals("iOS")) {
//                imageView.setImageResource(R.drawable.ios_logo);
//            } else if (mobile.equals("Blackberry")) {
//                imageView.setImageResource(R.drawable.blackberry_logo);
//            } else {
//                imageView.setImageResource(R.drawable.android_logo);
//            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return FilesARR.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
//    private Context context;
//    private final String[] filesARR;
//
//    public Grid_View_Adapter(Context context, String[] filePaths) {
//        this.context = context;
//        this.filesARR = filePaths;
//    }
//
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//        View gridView;
//
//        if (convertView == null) {
//
//            gridView = new View(context);
//
//            // get layout from mobile.xml
//            gridView = inflater.inflate(R.layout.grid_item, null);
//
//            // set value into textview
//            TextView textView = (TextView) gridView
//                    .findViewById(R.id.grid_item_label);
//            textView.setText(filesARR[position]);
//
//            // set image based on selected text
////            VideoView videoView =  gridView
////                    .findViewById(R.id.grid_item_video_view);
////
////            String filePath = filesARR[position];
////            Uri uri = Uri.parse(filePath);
////            videoView.setVideoURI(uri);
//            //videoView.start();
//
////            if (mobile.equals("Windows")) {
////                imageView.setImageResource(R.drawable.windows_logo);
////            } else if (mobile.equals("iOS")) {
////                imageView.setImageResource(R.drawable.ios_logo);
////            } else if (mobile.equals("Blackberry")) {
////                imageView.setImageResource(R.drawable.blackberry_logo);
////            } else {
////                imageView.setImageResource(R.drawable.android_logo);
////            }
//
//        } else {
//            gridView = (View) convertView;
//        }
//
//        return gridView;
//    }
//
//    @Override
//    public int getCount() {
//        return filesARR.length;
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//}
