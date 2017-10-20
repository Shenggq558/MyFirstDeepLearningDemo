package com.qwer.myfirstdeeplearningdemo.Bean;

import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by a on 2017/9/27.
 */
public class DialogBean {

    private Bitmap icon;
    private String objectName;
    private String dialogContent;

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getDialogContent() {
        return dialogContent;
    }

    public void setDialogContent(String dialogContent) {
        this.dialogContent = dialogContent;
    }
}
