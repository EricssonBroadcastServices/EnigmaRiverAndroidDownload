package com.redbeemedia.enigma.download;

import com.redbeemedia.enigma.core.format.IMediaFormatSelector;
import com.redbeemedia.enigma.core.session.ISession;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class DownloadStartRequest {
    private final String assetId;
    private final ISession session;
    private IMediaFormatSelector mediaFormatSelector = null;
    private VideoDownloadable videoTrack = null;
    private final List<AudioDownloadable> audioTracks = new ArrayList<>();
    private final List<SubtitleDownloadable> subtitleTracks = new ArrayList<>();

    public DownloadStartRequest(String assetId, ISession session) {
        this.assetId = assetId;
        this.session = session;
    }

    public String getAssetId() {
        return assetId;
    }

    public ISession getSession() {
        return session;
    }

    public DownloadStartRequest setVideo(VideoDownloadable video) {
        this.videoTrack = video;
        return this;
    }

    public DownloadStartRequest addAudio(AudioDownloadable audio) {
        this.audioTracks.add(audio);
        return this;
    }

    public DownloadStartRequest addSubtitle(SubtitleDownloadable subtitle) {
        this.subtitleTracks.add(subtitle);
        return this;
    }

    public DownloadStartRequest setMediaFormatSelector(IMediaFormatSelector mediaFormatSelector) {
        this.mediaFormatSelector = mediaFormatSelector;
        return this;
    }

    public IMediaFormatSelector getMediaFormatSelector() {
        return mediaFormatSelector;
    }

    public String getContentId() {
        return UUID.randomUUID().toString();
    }

    public VideoDownloadable getVideo() {
        return videoTrack;
    }
}
