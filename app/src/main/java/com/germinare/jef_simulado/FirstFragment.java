package com.germinare.jef_simulado;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.germinare.jef_simulado.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.button.setOnClickListener(v -> enviarDadosParaSecondFragment());
    }

    private void enviarDadosParaSecondFragment() {
        String nome = binding.nome.getText().toString().trim();
        String matricula = binding.matricula.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            binding.nome.setError("Digite seu nome");
            return;
        }

        if (TextUtils.isEmpty(matricula) || !matricula.matches("\\d{8}")) {
            binding.matricula.setError("Digite uma matrícula válida com 8 dígitos");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("nome", nome);
        bundle.putString("matricula", matricula);

        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
