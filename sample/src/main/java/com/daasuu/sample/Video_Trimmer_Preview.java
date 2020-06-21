package com.daasuu.sample;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;

public class Video_Trimmer_Preview extends AppCompatActivity {
    GridView gridView;
    private static final String FILEPATH = "filepath";
    TextView textView,close_button;
    CardView video_card;
    VideoView videoView;

    String[] filesArr;

    ArrayList<String> cutFileNames;
    String[] carBrands = {
            "Ferrari",
            "McLaren",
            "Jaguar",
            "Skoda",
            "Suzuki",
            "Hyundai",
            "Toyota",
            "Renault",
            "Mercedes",
            "BMW",
            "Ford",
            "Honda",
            "Chevrolet",
            "Volkswagon",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__trimmer__preview);

        video_card=findViewById(R.id.video_card);
        close_button=findViewById(R.id.close_button);
        videoView=findViewById(R.id.videoView);

        Bundle args = getIntent().getBundleExtra(FILEPATH);
         cutFileNames = (ArrayList<String>) args.getSerializable("ARRAYLIST");
        filesArr = new String[cutFileNames.size()];
        filesArr = cutFileNames.toArray(filesArr);


        gridView = (GridView) findViewById(R.id.gridView);

        gridView.setAdapter(new Grid_View_Adapter(this, filesArr));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

            String filePath = filesArr[position];
            final Uri uri = Uri.parse(filePath);
            videoView.setVideoURI(uri);
            videoView.start();

                video_card.setVisibility(View.VISIBLE);
                close_button.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);

                TextView share=v.findViewById(R.id.share);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        shareSingleFile(uri);
                    }
                });


            }
        });

    }

    private void shareSingleFile(Uri uri) {

        Intent trimmedVideos = new Intent(Intent.ACTION_SEND);
        trimmedVideos.setType("*/*");
        trimmedVideos.setPackage("com.whatsapp");
        trimmedVideos.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        trimmedVideos.putExtra(Intent.EXTRA_STREAM,uri);

        startActivity(trimmedVideos);

    }

    public void closeVideoPlayer(View view) {
        video_card.setVisibility(View.GONE);
        close_button.setVisibility(View.GONE);
        gridView.setVisibility(View.VISIBLE);
        videoView.stopPlayback();

    }

    public void shareAllFiles(View view) {

        ArrayList<Uri> uris = new ArrayList<>();

        for(int i=0;i<cutFileNames.size();i++)
        {
            uris.add(Uri.parse(cutFileNames.get(i)));
        }

        Intent trimmedVideos = new Intent(Intent.ACTION_SEND_MULTIPLE);
        trimmedVideos.setType("*/*");
        trimmedVideos.setPackage("com.whatsapp");
        trimmedVideos.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        trimmedVideos.putExtra(Intent.EXTRA_STREAM,uris);

        startActivity(trimmedVideos);

    }



    public void goBack(View view) {

        for(int i=0;i<filesArr.length;i++)
        {
            File file = new File(filesArr[i]);
            if(file.exists())
            file.delete();
        }

        super.onBackPressed();



    }
}