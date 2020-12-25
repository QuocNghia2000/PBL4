package com.example.myapplication.model;

import java.io.Serializable;

public class Work implements Serializable {
    public int IDwork;
    public int UserID;
    public String Namework;
    public String Detail_work;
    public String Time;
    public int status;

    public Work(int _IDwork,int _UserID,String _Namework,String detail_work,String date,int status)
    {
        this.IDwork=_IDwork;
        this.UserID=_UserID;
        this.Namework=_Namework;
        this.Detail_work=detail_work;
        this.Time=date;
        this.status=status;
    }
    public Work(int _IDwork,int _UserID,String _Namework,String detail_work,int status)
    {
        this.IDwork=_IDwork;
        this.UserID=_UserID;
        this.Namework=_Namework;
        this.Detail_work=detail_work;
        this.status=status;
    }

    public Work(String _Namework,String detail_work,String date,int status)
    {
        this.Namework=_Namework;
        this.Detail_work=detail_work;
        this.Time=date;
        this.status=status;
    }
    public Work(int _UserID,String _Namework,String detail_work,String date,int status)
    {
        this.UserID=_UserID;
        this.Namework=_Namework;
        this.Detail_work=detail_work;
        this.Time=date;
        this.status=status;
    }

    public int getIDwork() {
        return IDwork;
    }

    public void setIDwork(int IDwork) {
        this.IDwork = IDwork;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public void setNamework(String namework) {
        Namework = namework;
    }

    public String getNamework() {
        return Namework;
    }

    public String getDetail_work() {
        return Detail_work;
    }

    public void setDetail_work(String detail_work) {
        Detail_work = detail_work;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String date) {
        Time = date;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int isStatus() {
        return status;
    }
}
