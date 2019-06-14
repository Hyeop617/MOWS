package com.example.mows.fragment


import android.os.Bundle
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.util.Log

import com.example.mows.R


class SettingFragment : PreferenceFragmentCompat() {

    @Override
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preference)

        val listAgePref = findPreference("list_preference_1") as ListPreference
        listAgePref.setOnPreferenceChangeListener { preference, newValue ->

            Log.d("Setting","value : ${listAgePref.value}")


            true
        }
    }






}
