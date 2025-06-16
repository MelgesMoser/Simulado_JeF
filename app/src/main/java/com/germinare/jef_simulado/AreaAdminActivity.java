package com.germinare.jef_simulado;

import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AreaAdminActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterCracha adapter;
    List<Cracha> listaCracha;
    Database database = new Database();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_admin);

        recyclerView = findViewById(R.id.recyclerViewAdmin);
        listaCracha = new ArrayList<>();
        adapter = new AdapterCracha(listaCracha);
        Button btnExcluirAntigos = findViewById(R.id.btnExcluirAntigos);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        database.lerCracha(listaCracha, adapter, this);

        btnExcluirAntigos.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AreaAdminActivity.this);
            builder.setTitle("Digite a senha de administrador:");

            final EditText input = new EditText(AreaAdminActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                String senha = input.getText().toString();
                if (senha.equals("admin123")) {
                    Database db = new Database();
                    db.removerRegistrosAntigos(AreaAdminActivity.this);
                } else {
                    Toast.makeText(AreaAdminActivity.this, "Senha incorreta!", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

            builder.show();
        });

    }
}