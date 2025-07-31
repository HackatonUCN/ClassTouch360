package com.example.classtouch360.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.developer.gbuttons.GoogleSignInButton;
import com.example.classtouch360.MainActivity;
import com.example.classtouch360.R;
import com.example.classtouch360.db.AppDatabase;
import com.example.classtouch360.db.DAO.UserDao;
import com.example.classtouch360.db.entity.UserEntity;
import com.example.classtouch360.ui.main.HomeActivity;
import com.example.classtouch360.utils.HelperUser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    // Views
    private EditText loginUsername, loginPassword;
    private Button loginButton;
    private TextView signupRedirectText;
    private GoogleSignInButton googleBtn;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth auth;
    private FirebaseDatabase database;

    // Google Sign-In
    private GoogleSignInClient googleSignInClient;
    private ActivityResultLauncher<Intent> googleSignInLauncher;

    // Room Database
    private UserDao userDao;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        initializeViews();
        setupFirebase();
        setupGoogleSignIn();
        setupClickListeners();

        // Inicializar la base de datos de Room y el DAO
        AppDatabase db = AppDatabase.getDatabase(this);
        userDao = db.userDao();

        logAllLocalUsers();


    }

    private void initializeViews() {
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        loginButton = findViewById(R.id.login_button);
        googleBtn = findViewById(R.id.googleBtn);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupFirebase() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        handleGoogleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(result.getData()));
                    } else {
                        showToast("Error en el inicio de sesión con Google");
                    }
                });
    }

    private void setupClickListeners() {
        googleBtn.setOnClickListener(v -> signInWithGoogle());

        loginButton.setOnClickListener(v -> {
            if (validateInputs()) {
                authenticateUser();
            }
        });

        signupRedirectText.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }


    private boolean validateInputs() {

        boolean isValid = true;

        if (loginUsername.getText().toString().trim().isEmpty()) {
            loginUsername.setError("El nombre de usuario no puede estar vacío");
            isValid = false;
        }

        if (loginPassword.getText().toString().trim().isEmpty()) {
            loginPassword.setError("La contraseña no puede estar vacía");
            isValid = false;
        } else if (loginPassword.getText().toString().length() < 6) {
            loginPassword.setError("La contraseña debe tener al menos 6 caracteres");
            isValid = false;
        }

        return isValid;
    }

    private void authenticateUser() {
        showLoading(true);

        String email = loginUsername.getText().toString().trim();  // ahora usas email, no username
        String password = loginPassword.getText().toString().trim();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String uid = auth.getCurrentUser().getUid();

                        database.getReference("users")
                                .child(uid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        showLoading(false);
                                        if (snapshot.exists()) {
                                            HelperUser user = snapshot.getValue(HelperUser.class);
                                            redirectToHomeActivity(user);
                                        } else {
                                            showError("Usuario no encontrado en base de datos");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        showLoading(false);
                                        showError("Error: " + error.getMessage());
                                    }
                                });

                    } else {
                        showLoading(false);
                        showError("Credenciales incorrectas: " + task.getException().getMessage());
                    }
                });
    }

    private void signInWithGoogle() {
        googleSignInLauncher.launch(googleSignInClient.getSignInIntent());
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                firebaseAuthWithGoogle(account.getIdToken());
            }
        } catch (ApiException e) {
            showToast("Error en Google Sign-In: " + e.getStatusCode());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        showLoading(true);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            saveUserToDatabase(user);
                        }
                    } else {
                        showError("Error en autenticación con Firebase");
                    }
                    showLoading(false);
                });
    }

    private void saveUserToDatabase(FirebaseUser firebaseUser) {
        String uid = firebaseUser.getUid();
        DatabaseReference userRef = database.getReference("users").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Usuario ya registrado, lo redirigimos directamente
                    HelperUser existingUser = snapshot.getValue(HelperUser.class);
                    // También lo guardamos en la base de datos local de Room si no existe
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        UserEntity localUser = userDao.getUserByEmail(existingUser.getEmail());
                        if (localUser == null) {
                            UserEntity userEntity = new UserEntity(uid, existingUser.getName(), existingUser.getEmail(), existingUser.getUsername());
                            userDao.insertUser(userEntity);
                            Log.d(TAG, "Usuario existente de Firebase guardado localmente en Room.");
                        }
                    });
                    redirectToHomeActivity(existingUser);
                } else {
                    // Usuario nuevo con Google, lo guardamos en Firebase y en Room
                    String username = firebaseUser.getUid();
                    HelperUser newUser = new HelperUser(
                            firebaseUser.getDisplayName() != null ? firebaseUser.getDisplayName() : "Usuario Google",
                            firebaseUser.getEmail() != null ? firebaseUser.getEmail() : "",
                            username // Usamos el UID como identificador único
                    );

                    userRef.setValue(newUser).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Guardamos el usuario en la base de datos local de Room
                            UserEntity userEntity = new UserEntity(uid, newUser.getName(), newUser.getEmail(), newUser.getUsername());
                            AppDatabase.databaseWriteExecutor.execute(() -> {
                                userDao.insertUser(userEntity);
                                Log.d(TAG, "Nuevo usuario de Google guardado localmente en Room.");
                            });
                            redirectToHomeActivity(newUser);
                        } else {
                            showError("Error al guardar usuario de Google");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showError("Error en la base de datos: " + error.getMessage());
            }
        });
    }


    private void redirectToHomeActivity(HelperUser user) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
        finish();
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        loginButton.setEnabled(!isLoading);
        googleBtn.setEnabled(!isLoading);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Método para obtener y mostrar todos los usuarios en la base de datos local
    private void logAllLocalUsers() {
        // Se obtiene una nueva instancia del DAO para evitar errores de contexto
        AppDatabase db = AppDatabase.getDatabase(this);
        UserDao localUserDao = db.userDao();

        // El método getAllUsers() devuelve LiveData, así que lo observamos
        LiveData<List<UserEntity>> allUsersLiveData = localUserDao.getAllUsers();

        allUsersLiveData.observe(this, new Observer<List<UserEntity>>() {
            @Override
            public void onChanged(List<UserEntity> allUsers) {
                if (allUsers != null) {
                    if (allUsers.isEmpty()) {
                        Log.d("ROOM_DB", "La base de datos local de Room no contiene usuarios.");
                    } else {
                        Log.d("ROOM_DB", "Usuarios encontrados en la base de datos local:");
                        for (UserEntity user : allUsers) {
                            Log.d("ROOM_DB", "ID: " + user.getUserId() + ", Nombre: " + user.getName() + ", Email: " + user.getEmail()+ ", Username: " + user.getUsername());
                        }
                    }
                    // Después de obtener y mostrar los datos, se elimina el observador
                    allUsersLiveData.removeObserver(this);
                }
            }
        });
    }

}