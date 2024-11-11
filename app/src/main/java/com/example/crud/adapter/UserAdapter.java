package com.example.crud.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crud.R;
import com.example.crud.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.myViewHolder> {

    private Context context;
    private List<User> list;
    private Dialog dialog;

    public interface Dialog{
        void onClick(int pos);
    }
    public void setDialog(Dialog dialog){
        this.dialog=dialog;
    }

    public UserAdapter(Context context,List<User> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row,parent,false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.nama.setText(list.get(position).getNama());
        holder.kelas.setText(list.get(position).getKelas());
        holder.absen.setText(list.get(position).getAbsen());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView nama, kelas, absen;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            kelas = itemView.findViewById(R.id.kelas);
            absen = itemView.findViewById(R.id.absen);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(dialog!=null){
                        dialog.onClick(getLayoutPosition());
                    }
                }
            });

        }
    }
}
