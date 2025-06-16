package com.germinare.jef_simulado;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nota, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cracha cracha = listaCracha.get(position);

        // Preencher o número do crachá
        holder.numeroCracha.setText("Crachá Nº: " + cracha.getNumeroCracha());

        // Formatador de datas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        // Início do atendimento
        if (cracha.getInicioAtendimento() != null) {
            holder.inicioAtendimento.setText("Início: " + sdf.format(cracha.getInicioAtendimento()));
        } else {
            holder.inicioAtendimento.setText("Início: -");
        }

        // Fim do atendimento
        if (cracha.getFimAtendimento() != null) {
            holder.fimAtendimento.setText("Fim: " + sdf.format(cracha.getFimAtendimento()));
        } else {
            holder.fimAtendimento.setText("Fim: -");
        }

        // Long Click → Remover crachá
        holder.itemView.setOnLongClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                db.removerCracha(listaCracha.get(currentPosition), v.getContext());
            }
            return true;
        });

        // (Opcional) Clique curto → Editar ou ação futura
        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),
                    "Crachá " + cracha.getNumeroCracha() + " selecionado",
                    Toast.LENGTH_SHORT).show();
            // Aqui você pode abrir uma tela de detalhes, edição, etc.
        });

        // Alternância de cor no fundo
        if (position % 2 == 0) {
            holder.fundo.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.blue)
            );
        } else {
            holder.fundo.setBackgroundColor(
                    ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent)
            );
        }
    }

    @Override
    public int getItemCount() {
        return listaCracha.size();
    }

    // ViewHolder interno
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView numeroCracha;
        private final TextView inicioAtendimento;
        private final TextView fimAtendimento;
        private final ConstraintLayout fundo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroCracha = itemView.findViewById(R.id.numero_cracha);
            inicioAtendimento = itemView.findViewById(R.id.inicio_atendimento);
            fimAtendimento = itemView.findViewById(R.id.fim_atendimento);
            fundo = itemView.findViewById(R.id.fundo);
        }
    }
}
