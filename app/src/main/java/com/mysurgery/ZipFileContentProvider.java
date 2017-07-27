package com.mysurgery;

import com.android.vending.expansion.zipfile.APEZProvider;

public class ZipFileContentProvider extends APEZProvider {

    @Override
    public String getAuthority() {
        return "com.mysurgery.ZipFileContentProvider";
    }
}