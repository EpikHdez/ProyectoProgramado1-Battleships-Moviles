package com.boom.battleships.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.boom.battleships.R;

import java.net.URLEncoder;

public class ReportActivity extends AppCompatActivity {

    public void sendEmail(View view) {
        EditText editText=findViewById(R.id.txtReport);
        String message=editText.getText().toString();
        String uriText = "mailto:soporte.boom.battleships@gmail.com" +
                "?subject=" + URLEncoder.encode("Reporte") +
                "&body=" + URLEncoder.encode(message);
        Uri uri = Uri.parse(uriText);
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(uri);

        try {
            startActivity(Intent.createChooser(sendIntent, "Send mail..."));
            finish();
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Éxito");
            alertDialog.setMessage("Muchas gracias por sus Comentarios y sugerencias.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
        } catch (android.content.ActivityNotFoundException ex) {
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Algo ha salido mal :(");
            alertDialog.setMessage("No se pudo enviar su solicitud.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
    }
}
