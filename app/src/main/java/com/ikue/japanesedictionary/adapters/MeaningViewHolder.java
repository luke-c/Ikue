package com.ikue.japanesedictionary.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ikue.japanesedictionary.R;

/**
 * Created by luke_c on 06/02/2017.
 */

public class MeaningViewHolder extends RecyclerView.ViewHolder {

    private TextView glosses, fieldOfApplication, dialect;

    public MeaningViewHolder(View v) {
        super(v);
        glosses = (TextView) v.findViewById(R.id.glosses);
        fieldOfApplication = (TextView) v.findViewById(R.id.fieldOfApplication);
        dialect = (TextView) v.findViewById(R.id.dialect);
    }

    public TextView getDialect() {
        return dialect;
    }

    public void setDialect(TextView dialect) {
        this.dialect = dialect;
    }

    public TextView getFieldOfApplication() {
        return fieldOfApplication;
    }

    public void setFieldOfApplication(TextView fieldOfApplication) {
        this.fieldOfApplication = fieldOfApplication;
    }

    public TextView getGlosses() {
        return glosses;
    }

    public void setGlosses(TextView glosses) {
        this.glosses = glosses;
    }
}
