package com.germinare.jef_simulado;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CadastroExameActivity extends AppCompatActivity {

    private EditText edtCracha;
    private TextView txtColaborador, txtInicio, txtTermino, txtUsuario;
    private Button btnRegistrarTermino;

    private String nomeColaborador = "Fulano da Silva"; // Simulado
    private String dataInicio;
    private String dataTermino;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second);

        edtCracha = findViewById(R.id.edtCracha);
        txtColaborador = findViewById(R.id.txtColaborador);
        txtInicio = findViewById(R.id.txtInicio);
        txtTermino = findViewById(R.id.txtTermino);
        txtUsuario = findViewById(R.id.txtUsuario);
        btnRegistrarTermino = findViewById(R.id.btnRegistrarTermino);

        database = FirebaseDatabase.getInstance().getReference("exames");

        edtCracha.setOnEditorActionListener((v, actionId, event) -> {
            if (!edtCracha.getText().toString().isEmpty()) {
                txtColaborador.setText("Colaborador: " + nomeColaborador);
                dataInicio = getDataHoraAtual();
                txtInicio.setText("Início Atendimento: " + dataInicio);
            }
            return false;
        });

        btnRegistrarTermino.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Término de Atendimento")
                    .setMessage("Deseja indicar o término do atendimento?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        dataTermino = getDataHoraAtual();
                        txtTermino.setText("Término Atendimento: " + dataTermino);

                        String cracha = edtCracha.getText().toString();
                        Exame exame = new Exame(cracha, nomeColaborador, dataInicio, dataTermino);

                        database.push().setValue(exame);

                        atualizarRecyclerView();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        });
    }

    private String getDataHoraAtual() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    private void atualizarRecyclerView() {

    }
}
