package com.germinare.jef_simulado;

import android.os.Bundle;
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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        database.listar(listaCracha, adapter, this);
    }
}
