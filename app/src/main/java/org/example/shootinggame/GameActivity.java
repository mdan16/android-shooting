package org.example.shootinggame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class GameActivity extends AppCompatActivity implements GameView.EventCallback {
    private GameView gameView;
    public static int winWidth, winHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        gameView.setEventCallback(this);
        gameView.startSensor();
        setContentView(gameView);

        WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
        Display disp = wm.getDefaultDisplay();
        winWidth = disp.getWidth();
        winHeight = disp.getHeight();
    }

    @Override
    public void onGameOver(String winnerName, String loserName, boolean win) {
        gameView.stopDrawThread();
        String message;
        if (win) {
            message = "You Win";
        } else {
            message = "You Lose";
        }
        Toast.makeText(this, message + "\nWinner: " + winnerName + "\nLoser: " + loserName, Toast.LENGTH_LONG).show();
    }
}
