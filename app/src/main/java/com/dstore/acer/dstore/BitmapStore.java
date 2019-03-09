package com.dstore.acer.dstore;

import android.graphics.Bitmap;
import android.net.Uri;

public class BitmapStore {

    private String name;
    private Uri uri;

    public BitmapStore() {
    }

    public BitmapStore(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
