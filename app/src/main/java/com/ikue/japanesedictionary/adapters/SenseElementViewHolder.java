package com.ikue.japanesedictionary.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ikue.japanesedictionary.R;

public class SenseElementViewHolder extends RecyclerView.ViewHolder {

    private TextView partOfSpeech, meaningNumber, glosses, fieldOfApplication, dialect;

    public SenseElementViewHolder(View v) {
        super(v);
        partOfSpeech = (TextView) v.findViewById(R.id.partOfSpeech);
        meaningNumber = (TextView) v.findViewById(R.id.meaningNumber);
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

    public TextView getMeaningNumber() {
        return meaningNumber;
    }

}
