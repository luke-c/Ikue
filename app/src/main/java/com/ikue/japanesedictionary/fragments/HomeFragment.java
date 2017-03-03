package com.ikue.japanesedictionary.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.ikue.japanesedictionary.database.GetRandomEntryTask;
import com.ikue.japanesedictionary.interfaces.DetailAsyncCallbacks;
import com.ikue.japanesedictionary.models.DictionaryItem;
import com.ikue.japanesedictionary.models.KanjiElement;
import com.ikue.japanesedictionary.models.ReadingElement;

import java.util.List;

/**
 * Created by luke_c on 02/03/2017.
 */

public class HomeFragment extends Fragment implements DetailAsyncCallbacks {
    // Singleton variable. DO NOT CHANGE
    private static DictionaryDbHelper helper;

    private static AsyncTask task;
    private DetailAsyncCallbacks listener;

    private TextView wordOfTheDayPrimaryText;
    private TextView wordOfTheDaySecondaryText;
    private TextView wordOfTheDayMeaning;
    private Button wordOfTheDayMoreButton;
    private ImageButton wordOfTheDayShareButton;

    private DictionaryItem wordOfTheDay;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain the fragment so rotation does not repeatedly fire off new AsyncTasks
        setRetainInstance(true);

        // Set the OnTaskComplete listener
        listener = this;

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

        task = new GetRandomEntryTask(listener, helper).execute();
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

        // On click, share the entry to the user's choosen app
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
}
