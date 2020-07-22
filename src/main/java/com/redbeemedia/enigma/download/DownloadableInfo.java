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

    public DownloadableInfo(JSONObject jsonObject) throws JSONException {
        JSONArray audios = jsonObject.getJSONArray("audios");
        JSONArray videos = jsonObject.getJSONArray("videos");
        JSONArray subtitles = jsonObject.getJSONArray("subtitles");

        this.audioTracks = convert(audios, item -> AudioDownloadable.parse(item));
        this.videoTracks = convert(videos, item -> VideoDownloadable.parse(item));
        this.subtitleTracks = convert(subtitles, item -> SubtitleDownloadable.parse(item));
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
}