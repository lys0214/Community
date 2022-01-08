package com.lys.community.entity;

import java.util.Date;

public class LoginTicket {
    private int id;
    private int userid;
    private String ticket;
    private int status;
    private Date expired;


    @Override
    public String toString() {
        return "LgTicket{" +
                "id=" + id +
                ", userid=" + userid +
                ", ticket='" + ticket + '\'' +
                ", status=" + status +
                ", expired=" + expired +
                '}';
    }

    public LoginTicket() {

    }

    public LoginTicket(int id, int userid, String ticket, int status, Date expired) {
        this.id = id;
        this.userid = userid;
        this.ticket = ticket;
        this.status = status;
        this.expired = expired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getExpired() {
        return expired;
    }

    public void setExpired(Date expired) {
        this.expired = expired;
    }
}
