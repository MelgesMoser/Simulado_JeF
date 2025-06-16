package com.germinare.jef_simulado;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.germinare.jef_simulado.databinding.FragmentSecondBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private FirebaseFirestore firestore;
    private boolean atendimentoIniciado = false;
    private String matriculaRegistrador;
    private String nomeRegistrador;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        firestore = FirebaseFirestore.getInstance();

        if (getArguments() != null) {
            matriculaRegistrador = getArguments().getString("matricula");
            nomeRegistrador = getArguments().getString("nome");

            if (!TextUtils.isEmpty(nomeRegistrador)) {
                binding.txtNomeRegistrador.setText("Bem-vindo(a), " + nomeRegistrador + "\nMatricula: " + matriculaRegistrador);
            }
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnIniciar.setOnClickListener(v -> iniciarAtendimento());
        binding.btnFinalizar.setOnClickListener(v -> finalizarAtendimento());

        binding.fabLock.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AdminLoginActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarAtendimento() {
        if (atendimentoIniciado) {
            showToast("Atendimento já está em andamento!");
            return;
        }

        String crachaPaciente = binding.editCracha.getText().toString().trim();

        if (!validarCracha(crachaPaciente)) {
            binding.editCracha.setError("Número do crachá deve conter 8 dígitos");
            return;
        }

        Map<String, Object> atendimento = new HashMap<>();
        atendimento.put("crachaPaciente", crachaPaciente);
        atendimento.put("crachaRegistrador", matriculaRegistrador);
        atendimento.put("nomeRegistrador", nomeRegistrador);
        atendimento.put("dataInicio", new Timestamp(new Date()));  // Salvar como Timestamp
        atendimento.put("dataFim", null);

        firestore.collection("atendimentos")
                .add(atendimento)
                .addOnSuccessListener(documentReference -> {
                    atendimentoIniciado = true;
                    showToast("Atendimento iniciado com sucesso");
                    binding.editCracha.setEnabled(false);

                    salvarCrachaNoFirebase(crachaPaciente);
                })
                .addOnFailureListener(e ->
                        showToast("Erro ao iniciar atendimento: " + e.getMessage())
                );
    }

    private void finalizarAtendimento() {
        if (!atendimentoIniciado) {
            showToast("Nenhum atendimento em andamento para finalizar");
            return;
        }

        String crachaPaciente = binding.editCracha.getText().toString().trim();

        firestore.collection("atendimentos")
                .whereEqualTo("crachaPaciente", crachaPaciente)
                .whereEqualTo("crachaRegistrador", matriculaRegistrador)
                .whereEqualTo("dataFim", null)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        showToast("Nenhum atendimento aberto encontrado para finalizar");
                        return;
                    } else {
                        Timestamp dataFim = new Timestamp(new Date());  // Salvar como Timestamp

                        binding.btnFinalizar.setOnClickListener(v -> {
                            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                            builder.setTitle("Deseja finalizar o atendimento?");

                            builder.setPositiveButton("Confirmar", (dialog, which) -> {
                                for (var document : querySnapshot.getDocuments()) {
                                    firestore.collection("atendimentos")
                                            .document(document.getId())
                                            .update("dataFim", dataFim)
                                            .addOnSuccessListener(aVoid -> {
                                                atendimentoIniciado = false;
                                                showToast("Atendimento finalizado com sucesso");
                                                binding.editCracha.setEnabled(true);
                                                binding.editCracha.setText("");
                                            })
                                            .addOnFailureListener(e ->
                                                    showToast("Erro ao finalizar atendimento: " + e.getMessage())
                                            );
                                }
                            });
                            builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

                            builder.show();
                        });
                    }
                })
                .addOnFailureListener(e ->
                        showToast("Erro ao buscar atendimento: " + e.getMessage())
                );
    }

    private boolean validarCracha(String cracha) {
        return !TextUtils.isEmpty(cracha) && cracha.matches("\\d{8}");
    }

    private void showToast(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    private void salvarCrachaNoFirebase(String crachaPaciente) {
        try {
            int numeroCracha = Integer.parseInt(crachaPaciente);
            Cracha novoCracha = new Cracha();
            novoCracha.setNumeroCracha(numeroCracha);

            Database db = new Database();
            db.adicionarCracha(novoCracha, requireContext());
        } catch (NumberFormatException e) {
            showToast("Número de crachá inválido para salvar no BancoDeNotas");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
