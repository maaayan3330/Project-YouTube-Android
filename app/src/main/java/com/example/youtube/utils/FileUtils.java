package com.example.youtube.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import java.io.InputStream;

public class FileUtils {

    public static InputStream getInputStreamFromUri(Context context, Uri uri) throws Exception {
        ContentResolver contentResolver = context.getContentResolver();
        return contentResolver.openInputStream(uri);
    }
}
