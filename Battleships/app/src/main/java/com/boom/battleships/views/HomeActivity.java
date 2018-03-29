package com.boom.battleships.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.boom.battleships.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private void openBoardActivity() {
        Intent intent = new Intent(this, BoardActivity.class);
        startActivity(intent);


    }

    public void startGame(View view){
        openBoardActivity();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ListView currentGamesView;
        ListView finishedGamesView;
        List<RowItem> rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < 3; i++) {
            RowItem item = new RowItem(R.drawable.profile,"Name");
            rowItems.add(item);
        }

        currentGamesView = (ListView) findViewById(R.id.currentGames);
        CustomListViewAdapter adapter = new CustomListViewAdapter(this,
                R.layout.listview_game, rowItems);
        currentGamesView.setAdapter(adapter);
        finishedGamesView = (ListView) findViewById(R.id.finishedGames);
        finishedGamesView .setAdapter(adapter);

    }
}
