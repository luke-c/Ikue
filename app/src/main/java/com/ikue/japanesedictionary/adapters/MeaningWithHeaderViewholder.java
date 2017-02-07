package com.ikue.japanesedictionary.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ikue.japanesedictionary.R;

/**
 * Created by luke_c on 06/02/2017.
 */

public class MeaningWithHeaderViewHolder extends RecyclerView.ViewHolder {

    private TextView partOfSpeech, glosses, fieldOfApplication, dialect;

    public MeaningWithHeaderViewHolder(View v) {
        super(v);
        partOfSpeech = (TextView) v.findViewById(R.id.partOfSpeech);
        glosses = (TextView) v.findViewById(R.id.glosses);
        fieldOfApplication = (TextView) v.findViewById(R.id.fieldOfApplication);
        dialect = (TextView) v.findViewById(R.id.dialect);
    }

    public TextView getPartOfSpeech() {
        return partOfSpeech;
    }


    public TextView getDialect() {
        return dialect;
    }


    public TextView getFieldOfApplication() {
        return fieldOfApplication;
    }


    public TextView getGlosses() {
        return glosses;
    }

}
