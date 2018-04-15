package com.boom.battleships.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AlertDialog;
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

import com.android.volley.VolleyError;
import com.boom.battleships.R;
import com.boom.battleships.asynctasks.APICalls;
import com.boom.battleships.interfaces.ApiCaller;
import com.boom.battleships.interfaces.AsyncTaskRequester;
import com.boom.battleships.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity implements AsyncTaskRequester, ApiCaller {
    private ArrayList<Integer> board=new ArrayList<Integer>();
    private int state=0;
    private int numbomb=1;
    private User user;
    private ApiCaller caller;
    private int flag;
    private boolean iniciada=false;
    private JSONObject rivalBoard;
    private int destroy_ships;

    public void sendBoard(View view){
        if(!iniciada) {
            flag = 1;
            createBoard();
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("Se han posicionado sus botes.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        }else{
            sendBomb();


        }
        findViewById(R.id.btnSendBoard).setEnabled(false);



    }
    public void openHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    public void sendBomb(){
        int index= board.indexOf(2);
        try {
            String sindex=String.valueOf(index);
            Log.d("sindex",sindex);
            int boat= rivalBoard.getInt(sindex);
            final GridLayout boardGame= findViewById(R.id.boardGame);

            View view= (View) boardGame.getChildAt(index);
            ImageView imageView=view.findViewById(R.id.image);
            TextView points=findViewById(R.id.txtNumPoints);

            String pointsS= (String) points.getText();

            int pointsI= Integer.parseInt(pointsS);



            if(boat==1){

               imageView.setImageResource(R.drawable.win);
               rivalBoard.put(sindex,4);
               pointsI+=100;
               destroy_ships+=1;
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(":)");
                alertDialog.setMessage("Le ha dado a un barco.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                openHomeActivity(); // Call once you redirect to another activity
                            }
                        });
                alertDialog.show();

            }else{
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(":(");
                alertDialog.setMessage("No le ha dado a ningún barco.");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                openHomeActivity();
                            }
                        });
                alertDialog.show();
                imageView.setImageResource(R.drawable.fail);
                rivalBoard.put(sindex,5);

            }
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("board",rivalBoard.toString());
            jsonObject.put("score",pointsI);
            jsonObject.put("destroyed_ships",destroy_ships);
            APICalls.put("user/match/"+String.valueOf(user.getCurrentGame()),jsonObject,caller);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
    public void createBoard(){
        JSONObject jsonObject=new JSONObject();
        JSONObject boardJSON=new JSONObject();
        for(int i=0; i<board.size();i++){
            try {
                boardJSON.put(String.valueOf(i),board.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }




        try {


            jsonObject.put("board",boardJSON.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        APICalls.put("user/match_board/"+String.valueOf(user.getCurrentGame()),jsonObject,caller);

    }

    public void onClickBoats(View view){
        state=0;
        Button button=findViewById(R.id.btnBoat);
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        Button button1=findViewById(R.id.btnBomb);
        button1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Button button2=findViewById(R.id.btnSendBoard);
        button2.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public void onClickBombs(View view){
        state=1;
        Button button=findViewById(R.id.btnBomb);
        button.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        Button button1=findViewById(R.id.btnBoat);
        button1.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        Button button2=findViewById(R.id.btnSendBoard);
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
                Log.d("PositionC", String.valueOf(board.get(position)));
                if( board.get(position)!=2 && numbomb!=0){
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
        user= User.getInstance();
        caller=this;
        Log.d("CurrentGame", String.valueOf(user.getCurrentGame()));
        APICalls.get("user/match/"+String.valueOf(user.getCurrentGame()),caller);










    }
    public void loadBoard(JSONObject boardrival){
        Log.d("loadBoard",boardrival.toString());
        rivalBoard=boardrival;

    }

    public void initBoard(){
        board=new ArrayList<Integer>();

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


            if(rivalBoard!=null){
                try {
                    int valuePosition=rivalBoard.getInt(String.valueOf(i));
                    if(valuePosition==4){
                        imageView.setImageResource(R.drawable.win);
                    }else{
                        if(valuePosition==5) {
                            imageView.setImageResource(R.drawable.fail);
                        }
                    }
                    board.add(valuePosition);

                    boardGame.addView(convertView,i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }else {
                board.add(0);
                boardGame.addView(convertView,i);
            }

        }

    }
    @Override
    public void receiveApiResponse(JSONObject response) {
        Log.d("ResponseBoard",response.toString());
        switch (flag){
            case 0:
                if(!response.optBoolean("board_set")){
                    state=0;
                    Log.d("Partida","No se ha iniciado la partida");
                    initBoard();
                    findViewById(R.id.btnBomb).setVisibility(View.INVISIBLE);
                    findViewById(R.id.btnBoat).setVisibility(View.INVISIBLE);
                }else{
                    state=1;
                    iniciada=true;
                    TextView tv_destroyedBoats= findViewById(R.id.txtNumDestroy);
                    TextView tv_points= findViewById(R.id.txtNumPoints);
                    TextView boats= findViewById(R.id.txtNumBoats);
                    String boatsS= (String) boats.getText();

                    String points=response.optString("score");
                    String deads=response.optString("lost_ships");
                    Log.d("boats",boatsS);
                    int boatsI= Integer.parseInt(boatsS)-Integer.parseInt(deads);
                    boats.setText(String.valueOf(boatsI));
                    tv_destroyedBoats.setText(deads);
                    tv_points.setText(points);
                    try {
                        destroy_ships=response.getInt("destroyed_ships");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("PasoPorAca",response.toString());
                    APICalls.get("user/match/"+String.valueOf(user.getCurrentGame()),caller);
                    flag=1;
                    findViewById(R.id.btnBoat).setVisibility(View.INVISIBLE);
                    findViewById(R.id.btnBomb).setVisibility(View.INVISIBLE);

                }

                break;
            case 1:


                try {
                    JSONObject obj = new JSONObject(response.optString("board"));
                    loadBoard(obj);
                    initBoard();
                    Log.d("flag","1");
                    flag=2;
                } catch (JSONException e) {
                    e.printStackTrace();
                }



                break;
            case 2:
                break;



        }



    }

    @Override
    public void receiveApiError(VolleyError error) {

    }

    @Override
    public void receiveAsyncResponse(Object response) {

    }
}
