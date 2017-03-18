package com.ikue.japanesedictionary.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ikue.japanesedictionary.R;
import com.ikue.japanesedictionary.activities.EntryDetailActivity;
import com.ikue.japanesedictionary.database.DictionaryDbHelper;
import com.ikue.japanesedictionary.database.GetEntryDetailTask;
import com.ikue.japanesedictionary.database.GetRandomEntryTask;
import com.ikue.japanesedictionary.interfaces.DetailAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionaryItem;
import com.ikue.japanesedictionary.models.KanjiElement;
import com.ikue.japanesedictionary.models.ReadingElement;
import com.ikue.japanesedictionary.models.Tip;
import com.ikue.japanesedictionary.utils.DateTimeUtils;
import com.ikue.japanesedictionary.utils.TipsUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by luke_c on 02/03/2017.
 */

public class HomeFragment extends Fragment implements DetailAsyncCallbacks {
    // Singleton variable. DO NOT CHANGE
    private static DictionaryDbHelper helper;
    private static SharedPreferences sharedPref;
    private static AsyncTask task;
    private DetailAsyncCallbacks listener;

    private TextView wordOfTheDayPrimaryText;
    private TextView wordOfTheDaySecondaryText;
    private TextView wordOfTheDayMeaning;
    private Button wordOfTheDayMoreButton;
    private ImageButton wordOfTheDayShareButton;

    private Button feedbackButton;

    private TextView tipsTitleText;
    private TextView tipsBodyText;

    private DictionaryItem wordOfTheDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain the fragment so rotation does not repeatedly fire off new AsyncTasks
        setRetainInstance(true);

        // Set the OnTaskComplete listener
        listener = this;

        // Get the shared preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());

        // Get a database on startup.
        helper = DictionaryDbHelper.getInstance(this.getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        wordOfTheDayPrimaryText = (TextView) view.findViewById(R.id.word_of_the_day_primary);
        wordOfTheDaySecondaryText = (TextView) view.findViewById(R.id.word_of_the_day_secondary);
        wordOfTheDayMeaning = (TextView) view.findViewById(R.id.word_of_the_day_meanings);
        wordOfTheDayMoreButton = (Button) view.findViewById(R.id.word_of_the_day_button);
        wordOfTheDayShareButton = (ImageButton) view.findViewById(R.id.word_of_the_day_share_button);

        feedbackButton = (Button) view.findViewById(R.id.feedback_card_button);
        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uriText = "mailto:lukecasey94+ikue@gmail.com" + "?subject="
                        + Uri.encode("Ikue Japanese Dictionary - Feedback");

                Uri uri = Uri.parse(uriText);

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                if (sendIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(Intent.createChooser(sendIntent, "Send email"));
                }
            }
        });

        tipsTitleText = (TextView) view.findViewById(R.id.tips_card_title);
        tipsBodyText = (TextView) view.findViewById(R.id.tips_card_content);
        setupTipsCard();

        // Get the stored word of the day entry Id
        int wordOfTheDayEntryId = sharedPref.getInt("pref_wordOfTheDay_EntryId", 0);

        // If there is no stored entry, we need to make a new query
        if(wordOfTheDayEntryId == 0) {
            task = new GetRandomEntryTask(listener, helper).execute();
        } else {
            DateFormat df = DateFormat.getDateInstance();

            // Get the current date and time
            String currentDate = df.format(new Date());

            // Get the stored date and time to compare
            String wordOfTheDayDate = sharedPref.getString("pref_wordOfTheDay_Date", currentDate);

            long differenceInDays;
            try {
                differenceInDays = DateTimeUtils.getDifferenceInDays(df.parse(wordOfTheDayDate), df.parse(currentDate));
            } catch (ParseException e) {
                e.printStackTrace();
                differenceInDays = 1;
            }

            if(differenceInDays >= 1) {
                task = new GetRandomEntryTask(listener, helper).execute();

                // Update the stored date
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("pref_wordOfTheDay_Date", currentDate);
                editor.apply();

            } else {
                task = new GetEntryDetailTask(listener, helper, wordOfTheDayEntryId).execute();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // Cancel the AsyncTask if it is running when Activity is about to close
        // cancel(false) is safer and doesn't force an instant cancellation
        if (task != null) {
            task.cancel(false);
        }

        // Close the SQLiteHelper instance
        helper.close();
        super.onDestroy();
    }

    private void updateWordOfTheDay() {
        List<KanjiElement> kanjiElementList = wordOfTheDay.getKanjiElements();
        List<ReadingElement> readingElementList = wordOfTheDay.getReadingElements();

        // If there is a Kanji Element, then set it as the primary text view, and set the reading as
        // the secondary text view.
        if (kanjiElementList != null && !kanjiElementList.isEmpty()) {
            wordOfTheDayPrimaryText.setText(kanjiElementList.get(0).getValue());
            wordOfTheDaySecondaryText.setText(readingElementList.get(0).getValue());
        } else {
            // Otherwise just set the reading as the primary text view and hide the secondary text view
            wordOfTheDayPrimaryText.setText(readingElementList.get(0).getValue());
            wordOfTheDaySecondaryText.setVisibility(View.GONE);
        }

        // Get the glosses from the first Sense Element
        List<String> glosses = wordOfTheDay.getSenseElements().get(0).getGlosses();
        if(!glosses.isEmpty()) {
            wordOfTheDayMeaning.setText(TextUtils.join("; ", glosses));
        } else {
            wordOfTheDayMeaning.setVisibility(View.GONE);
        }

        // On click, take the user to the detail view
        wordOfTheDayMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = EntryDetailActivity.newIntent(view.getContext(), wordOfTheDay.getEntryId());
                getActivity().startActivity(i);
            }
        });

        // On click, share the entry to the user's chosen app
        wordOfTheDayShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getSharableWordOfTheDay());
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
            }
        });
    }

    @Override
    public void onResult(DictionaryItem result) {
        wordOfTheDay = result;

        // Get the stored word of the day entry Id
        int wordOfTheDayEntryId = sharedPref.getInt("pref_wordOfTheDay_EntryId", 0);

        // If the ID of the entry we retrieved is different to the ID stored, then update the
        // stored ID
        if(wordOfTheDayEntryId != wordOfTheDay.getEntryId()) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("pref_wordOfTheDay_EntryId", wordOfTheDay.getEntryId());
            editor.apply();
        }

        updateWordOfTheDay();
    }

    // Get a shareable string for the word of the day
    private String getSharableWordOfTheDay() {
        StringBuilder builder = new StringBuilder();

        List<KanjiElement> kanjiElementList = wordOfTheDay.getKanjiElements();
        List<ReadingElement> readingElementList = wordOfTheDay.getReadingElements();

        if (kanjiElementList != null && !kanjiElementList.isEmpty()) {
            builder.append(kanjiElementList.get(0).getValue());
            builder.append(" [");
            builder.append(readingElementList.get(0).getValue());
            builder.append("]");
        } else {
            builder.append(readingElementList.get(0).getValue());
        }

        List<String> glosses = wordOfTheDay.getSenseElements().get(0).getGlosses();
        if(!glosses.isEmpty()) {
            builder.append(" - ");
            builder.append(TextUtils.join("; ", glosses));
        }

        return builder.toString();
    }

    private void setupTipsCard() {
        Tip randomTip = TipsUtils.getRandomTip();

        tipsTitleText.setText(randomTip.getTitle());
        tipsBodyText.setText(randomTip.getBody());
    }


}
