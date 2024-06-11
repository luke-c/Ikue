package com.ikue.japanesedictionary.adapters;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.models.SenseElement;

import java.util.List;

public class DetailViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int SENSE_ELEMENT_ITEM = 0;

    private final String LOG_TAG = this.getClass().toString();

    private final List<SenseElement> items;

    public DetailViewAdapter(List<SenseElement> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return SENSE_ELEMENT_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if (viewType == SENSE_ELEMENT_ITEM) {
            View v1 = inflater.inflate(R.layout.sense_element_item, viewGroup, false);
            viewHolder = new SenseElementViewHolder(v1);
        } else {// TODO: Handle unknown view type error more gracefully
            Log.e(LOG_TAG, "Unknown view type");
            viewHolder = null;
        }
        assert viewHolder != null;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == SENSE_ELEMENT_ITEM) {
            SenseElementViewHolder vh1 = (SenseElementViewHolder) holder;
            configureMeaningWithHeaderViewHolder(vh1, position);
        } else {// TODO: Handle unknown view type error more gracefully
            Log.e(LOG_TAG, "Unknown view type");
        }
    }

    private void configureMeaningWithHeaderViewHolder(SenseElementViewHolder holder, int position) {
        SenseElement senseElement = items.get(position);

        // TODO: Simplify Part of Speech values, currently long and too detailed
        // Get all the Part of Speech elements, and join them into a single string.
        List<String> partOfSpeech = senseElement.getPartOfSpeech();
        if(!partOfSpeech.isEmpty()) {
            holder.getPartOfSpeech().setText(TextUtils.join(", ", partOfSpeech));
        } else {
            holder.getPartOfSpeech().setVisibility(View.GONE);
        }

        String formattedPosition = holder.getMeaningNumber().getResources()
                .getString(R.string.detail_view_meaning_number, position + 1);
        holder.getMeaningNumber().setText(formattedPosition);

        // Get all the glosses for a Sense element, and join them into a single string
        List<String> glosses = senseElement.getGlosses();
        if(!glosses.isEmpty()) {
            holder.getGlosses().setText(TextUtils.join("; ", glosses));
        } else {
            holder.getGlosses().setVisibility(View.GONE);
        }

        // Get all the Field of Application elements, and join them into a single string
        List<String> fieldOfApplication = senseElement.getFieldOfApplication();
        if(!fieldOfApplication.isEmpty()) {
            holder.getFieldOfApplication().setText(TextUtils.join(", ", fieldOfApplication));
        } else {
            holder.getFieldOfApplication().setVisibility(View.GONE);
        }

        // Get all the Dialect elements, and join them into a single string
        List<String> dialect = senseElement.getDialect();
        if(!dialect.isEmpty()) {
            holder.getDialect().setText(TextUtils.join(", ", dialect));
        } else {
            holder.getDialect().setVisibility(View.GONE);
        }
    }
}
