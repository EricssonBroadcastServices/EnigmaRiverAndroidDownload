package com.redbeemedia.enigma.download;

import java.util.List;

public interface IDownloadableInfo {
    List<AudioDownloadable> getAudioTracks();
    List<VideoDownloadable> getVideoTracks();
    List<SubtitleDownloadable> getSubtitleTracks();
}
