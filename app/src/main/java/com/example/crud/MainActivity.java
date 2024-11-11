package com.example.crud;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crud.adapter.UserAdapter;
import com.example.crud.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton btnAdd;
    private RecyclerView recyclerView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<User> list = new ArrayList<>();
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);
        userAdapter = new UserAdapter(getApplicationContext(), list);

        userAdapter.setDialog(new UserAdapter.Dialog() {
            @Override
            public void onClick(int pos) {
                final CharSequence[] dialogItem = {"Edit", "Delete"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Pilih Aksi");
                dialog.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0: // Edit
                                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                                intent.putExtra("id", list.get(pos).getId()); // Pass user ID to EditActivity
                                intent.putExtra("nama", list.get(pos).getNama());
                                intent.putExtra("kelas", list.get(pos).getKelas());
                                intent.putExtra("absen", list.get(pos).getAbsen());
                                startActivity(intent);
                                break;
                            case 1: // Delete
                                deleteUser(list.get(pos).getId());
                                break;
                        }
                    }
                });
                dialog.show();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(userAdapter);

        btnAdd.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    private void getData() {

        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Toast.makeText(MainActivity.this, "Gagal Mendengarkan Perubahan Data", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        list.clear(); // Clear the list before adding new data
                        if (snapshots != null && !snapshots.isEmpty()) {
                            for (QueryDocumentSnapshot document : snapshots) {
                                User user = new User(document.getId(), document.getString("nama"), document.getString("kelas"),
                                        document.getString("absen"));
                                list.add(user);
                            }
                            userAdapter.notifyDataSetChanged(); // Notify adapter of data changes
                        } else {
                            Toast.makeText(MainActivity.this, "Data kosong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Function to delete a user
    private void deleteUser(String id) {
        db.collection("users").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
