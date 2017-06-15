package com.example.android.movilidad_madrid;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.icu.util.ULocale;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.android.movilidad_madrid.R.id.parent;

public class DestinosRecientesActivity extends AppCompatActivity {

    private String fav = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destinos_recientes);
        String destinos = getIntent().getStringExtra("DESTINOS");
        if (destinos.contains(",")){
            String[] d = destinos.split(",");
            TextView t = (TextView) findViewById(R.id.d1);
            t.setText(d[0]);
            TextView tw = (TextView) findViewById(R.id.d2);
            t.setText(d[1]);
        } else {
            final TextView t = (TextView) findViewById(R.id.d1);
            t.setText(destinos);

            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fav = (String) t.getText();
                    new PutDataTask().execute("http://192.168.56.1:1000/api/status/" + getIntent().getStringExtra("ID_USUARIO"));
                }
            });
        }

        Button retorno = (Button) findViewById(R.id.retorno);
        retorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DestinosRecientesActivity.this, MenuActivity.class);
                intent.putExtra("ID_USUARIO", getIntent().getStringExtra("ID_USUARIO"));
                intent.putExtra("DESTINOS", getIntent().getStringExtra("DESTINOS"));
                if (!fav.equals("")){
                    intent.putExtra("FAVORITOS", fav);
                } else {
                    intent.putExtra("FAVORITOS", getIntent().getStringExtra("FAVORITOS"));
                }
                startActivity(intent);
            }
        });

    }

    // Actualizar favoritos
    class PutDataTask extends AsyncTask<String, Void, String> {


        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(DestinosRecientesActivity.this);
            progressDialog.setMessage("Procesando...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                return putData(params[0]);
            } catch (IOException e){
                return "Network error!";
            } catch (JSONException e){
                return "Datos incorrectos";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        private String putData(String urlPath) throws IOException, JSONException{

            StringBuilder result = new StringBuilder();
            BufferedWriter bufferedWriter = null;
            BufferedReader bufferedReader = null;

            try {
                JSONObject dataToSend = new JSONObject();
                TextView favorito = (TextView) findViewById(R.id.d1);
                JSONArray list = new JSONArray();

                list.put(favorito.getText());

                dataToSend.put("Favoritos", list);
//                dataToSend.put("password", mPasswordView.getText().toString());
//                dataToSend.put("Destinos_recientes", "Pepe");

                Log.d("json->", dataToSend.toString());
                // Iniciar request
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json"); //header
                urlConnection.connect();

                // Escribir datos
                OutputStream outputStream = urlConnection.getOutputStream();
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(dataToSend.toString());
                bufferedWriter.flush();

                // Comprobar
                if (urlConnection.getResponseCode() == 200){
                    return "Funciona";
                } else {
                    return "Error al actualizar";
                }
            } finally {
                if (bufferedReader != null){
                    bufferedReader.close();
                }
                if (bufferedWriter != null){
                    bufferedWriter.close();
                }
            }

        }
    }

}
