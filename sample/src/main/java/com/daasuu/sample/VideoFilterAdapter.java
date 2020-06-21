package com.daasuu.sample;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.view.LayoutInflater;
import java.util.List;

// The adapter class which
// extends RecyclerView Adapter
public class VideoFilterAdapter extends RecyclerView.Adapter<VideoFilterAdapter.MyView> {

    // List with String type
    private FilterType[] filters;

    RecyclerViewItemClickListener recyclerViewItemClickListener;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Text View
        TextView textView;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view)
        {
            super(view);

            // initialise TextView with id
            textView = (TextView)view
                    .findViewById(R.id.textview);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnItem(filters[this.getAdapterPosition()]);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public VideoFilterAdapter(FilterType[] horizontalList,RecyclerViewItemClickListener listener)
    {
        this.filters = horizontalList;
        this.recyclerViewItemClickListener = listener;
    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType)
    {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.video_filter_item,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position)
    {

        // Set the text of each item of
        // Recycler view with the list items
        holder.textView.setText(filters[position].name());
    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount()
    {
        return filters.length;
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(FilterType data);
    }


}