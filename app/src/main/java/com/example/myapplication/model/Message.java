package com.example.myapplication.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.sql.Date;

public class Message implements Serializable {
    public int MessageID;
    public  int RoomID;
    public int UserID;
    public int ToUserID;
    public String Text;
    public Bitmap image;
    public String Time;
    public int IsImage;

    public Message(int _MessageID,int _RoomID,int _UserID, int _ToUserID,String _Text,String _Time,int image)
    {
        this.MessageID=_MessageID;
        this.RoomID=_RoomID;
        this.UserID=_UserID;
        this.ToUserID=_ToUserID;
        this.Text=_Text;
        //this.image=image;
        this.Time=_Time;
        this.IsImage=image;
    }

    public Message(int UserID,int ToUserID,String image,String time)
    {
        this.UserID=UserID;
        this.ToUserID=ToUserID;
        this.Text=image;
        this.Time=time;
    }
    public Message(int _UserID, int _ToUserID,String _Text)
    {
        this.UserID=_UserID;
        this.ToUserID=_ToUserID;
        this.Text=_Text;
    }

    public Message(int _UserID, int _ToUserID,Bitmap img)
    {
        this.UserID=_UserID;
        this.ToUserID=_ToUserID;
        this.image = img;
    }

    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getMessageID() {
        return MessageID;
    }

    public void setMessageID(int messageID) {
        MessageID = messageID;
    }

    public int getRoomID() {
        return RoomID;
    }

    public void setRoomID(int roomID) {
        RoomID = roomID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public int getToUserID() {
        return ToUserID;
    }

    public void setToUserID(int toUserID) {
        ToUserID = toUserID;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int IsImage() { return IsImage;}

    public void setIsImage(int IsImage) { this.IsImage = IsImage;}
}
