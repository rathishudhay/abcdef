package com.daasuu.sample;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.daasuu.mp4compose.FillMode;
import com.daasuu.mp4compose.composer.Mp4Composer;
import com.daasuu.mp4compose.filter.GlFilter;
import com.daasuu.mp4compose.filter.GlFilterGroup;
import com.daasuu.mp4compose.filter.GlMonochromeFilter;
import com.daasuu.mp4compose.filter.GlVignetteFilter;
import com.wang.avi.AVLoadingIndicatorView;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Video_Trimmer extends AppCompatActivity implements VideoFilterAdapter.RecyclerViewItemClickListener{

    private static final int REQUEST_TAKE_GALLERY_VIDEO = 100;
    private VideoView videoView;
    private RangeSeekBar rangeSeekBar;
    private int stopPosition;
    private Uri selectedVideoUri;
    private TextView tvLeft, tvRight,bg_design;
    private int duration;
    private Runnable r;
    private int choice = 0;
//    private ScrollView mainlayout;
    private static final String TAG = "BHUVNESH";
    private String filePath;
    private ProgressDialog progressDialog;
    private static final String FILEPATH = "filepath";
    private String[] lastReverseCommand;
    private String appname="Rado";
    int totalFiles=0;
    int successFiles=0;
    ArrayList<String> cutFileNames=new ArrayList<String>();
    Date dateDT;
    TextView uploadVideo,cutVideo,play_pause,header_text,error_PopUp;
    CardView player_control;
    RelativeLayout player;
    Boolean VideoStatus=false;
    public static final String DOCUMENTS_DIR = "documents";
    String dummy_URL;
    File source;
    Boolean Edit_Mode=false;
    AVLoadingIndicatorView avi;
    String EditingVideoName=null;
    LinearLayout editLayout;
    TextView videoName_TV;
    EditText VideoName_ET;
    ImageView edit_icon;
    Boolean edit_mode=false;
    Typeface customFont;
    RecyclerView filterRecyclerView;
    int CUT_VIDEO_IN_LOOP_TIME=30000;


    private Mp4Composer mp4Composer;

//    05/06/2020
    // Recycler View object
    RecyclerView recyclerView;

    // Array list for recycler view data source
    FilterType[] filters;

    // Layout Manager
    RecyclerView.LayoutManager RecyclerViewLayoutManager;

    // adapter class object
    VideoFilterAdapter adapter;

    // Linear Layout Manager
    LinearLayoutManager HorizontalLayout;

    View ChildView;
    int RecyclerViewItemPosition;

    private GlFilter glFilter = new GlFilter();

    AlertDialog.Builder builder;

    CheckBox mute,horizontalFlip,verticalFlip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__trimmer);
         uploadVideo = (TextView) findViewById(R.id.uploadVideo);
         cutVideo = (TextView) findViewById(R.id.cropVideo);

        tvLeft = (TextView) findViewById(R.id.tvLeft);
        tvRight = (TextView) findViewById(R.id.tvRight);
        videoView = (VideoView) findViewById(R.id.videoView);
        rangeSeekBar = (RangeSeekBar) findViewById(R.id.rangeSeekBar);
//        mainlayout = (ScrollView) findViewById(R.id.mainlayout);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Hang tight! This may take a while");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);//initially progress is 0
        progressDialog.setMax(100);//sets the maximum value 100
        bg_design=findViewById(R.id.bg_design);
        player_control=findViewById(R.id.player_control);
        player=findViewById(R.id.player);
        play_pause=findViewById(R.id.play_pause);
        header_text=findViewById(R.id.header_text);
        error_PopUp=findViewById(R.id.error_PopUp);
        avi=findViewById(R.id.avi);
        avi.show();

        editLayout=findViewById(R.id.edit_Layout);
        videoName_TV=findViewById(R.id.videoName_TV);
        VideoName_ET=findViewById(R.id.videoName_ET);
        edit_icon=findViewById(R.id.edit_icon);
        filterRecyclerView=findViewById(R.id.recyclerview);

//        customFont = Typeface.createFromAsset(getAssets(),  "fonts/GOTHICB.TTF");
//        videoName_TV.setTypeface(customFont);
//        VideoName_ET.setTypeface(customFont);
//        header_text.setTypeface(customFont);


        setEditingVideoName();

        videoName_TV.setText(EditingVideoName);




//        loadFFMpegBinary();
//temp*
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo.setVisibility(View.GONE);
                avi.show();
                if (Build.VERSION.SDK_INT >= 23) {
                    Log.d("View", "Permission");
                    getPermission();
                }
                else{
                    Log.d("View", "Upload");
                    uploadVideo();
                }
            }
        });

        cutVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cutFileNames.clear();

                choice = 2;

                if (selectedVideoUri != null) {
//                    executeCutViduteoCommand(rangeSeekBar.getSelectedMinValue().intValue() * 1000, rangeSeekBar.getSelectedMaxValue().intValue() * 1000);
                    dateDT= new Date();
                    showTrimDialog();

                }
                //else
                   // Snackbar.make(mainlayout, "Please upload a video", 4000).show();
            }
        });


//        05/06/2020
        recyclerView
                = (RecyclerView)findViewById(
                R.id.recyclerview);
        RecyclerViewLayoutManager
                = new LinearLayoutManager(
                getApplicationContext());

        // Set LayoutManager on Recycler View
        recyclerView.setLayoutManager(
                RecyclerViewLayoutManager);

        // Adding items to RecyclerView.
        AddItemsToRecyclerViewArrayList();

        // calling constructor of adapter
        // with source list as a parameter
        adapter = new VideoFilterAdapter(filters,this);

        // Set Horizontal Layout Manager
        // for Recycler view
        HorizontalLayout
                = new LinearLayoutManager(
                Video_Trimmer.this,
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(HorizontalLayout);

        // Set adapter on recycler view
        recyclerView.setAdapter(adapter);
        builder = new AlertDialog.Builder(this);
        mute=findViewById(R.id.mute);
        horizontalFlip=findViewById(R.id.flip_horizontal);
        verticalFlip=findViewById(R.id.flip_vertical);
    }

    private void showTrimDialog(){
        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage("Message") .setTitle("Title");

        //Setting message manually and performing action on button click
        builder.setMessage("Select the type of trim?")
                .setCancelable(false)
                .setPositiveButton("Normal Trim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        totalFiles++;
                        executeCutVideoCommand(rangeSeekBar.getSelectedMinValue().intValue() * 1000, rangeSeekBar.getSelectedMaxValue().intValue() * 1000,1);
                    }
                })
                .setNegativeButton("Whatsapp Trim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                        cutVideoInLoop(rangeSeekBar.getSelectedMinValue().intValue() * 1000, rangeSeekBar.getSelectedMaxValue().intValue() * 1000);
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("");
        alert.show();
    }




    private void AddItemsToRecyclerViewArrayList(){
        filters= FilterType.values();
    }

    private void setEditingVideoName() {

        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/" + appname);
            if (!file.exists()) {
                //Toast.makeText(getApplicationContext(),"Not Exists",Toast.LENGTH_SHORT).show();
                file.mkdir();
               // Toast.makeText(getApplicationContext(),"Created "+file.getPath().toString(),Toast.LENGTH_SHORT).show();
            }

            File file2 = new File(Environment.getExternalStorageDirectory() + "/" + appname+"/Trimmed Videos");
            if (!file2.exists()) {
               // Toast.makeText(getApplicationContext(),"Not Exists",Toast.LENGTH_SHORT).show();
                file2.mkdir();
               // Toast.makeText(getApplicationContext(),"Created "+file2.getPath().toString(),Toast.LENGTH_SHORT).show();
            }
            int totalNumFiles = file2.listFiles().length;

            if(totalNumFiles ==0)
            totalNumFiles++;
            else
                totalNumFiles++;


            EditingVideoName="My Video "+totalNumFiles;
           // Toast.makeText(getApplicationContext(),"Video Name  "+EditingVideoName,Toast.LENGTH_SHORT).show();

        }
        catch (Exception e)
        {

        }


    }

    public void cutVideoInLoop(int rangeStart,int rangeEnd){
        int totalDuration=rangeEnd;
        int start=rangeStart;
        int end=rangeStart+CUT_VIDEO_IN_LOOP_TIME;
        int no=1;
        while(end<totalDuration){
            Log.d("Start:", String.valueOf(start));
            Log.d("End:", String.valueOf(end));
            executeCutVideoCommand(start,end,no++);
            start+=CUT_VIDEO_IN_LOOP_TIME;
            end+=CUT_VIDEO_IN_LOOP_TIME;
        }
        Log.d("Start:", String.valueOf(start));
        Log.d("End:", String.valueOf(totalDuration));
        totalFiles=no;
        Log.d("totalFiles", String.valueOf(totalFiles));
        executeCutVideoCommand(start,totalDuration,no++);
    }





    private void getPermission() {
        Log.d("View", "IPermission");
        String[] params = null;
        String writeExternalStorage = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String readExternalStorage = Manifest.permission.READ_EXTERNAL_STORAGE;

        int hasWriteExternalStoragePermission = ActivityCompat.checkSelfPermission(this, writeExternalStorage);
        int hasReadExternalStoragePermission = ActivityCompat.checkSelfPermission(this, readExternalStorage);
        List<String> permissions = new ArrayList<String>();

        if (hasWriteExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(writeExternalStorage);
        if (hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(readExternalStorage);

        if (!permissions.isEmpty()) {
            params = permissions.toArray(new String[permissions.size()]);
        }
        if (params != null && params.length > 0) {

            Log.d("View", "OPermission");
            ActivityCompat.requestPermissions(Video_Trimmer.this,
                    params,
                    100);
        } else
            uploadVideo();
    }


    /**
     * Handling response for permission request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    uploadVideo();
                }
            }
            break;
//            case 200: {
//
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    extractAudioVideo();
//                }
//            }


        }
    }



    private void uploadVideo() {

//        avi.show();

        try {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_TAKE_GALLERY_VIDEO);
        } catch (Exception e) {

        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.seekTo(stopPosition);
        videoView.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_GALLERY_VIDEO) {
                Log.d("Main", "onActivityResult: data.getData()");
                selectedVideoUri = data.getData();
                videoView.setVideoURI(selectedVideoUri);
                videoView.start();
                bg_design.setVisibility(View.GONE);
                uploadVideo.setVisibility(View.GONE);
                avi.hide();
                cutVideo.setVisibility(View.VISIBLE);
                player_control.setVisibility(View.VISIBLE);
                player.setVisibility(View.VISIBLE);
                VideoStatus=true;
                editLayout.setVisibility(View.VISIBLE);
                filterRecyclerView.setVisibility(View.VISIBLE);
                mute.setVisibility(View.VISIBLE);
                horizontalFlip.setVisibility(View.VISIBLE);
                verticalFlip.setVisibility(View.VISIBLE);
                //header_text.setBackgroundResource(R.drawable.pick_head);
                header_text.setText("Edit");
                Edit_Mode=true;



                //new code

                //Toast.makeText(getApplicationContext(),FileUtils_Dummy.getRealPathFromURI(this,selectedVideoUri)+"@@@"+"\n"+selectedVideoUri.getPath(),Toast.LENGTH_SHORT).show();
                dummy_URL=selectedVideoUri.getPath().toString();
               // Toast.makeText(getApplicationContext(),"Dummy URL: "+dummy_URL,Toast.LENGTH_SHORT).show();


                Uri uri= data.getData();
                File file= new File(uri.getPath());
                file.getName();

                //Toast.makeText(getApplicationContext(),"File  Name: "+file.getName().toString(),Toast.LENGTH_SHORT).show();

                String src = selectedVideoUri.getPath();
                source = new File(src);
                Log.d("SOURCE", source.toString());
                String filename = selectedVideoUri.getLastPathSegment();
                Log.d("FILENAME",filename);
                File destination = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Test/TestTest/" + filename);
                Log.d("DESTINATION", destination.toString());

                //new code




                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        duration = mp.getDuration() / 1000;
                        tvLeft.setText("00:00:00");

                        tvRight.setText(getTime(mp.getDuration() / 1000));
                        mp.setLooping(true);
                        rangeSeekBar.setRangeValues(0, duration);
                        rangeSeekBar.setSelectedMinValue(0);
                       // Toast.makeText(getApplicationContext(),mp.getDuration()+"",Toast.LENGTH_LONG).show();
                        rangeSeekBar.setSelectedMaxValue(mp.getDuration());
                        rangeSeekBar.setEnabled(true);

                        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {


                            @Override
                            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                                videoView.seekTo((int) minValue * 1000);

//                                try
//                                {
//                                    Log.d("SEEKBAR", "onRangeSeekBarValuesChanged: "+getTime((int) bar.getSelectedMinValue()));
//                                 rangeSeekBar.setSelectedMinValue(Integer.parseInt("STR"));
//                                rangeSeekBar.setSelectedMaxValue(Integer.parseInt(getTime((int) bar.getSelectedMaxValue()).replaceAll("[^\\d-.]", "")));
//                                //rangeSeekBar.setTooltipText("");
//                                }catch (Exception e)
//                                {
//                                    Log.d("ERROR IN SEEKBAR", "onRangeSeekBarValuesChanged: "+e);
//                                }


                                tvLeft.setText(getTime((int) bar.getSelectedMinValue()));

                                tvRight.setText(getTime((int) bar.getSelectedMaxValue()));

                            }
                        });

                        final Handler handler = new Handler();
                        handler.postDelayed(r = new Runnable() {
                            @Override
                            public void run() {

                                if (videoView.getCurrentPosition() >= rangeSeekBar.getSelectedMaxValue().intValue() * 1000)
                                    videoView.seekTo(rangeSeekBar.getSelectedMinValue().intValue() * 1000);
                                handler.postDelayed(r, 1000);
                            }
                        }, 1000);

                    }
                });

//                }
            }
        }
        else
        {
            uploadVideo.setVisibility(View.VISIBLE);
            avi.show();
        }
    }

    private String getTime(int seconds) {
        int hr = seconds / 3600;
        int rem = seconds % 3600;
        int mn = rem / 60;
        int sec = rem % 60;
        return String.format("%02d", hr) + ":" + String.format("%02d", mn) + ":" + String.format("%02d", sec);
    }



    /**
     * Command for cutting video
     */
    private void executeCutVideoCommand(int startMs, int endMs,int prefix) {
        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES
        );

        String filePrefix = String.valueOf(prefix);
        String fileExtn = ".mp4";
        String yourRealPath = getPath(Video_Trimmer.this, selectedVideoUri);

        if (yourRealPath == null) {

            openErrorPopUP();
        }
        else{

        File dest = new File(moviesDir, filePrefix + fileExtn);
        int fileNo = 0;
        while (dest.exists()) {
            fileNo++;
            dest = new File(moviesDir, filePrefix + fileNo + fileExtn);
        }

        File file = new File(Environment.getExternalStorageDirectory() + "/" + appname);
        if (!file.exists()) {
            file.mkdir();
        }
            long totalNumFiles = file.listFiles().length;

        long time = dateDT.getTime();

        File dir1 = new File(file.getPath() + "/Trimmed Videos");
        if (!dir1.exists()) {
            dir1.mkdir();
        }

            File dir2 = new File(file.getPath() + "/Trimmed Videos/" + EditingVideoName);
            if (!dir2.exists()) {
                dir2.mkdir();
            }

        filePath = file.getPath() + "/Trimmed Videos/" + EditingVideoName + "/" + filePrefix + fileExtn;
        cutFileNames.add(filePath);
        Log.d("filepath123", filePath);

            mp4Composer = new Mp4Composer(yourRealPath, filePath)
                    // .rotation(Rotation.ROTATION_270)
                    .size(720, 720)
                    .fillMode(FillMode.PRESERVE_ASPECT_FIT)
                    .filter(glFilter)
                    .mute(mute.isChecked())
                    //.timeScale(2f)
                    //.changePitch(false)
                    .trim(startMs, endMs)
                    .flipHorizontal(horizontalFlip.isChecked())
                    .flipVertical(verticalFlip.isChecked())
                    .listener(new Mp4Composer.Listener() {
                        @Override
                        public void onProgress(double progress) {
                            Log.d(TAG, "onProgress = " + progress);
//                            runOnUiThread(() -> progressBar.setProgress((int) (progress * 100)));
                        }

                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted()");
                            successFiles++;
                            Log.d("successFiles", String.valueOf(successFiles));
                            if (totalFiles==successFiles) {
                                successFiles = 0;
                                Intent intent = new Intent(Video_Trimmer.this, Video_Trimmer_Preview.class);
                                Bundle args = new Bundle();
                                args.putSerializable("ARRAYLIST", (Serializable) cutFileNames);
                                intent.putExtra(FILEPATH, args);
                                progressDialog.dismiss();
                                startActivity(intent);
                            }


//                            exportMp4ToGallery(getApplicationContext(), videoPath);
                            runOnUiThread(() -> {
//                                progressBar.setProgress(100);
//                                findViewById(R.id.start_codec_button).setEnabled(true);
//                                findViewById(R.id.start_play_movie).setEnabled(true);
//                                Toast.makeText(BasicUsageActivity.this, "codec complete path =" + videoPath, Toast.LENGTH_SHORT).show();
                            });
                        }

                        @Override
                        public void onCanceled() {

                        }

                        @Override
                        public void onFailed(Exception exception) {
                            Log.e(TAG, "onFailed() " + exception.getMessage());
                        }
                    })
                    .start();


    }

    }



    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     */
    private String getPath(final Video_Trimmer context, final Uri uri) {

        try{

            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {

                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

//                   // Toast.makeText(getApplicationContext(),"From External Storagepath",Toast.LENGTH_SHORT).show();
////                    final String[] split = dummy_URL.split(":");
////                    final String type = split[0];
//                   // return Environment.getExternalStorageDirectory() + "/" + getFileName(selectedVideoUri);  //new
//                    return getExternalSdCardPath() + "/" + getFileName(selectedVideoUri);  //new


                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    //Toast.makeText(getApplicationContext(),"From Dwonloads",Toast.LENGTH_LONG).show();
                    String URL=null;
                    String filename=getFileName(selectedVideoUri);
                    URL= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+"/"+filename;
                    //53

                    return  URL;
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {

                    // format path = /document/video:77

                    //Toast.makeText(getApplicationContext(),"From Media Document",Toast.LENGTH_LONG).show();


                    final String docId = DocumentsContract.getDocumentId(uri);
                   // Toast.makeText(getApplicationContext(),"ID: "+DocumentsContract.getDocumentId(uri).toString(),Toast.LENGTH_LONG).show();

                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    Toast.makeText(getApplicationContext(),"Path: "+getDataColumn(context, contentUri, selection, selectionArgs), Toast.LENGTH_LONG).show();

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Invalid file"+e, Toast.LENGTH_LONG).show();

            Log.d(TAG,e.toString());

        }


        return null;
    }

    /**
     * Executing ffmpeg binary
     */
//    private void execFFmpegBinary(final String[] command) {
//        try {
//            if (FFmpeg.getInstance(this).isSupported()) {
//                // ffmpeg is supported
//                Log.d(TAG,"Supported");
//                //ffmpegTestTaskQuit();
//            }
//            else{
//
//                Log.d(TAG,"Not Supported");
//            }
//            FFmpeg.getInstance(this).execute(command, new ExecuteBinaryResponseHandler() {
//
//                @Override
//
//                public void onFailure(String s) {
//                    Log.d(TAG, "FAILED with output : " + s);
//                }
//
//                @Override
//                public void onSuccess(String s) {
//                    successFiles++;
//                    Log.d(TAG, "SUCCESS with output : " + s);
//                    Log.d("successFiles", String.valueOf(successFiles));
//                    if (totalFiles==successFiles) {
//                        successFiles=0;
//                        Intent intent = new Intent(Video_Trimmer.this, Video_Trimmer_Preview.class);
//                        Bundle args = new Bundle();
//                        args.putSerializable("ARRAYLIST",(Serializable)cutFileNames);
//                        intent.putExtra(FILEPATH, args);
//                        progressDialog.dismiss();
//                        startActivity(intent);
//                    } else if (choice == 8) {
//                        choice = 9;
//                        reverseVideoCommand();
//                    } else if (Arrays.equals(command, lastReverseCommand)) {
//                        choice = 10;
//                        Log.d(TAG, "onSuccess() returned: " + "FAILURE..................................................");
////                        concatVideoCommand();
//                    } else if (choice == 10) {
//                        File moviesDir = Environment.getExternalStoragePublicDirectory(
//                                Environment.DIRECTORY_MOVIES
//                        );
//                        File destDir = new File(moviesDir, ".VideoPartsReverse");
//                        File dir = new File(moviesDir, ".VideoSplit");
//                        if (dir.exists())
//                            deleteDir(dir);
//                        if (destDir.exists())
//                            deleteDir(destDir);
//                        choice = 11;
//                        Intent intent = new Intent(Video_Trimmer.this, Video_Trimmer_Preview.class);
//                        intent.putExtra(FILEPATH, filePath);
//                        startActivity(intent);
//                    }
//                }
//
//                @Override
//                public void onProgress(String s) {
//                    Log.d(TAG, "Started command : ffmpeg " + command);
//                    if (choice == 8)
//                    {
//
//                    }
//                        //progressDialog.setMessage("progress : splitting video " + s);
//                    else if (choice == 9)
//                    {
//
//                    }
//                        //progressDialog.setMessage("progress : reversing splitted videos " + s);
//                    else if (choice == 10)
//                    {
//
//                    }
//                       // progressDialog.setMessage("progress : concatenating reversed videos " + s);
//                    else
//                    {
//
//                    }
//                      //  progressDialog.setMessage("progress : " + s);
//
//
//                    Log.d(TAG, "progress : " + s);
//                }
//
//                @Override
//                public void onStart() {
//                    Log.d(TAG, "Started command : ffmpeg " + command);
//                    //progressDialog.setMessage("Processing...");
//                    progressDialog.show();
//                    progressDialog.setProgress(40);
//                }
//
//                @Override
//                public void onFinish() {
//                    Log.d(TAG, "Finished command : ffmpeg " + command);
//                    if (choice != 8 && choice != 9 && choice != 10) {
//
//                        progressDialog.setProgress(90);
//                        //progressDialog.dismiss();
//                    }
//
//                }
//            });
//        } catch (Exception e) {
//            // do nothing for now
//        }
//    }



    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }



    /**
     * Get the value of the data column for this Uri.
//     */
    private String getDataColumn(Video_Trimmer context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Log.d("GETCOLUMN","CALLED");
        Log.d("CONTEXT",context.toString());
        Log.d("URI",uri.toString());
        Log.d("SELECTION",selection.toString());
        Log.d("SELECTIONARGS_SIZE", String.valueOf(selectionArgs.length));
        for(int i=0;i<selectionArgs.length;i++)
        {
            Log.d("SELECTIONARGS",selectionArgs[i]);
        }


        //for recent media  path= /document/video:25072

//        D/GETCOLUMN: CALLED
//        D/CONTEXT: com.sundeep.whatsappstatusdownload.Video_Trimmer@a44ba81
//        D/URI: content://media/external/video/media
//        D/SELECTION: _id=?
//        D/SELECTIONARGS_SIZE: 1
//        D/SELECTIONARGS: 25072

       // Toast.makeText(getApplicationContext(),""+selection,Toast.LENGTH_SHORT).show();

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * Command for reversing segmented videos
     */
    private void reverseVideoCommand() {
        File moviesDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES
        );
        File srcDir = new File(moviesDir, ".VideoSplit");
        File[] files = srcDir.listFiles();
        String filePrefix = "reverse_video";
        String fileExtn = ".mp4";
        File destDir = new File(moviesDir, ".VideoPartsReverse");
        if (destDir.exists())
            deleteDir(destDir);
        destDir.mkdir();
        for (int i = 0; i < files.length; i++) {
            File dest = new File(destDir, filePrefix + i + fileExtn);
            String command[] = {"-i", files[i].getAbsolutePath(), "-vf", "reverse", "-af", "areverse", dest.getAbsolutePath()};
            if (i == files.length - 1)
                lastReverseCommand = command;
//            execFFmpegBinary(command);
        }


    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }

//    private void loadFFMpegBinary() {
//        try {
//            if (ffmpeg == null) {
//                Log.d(TAG, "ffmpeg : era nulo");
//                ffmpeg = FFmpeg.getInstance(this);
//            }
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//                @Override
//                public void onFailure() {
//                    showUnsupportedExceptionDialog();
//                }
//
//                @Override
//                public void onSuccess() {
//                    Log.d(TAG, "ffmpeg : correct Loaded");
//                }
//            });
//        } catch (FFmpegNotSupportedException e) {
//            showUnsupportedExceptionDialog();
//        } catch (Exception e) {
//            Log.d(TAG, "EXception no controlada : " + e);
//        }
//    }

    private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(Video_Trimmer.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Not Supported")
                .setMessage("Device Not Supported")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Video_Trimmer.this.finish();
                    }
                })
                .create()
                .show();

    }


    public void playOrPause(View view) {

        if(VideoStatus)
        {
            videoView.pause();
            VideoStatus=false;
            play_pause.setBackground(getResources().getDrawable(R.drawable.trim_play));
        }
        else{
            VideoStatus=true;
            videoView.start();
            play_pause.setBackground(getResources().getDrawable(R.drawable.trim_pause));
        }


    }

    //new code for DOwnload picker crash

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public String getFileName1(Uri uri) {
        String result;

        //if uri is content
        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            Cursor cursor =getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    //local filesystem
                    int index = cursor.getColumnIndex("_data");
                    if(index == -1)
                        //google drive
                        index = cursor.getColumnIndex("_display_name");
                    result = cursor.getString(index);
                    if(result != null)
                        uri = Uri.parse(result);
                    else
                        return null;
                }
            } finally {
                cursor.close();
            }
        }

        result = uri.getPath();

        //get filename + ext of path
        int cut = result.lastIndexOf('/');
        if (cut != -1)
            result = result.substring(cut + 1);
        return result;
    }

    public static String getExternalSdCardPath() {
        String path = null;

        File sdCardFile = null;
        List<String> sdCardPossiblePath = Arrays.asList("external_sd", "ext_sd", "external", "extSdCard");

        for (String sdPath : sdCardPossiblePath) {
            File file = new File("/mnt/", sdPath);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();

                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                }
                else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
        }
        else {
            sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        }

        return sdCardFile.getAbsolutePath();
    }

    public void goBack(View view) {

        if(Edit_Mode)
        {
            bg_design.setVisibility(View.VISIBLE);
            uploadVideo.setVisibility(View.VISIBLE);
            cutVideo.setVisibility(View.GONE);
            player_control.setVisibility(View.GONE);
            player.setVisibility(View.GONE);
            editLayout.setVisibility(View.GONE);
            //header_text.setBackgroundResource(R.drawable.home);
            header_text.setText("Video trimmer");
            videoView.stopPlayback();
            Edit_Mode=false;
        }
        else
        {
            Edit_Mode=false;

            super.onBackPressed();

        }

    }

    @Override
    public void onBackPressed() {
        if(Edit_Mode)
        {
            bg_design.setVisibility(View.VISIBLE);
            uploadVideo.setVisibility(View.VISIBLE);
            cutVideo.setVisibility(View.GONE);
            player_control.setVisibility(View.GONE);
            player.setVisibility(View.GONE);
            editLayout.setVisibility(View.GONE);
            //header_text.setBackgroundResource(R.drawable.home);
            header_text.setText("Video trimmer");
            videoView.stopPlayback();
            Edit_Mode=false;

        }
        else
        {
            Edit_Mode=false;

            super.onBackPressed();

        }
    }

    public void closeErrorPopUp(View view) {
        uploadVideo.setVisibility(View.VISIBLE);
        bg_design.setVisibility(View.VISIBLE);
        error_PopUp.setVisibility(View.GONE);
    }

    private void openErrorPopUP() {
        error_PopUp.setVisibility(View.VISIBLE);

        editLayout.setVisibility(View.GONE);

        bg_design.setVisibility(View.GONE);
        uploadVideo.setVisibility(View.GONE);
        cutVideo.setVisibility(View.GONE);
        player_control.setVisibility(View.GONE);
        player.setVisibility(View.GONE);
        //header_text.setBackgroundResource(R.drawable.home);
        header_text.setText("Video trimmer");
        videoView.stopPlayback();
        Edit_Mode=false;
    }

    public void changeMyVideoName(View view) {
        if(!edit_mode)
        {
            VideoName_ET.setVisibility(View.VISIBLE);
            videoName_TV.setVisibility(View.GONE);
            edit_icon.setBackgroundResource(R.drawable.ok);
            edit_mode=true;
            //VideoName_ET.setHint(EditingVideoName);


            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

            VideoName_ET.setSelection(0);
        }
        else
        {
            VideoName_ET.setVisibility(View.GONE);
            videoName_TV.setVisibility(View.VISIBLE);
            edit_icon.setBackgroundResource(R.drawable.draw);
            edit_mode=false;

            EditingVideoName=VideoName_ET.getText().toString().trim();
            if(TextUtils.isEmpty(EditingVideoName))
            {
                setEditingVideoName();
            }
            videoName_TV.setText(EditingVideoName);

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    @Override
    public void clickOnItem(FilterType data) {
        glFilter = FilterType.createGlFilter(data, this);
    }
}

