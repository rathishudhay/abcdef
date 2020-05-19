package com.steelkiwi.cropiwa.stickersDialog;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.steelkiwi.cropiwa.R;
import com.steelkiwi.cropiwa.WhatsappStickers.WhatsAppBasedCode.StickerPack;

import java.util.ArrayList;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.FruitViewHolder>  {
    private ArrayList<StickerPack> stickerPacks;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public DataAdapter(ArrayList<StickerPack> stickerPacks, RecyclerViewItemClickListener listener) {
        this.stickerPacks = stickerPacks;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_dialog_list_item, parent, false);

        FruitViewHolder vh = new FruitViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder fruitViewHolder, int i) {
        fruitViewHolder.mTextView.setText(stickerPacks.get(i).getName());
    }

    @Override
    public int getItemCount() {
        return stickerPacks.size();
    }



    public  class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        public FruitViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textView);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnItem(stickerPacks.get(this.getAdapterPosition()));
        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(StickerPack data);
    }
}