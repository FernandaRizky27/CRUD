package com.example.crud;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditActivity extends AppCompatActivity {
    private EditText etNama, etKelas, etAbsen;
    private Button btnSave;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        etNama = findViewById(R.id.etNama);
        etKelas = findViewById(R.id.etKelas);
        etAbsen = findViewById(R.id.etAbsen);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view -> {
            if (!etNama.getText().toString().isEmpty() && !etKelas.getText().toString().isEmpty() && !etAbsen.getText().toString().isEmpty()) {
            saveData(etNama.getText().toString(), etKelas.getText().toString(), etAbsen.getText().toString());
            }
        });
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            etNama.setText(intent.getStringExtra("nama"));
            etKelas.setText(intent.getStringExtra("kelas"));
            etAbsen.setText(intent.getStringExtra("absen"));
        }
    }

    private void saveData(String etNama, String etKelas, String etAbsen) {
        Map<String, Object> user = new HashMap<>();
        user.put("nama", etNama);
        user.put("kelas", etKelas);
        user.put("absen", etAbsen);
        if (id != null && id.length() > 0) {
            db.collection("users").document(id)
                    .set(user)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(EditActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                            finish();
                            }else {
                            Toast.makeText(EditActivity.this, "Gagal Menyimpan", Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
        } else {
            db.collection("users")
                    .add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(EditActivity.this, "Berhasil Menyimpan", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditActivity.this, "Gagal Menyimpan", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}