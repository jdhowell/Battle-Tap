package com.jhowell.battletap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BattleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
    }

    /* will change to once battle is completed and user presses a button */
    // test
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Intent returnIntent = new Intent();
//            setResult(RESULT_OK, returnIntent);
//            finish();
//        }
//    }
    // end
}