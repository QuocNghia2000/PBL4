package com.example.myapplication.activity;

import android.graphics.Bitmap;

public class Image {
    private String imgBytes;
    private String idUserSend;
    private String idUserReceive;

    public Image() {
    }
    public Image(String imgBytes, String idUserSend, String idUserReceive) {
        this.imgBytes = imgBytes;
        this.idUserSend = idUserSend;
        this.idUserReceive = idUserReceive;
    }

    public String getImgBytes() {
        return imgBytes;
    }

    public void setImgBytes(String imgBytes) {
        this.imgBytes = imgBytes;
    }

    public String getIdUserSend() {
        return idUserSend;
    }

    public void setIdUserSend(String idUserSend) {
        this.idUserSend = idUserSend;
    }

    public String getIdUserReceive() {
        return idUserReceive;
    }

    public void setIdUserReceive(String idUserReceive) {
        this.idUserReceive = idUserReceive;
    }
}
