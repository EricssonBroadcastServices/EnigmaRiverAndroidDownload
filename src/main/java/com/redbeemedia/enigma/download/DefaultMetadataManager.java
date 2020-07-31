package com.redbeemedia.enigma.download;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

/*package-protected*/ class DefaultMetadataManager implements IMetadataManager {
    private final SharedPreferences sharedPreferences;

    public DefaultMetadataManager(Application application) {
        this.sharedPreferences = application.getSharedPreferences("ENIGMA_RIVER_DOWNLOAD_METADATA", Context.MODE_PRIVATE);
    }

    @Override
    public void store(String contentId, byte[] data) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(contentId, Base64.encodeToString(data, Base64.DEFAULT));
        if(!editor.commit()) {
            throw new RuntimeException("Failed to store metadata for contentId "+contentId);
        }
    }

    @Override
    public void clear(String contentId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(contentId);
        if(!editor.commit()) {
            throw new RuntimeException("Failed to clear metadata for contentId "+contentId);
        }
    }

    @Override
    public byte[] load(String contentId) {
        String metadataBase64Encoded = sharedPreferences.getString(contentId, null);
        if(metadataBase64Encoded == null) {
            return null;
        } else {
            return Base64.decode(metadataBase64Encoded, Base64.DEFAULT);
        }
    }
}
