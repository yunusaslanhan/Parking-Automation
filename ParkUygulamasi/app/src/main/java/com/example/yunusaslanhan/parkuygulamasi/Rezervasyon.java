package com.example.yunusaslanhan.parkuygulamasi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.nio.file.Files;
import java.util.Date;

public class Rezervasyon extends AppCompatActivity {

    Button btnRezervasyonYap;
    EditText adı,soyad;
    EditText plaka;
    EditText sure;
    Spinner spinnerOtopark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rezervasyon);


        adı = findViewById(R.id.ad);
        soyad = findViewById(R.id.soyad);
        plaka = findViewById(R.id.plaka);
        sure = findViewById(R.id.süre);

        spinnerOtopark = findViewById(R.id.comboOtoparkListesi);

        String[] otoparkList = new String[MainActivity.otoparkDizisi.length];

        for(int i = 0; i < MainActivity.otoparkDizisi.length; i++) {
            otoparkList[i] = MainActivity.otoparkDizisi[i].getIsim();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Rezervasyon.this, android.R.layout.simple_spinner_dropdown_item,otoparkList);

        spinnerOtopark.setAdapter(adapter);

        btnRezervasyonYap = (Button) findViewById(R.id.btnRezervasyonYap);


        btnRezervasyonYap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isim = adı.getText().toString().trim();
                String soyisim = soyad.getText().toString().trim();
                String plakaText = plaka.getText().toString().trim();
                String sureText = sure.getText().toString().trim();


                if(isNullOrEmpty(isim,soyisim,plakaText, sureText))
                {
                    Toast.makeText(Rezervasyon.this,"Tüm Alanların Doldurulması Zorunludur!", Toast.LENGTH_SHORT).show();
                } else {
                    Kisi kisi = new Kisi();
                    kisi.setIsim(isim);
                    kisi.setSoyisim(soyisim);
                    kisi.setPlaka(plakaText);
                    kisi.setKalinacakSure(sureText);
                    kisi.setRezervasyonSaati(new Date());

                    Otopark secilen = MainActivity.otoparkDizisi[spinnerOtopark.getSelectedItemPosition()];
        
                    if(secilen .getBosYerSayisi() > 0) {
                        secilen.setBosYerSayisi(secilen.getBosYerSayisi() - 1); secilen.getRezerveEdenler().add(kisi);

                        MainActivity.firebase.child("otoparkListesi").setValue(MainActivity.otoparkDizisi);

                        startActivity(new Intent(Rezervasyon.this,MapsActivity.class));
                    } else {
                        Toast.makeText(Rezervasyon.this, "Seçilen Otopark Tamamen Dolu!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }


    public boolean isNullOrEmpty(String... var) { //var - args
        for(String item : var) {
            if(item == null || item.length() == 0)
                return true;
        }
        return
                 false;
    }
}
