package com.example.yunusaslanhan.parkuygulamasi;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {


    Button btnSignin, btnSignup;
    EditText userMail, userPassword;
    ProgressBar progressBar;
    FirebaseAuth auth;

    public static Otopark[] otoparkDizisi;
    public static Firebase firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        userMail = (EditText) findViewById(R.id.userEmail);
        userPassword = (EditText) findViewById(R.id.userPassword);

        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnSignup = (Button) findViewById(R.id.btnSignup);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        btnSignin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://otoparksistemi-b39b4.firebaseio.com/");

        firebase.endAt("otoparkListesi");

        firebase.child("otoparkListesi").addValueEventListener(new ValueEventListener() {

        @Override
         public void onDataChange(DataSnapshot ds) {
                otoparkDizisi = ds.getValue(Otopark[].class);
                if (!MapsActivity.markerList.isEmpty()) {
                    for (int i = 0; i < otoparkDizisi.length; i++) {
                        Otopark otopark = otoparkDizisi[i];

                        NumberFormat format = new DecimalFormat("#.00");
                        LatLng otoparkKonumu = new LatLng(Double.parseDouble(otopark.getLatitude()), Double.parseDouble(otopark.getLongitude()));
                        double enlem = 40.904817,
                                boylam = 31.180801;
                        String mesafe = format.format(MapsActivity.MesafeHesaplama(new LatLng(enlem, boylam), otoparkKonumu));

                        MapsActivity.markerList.get(i).setSnippet("Mesafe " + mesafe + " Km - Doluluk " + (otopark.getToplamYerSayisi() - otopark.getBosYerSayisi()) + " / " + otopark.getToplamYerSayisi());

                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError arg0) {
                System.out.println("onCancelled");
            }
        });

    }


    public void signUp() {
        String inputEmail = userMail.getText().toString().trim();
        String inputPassword = userPassword.getText().toString().trim();

        if (TextUtils.isEmpty(inputEmail)) {
            Toast.makeText(getApplicationContext(), "Mail adresinizi giriniz!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(inputPassword)) {
            Toast.makeText(getApplicationContext(), "Şifrenizi giriniz!", Toast.LENGTH_SHORT).show();
        } else if (inputPassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Şifreniz çok kısa!", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            //Create user
            auth.createUserWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Kayıt başarılı!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, MapsActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Kayıt Başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }


    public void signIn() {
        String inputEmail = userMail.getText().toString().trim();
        String inputPassword = userPassword.getText().toString().trim();

        if (TextUtils.isEmpty(inputEmail)) {
            Toast.makeText(getApplicationContext(), "Mail adresinizi giriniz!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(inputPassword)) {
            Toast.makeText(getApplicationContext(), "Şifrenizi giriniz!", Toast.LENGTH_LONG).show();
        } else if (inputPassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Şifreniz çok kısa!", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);

            //Sign in user
            auth.signInWithEmailAndPassword(inputEmail, inputPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        startActivity(new Intent(MainActivity.this, MapsActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Kimlik doğrulama başarısız!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


}