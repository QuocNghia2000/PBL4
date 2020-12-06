package com.example.myapplication.model;

import java.sql.Date;

public class Message {
    public int MessageID;
    public  int RoomID;
    public int UserID;
    public int ToUserID;
    public String Text;
    public Date Time;

    public Message(int _MessageID,int _RoomID,int _UserID, int _ToUserID,String _Text,Date _Time)
    {
        this.MessageID=_MessageID;
        this.RoomID=_RoomID;
        this.UserID=_UserID;
        this.ToUserID=_ToUserID;
        this.Text=_Text;
        this.Time=_Time;
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

    public Date getTime() {
        return Time;
    }

    public void setTime(Date time) {
        Time = time;
    }
}
