package com.redbeemedia.enigma.download;

import org.json.JSONException;
import org.json.JSONObject;

public final class AudioDownloadable implements IDownloadablePart {
    private final String name;
    private final int bitrate;
    private final String language;
    private final long fileSize;
    private final JSONObject rawJson;

    public AudioDownloadable(String name, int bitrate, String language, long fileSize) {
        this(name, bitrate, language, fileSize, new JSONObject());
    }

    private AudioDownloadable(
            String name,
            int bitrate,
            String language,
            long fileSize,
            JSONObject rawJson) {
        this.name = name;
        this.bitrate = bitrate;
        this.language = language;
        this.fileSize = fileSize;
        this.rawJson = rawJson;
    }

    public static AudioDownloadable parse(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) {
            return null;
        }
        return new AudioDownloadable(
                jsonObject.getString("name"),
                jsonObject.getInt("bitrate"),
                jsonObject.getString("language"),
                jsonObject.getLong("fileSize"),
                jsonObject);
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

    @Override
    public JSONObject getRawJson() {
        return rawJson;
    }
}
