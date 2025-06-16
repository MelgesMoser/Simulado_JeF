package com.germinare.jef_simulado;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
                .inflate(R.layout.item_atendimento_admin, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cracha cracha = listaCracha.get(position);


        holder.numeroCracha.setText("Crachá Nº: " + cracha.getNumeroCracha());

        // Formatador de datas
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());


        if (cracha.getInicioAtendimento() != null) {
            holder.inicioAtendimento.setText("Início: " + sdf.format(cracha.getInicioAtendimento()));
        } else {
            holder.inicioAtendimento.setText("Início: -");
        }


        if (cracha.getFimAtendimento() != null) {
            holder.fimAtendimento.setText("Fim: " + sdf.format(cracha.getFimAtendimento()));
        } else {
            holder.fimAtendimento.setText("Fim: -");
        }


        holder.botaoExcluir.setOnClickListener(v -> {
            int currentPosition = holder.getAdapterPosition();
            if (currentPosition != RecyclerView.NO_POSITION) {
                int numeroCracha = cracha.getNumeroCracha();
                db.removerAtendimento(numeroCracha, v.getContext());


                listaCracha.remove(currentPosition);
                notifyItemRemoved(currentPosition);
            }
        });


        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(v.getContext(),
                    "Crachá " + cracha.getNumeroCracha() + " selecionado",
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listaCracha.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView numeroCracha;
        private final TextView inicioAtendimento;
        private final TextView fimAtendimento;
        private final Button botaoExcluir;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numeroCracha = itemView.findViewById(R.id.numero_cracha);
            inicioAtendimento = itemView.findViewById(R.id.inicio_atendimento);
            fimAtendimento = itemView.findViewById(R.id.fim_atendimento);
            botaoExcluir = itemView.findViewById(R.id.buttonExcluir);
        }
    }
}