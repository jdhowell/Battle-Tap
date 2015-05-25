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

public class HomeActivity extends AppCompatActivity {

    // Database + Player
    private DatabaseHandler db;
    private Player player;

    // Components
    private TextView username, textCounter;
    private TextView firstPlusOneTextView, secondPlusOneTextView, thirdPlusOneTextView;
    private TextView archers, knights, cavalry;

    // Animations + Threads
    private Animation fadeOut;
    private Thread countAnimationThread;

    // Primitives
    private int count;
    private boolean shouldRun = true;

    // Static variables
    private static final int BATTLE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialization of database and player
        db = new DatabaseHandler(this);
        player = (Player) getIntent().getExtras().getSerializable("player");

        // Initialization of components
        archers = (TextView) findViewById(R.id.archer_counter);
        knights = (TextView) findViewById(R.id.knight_counter);
        cavalry = (TextView) findViewById(R.id.cavalry_counter);

        username = (TextView) findViewById(R.id.username);
        textCounter = (TextView) findViewById(R.id.text_counter);
        firstPlusOneTextView = (TextView) findViewById(R.id.firstPlusOneText);
        secondPlusOneTextView = (TextView) findViewById(R.id.secondPlusOneText);
        thirdPlusOneTextView = (TextView) findViewById(R.id.thirdPlusOneText);

        // Initialization of animations
        fadeOut = new AlphaAnimation(1.0f , 0.0f);
        fadeOut.setDuration(300);
        fadeOut.setFillBefore(true);

        // Restore counter
        username.setText(player.getUsername());
        count = player.getCount();
        updateCounter(count);

        // Display "+1" wherever the user touches the screen
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
        startAnimationThread();
    }

    @Override
    public void onResume() {
        super.onResume();
        shouldRun = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        shouldRun = false;
        player.setCount(count);
        db.updatePlayer(player);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If returning from battle, do the following...
        if (requestCode == BATTLE_REQUEST && resultCode == RESULT_OK) {
            startAnimationThread();
        }
    }

    public void incrementCounter(int x, int y) {
        // If counter hits max value, stop incrementing
        if (count != Integer.MAX_VALUE) {
            updateCounter(++count);
        }
        switch(count % 3) {
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

    public void updateCounter(int count) {
        textCounter.setText(String.format("%,d", count));
    }

    public void startBattle(View v) {
        // Initialization of the notification
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

    public void startAnimationThread() {
        countAnimationThread = new Thread() {
            public void run() {
                while (shouldRun) {
                    try {
                        Thread.sleep(2);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                firstPlusOneTextView.setY(firstPlusOneTextView.getY() - 2);
                                secondPlusOneTextView.setY(secondPlusOneTextView.getY() - 2);
                                thirdPlusOneTextView.setY(thirdPlusOneTextView.getY() - 2);
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

    public void purchaseArcher(View v) {
        if (count >= 10) {
            String currentArcherAmount = archers.getText().toString();
            int newArcherAmount = Integer.parseInt(currentArcherAmount) + 1;
            archers.setText(String.valueOf(newArcherAmount));
            count -= 10;
            updateCounter(count);
        }
    }

    public void purchaseKnight(View v) {
        if (count >= 5) {
            String currentKnightAmount = knights.getText().toString();
            int newKnightAmount = Integer.parseInt(currentKnightAmount) + 1;
            knights.setText(String.valueOf(newKnightAmount));
            count -= 5;
            updateCounter(count);
        }
    }

    public void purchaseCavalry(View v) {
        if (count >= 20) {
            String currentCavalryAmount = cavalry.getText().toString();
            int newCavalryAmount = Integer.parseInt(currentCavalryAmount) + 1;
            cavalry.setText(String.valueOf(newCavalryAmount));
            count -= 20;
            updateCounter(count);
        }
    }
}