package com.model;


public class User {
    public String name;
    public String email;
    public String avata;
    public Status status;
    public String typing;
    public Message message;
    public String Uid;


    public User() {
        status = new Status();
        message = new Message();
        status.isOnline = false;
        status.timestamp = 0;
        message.idReceiver = "0";
        message.idSender = "0";
        message.text = "";
        typing = "false" ;
        message.timestamp = 0;
        Uid = "";
    }
}
