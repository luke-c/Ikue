package com.ikue.japanesedictionary.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ikue.japanesedictionary.R
import com.ikue.japanesedictionary.databinding.SettingsBinding
import com.ikue.japanesedictionary.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setTitle(R.string.settings_activity_title)
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.commit { replace(R.id.settings_container, SettingsFragment()) }
    }
}