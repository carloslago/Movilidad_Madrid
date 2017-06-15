package com.example.android.movilidad_madrid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FavoritosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        String favs = getIntent().getStringExtra("FAVORITOS");

        TextView f = (TextView) findViewById(R.id.d1);
        favs = favs.replace("\\n","").replaceAll("\\\\","");
        f.setText(favs);
        Button retorno = (Button) findViewById(R.id.retorno);
        retorno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritosActivity.this, MenuActivity.class);
                intent.putExtra("ID_USUARIO", getIntent().getStringExtra("ID_USUARIO"));
                intent.putExtra("DESTINOS", getIntent().getStringExtra("DESTINOS"));
                intent.putExtra("FAVORITOS", getIntent().getStringExtra("FAVORITOS"));
                startActivity(intent);
            }
        });
    }

}
