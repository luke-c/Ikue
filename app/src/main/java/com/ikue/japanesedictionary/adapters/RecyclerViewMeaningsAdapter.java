package com.ikue.japanesedictionary.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.models.SenseElement;

import java.util.List;

/**
 * Created by luke_c on 06/02/2017.
 */

public class RecyclerViewMeaningsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0, TYPE_ITEM_WITH_HEADER = 1;

    private final String LOG_TAG = this.getClass().toString();

    private List<SenseElement> mItems;

    public RecyclerViewMeaningsAdapter(List<SenseElement> items) {
        mItems = items;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    // If the Sense Element has a part of speech, we want a header as well
    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position).getPartOfSpeech() == null) {
            return TYPE_ITEM;
        } else {
            return TYPE_ITEM_WITH_HEADER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case TYPE_ITEM:
                View v1 = inflater.inflate(R.layout.layout_meaning_item, viewGroup, false);
                viewHolder = new MeaningViewHolder(v1);
                break;
            case TYPE_ITEM_WITH_HEADER:
                View v2 = inflater.inflate(R.layout.layout_meaning_item_with_header, viewGroup, false);
                viewHolder = new MeaningWithHeaderViewHolder(v2);
                break;
            default:
                // TODO: Handle unknown view type error more gracefully
                Log.e(LOG_TAG, "Unknown view type");
                viewHolder = null;
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_ITEM:
                MeaningViewHolder vh1 = (MeaningViewHolder) holder;
                configureMeaningViewHolder(vh1, position);
                break;
            case TYPE_ITEM_WITH_HEADER:
                MeaningWithHeaderViewHolder vh2 = (MeaningWithHeaderViewHolder) holder;
                configureMeaningWithHeaderViewHolder(vh2, position);
                break;
            default:
                // TODO: Handle unknown view type error more gracefully
                Log.e(LOG_TAG, "Unknown view type");
                break;
        }
    }

    private void configureMeaningViewHolder(MeaningViewHolder vh1, int position) {
        SenseElement senseElement = mItems.get(position);

        // Get all the glosses for a Sense element, and join them into a single string
        List<String> glosses = senseElement.getGlosses();
        if(glosses != null) {
            vh1.getGlosses().setText(TextUtils.join("; ", glosses));
        } else {
            vh1.getGlosses().setVisibility(View.GONE);
        }

        // Get all the Field of Application elements, and join them into a single string
        List<String> fieldOfApplication = senseElement.getFieldOfApplication();
        if(fieldOfApplication != null) {
            vh1.getFieldOfApplication().setText(TextUtils.join(", ", fieldOfApplication));
        } else {
            vh1.getFieldOfApplication().setVisibility(View.GONE);
        }

        // Get all the Dialect elements, and join them into a single string
        List<String> dialect = senseElement.getDialect();
        if(dialect != null) {
            vh1.getDialect().setText(TextUtils.join(", ", dialect));
        } else {
            vh1.getDialect().setVisibility(View.GONE);
        }
    }

    private void configureMeaningWithHeaderViewHolder(MeaningWithHeaderViewHolder vh2, int position) {
        SenseElement senseElement = mItems.get(position);

        // TODO: Simplify Part of Speech values, currently long and too detailed
        // Get all the Part of Speech elements, and join them into a single string.
        List<String> partOfSpeech = senseElement.getPartOfSpeech();
        if(partOfSpeech != null) {
            vh2.getPartOfSpeech().setText(TextUtils.join(", ", partOfSpeech));
        } else {
            vh2.getPartOfSpeech().setVisibility(View.GONE);
        }

        // Get all the glosses for a Sense element, and join them into a single string
        List<String> glosses = senseElement.getGlosses();
        if(glosses != null) {
            vh2.getGlosses().setText(TextUtils.join("; ", glosses));
        } else {
            vh2.getGlosses().setVisibility(View.GONE);
        }

        // Get all the Field of Application elements, and join them into a single string
        List<String> fieldOfApplication = senseElement.getFieldOfApplication();
        if(fieldOfApplication != null) {
            vh2.getFieldOfApplication().setText(TextUtils.join(", ", fieldOfApplication));
        } else {
            vh2.getFieldOfApplication().setVisibility(View.GONE);
        }

        // Get all the Dialect elements, and join them into a single string
        List<String> dialect = senseElement.getDialect();
        if(dialect != null) {
            vh2.getDialect().setText(TextUtils.join(", ", dialect));
        } else {
            vh2.getDialect().setVisibility(View.GONE);
        }
    }
}
