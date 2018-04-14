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

    public void sendBoard(View view){
        flag=1;
        createBoard();

        APICalls.get("user/match/"+String.valueOf(user.getCurrentGame()),caller);


    }
    public void createBoard(){
        JSONObject jsonObject=new JSONObject();
        JSONObject boardJSON=new JSONObject();
        JSONObject boardJSONRival=new JSONObject();
        for(int i=0; i<board.size();i++){
            try {
                boardJSON.put(String.valueOf(i),board.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(iniciada==false){


            for(int i=0; i<board.size();i++){
                try {
                    boardJSONRival.put(String.valueOf(i),0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        JSONArray boards=new JSONArray();
        TextView tv_destroyedBoats= findViewById(R.id.txtNumDestroy);
        TextView tv_points= findViewById(R.id.txtNumPoints);


        String str_destroyedBoats= (String) tv_destroyedBoats.getText();
        String str_points= (String) tv_points.getText();

        try {
            boards.put( boardJSON);
            boards.put(boardJSONRival);
            JSONObject objBoards=new JSONObject();
            objBoards.put("boards",boards);
            jsonObject.put("board",objBoards);
            jsonObject.put("destroyed_ships",Integer.parseInt(str_destroyedBoats));
            jsonObject.put("score",Integer.parseInt(str_points));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        APICalls.put("user/match/"+String.valueOf(user.getCurrentGame()),jsonObject,caller);

    }

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
        user= User.getInstance();
        caller=this;
        Log.d("CurrentGame", String.valueOf(user.getCurrentGame()));
        APICalls.get("user/match/"+String.valueOf(user.getCurrentGame()),caller);










    }
    public void loadBoard(){

    }

    public void initBoard(){

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
    @Override
    public void receiveApiResponse(JSONObject response) {
        Log.d("BoardMatch",response.toString());
        switch (flag){
            case 0:
                try {

                    JSONObject game=response.getJSONObject("game");

                    if(game.isNull("board")){
                        Log.d("Partida","No se ha iniciado la partida");
                        initBoard();
                    }else{
                        iniciada=true;
                        loadBoard();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 1:

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
