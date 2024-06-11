package com.ikue.japanesedictionary.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ikue.japanesedictionary.R;

public class SenseElementViewHolder extends RecyclerView.ViewHolder {

    private final TextView partOfSpeech;
    private final TextView meaningNumber;
    private final TextView glosses;
    private final TextView fieldOfApplication;
    private final TextView dialect;

    public SenseElementViewHolder(View v) {
        super(v);
        partOfSpeech = v.findViewById(R.id.partOfSpeech);
        meaningNumber = v.findViewById(R.id.meaningNumber);
        glosses = v.findViewById(R.id.glosses);
        fieldOfApplication = v.findViewById(R.id.fieldOfApplication);
        dialect = v.findViewById(R.id.dialect);
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
