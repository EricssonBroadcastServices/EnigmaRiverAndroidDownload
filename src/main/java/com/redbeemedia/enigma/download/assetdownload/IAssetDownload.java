package com.redbeemedia.enigma.download.assetdownload;

import android.os.Handler;

import com.redbeemedia.enigma.core.player.controls.IControlResultHandler;
import com.redbeemedia.enigma.download.listener.IAssetDownloadListener;

/**
 * Represents a download in progress.
 */
public interface IAssetDownload {
    /**
     *
     * @return The asset id of the asset being downloaded
     */
    String getAssetId();

    /**
     * Returns an estimation of the fraction of the size that has been downloaded.
     * If the download has failed or been cancelled, no guarantees are made for this value.
     * @return A fraction between 0.0 and 1.0
     */
    float getProgress();

    /**
     * @return the current state of the asset download (never null)
     */
    AssetDownloadState getState();

    /**
     * Attempt to pause this {@code IAssetDownload}.
     * @param controlResultHandler
     */
    void pauseDownload(IControlResultHandler controlResultHandler);

    /**
     * Attempt to pause this {@code IAssetDownload}.
     */
    void pauseDownload();

    /**
     * Attempt to resume this {@code IAssetDownload}.
     * @param controlResultHandler
     */
    void resumeDownload(IControlResultHandler controlResultHandler);

    /**
     * Attempt to resume this {@code IAssetDownload}.
     */
    void resumeDownload();

    /**
     * Attempt to cancel this {@code IAssetDownload}. Cancelling a download will remove any data
     * related to the download.
     * @param controlResultHandler
     */
    void cancelDownload(IControlResultHandler controlResultHandler);

    /**
     * Attempt to cancel this {@code IAssetDownload}. Cancelling a download will remove any data
     * related to the download.
     */
    void cancelDownload();

    /**
     * Adds a listener that will only receive events for this {@code IAssetDownload}.
     * @param listener
     * @return
     */
    boolean addListener(IAssetDownloadListener listener);

    /**
     * Adds a listener that will only receive events for this {@code IAssetDownload}.
     * @param listener
     * @return
     */
    boolean addListener(IAssetDownloadListener listener, Handler handler);

    /**
     * Removes a listener that is only receiving events for this {@code IAssetDownload}.
     * @param listener
     * @return
     */
    boolean removeListener(IAssetDownloadListener listener);
}
