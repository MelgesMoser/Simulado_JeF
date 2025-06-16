package com.germinare.jef_simulado;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.germinare.jef_simulado.AdapterCracha;
import com.germinare.jef_simulado.Cracha;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Database {

    public Database() {}

    public void adicionarCracha(Cracha cracha, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (cracha.getNumeroCracha() == 0) {
            db.collection("CountersID").document("nota_id").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            int numeroCracha = 1;
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                numeroCracha = document.getLong("numero_cracha").intValue();
                            }
                            cracha.setNumeroCracha(numeroCracha);
                            db.collection("CountersID").document("nota_id")
                                    .update("numero_cracha", numeroCracha + 1);

                            db.collection("BancoDeNotas")
                                    .document(String.valueOf(cracha.getNumeroCracha()))
                                    .set(cracha)
                                    .addOnSuccessListener(aVoid ->
                                            Toast.makeText(context, "Crachá adicionado!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(context, "Erro ao adicionar crachá", Toast.LENGTH_SHORT).show());
                        }
                    });
        } else {
            db.collection("BancoDeNotas")
                    .document(String.valueOf(cracha.getNumeroCracha()))
                    .set(cracha)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(context, "Crachá adicionado!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Erro ao adicionar crachá", Toast.LENGTH_SHORT).show());
        }
    }

    public void atualizarCracha(Cracha cracha, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("BancoDeNotas")
                .document(String.valueOf(cracha.getNumeroCracha()))
                .set(cracha)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Crachá atualizado!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Erro ao atualizar crachá", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void removerAtendimento(int numeroCracha, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("atendimentos")
                .whereEqualTo("numeroCracha", numeroCracha)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            db.collection("atendimentos").document(document.getId()).delete();
                        }
                        Toast.makeText(context, "Atendimento excluído com sucesso", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Nenhum atendimento encontrado com esse número de crachá", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Erro ao excluir: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void lerCracha(List<Cracha> listaCracha, AdapterCracha adapter, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("atendimentos").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(context, "Você está offline!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (value == null || value.isEmpty()) {
                    listaCracha.clear();
                    adapter.notifyDataSetChanged();
                    return;
                }

                listaCracha.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    try {
                        Cracha cracha = new Cracha();

                        cracha.setNumeroCracha(Integer.parseInt(doc.getString("crachaPaciente")));

                        Timestamp tsDataInicio = doc.getTimestamp("dataInicio");
                        Timestamp tsDataFim = doc.getTimestamp("dataFim");

                        if (tsDataInicio != null) {
                            cracha.setInicioAtendimento(tsDataInicio.toDate());
                        }
                        if (tsDataFim != null) {
                            cracha.setFimAtendimento(tsDataFim.toDate());
                        }

                        listaCracha.add(cracha);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }


    public void removerRegistrosAntigos(Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Calendar agora = Calendar.getInstance();
        agora.add(Calendar.MONTH, -1); // Mês passado
        int mesAnterior = agora.get(Calendar.MONTH);
        int anoAnterior = agora.get(Calendar.YEAR);

        db.collection("BancoDeNotas").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot doc : task.getResult()) {
                    Cracha cracha = doc.toObject(Cracha.class);
                    Date dataRef = cracha.getFimAtendimento() != null ? cracha.getFimAtendimento() : cracha.getInicioAtendimento();
                    if (dataRef != null) {
                        Calendar data = Calendar.getInstance();
                        data.setTime(dataRef);
                        int mes = data.get(Calendar.MONTH);
                        int ano = data.get(Calendar.YEAR);

                        if (mes == mesAnterior && ano == anoAnterior) {
                            db.collection("BancoDeNotas").document(doc.getId()).delete();
                        }
                    }
                }
                Toast.makeText(context, "Registros do mês anterior removidos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Erro ao excluir antigos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}