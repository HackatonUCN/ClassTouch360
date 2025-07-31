package com.example.classtouch360.ui.main;

import android.os.Bundle;
import android.content.Intent; // Necesario para iniciar nuevas actividades
import android.view.View;
import android.widget.TextView;
import android.widget.Toast; // Opcional: para mostrar mensajes cortos


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.classtouch360.R;
import com.example.classtouch360.ui.auth.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);

        mAuth = FirebaseAuth.getInstance();

        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        updateNavHeader();
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // --- Implementación del listener con if-else if ---
        navView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId(); // Obtenemos el ID del ítem seleccionado

            if (id == R.id.nav_inicio) {
                Toast.makeText(this, "Ya estás en Inicio", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_lista_clases_estudiantes) {
                // Aquí iniciarías una nueva actividad para la lista de estudiantes
                // startActivity(new Intent(HomeActivity.this, ListaClasesEstudiantesActivity.class));
                Toast.makeText(this, "Lista de Clases y Estudiantes", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_asistencias) {
                // startActivity(new Intent(HomeActivity.this, AsistenciasActivity.class));
                Toast.makeText(this, "Tomar Asistencia", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_calificaciones) {
                // startActivity(new Intent(HomeActivity.this, CalificacionesActivity.class));
                Toast.makeText(this, "Calificaciones", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_calculadora) {
                // startActivity(new Intent(HomeActivity.this, CalculadoraActivity.class));
                Toast.makeText(this, "Calculadora", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_block_notas) {
                // startActivity(new Intent(HomeActivity.this, BlockDeNotasActivity.class));
                Toast.makeText(this, "Block de Notas", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_recordatorios) {
                // startActivity(new Intent(HomeActivity.this, RecordatoriosActivity.class));
                Toast.makeText(this, "Recordatorios", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_ruleta_estudiantes) {
                // startActivity(new Intent(HomeActivity.this, RuletaEstudiantesActivity.class));
                Toast.makeText(this, "Ruleta de Estudiantes", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_temporizador) {
                // startActivity(new Intent(HomeActivity.this, TemporizadorActivity.class));
                Toast.makeText(this, "Temporizador", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_recursos) {
                // startActivity(new Intent(HomeActivity.this, RecursosActivity.class));
                Toast.makeText(this, "Recursos Adicionales", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_ajustes) {
                // startActivity(new Intent(HomeActivity.this, AjustesActivity.class));
                Toast.makeText(this, "Ajustes de la App", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_ayuda) {
                // startActivity(new Intent(HomeActivity.this, AyudaActivity.class));
                Toast.makeText(this, "Centro de Ayuda", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_acerca_de) {
                // startActivity(new Intent(HomeActivity.this, AcercaDeActivity.class));
                Toast.makeText(this, "Acerca de ClassTouch360", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_cerrar_sesion) {
                Toast.makeText(this, "Cerrando Sesión...", Toast.LENGTH_SHORT).show();
                // --- Lógica para cerrar sesión con Firebase ---
                mAuth.signOut(); // Cierra la sesión del usuario actual en Firebase
                // Navega a la pantalla de Login y limpia la pila de actividades
                Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class); // <-- Reemplaza 'LoginActivity.class' con el nombre real de tu actividad de inicio de sesión
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish(); // Cierra HomeActivity para que el usuario no pueda volver con el botón de atrás
                // --- Fin Lógica para cerrar sesión ---
            }

            // Cierra el menú lateral después de que se ha seleccionado un ítem
            drawerLayout.closeDrawer(GravityCompat.START);
            return true; // Indica que el evento fue manejado
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void updateNavHeader() {
        // Obtener la instancia del encabezado de navegación
        View headerView = navView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.textViewUserName);
        TextView userEmailTextView = headerView.findViewById(R.id.textViewUserEmail);

        // Obtener el usuario actual de Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // El usuario ha iniciado sesión.
            String userName = user.getDisplayName();
            String userEmail = user.getEmail();

            // Si el nombre no está disponible, mostrar un valor predeterminado
            if (userName != null && !userName.isEmpty()) {
                userNameTextView.setText(userName);
            } else {
                userNameTextView.setText("Usuario Desconocido");
            }

            // Si el correo no está disponible, mostrar un valor predeterminado
            if (userEmail != null && !userEmail.isEmpty()) {
                userEmailTextView.setText(userEmail);
            } else {
                userEmailTextView.setText("Correo no disponible");
            }
        } else {
            // El usuario no ha iniciado sesión.
            userNameTextView.setText("Invitado");
            userEmailTextView.setText("");
        }
    }
}