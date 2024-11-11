package com.example.crud.model;

public class User {
    private String id, nama, kelas, absen;
    public User(){
    }

    public String getAbsen() {
        return absen;
    }

    public void setAbsen(String absen) {
        this.absen = absen;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String id, String nama, String kelas, String absen) {
        this.id = id;
        this.nama = nama;
        this.kelas = kelas;
        this.absen = absen;
    }

}
