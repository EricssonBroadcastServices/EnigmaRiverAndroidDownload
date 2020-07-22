package com.redbeemedia.enigma.download;

import org.json.JSONException;
import org.json.JSONObject;

public class VideoDownloadable implements IDownloadablePart {
    private final String name;
    private final int bitrate;
    private final long fileSize;

    public VideoDownloadable(String name, int bitrate, long fileSize) {
        this.name = name;
        this.bitrate = bitrate;
        this.fileSize = fileSize;
    }

    public static VideoDownloadable parse(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) {
            return null;
        }
        return new VideoDownloadable(
                jsonObject.getString("name"),
                jsonObject.getInt("bitrate"),
                jsonObject.getLong("fileSize"));
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
}
