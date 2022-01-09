package com.example.hellodroid;

import android.animation.*;
import android.os.Build;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class HelloDroidActivity extends AppCompatActivity{
    int currentMoney = 100;
    int currentHealth = 100;
    int currentEnemyHealth = 100;
    boolean utilityOnHand = false;
    double[][] defenderLoc = new double[4][2];
    double[] enemyLoc = new double[2];
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_scene);
    }

    public void transferButtonClicked(View view) {
        setContentView(R.layout.second_scene);
        setMoney(currentMoney);
        setHealth(currentHealth);
        setEnemyHealth(currentEnemyHealth);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void playButtonClicked(View view) {
        ImageView enemy = (ImageView)findViewById(R.id.enemy);
        enemy.setVisibility(View.VISIBLE);

        ObjectAnimator translateXAnimation1= ObjectAnimator.ofFloat(enemy, "translationX", 0, 0);
        ObjectAnimator translateYAnimation1= ObjectAnimator.ofFloat(enemy, "translationY", 0, 600);

        ObjectAnimator translateXAnimation2= ObjectAnimator.ofFloat(enemy, "translationX", 0, 400);
        ObjectAnimator translateYAnimation2= ObjectAnimator.ofFloat(enemy, "translationY", 600, 600);

        AnimatorSet set1 = new AnimatorSet();
        set1.setDuration(3000);
        set1.playTogether(translateXAnimation1, translateYAnimation1);
        set1.start();
        AnimatorSet set2 = new AnimatorSet();
        set2.setDuration(2000);
        set2.playTogether(translateXAnimation2, translateYAnimation2);

        translateXAnimation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                enemyLoc[0] = (Float)animation.getAnimatedValue();
            }
        });
        translateYAnimation1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                enemyLoc[1] = (Float)animation.getAnimatedValue();
            }
        });
        translateXAnimation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                enemyLoc[0] = (Float)animation.getAnimatedValue();
            }
        });
        translateYAnimation2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                enemyLoc[1] = (Float)animation.getAnimatedValue();
            }
        });

        set1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                set2.start();
            }
        });
        set2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                enemy.setVisibility(View.INVISIBLE);
                setHealth(currentHealth-50);
            }
        });

        Timer t = new Timer();
        TimerTask tt = new TimerTask() {
            double distance;
            @Override
            public void run() {
                if (defenderLoc[0][0] != 0) {
                    double ex = defenderLoc[0][0] - 540;
                    double ey = defenderLoc[0][1] - 241;

                    distance = Math.pow(Math.pow(ex - enemyLoc[0], 2) + (Math.pow(ey - enemyLoc[1], 2)), 0.5);
                    if (distance <= 100) {
                        setEnemyHealth(currentEnemyHealth - 5);
                    }
                }
            }
        };
        t.scheduleAtFixedRate(tt, 0,100);


    }

    public void setMoney(int i) {
        currentMoney = i;
        TextView moneyTextView = findViewById(R.id.money);
        moneyTextView.setText("Money: "+Integer.toString(currentMoney));
    }

    public void setHealth(int i) {
        currentHealth = i;
        TextView healthTextView = findViewById(R.id.health);
        healthTextView.setText("Health: "+Integer.toString(currentHealth));
    }

    public void setEnemyHealth(int i) {
        currentEnemyHealth = i;
        ProgressBar enemyHealthBar = findViewById(R.id.enemyHealthBar);
        enemyHealthBar.setProgress(currentEnemyHealth);
    }

    public void buyButtonClicked(View view) {
        if (currentMoney > 0) {
            setMoney(currentMoney - 25);
            utilityOnHand = true;
        }
    }

    public void imageviewClikcked(View view) {
        if (utilityOnHand) {
            ImageView toupdate = findViewById(view.getId());
            toupdate.setImageResource(R.drawable.utility);
            toupdate.getLayoutParams().height = 50;
            toupdate.getLayoutParams().width = 50;

            int[] loc = new int[2];
            view.getLocationOnScreen(loc);
            double[] defloc = new double[2];
            defloc[0] = (double) loc[0];
            defloc[1] = (double) loc[1];
            defenderLoc[count] = defloc;
            count++;
            utilityOnHand = false;
        }
    }
}
