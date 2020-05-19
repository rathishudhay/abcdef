package com.steelkiwi.cropiwa.stickersDialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.steelkiwi.cropiwa.R;
import com.steelkiwi.cropiwa.WhatsappStickers.StickerBook;
import com.steelkiwi.cropiwa.WhatsappStickers.WhatsAppBasedCode.StickerPack;
import com.steelkiwi.cropiwa.WhatsappStickers.WhatsAppBasedCode.StickerPackListActivity;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity implements DataAdapter.RecyclerViewItemClickListener {
    Button clickButton;
Activity activity;
    Uri stickerUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        clickButton = (Button) findViewById(R.id.button);

//        clickButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickHere();
//            }
//        });
    }

    CustomListViewDialog customDialog;

    public void clickHere(Activity activity, Uri stickerUri) {
this.activity=activity;
        this.stickerUri=stickerUri;
        ArrayList<StickerPack> stickerPacks= StickerBook.allStickerPacks;

        DataAdapter dataAdapter = new DataAdapter(stickerPacks, this);
        customDialog = new CustomListViewDialog(activity, dataAdapter,stickerUri);

        customDialog.show();
        customDialog.setCanceledOnTouchOutside(false);
    }




    @Override
    public void clickOnItem(StickerPack stickerPack) {

        Log.d("Synctest","addsticker");
        stickerPack.addSticker(stickerUri,activity.getApplicationContext());
        if (customDialog != null){
            customDialog.dismiss();
        }
        Intent intent = new Intent(activity, StickerPackListActivity.class);
        intent.setAction("Already created");
        startActivity(intent);

    }
}
