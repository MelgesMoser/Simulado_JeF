package com.germinare.jef_simulado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminLoginActivity extends AppCompatActivity {

    private static final String SENHA_CORRETA = "admin123";
    EditText editTextPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(v -> {
            String senha = editTextPassword.getText().toString();
            if (senha.equals(SENHA_CORRETA)) {
                startActivity(new Intent(this, AreaAdminActivity.class));
            } else {
                Toast.makeText(this, "Senha incorreta!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
