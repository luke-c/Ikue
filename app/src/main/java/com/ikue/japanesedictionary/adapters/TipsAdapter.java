package com.ikue.japanesedictionary.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.models.Tip;

import java.util.List;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tipTitle;
        public TextView tipContent;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tipTitle = (TextView) itemView.findViewById(R.id.tips_card_title);
            tipContent = (TextView) itemView.findViewById(R.id.tips_card_content);
        }
    }

    // Store a member variable for the contacts
    private List<Tip> tips;
    // Store the context for easy access
    private Context context;

    // Pass in the contact array into the constructor
    public TipsAdapter(Context context, List<Tip> tips) {
        this.tips = tips;
        this.context = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return this.context;
    }

    @Override
    public TipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View tipView = inflater.inflate(R.layout.item_tip, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(tipView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TipsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Tip tip = tips.get(position);

        // Set item views based on your views and data model
        TextView title = viewHolder.tipTitle;
        title.setText(tip.getTitle());

        TextView content = viewHolder.tipContent;
        content.setText(tip.getBody());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return tips.size();
    }
}