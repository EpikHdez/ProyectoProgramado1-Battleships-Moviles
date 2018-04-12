package com.boom.battleships.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.boom.battleships.R;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity {
    private ArrayList<Integer> board=new ArrayList<Integer>();
    private int state=0;
    private int numbomb=1;



    public void onClickBoats(View view){
        state=0;
        Button button=findViewById(R.id.btnBoat);
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        Button button1=findViewById(R.id.btnBomb);
        button1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Button button2=findViewById(R.id.btnStore);
        button2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public void onClickBombs(View view){
        state=1;
        Button button=findViewById(R.id.btnBomb);
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        Button button1=findViewById(R.id.btnBoat);
        button1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Button button2=findViewById(R.id.btnStore);
        button2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


    }

    public void changeImage(ImageView imageView, int position){
        TextView tv_availableBoats= findViewById(R.id.txtNumBoats);
        TextView tv_destroyedBoats= findViewById(R.id.txtNumDestroy);
        TextView tv_points= findViewById(R.id.txtNumPoints);

        String str_availableBoats = (String) tv_availableBoats.getText();
        String str_destroyedBoats= (String) tv_destroyedBoats.getText();
        String str_points= (String) tv_points.getText();
        int numb_boats=Integer.parseInt(str_availableBoats);
        switch (state){
            case 0:
                if( board.get(position)==0){
                    if(numb_boats!=0) {
                        imageView.setImageResource(R.drawable.boat);
                        board.set(position, 1);
                        numb_boats--;
                        tv_availableBoats.setText(String.valueOf(numb_boats));
                    }
                }else{
                    if( board.get(position)==1) {
                        imageView.setImageResource(R.drawable.wave);
                        board.set(position, 0);
                        numb_boats++;
                        tv_availableBoats.setText(String.valueOf(numb_boats));
                    }
                }


                break;
            case 1:
                if( board.get(position)==0 && numbomb!=0){
                    imageView.setImageResource(R.drawable.boom);
                    board.set(position,2);
                    numbomb--;
                }else{
                    if( board.get(position)==2) {
                        imageView.setImageResource(R.drawable.wave);
                        board.set(position, 0);
                        numbomb++;
                    }
                }

                break;
            default:
                imageView.setImageResource(R.drawable.wave);
                break;

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);





        TextView tv_availableBoats= findViewById(R.id.txtNumBoats);
        TextView tv_destroyedBoats= findViewById(R.id.txtNumDestroy);
        TextView tv_points= findViewById(R.id.txtNumPoints);

        String str_availableBoats = (String) tv_availableBoats.getText();
        String str_destroyedBoats= (String) tv_destroyedBoats.getText();
        String str_points= (String) tv_points.getText();
        final GridLayout boardGame= findViewById(R.id.boardGame);
        boardGame.setColumnCount(7);
        boardGame.setRowCount(8);

        for(int i=0; i<(boardGame.getColumnCount()*boardGame.getRowCount()); i++){

            LayoutInflater mInflater = (LayoutInflater) this
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View convertView = mInflater.inflate(R.layout.gridview_game, null);
            final ImageView imageView= convertView.findViewById(R.id.image);
            final int position=i;
            View.OnClickListener clickListener=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeImage(imageView,position);
                }
            };
            imageView.setOnClickListener(clickListener);
            convertView.setVisibility(View.VISIBLE);

            boardGame.addView(convertView,i);
            board.add(0);

        }





    }
}
