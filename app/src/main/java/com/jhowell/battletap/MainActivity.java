package com.jhowell.battletap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Components
    private TextView textCounter;
    private TextView firstPlusOneTextView, secondPlusOneTextView, thirdPlusOneTextView;
    private TextView archers, knights, cavalry;

    // Animations + Threads
    private Animation fadeOut;
    private Thread countAnimationThread;

    // Primitives
    private int count;
    private int archerCount, knightCount, calvaryCount;
    private boolean shouldRun = true;

    // Static variables
    private static final int BATTLE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialization of components
        archers = (TextView) findViewById(R.id.archer_counter);
        knights = (TextView) findViewById(R.id.knight_counter);
        cavalry = (TextView) findViewById(R.id.cavalry_counter);

        textCounter = (TextView) findViewById(R.id.text_counter);
        firstPlusOneTextView = (TextView) findViewById(R.id.firstPlusOneText);
        secondPlusOneTextView = (TextView) findViewById(R.id.secondPlusOneText);
        thirdPlusOneTextView = (TextView) findViewById(R.id.thirdPlusOneText);

        //Initialization of animations
        fadeOut = new AlphaAnimation(1.0f , 0.0f);
        fadeOut.setDuration(300);
        fadeOut.setFillBefore(true);

        // Implement try/catch with saving - What does this even mean josh?
        count = 0;
        archerCount = knightCount = calvaryCount = 0;
        updateCounter(count);

        Button buttonCounter = (Button) findViewById(R.id.button_counter);
        buttonCounter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) motionEvent.getX();
                    int y = (int) motionEvent.getY();
                    incrementCounter(x, y);
                }
                return true;
            }
        });

        countAnimationThread = new Thread() {
            public void run() {
                while (shouldRun) {
                    try {
                        Thread.sleep(2);
                        runOnUiThread(new Runnable() {
                            public void run() {
//                                if (firstPlusOneTextView.getY() > 50) {
                                    firstPlusOneTextView.setY(firstPlusOneTextView.getY() - 2);
//                                    if (firstPlusOneTextView.getY() < 200) {
//                                        firstPlusOneTextView.setVisibility(View.INVISIBLE);
//                                    }
//                                }
//                                if (secondPlusOneTextView.getY() > 50) {
                                    secondPlusOneTextView.setY(secondPlusOneTextView.getY() - 2);
//                                    if (secondPlusOneTextView.getY() < 200) {
//                                        secondPlusOneTextView.setVisibility(View.INVISIBLE);
//                                    }
//                                }
//                                if (thirdPlusOneTextView.getY() > 50) {
                                    thirdPlusOneTextView.setY(thirdPlusOneTextView.getY() - 2);
//                                    if (thirdPlusOneTextView.getY() < 200) {
//                                        thirdPlusOneTextView.setVisibility(View.INVISIBLE);
//                                    }
//                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        countAnimationThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If returning from battle, do the following...
        if (requestCode == BATTLE_REQUEST && resultCode == RESULT_OK) {
            // Do something
        }
    }

    /**********  --------------  **********/
    /********** | GAME METHODS | **********/
    /**********  --------------  **********/

    public void incrementCounter(int x, int y) {
        updateCounter(++count);
        switch(count%3) {
            case 0:
                firstPlusOneTextView.setX(x);
                firstPlusOneTextView.setY(y);
                firstPlusOneTextView.startAnimation(fadeOut);
                break;
            case 1:
                secondPlusOneTextView.setX(x);
                secondPlusOneTextView.setY(y);
                secondPlusOneTextView.startAnimation(fadeOut);
                break;
            case 2:
                thirdPlusOneTextView.setX(x);
                thirdPlusOneTextView.setY(y);
                thirdPlusOneTextView.startAnimation(fadeOut);
                break;
        }
    }

    public void updateCounter(double count) {
        // If counter is too large, use E notation, else display counter. -Seems completely unnecessary..I doubt theyll click over 2billion
        if (count > 1E15)
            textCounter.setText(String.format("%.0e", count));
        else
            textCounter.setText(String.format("%,d", (long) count));
    }

    public void startBattle(View v) {
        AlertDialog.Builder notification = new AlertDialog.Builder(this);
        notification
                .setMessage(R.string.battle_notification)
                .setPositiveButton(R.string.battle_notification_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // If yes, start the battle
                        Intent battleIntent = new Intent(getApplicationContext(), BattleActivity.class);
                        startActivityForResult(battleIntent, BATTLE_REQUEST);
                    }
                })
                .setNegativeButton(R.string.battle_notification_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Do nothing and return to screen
                    }
                })
                .create();
        // Display battle notification
        notification.show();
    }

    public void purchaseArcher(View v) {
        if (count >= 10) {
            String currentArcherAmount = archers.getText().toString();
            int newArcherAmount = Integer.parseInt(currentArcherAmount) + 1;
            archers.setText(newArcherAmount + "");
            count -= 10;
            updateCounter(count);
        }
    }

    public void purchaseKnight(View v) {
        if (count >= 5) {
            String currentKnightAmount = knights.getText().toString();
            int newKnightAmount = Integer.parseInt(currentKnightAmount) + 1;
            knights.setText(newKnightAmount + "");
            count -= 5;
            updateCounter(count);
        }
    }

    public void purchaseCavalry(View v) {
        if (count >= 20) {
            String currentCavalryAmount = cavalry.getText().toString();
            int newCavalryAmount = Integer.parseInt(currentCavalryAmount) + 1;
            cavalry.setText(newCavalryAmount + "");
            count -= 20;
            updateCounter(count);
        }
    }
}