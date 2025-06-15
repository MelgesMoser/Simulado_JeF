package com.germinare.jef_simulado;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.germinare.jef_simulado.AdapterCracha;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import android.content.Context;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.List;

public class Database {

    public Database() {}



    public void salvar(Cracha argCracha, Context c) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if(argCracha.getNumeroCracha() == 0){
            db.collection("CountersID").document("nota_id").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    int numeroCracha = 1;
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        numeroCracha = document.getLong("numero_cracha").intValue();
                    }
                    argCracha.setNumeroCracha(numeroCracha);
                    db.collection("").document("nota_id").update("numero_cracha", numeroCracha);
                    db.collection("CountersID").document(String.valueOf(argCracha.getNumeroCracha())).set(argCracha);
                }
            });
        }
        else {
            db.collection("BancoDeNotas").document(String.valueOf(argCracha.getNumeroCracha())).set(argCracha);
            Toast.makeText(c, "", Toast.LENGTH_SHORT).show();

        }
    }

    public void remover(Cracha argCracha, Context c){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("BancoDeNotas").document(String.valueOf(argCracha.getNumeroCracha())).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(c, "Nota removida", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(c, "Erro ao remover nota", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void listar(List<Cracha> argCracha,  AdapterCracha argAdapter, Context c) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //Obter dados em Tempo Real
        db.collection("BancoDeNotas").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    System.out.println("Deu Erro" + error.getMessage());
                    Toast.makeText(c, "Você está offline!", Toast.LENGTH_SHORT).show();
                } else {
                    argCracha.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Cracha objCracha = doc.toObject(Cracha.class);
                        argCracha.add(objCracha);
                    }
                    argAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}
