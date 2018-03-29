package com.boom.battleships.views;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.boom.battleships.R;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {
    private ArrayList<Integer> board=new ArrayList<Integer>();


    public ArrayList<ImageView> generateButtons(){
        ArrayList<ImageView> buttons=new ArrayList<ImageView>();
        for(int i=0;i<64;i++){
            Log.d("position", String.valueOf(i));
            ImageView newbutton=new ImageView(this);
            board.add(0);

            buttons.add(newbutton);

        }
        return buttons;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);


        final GridView boardGame;
        boardGame = (GridView) findViewById(R.id.gridView);
        final ArrayList<ImageView> images= generateButtons();
        final GridViewAdapter gridAdapter=new GridViewAdapter(this,images);
        boardGame.setAdapter(gridAdapter);
        boardGame.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            TextView tv_availableBoats= findViewById(R.id.txtNumBoats);
            TextView tv_destroyedBoats= findViewById(R.id.txtNumDestroy);
            TextView tv_points= findViewById(R.id.txtNumPoints);

            String str_availableBoats = (String) tv_availableBoats.getText();
            String str_destroyedBoats= (String) tv_destroyedBoats.getText();
            String str_points= (String) tv_points.getText();
            int availableBoats= Integer.parseInt(str_availableBoats);
            int destroyedBoats= Integer.parseInt(str_destroyedBoats);
            int points=  Integer.parseInt(str_points);

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView currentPosition;
                Log.d("Entre", String.valueOf(position));
                str_availableBoats = (String) tv_availableBoats.getText();
                if(board.get(position)==0  ){
                    if(!str_availableBoats.equals("0")) {
                        board.set(position, 1);
                        currentPosition = (ImageView) gridAdapter.getItem(position);
                        currentPosition.setImageResource(R.drawable.boat);

                        availableBoats = Integer.parseInt(str_availableBoats) - 1;
                        tv_availableBoats.setText(String.valueOf(availableBoats));
                    }
                }else{
                    board.set(position,0);
                    currentPosition=(ImageView)gridAdapter.getItem(position);
                    currentPosition.setImageResource(R.drawable.wave);

                    availableBoats= Integer.parseInt(str_availableBoats)+1;
                    tv_availableBoats.setText(String.valueOf(availableBoats));
                }

            }
        });





    }
}
