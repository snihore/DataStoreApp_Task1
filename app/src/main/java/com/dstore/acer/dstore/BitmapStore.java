package com.dstore.acer.dstore;

import android.graphics.Bitmap;

public class BitmapStore {

    private Bitmap imageBitmap;

    public BitmapStore() {
    }

    public BitmapStore(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
}
