package com.example.erronkalogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.sql.Connection;
import java.util.ArrayList;

public class ErosketakActivity extends AppCompatActivity {

    TextView txtErabiltzailea;
    TableLayout erosketenTaula;
    Button btnProduktuak;


    Connection conn;
    ArrayList<Erosketa> jasotakoErosketak = new ArrayList<>();

    String erabiltzaileIzena = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erosketak);

        txtErabiltzailea = findViewById(R.id.textViewErabiltzailea);
        erosketenTaula = findViewById(R.id.erosketenTaula);

       erabiltzaileIzena = getIntent().getStringExtra("Erabiltzailea");
        txtErabiltzailea.setText("Erabiltzailea: " + erabiltzaileIzena);

        KonektatuHaria konektatuHaria = new KonektatuHaria();
        konektatuHaria.start();

        try {
            konektatuHaria.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        conn = konektatuHaria.getConnection();
        if(conn == null){
            //Toast.makeText(this,"Ez dago konexiorik",Toast.LENGTH_SHORT).show();
        }else{
            //Toast.makeText(this,"Konexioa dago",Toast.LENGTH_SHORT).show();
        }

        ErosketakIkusiHaria erosketakIkusiHaria = new ErosketakIkusiHaria(conn,erabiltzaileIzena);
        erosketakIkusiHaria.start();
        try {
            erosketakIkusiHaria.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
       jasotakoErosketak = erosketakIkusiHaria.getErosketak();
        taulaBete();

        btnProduktuak = findViewById(R.id.btnProduktuak);

        btnProduktuak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Produktuak();
            }
        });


    }

    private void Produktuak() {
        Intent i = new Intent(getBaseContext(),Menua.class);
        i.putExtra("Erabiltzailea", erabiltzaileIzena);
        startActivity(i);
    }


    private void taulaBete(){
        TableRow.LayoutParams params = new TableRow.LayoutParams(50, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        for(int i = 0;i < jasotakoErosketak.size();i++){
            TableRow tr1 = new TableRow(this);
            for (int j = 0;j < 6;j++){
                TextView tv1 = new TextView(this);
                switch(j){
                    case 0:
                        tv1.setText(String.valueOf(jasotakoErosketak.get(i).getOrderId()));
                        tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        break;
                    case 1:
                        tv1.setText(String.valueOf(jasotakoErosketak.get(i).getBezeroa().getIzena()));
                        break;
                    case 2:
                        tv1.setText(jasotakoErosketak.get(i).getProduktua().getIzena());
                        break;
                    case 3:
                        tv1.setText(String.valueOf(jasotakoErosketak.get(i).getKantitatea()));
                        tv1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        break;
                    case 4:
                        tv1.setText(String.valueOf(jasotakoErosketak.get(i).getPrezioa()));
                        break;
                    case 5:
                        tv1.setText(String.valueOf(jasotakoErosketak.get(i).getData()));
                        break;
                }
                tv1.setLayoutParams(params);
                tv1.setTextColor(Color.BLACK);
                tr1.addView(tv1);
            }
            erosketenTaula.addView(tr1);
        }
    }
}