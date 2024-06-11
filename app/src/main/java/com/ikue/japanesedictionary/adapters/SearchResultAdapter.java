package com.ikue.japanesedictionary.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.activities.EntryDetailActivity;
import com.ikue.japanesedictionary.models.DictionaryListEntry;

import java.util.List;

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

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            primaryTextView = itemView.findViewById(R.id.list_primary);
            primaryTextView2 = itemView.findViewById(R.id.list_primary2);
            secondaryTextView = itemView.findViewById(R.id.list_secondary);
        }
    }

    // Store a member variable for the contacts
    private List<DictionaryListEntry> searchResultItems;
    // Store the context for easy access
    private final Context context;

    // Pass in the contact array into the constructor
    public SearchResultAdapter(Context context, List<DictionaryListEntry> searchResultItems) {
        this.searchResultItems = searchResultItems;
        this.context = context;
    }

    public void swapItems(List<DictionaryListEntry> items) {
        this.searchResultItems = items;
        notifyDataSetChanged();
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View view = inflater.inflate(R.layout.search_list_item, parent, false);

        // Return a new holder instance
        return new ViewHolder(view);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(SearchResultAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        DictionaryListEntry item = searchResultItems.get(position);
        final int entryId = item.getEntryId();

        // Set item views based on your views and data model
        TextView primaryText = viewHolder.primaryTextView;
        TextView primaryText2 = viewHolder.primaryTextView2;
        TextView secondaryText = viewHolder.secondaryTextView;

        viewHolder.itemView.setOnClickListener(view -> {
            Intent i = EntryDetailActivity.newIntent(view.getContext(), entryId);
            context.startActivity(i);
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
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return searchResultItems.size();
    }
}