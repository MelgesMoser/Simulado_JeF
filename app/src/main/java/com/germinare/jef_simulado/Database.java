package com.germinare.jef_simulado;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class Database {

    public Database() {}

    // Método para adicionar crachá
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

    // Método para atualizar crachá
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

    // Método para remover crachá
    public void removerCracha(Cracha cracha, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("BancoDeNotas")
                .document(String.valueOf(cracha.getNumeroCracha()))
                .delete()
                .addOnSuccessListener(unused ->
                        Toast.makeText(context, "Crachá removido", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Erro ao remover crachá", Toast.LENGTH_SHORT).show());
    }

    // Método para ler crachás em tempo real
    public void lerCracha(List<Cracha> listaCracha, AdapterCracha adapter, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("BancoDeNotas").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(context, "Você está offline!", Toast.LENGTH_SHORT).show();
                    return;
                }

                listaCracha.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    Cracha cracha = doc.toObject(Cracha.class);
                    listaCracha.add(cracha);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
