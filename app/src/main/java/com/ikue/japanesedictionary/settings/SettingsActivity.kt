package com.ikue.japanesedictionary.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.ikue.japanesedictionary.R
import com.ikue.japanesedictionary.databinding.SettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: SettingsBinding

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        supportFragmentManager.commit {
            add(
                R.id.settings_container,
                SettingsFragment()
            )
        }
    }
}