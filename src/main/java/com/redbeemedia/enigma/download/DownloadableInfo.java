package com.redbeemedia.enigma.download;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*package-protected*/ class DownloadableInfo implements IDownloadableInfo {
    private final List<AudioDownloadable> audioTracks;
    private final List<VideoDownloadable> videoTracks;
    private final List<SubtitleDownloadable> subtitleTracks;
    private final int downloadCount;
    private final int maxDownloadCount;

    public DownloadableInfo(JSONObject jsonObject) throws JSONException {
        JSONArray audios = jsonObject.getJSONArray("audios");
        JSONArray videos = jsonObject.getJSONArray("videos");
        JSONArray subtitles = jsonObject.getJSONArray("subtitles");
        this.audioTracks = convert(audios, item -> AudioDownloadable.parse(item));
        this.videoTracks = convert(videos, item -> VideoDownloadable.parse(item));
        this.subtitleTracks = convert(subtitles, item -> SubtitleDownloadable.parse(item));

        this.downloadCount = jsonObject.optInt("downloadCount", UNAVAILABLE_INT);
        this.maxDownloadCount = jsonObject.optInt("maxDownloadCount", UNAVAILABLE_INT);
    }

    @Override
    public List<AudioDownloadable> getAudioTracks() {
        return audioTracks;
    }

    @Override
    public List<VideoDownloadable> getVideoTracks() {
        return videoTracks;
    }

    @Override
    public List<SubtitleDownloadable> getSubtitleTracks() {
        return subtitleTracks;
    }

    private static <T> List<T> convert(JSONArray jsonArray, IJsonConverter<T> converter) throws JSONException {
        if(jsonArray == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        for(int i = 0; i < jsonArray.length(); ++i) {
            list.add(converter.convert(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    @Override
    public int getDownloadCount() {
        return downloadCount;
    }

    @Override
    public int getMaxDownloadCount() {
        return maxDownloadCount;
    }

    @Override
    public boolean isMaxDownloadCountReached() {
        if(downloadCount == UNAVAILABLE_INT || maxDownloadCount == UNAVAILABLE_INT) {
            return false;
        } else {
            return downloadCount >= maxDownloadCount;
        }
    }
}