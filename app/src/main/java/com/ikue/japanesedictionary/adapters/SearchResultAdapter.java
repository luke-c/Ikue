package com.ikue.japanesedictionary.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.activities.EntryDetailActivity;
import com.ikue.japanesedictionary.models.DictionarySearchResultItem;

import java.util.List;

/**
 * Created by luke_c on 09/02/2017.
 */

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView primaryTextView;
        public TextView primaryTextView2;
        public TextView secondaryTextView;
        public ImageView favouriteButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            primaryTextView = (TextView) itemView.findViewById(R.id.list_primary);
            primaryTextView2 = (TextView) itemView.findViewById(R.id.list_primary2);
            secondaryTextView = (TextView) itemView.findViewById(R.id.list_secondary);
            favouriteButton = (ImageView) itemView.findViewById(R.id.favourite_button);
        }
    }

    // Store a member variable for the contacts
    private List<DictionarySearchResultItem> searchResultItems;
    // Store the context for easy access
    private Context context;

    // Pass in the contact array into the constructor
    public SearchResultAdapter(Context context, List<DictionarySearchResultItem> searchResultItems) {
        this.searchResultItems = searchResultItems;
        this.context = context;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return this.context;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.search_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(SearchResultAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        DictionarySearchResultItem item = searchResultItems.get(position);
        final int entryId = item.getEntryId();

        // Set item views based on your views and data model
        TextView primaryText = viewHolder.primaryTextView;
        TextView primaryText2 = viewHolder.primaryTextView2;
        TextView secondaryText = viewHolder.secondaryTextView;

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = EntryDetailActivity.newIntent(view.getContext(), entryId);
                context.startActivity(i);
            }
        });

        String kanjiValue = item.getKanjiElementValue();
        if(!kanjiValue.isEmpty()) {
            // If I don't reset the view to visible, when the view is recycled and you
            // scroll back to it the view is still set to gone from previous elements.
            primaryText.setVisibility(View.VISIBLE);
            secondaryText.setMaxLines(1);
            primaryText.setText(item.getKanjiElementValue());
        } else {
            primaryText.setVisibility(View.GONE);
            secondaryText.setMaxLines(2);
        }

        primaryText2.setText(item.getReadingElementValue());

        secondaryText.setText(TextUtils.join(", ", item.getGlossValue()));

        ImageView favouriteButton = viewHolder.favouriteButton;
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return searchResultItems.size();
    }
}