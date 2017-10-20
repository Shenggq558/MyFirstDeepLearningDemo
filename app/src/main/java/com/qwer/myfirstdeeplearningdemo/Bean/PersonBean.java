package com.qwer.myfirstdeeplearningdemo.Bean;

import android.graphics.Bitmap;

/**
 * Created by a on 2017/10/6.
 */
public class PersonBean {

    private Bitmap icon;
    private String personName;
    private String newMessage;

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }
}
