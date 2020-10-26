package com.redbeemedia.enigma.download;

import java.util.List;

public interface IDownloadableInfo {
    int UNAVAILABLE_INT = -1;
    List<AudioDownloadable> getAudioTracks();
    List<VideoDownloadable> getVideoTracks();
    List<SubtitleDownloadable> getSubtitleTracks();

    /**
     * Returns the number of times the asset has been downloaded for the user owning
     * the {@link com.redbeemedia.enigma.core.session.ISession ISession} used to retrieve the
     * IDownloadableInfo object. If this value is unavailable
     * {@link IDownloadableInfo#UNAVAILABLE_INT} is returned instead.
     *
     * @return The number of times the asset has been downloaded for the user or
     * {@link IDownloadableInfo#UNAVAILABLE_INT} if unavailable.
     */
    int getDownloadCount();
    /**
     * Returns the maximum number of times the asset may be downloaded for the user owning
     * the {@link com.redbeemedia.enigma.core.session.ISession ISession} used to retrieve the
     * IDownloadableInfo object. If this value is unavailable
     * {@link IDownloadableInfo#UNAVAILABLE_INT} is returned instead.
     *
     * @return The number of times the asset can been downloaded for the user or
     * {@link IDownloadableInfo#UNAVAILABLE_INT} if unavailable.
     */
    int getMaxDownloadCount();

    /**
     * Convenience method that checks if {@code downloadCount >= maxDownloadCount}. If either or
     * both of {@code downloadCount} and {@code maxDownloadCount} are
     * {@link IDownloadableInfo#UNAVAILABLE_INT} the method returns {@code false}.
     * @return {@code downloadCount >= maxDownloadCount} or {@code false} if any value is
     * unavailable.
     */
    boolean isMaxDownloadCountReached();
}
