package com.redbeemedia.enigma.download;

import org.json.JSONException;
import org.json.JSONObject;

public final class VideoDownloadable implements IDownloadablePart {
    private final String name;
    private final int bitrate;
    private final long fileSize;
    private final JSONObject rawJson;

    public VideoDownloadable(String name, int bitrate, long fileSize) {
        this(name, bitrate, fileSize, new JSONObject());
    }

    private VideoDownloadable(String name, int bitrate, long fileSize, JSONObject rawJson) {
        this.name = name;
        this.bitrate = bitrate;
        this.fileSize = fileSize;
        this.rawJson = rawJson;
    }

    public static VideoDownloadable parse(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) {
            return null;
        }
        return new VideoDownloadable(
                jsonObject.getString("name"),
                jsonObject.getInt("bitrate"),
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

    public long getFileSize() {
        return fileSize;
    }

    @Override
    public JSONObject getRawJson() {
        return rawJson;
    }
}
