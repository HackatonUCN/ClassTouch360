package com.example.classtouch360.ui.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.classtouch360.R;
import com.example.classtouch360.db.AppDatabase;
import com.example.classtouch360.db.DAO.UserDao;
import com.example.classtouch360.db.entity.UserEntity;
import com.example.classtouch360.utils.HelperUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText signupName, signupEmail, signupUsername, signupPassword;
    private TextView loginRedirectText;
    private Button signupButton;
    private ProgressBar progressBar;
    private long lastClickTime = 0;

    // Instancia del DAO para el usuario en la base de datos de Room
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        // Configuración de insets para edge-to-edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicialización de vistas
        initViews();

        // Obtener la instancia de la base de datos de Room y el UserDao
        AppDatabase db = AppDatabase.getDatabase(this);
        userDao = db.userDao();

        // Configuración de listeners
        setupListeners();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        signupName = findViewById(R.id.signup_name);
        signupEmail = findViewById(R.id.signup_email);
        signupUsername = findViewById(R.id.signup_username);
        signupPassword = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        loginRedirectText = findViewById(R.id.loginRedirectText);
    }

    private void setupListeners() {
        signupButton.setOnClickListener(view -> {
            // Prevenir múltiples clicks rápidos
            if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
            lastClickTime = SystemClock.elapsedRealtime();

            progressBar.setVisibility(View.VISIBLE);
            signupButton.setEnabled(false);

            if (!validateInputs()) {
                progressBar.setVisibility(View.GONE);
                signupButton.setEnabled(true);
                return;
            }

            registerUser();
        });

        loginRedirectText.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private boolean validateInputs() {
        signupName.setError(null);
        signupEmail.setError(null);
        signupUsername.setError(null);
        signupPassword.setError(null);

        // Obtener los valores de los campos
        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String username = signupUsername.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();

        boolean isValid = true;

        if (name.isEmpty()) {
            signupName.setError("Campo requerido");
            isValid = false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmail.setError("Correo inválido");
            isValid = false;
        }

        if (username.isEmpty() || username.length() < 4) {
            signupUsername.setError("Mínimo 4 caracteres");
            isValid = false;
        }

        if (password.isEmpty() || password.length() < 6) {
            signupPassword.setError("Mínimo 6 caracteres");
            isValid = false;
        }

        return isValid;
    }


    private void registerUser() {
        String name = signupName.getText().toString().trim();
        String email = signupEmail.getText().toString().trim();
        String username = signupUsername.getText().toString().trim();
        String password = signupPassword.getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Obtener el UID del nuevo usuario autenticado
                        String uid = auth.getCurrentUser().getUid();

                        // Crear el objeto HelperUser para guardar en la base de datos en la nube
                        HelperUser helperUser = new HelperUser(name, email, username, password);

                        usersRef.child(uid).setValue(helperUser)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        // TODO: Guardar en la base de datos local de Room
                                        // 1. Crear el objeto UserEntity con los datos del usuario
                                        UserEntity userEntity = new UserEntity(uid, name, email, username);

                                        // 2. Usar el ExecutorService de Room para ejecutar la inserción en un hilo secundario
                                        AppDatabase.databaseWriteExecutor.execute(() -> {
                                            userDao.insertUser(userEntity);
                                        });

                                        progressBar.setVisibility(View.GONE);
                                        signupButton.setEnabled(true);

                                        Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                        clearForm();
                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        signupButton.setEnabled(true);
                                        Toast.makeText(RegisterActivity.this,
                                                "Error al guardar datos: " + dbTask.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });

                    } else {
                        progressBar.setVisibility(View.GONE);
                        signupButton.setEnabled(true);
                        Toast.makeText(RegisterActivity.this,
                                "Error: " + task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearForm() {
        signupName.setText("");
        signupEmail.setText("");
        signupUsername.setText("");
        signupPassword.setText("");
    }
}
