package com.ikue.japanesedictionary.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ikue.japanesedictionary.BuildConfig;
import com.ikue.japanesedictionary.R;

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
                .addGroup("Connect with us")
                .addEmail("lukecasey94+ikue@gmail.com")
                .addTwitter("lukecasey94")
                .addGitHub("luke-c")
                .addGroup("Open source libraries")
                .addItem(getLibraryElement("SQLiteAssetHelper", "https://github.com/jgilfelt/android-sqlite-asset-helper"))
                .addItem(getLibraryElement("Android About Page", "https://github.com/medyo/android-about-page"))
                .addItem(getLibraryElement("ChipCloud", "https://github.com/fiskurgit/ChipCloud"))
                .addItem(getLibraryElement("FlexBoxLayout", "https://github.com/google/flexbox-layout"))
                .create();

        setContentView(aboutPage);
    }

    private Element getLibraryElement(String title, String url) {
        Element element = new Element();
        element.setTitle(title);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        element.setIntent(intent);
        return element;
    }
}
