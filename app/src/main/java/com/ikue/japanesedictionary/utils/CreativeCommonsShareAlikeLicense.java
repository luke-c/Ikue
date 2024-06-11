package com.ikue.japanesedictionary.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.colinrtwhite.licensesdialog.model.License;
import com.ikue.japanesedictionary.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class CreativeCommonsShareAlikeLicense implements License {

    private final Context context;

    public CreativeCommonsShareAlikeLicense(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public String getText() {
        return getContent(context, R.raw.cc_sa_40_full);
    }

    @NonNull
    @Override
    public String getTitle() {
        return "CC BY-SA 4.0 - ATTRIBUTION-SHAREALIKE 4.0 INTERNATIONAL";
    }

    @NonNull
    @Override
    public String getCopyrightPrefix() {
        return "Copyright";
    }

    private String getContent(final Context context, final int contentResourceId) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(contentResourceId), StandardCharsets.UTF_8))) {
            return toString(reader);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private String toString(final BufferedReader reader) throws IOException {
        final StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append(System.lineSeparator());
        }
        return builder.toString();
    }
}
