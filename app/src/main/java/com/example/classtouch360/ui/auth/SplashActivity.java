package com.example.classtouch360.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.classtouch360.MainActivity;
import com.example.classtouch360.R;
import com.example.classtouch360.ui.main.HomeActivity;
import com.example.classtouch360.utils.HelperUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen); // Asegúrate de tener este layout

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        new Handler(Looper.getMainLooper()).postDelayed(this::checkLoginStatus, 1500); // espera 1.5s
    }

    private void checkLoginStatus() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            //Usuario logueado, obtener info de la base de datos
            String uid = currentUser.getUid();

            database.getReference("users").child(uid)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                HelperUser user = snapshot.getValue(HelperUser.class);
                                goToHomeActivity(user);
                            } else {
                                // Si el UID no tiene datos (raro), enviar a login
                                goToLogin();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            goToLogin(); // En caso de error, mejor llevar al login
                        }
                    });

        } else {
            //No está autenticado
            goToLogin();
        }
    }

    private void goToHomeActivity(HelperUser user) {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    private void goToLogin() {
        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        finish();
    }
}
