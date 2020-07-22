package com.redbeemedia.enigma.download;

import org.json.JSONException;
import org.json.JSONObject;

public class SubtitleDownloadable implements IDownloadablePart {
    private final String name;

    public SubtitleDownloadable(String name) {
        this.name = name;
    }

    public static SubtitleDownloadable parse(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) {
            return null;
        }
        return new SubtitleDownloadable(
                jsonObject.getString("name"));
    }

    @Override
    public String getName() {
        return name;
    }
}
