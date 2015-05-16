package com.jhowell.battletap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {
    // Handles the main game counter
    private TextView textCounter;
    private double count;//a

    // Handles requests
    private static final int BATTLE_REQUEST = 1;

    // test
    private TextView archers,knights,cavalry;
    private int a,k,c;
    // end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the game counter and set it to 0
        textCounter = (TextView)findViewById(R.id.text_counter);
        /* Implement try/catch with saving */
        count = 0;
        updateCounter(count);

        // test
        archers = (TextView)findViewById(R.id.archer_counter);
        archers.setText("Archers: 0");
        knights = (TextView)findViewById(R.id.knight_counter);
        knights.setText("Knights: 0");
        cavalry = (TextView)findViewById(R.id.cavalry_counter);
        cavalry.setText("Cavalry: 0");
        a=k=c=0;
        // end
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If returning from battle, do the following...
        if (requestCode == BATTLE_REQUEST && resultCode == RESULT_OK) {
            //do stuff
        }
    }

    /**********  --------------  **********/
    /********** | GAME METHODS | **********/
    /**********  --------------  **********/

    public void incrementCounter(View v) {
        // Add one to the counter
        updateCounter(++count);
    }

    public void updateCounter(double count) {
        // If counter is too large, use E notation, else display counter
        if (count > 1E15) textCounter.setText(String.format("%.0e", count));
        else textCounter.setText(String.format("%,d", (long)count));
    }

    // test
    public void purchaseArcher(View v) {
        if (count >= 10) {
            String str = archers.getText().toString();
            String newstr = str.substring(0, str.length() - String.valueOf(a++).length()) + String.valueOf(a);
            archers.setText(newstr);
            count -= 10;
            updateCounter(count);
        }
    }

    public void purchaseKnight(View v) {
        if (count >= 5) {
            String str = knights.getText().toString();
            String newstr = str.substring(0, str.length() - String.valueOf(k++).length()) + String.valueOf(k);
            knights.setText(newstr);
            count -= 5;
            updateCounter(count);
        }
    }

    public void purchaseCavalry(View v) {
        if (count >= 20) {
            String str = cavalry.getText().toString();
            String newstr = str.substring(0, str.length() - String.valueOf(c++).length()) + String.valueOf(c);
            cavalry.setText(newstr);
            count -= 20;
            updateCounter(count);
        }
    }
    // end
}

// start battle when button is pressed
//        AlertDialog.Builder notification = new AlertDialog.Builder(getApplicationContext());
//        notification
//                .setMessage(R.string.battle_notification)
//                .setPositiveButton(R.string.battle_notification_yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent battleIntent = new Intent(getApplicationContext(), BattleActivity.class);
//                        startActivityForResult(battleIntent, BATTLE_REQUEST);
//                    }
//                })
//                .setNegativeButton(R.string.battle_notification_no, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                })
//                .create();
//        // Display battle notification
//        notification.show();