package com.steelkiwi.cropiwa.stickersDialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.steelkiwi.cropiwa.R;
import com.steelkiwi.cropiwa.WhatsappStickers.StickerBook;
import com.steelkiwi.cropiwa.WhatsappStickers.WhatsAppBasedCode.StickerPack;
import com.steelkiwi.cropiwa.WhatsappStickers.WhatsAppBasedCode.StickerPackDetailsActivity;

import java.util.UUID;

public class CustomListViewDialog extends Dialog {


    public CustomListViewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomListViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public Activity activity;
    public Dialog dialog;
    public Button createNewStickerGroupButton;
    TextView title;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter adapter;
    Uri stickerUri;



    public CustomListViewDialog(Activity a, RecyclerView.Adapter adapter,Uri stickerUri) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
        this.stickerUri=stickerUri;
        setupLayout();
    }

    private void setupLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog_layout);
        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        createNewStickerGroupButton=(Button)findViewById(R.id.create_new_sticker_group);

        createNewStickerGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addNewStickerPackInInterface(stickerUri);

            }
        });

    }


    private void addNewStickerPackInInterface(Uri stickerUri){

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Create New Sticker Pack");
        dialog.setMessage("Please specify title and creator for the pack.");

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameBox = new EditText(getContext());
        nameBox.setLines(1);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParams.setMargins(50, 0, 50, 10);
        nameBox.setLayoutParams(buttonLayoutParams);
        nameBox.setHint("Pack Name");
        nameBox.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        layout.addView(nameBox);

        final EditText creatorBox = new EditText(getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            creatorBox.setAutofillHints("name");
        }
        creatorBox.setLines(1);
        creatorBox.setLayoutParams(buttonLayoutParams);
        creatorBox.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
        creatorBox.setHint("Creator");
        layout.addView(creatorBox);

        dialog.setView(layout);

        dialog.setPositiveButton("OK", null);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        final AlertDialog ad = dialog.create();

        ad.show();

        Button b = ad.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(nameBox.getText())){
                    nameBox.setError("Package name is required!");
                }

                if(TextUtils.isEmpty(creatorBox.getText())){
                    creatorBox.setError("Creator is required!");
                }

                if(!TextUtils.isEmpty(nameBox.getText()) && !TextUtils.isEmpty(creatorBox.getText())) {
                    ad.dismiss();
//                    createDialogForPickingIconImage(nameBox, creatorBox);

//                    activity.getContentResolver().takePersistableUriPermission(Objects.requireNonNull(stickerUri), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    createNewStickerPackAndOpenIt(nameBox.getText().toString(), creatorBox.getText().toString(), stickerUri);
                }
            }
        });

        creatorBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    b.performClick();
                }
                return false;
            }
        });
    }


    private void createNewStickerPackAndOpenIt(String name, String creator, Uri trayImage){
        String newId = UUID.randomUUID().toString();
        StickerPack sp = new StickerPack(
                newId,
                name,
                creator,
                trayImage,
                "",
                "",
                "",
                "",
                getContext());
        StickerBook.addStickerPackExisting(sp);


        StickerPack stickerPack = StickerBook.getStickerPackById(newId);
        stickerPack.addSticker(stickerUri,getContext());

        Intent intent = new Intent(getContext(), StickerPackDetailsActivity.class);
        intent.putExtra(StickerPackDetailsActivity.EXTRA_SHOW_UP_BUTTON, true);
        intent.putExtra(StickerPackDetailsActivity.EXTRA_STICKER_PACK_DATA, newId);
        intent.putExtra("isNewlyCreated", true);
        getContext().startActivity(intent);
    }





}