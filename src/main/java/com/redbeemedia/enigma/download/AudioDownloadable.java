package com.redbeemedia.enigma.download;

import org.json.JSONException;
import org.json.JSONObject;

public class AudioDownloadable implements IDownloadablePart {
    private final String name;
    private final int bitrate;
    private final String language;
    private final long fileSize;

    public AudioDownloadable(String name, int bitrate, String language, long fileSize) {
        this.name = name;
        this.bitrate = bitrate;
        this.language = language;
        this.fileSize = fileSize;
    }

    public static AudioDownloadable parse(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) {
            return null;
        }
        return new AudioDownloadable(
                jsonObject.getString("name"),
                jsonObject.getInt("bitrate"),
                jsonObject.getString("language"),
                jsonObject.getLong("fileSize"));
    }

    @Override
    public String getName() {
        return name;
    }

    public int getBitrate() {
        return bitrate;
    }

    public String getLanguage() {
        return language;
    }

    public long getFileSize() {
        return fileSize;
    }
}
