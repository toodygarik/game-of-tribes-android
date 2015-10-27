package com.garik.android.gameoflife;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class GameActivity extends AppCompatActivity {

    private GameModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        model = new GameModel(65, 45);
        model.setupRandom(500);
        GameView gameView = (GameView) findViewById(R.id.game_view);
        gameView.setup(model);
        gameView.start(33);
    }

}
