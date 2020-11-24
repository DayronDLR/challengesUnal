package co.edu.unal.androidtic_tac_toe;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.graphics.Color;



@SuppressWarnings("deprecation")
public class Setting extends PreferenceActivity implements
        ColorPickerDialog.OnColorChangedListener {

    public static final String SOUND = "sound";
    public static final String GOES_FIRST = "goes_first";
    public static final String BOARD_COLOR = "board_color";
    public static final String VICTORY_MESSAGE = "victory_message";
    public static final String DIFFICULTY_LEVEL = "difficulty_level";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        final ListPreference goesFirstPref = (ListPreference) findPreference(GOES_FIRST);
        String goesFirst = prefs.getString(GOES_FIRST, "Alternate");
        goesFirstPref.setSummary((CharSequence) goesFirst);
        goesFirstPref.setOnPreferenceChangeListener((preference, newValue) -> {
            goesFirstPref.setSummary((CharSequence) newValue);

            SharedPreferences.Editor ed = prefs.edit();
            ed.putString(GOES_FIRST, newValue.toString());
            ed.apply();

            return true;
        });

        final ListPreference difficultyLevelPref = (ListPreference)
                findPreference(DIFFICULTY_LEVEL);
        String difficulty = prefs.getString(DIFFICULTY_LEVEL,
                getResources().getString(R.string.difficulty_expert));
        difficultyLevelPref.setSummary((CharSequence) difficulty);
        difficultyLevelPref.setOnPreferenceChangeListener((preference, newValue) -> {
            difficultyLevelPref.setSummary((CharSequence) newValue);

            // Since we are handling the pref, we must save it
            SharedPreferences.Editor ed = prefs.edit();
            ed.putString(DIFFICULTY_LEVEL, newValue.toString());
            ed.apply();

            return true;
        });

        final EditTextPreference victoryMessagePref = (EditTextPreference)
                findPreference(VICTORY_MESSAGE);
        String victoryMessage = prefs.getString(VICTORY_MESSAGE, getResources().getString(R.string.result_human_wins));
        victoryMessagePref.setSummary("\"" + victoryMessage + "\"");
        victoryMessagePref.setOnPreferenceChangeListener((preference, newValue) -> {
            victoryMessagePref.setSummary((CharSequence) newValue);

            SharedPreferences.Editor ed = prefs.edit();
            ed.putString(VICTORY_MESSAGE, newValue.toString());
            ed.apply();

            return true;
        });

        final Preference boardColorPref = (Preference) findPreference(BOARD_COLOR);
        boardColorPref.setOnPreferenceClickListener(preference -> {
            int color = prefs.getInt(BOARD_COLOR, Color.GRAY);
            new ColorPickerDialog(Setting.this, Setting.this,
                    color).show();
            return true;
        });

    }
    @Override
    public void colorChanged(int color) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(
                BOARD_COLOR, color).apply();
    }
}
