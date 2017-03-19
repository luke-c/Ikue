package com.ikue.japanesedictionary.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ikue.japanesedictionary.BuildConfig;
import com.ikue.japanesedictionary.R;

import de.psdev.licensesdialog.LicensesDialog;
import de.psdev.licensesdialog.licenses.ApacheSoftwareLicense20;
import de.psdev.licensesdialog.licenses.MITLicense;
import de.psdev.licensesdialog.model.Notice;
import de.psdev.licensesdialog.model.Notices;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

/**
 * Created by luke_c on 26/02/2017.
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(getResources().getString(R.string.about_activity_description))
                .addItem(new Element().setTitle(BuildConfig.VERSION_NAME))
                .addGroup(getResources().getString(R.string.about_activity_connect))
                .addEmail("lukecasey94+ikue@gmail.com")
                .addTwitter("lukecasey94")
                .addGitHub("luke-c")
                .addItem(getLicensesElement())
                .create();

        setContentView(aboutPage);
    }

    private Element getLicensesElement() {
        Element element = new Element();
        element.setTitle(getResources().getString(R.string.about_activity_licenses));
        element.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Notices notices = new Notices();
                notices.addNotice(new Notice("SQLiteAssetHelper",
                        "https://github.com/jgilfelt/android-sqlite-asset-helper",
                        "Copyright (C) 2011 readyState Software Ltd\n" +
                                "Copyright (C) 2007 The Android Open Source Project",
                        new ApacheSoftwareLicense20()));

                notices.addNotice(new Notice("Android About Page",
                        "https://github.com/medyo/android-about-page",
                        "The MIT License (MIT)\n" +
                                "Copyright (c) 2016 Mehdi Sakout",
                        new MITLicense()));

                notices.addNotice(new Notice("ChipCloud",
                        "https://github.com/fiskurgit/ChipCloud",
                        "The MIT License (MIT)\n" +
                                "\n" +
                                "Copyright (c) 2016 Jonathan Fisher",
                        new MITLicense()));

                notices.addNotice(new Notice("FlexBoxLayout",
                        "https://github.com/google/flexbox-layout",
                        "",
                        new ApacheSoftwareLicense20()));

                new LicensesDialog.Builder(AboutActivity.this)
                        .setTitle(getResources().getString(R.string.about_activity_licenses))
                        .setNotices(notices)
                        .setIncludeOwnLicense(true)
                        .build()
                        .show();
            }
        });
        return element;
    }
}
