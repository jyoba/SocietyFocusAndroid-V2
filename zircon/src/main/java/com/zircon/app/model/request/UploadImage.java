package com.zircon.app.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jyotishman on 28/06/17.
 */

public class UploadImage {


    @SerializedName("base64")
    String base64;

    @SerializedName("filename")
    String filename;

    public UploadImage(String base64, String filename) {
        this.base64 = base64;
        this.filename = filename;
    }
}
