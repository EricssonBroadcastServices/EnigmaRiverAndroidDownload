package com.redbeemedia.enigma.download;

import org.json.JSONException;
import org.json.JSONObject;

public final class SubtitleDownloadable implements IDownloadablePart {
    private final String name;
    private final JSONObject rawJson;

    public SubtitleDownloadable(String name) {
        this(name, new JSONObject());
    }

    private SubtitleDownloadable(String name, JSONObject rawJson) {
        this.name = name;
        this.rawJson = rawJson;
    }

    public static SubtitleDownloadable parse(JSONObject jsonObject) throws JSONException {
        if(jsonObject == null) {
            return null;
        }
        return new SubtitleDownloadable(
                jsonObject.getString("name"),
                jsonObject);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public JSONObject getRawJson() {
        return rawJson;
    }
}
