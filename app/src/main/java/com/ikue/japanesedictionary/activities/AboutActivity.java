package com.ikue.japanesedictionary.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.colinrtwhite.licensesdialog.LicensesDialog;
import com.colinrtwhite.licensesdialog.license.ApacheLicense20;
import com.colinrtwhite.licensesdialog.license.GnuGeneralPublicLicense30;
import com.colinrtwhite.licensesdialog.license.MitLicense;
import com.colinrtwhite.licensesdialog.model.Copyright;
import com.colinrtwhite.licensesdialog.model.Notice;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ikue.japanesedictionary.BuildConfig;
import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.utils.CreativeCommonsShareAlikeLicense;

import java.util.ArrayList;
import java.util.List;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.ic_launcher)
                .setDescription(getResources().getString(R.string.about_activity_description))
                .addItem(new Element().setTitle("Version: " + BuildConfig.VERSION_NAME))
                .addGroup(getResources().getString(R.string.about_activity_connect))
                .addEmail("lc94dev+ikue@gmail.com")
                .addGitHub("luke-c/Ikue")
                .addItem(getLicensesElement())
                .addItem(getTermsOfUseElement())
                .addItem(getThanksElement())
                .create();

        setContentView(aboutPage);
    }

    private Element getTermsOfUseElement() {
        Element element = new Element();
        element.setTitle(getResources().getString(R.string.about_activity_terms_of_use));
        element.setOnClickListener(view -> {
            Notice notice = new Notice("Ikue",
                    GnuGeneralPublicLicense30.INSTANCE,
                    "https://github.com/luke-c/Ikue",
                    new Copyright("Luke Casey", 2024));

            List<Notice> notices = List.of(notice);


            new LicensesDialog.Builder(AboutActivity.this)
                    .setTitle(getResources().getString(R.string.about_activity_terms_of_use))
                    .setNotices(notices)
                    .setAlwaysExpandLicenses(true)
                    .show();
        });
        return element;
    }

    private Element getLicensesElement() {
        Element element = new Element();
        element.setTitle(getResources().getString(R.string.about_activity_licenses));
        element.setOnClickListener(view -> {
            final List<Notice> notices = new ArrayList<>();
            notices.add(new Notice("SQLiteAssetHelper",
                    ApacheLicense20.INSTANCE,
                    "https://github.com/jgilfelt/android-sqlite-asset-helper",
                    new Copyright("readyState Software Ltd", 2011)
            ));

            notices.add(new Notice("Android About Page",
                    MitLicense.INSTANCE,
                    "https://github.com/medyo/android-about-page",
                    new Copyright("Mehdi Sakout", 2016)
            ));

            notices.add(new Notice("WanaKanaJava",
                    MitLicense.INSTANCE,
                    "https://github.com/MasterKale/WanaKanaJava",
                    new Copyright("Matthew Miller", 2013)
            ));

            notices.add(new Notice("ERDRG",
                    new CreativeCommonsShareAlikeLicense(this),
                    "http://www.edrdg.org/",
                    null
            ));

            new LicensesDialog.Builder(AboutActivity.this)
                    .setTitle(getResources().getString(R.string.about_activity_licenses))
                    .setNotices(notices)
                    .setIncludeOwnLicense(true)
                    .show();
        });
        return element;
    }

    private Element getThanksElement() {
        Element element = new Element();
        element.setTitle(getResources().getString(R.string.about_activity_thanks));
        element.setOnClickListener(view -> new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.about_activity_thanks))
                .setMessage(getString(R.string.about_activity_erdrg_thanks))
                .setPositiveButton(getString(R.string.about_activity_close), (dialog, which) -> dialog.dismiss())
                .show());
        return element;
    }
}
