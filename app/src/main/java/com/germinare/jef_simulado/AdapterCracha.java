package com.germinare.jef_simulado;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AdapterCracha extends RecyclerView.Adapter<AdapterCracha.MyViewHolder> {

    private List<Cracha> listaCracha;
    private Database db = new Database();

    public AdapterCracha(List<Cracha> listaCracha) {
        this.listaCracha = listaCracha;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_atendimento_admin, parent, false);
        return new MyViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cracha cracha = listaCracha.get(position);

        // Corrigir: transformar int em String
        holder.numeroCracha.setText(String.valueOf(cracha.getNumeroCracha()));

        // Opcional: formatar e exibir datas (se desejar)
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        if (cracha.getInicioAtendimento() != null) {
            holder.inicioAtendimento.setText(sdf.format(cracha.getInicioAtendimento()));
        } else {
            holder.inicioAtendimento.setText("Início: -");
        }

        if (cracha.getFimAtendimento() != null) {
            holder.fimAtendimento.setText(sdf.format(cracha.getFimAtendimento()));
        } else {
            holder.fimAtendimento.setText("Fim: -");
        }

        // Long click para deletar
        holder.itemView.setOnLongClickListener(v -> {
            db.remover(listaCracha.get(holder.getAdapterPosition()), v.getContext());
            return true;
        });

//        // Alternar cor de fundo
//        if (position % 2 == 0) {
//            holder.fundo.setBackgroundColor(
//                    ContextCompat.getColor(holder.itemView.getContext(), R.color.blue)
//            );
//        } else {
//            holder.fundo.setBackgroundColor(
//                    ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent)
//            );
//        }
    }

    @Override
    public int getItemCount() {
        return listaCracha.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView numeroCracha;
        private TextView inicioAtendimento;
        private TextView fimAtendimento;
        private ConstraintLayout fundo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroCracha = itemView.findViewById(R.id.numero_cracha);
            inicioAtendimento = itemView.findViewById(R.id.inicio_atendimento);
            fimAtendimento = itemView.findViewById(R.id.fim_atendimento);
//            fundo = itemView.findViewById(R.id.fundo);
        }
    }
}
