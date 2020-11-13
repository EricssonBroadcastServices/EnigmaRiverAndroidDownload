package com.redbeemedia.enigma.download.listener;

import com.redbeemedia.enigma.core.util.IInternalListener;
import com.redbeemedia.enigma.download.assetdownload.AssetDownloadState;
import com.redbeemedia.enigma.download.assetdownload.IAssetDownload;

public interface IAssetDownloadListener extends IInternalListener {
    /**
     * <p>Inspired by {@code org.hamcrest.Matcher} from JUnit lib.</p>
     * <br>
     * <p style="margin-left: 25px; font-weight:bold;">It's easy to ignore JavaDoc, but a bit harder to ignore compile errors .</p>
     * <p style="margin-left: 50px">-- Hamcrest source</p>
     */
    @Deprecated
    void _dont_implement_IAssetDownloadListener___instead_extend_BaseAssetDownloadListener_();

    /**
     * Triggered when the progress of the {@code IAssetDownload} changes.
     * @param oldProgress Fractional value between 0.0 and 1.0
     * @param newProgress Fractional value between 0.0 and 1.0
     */
    void onProgressChanged(float oldProgress, float newProgress);

    /**
     * Triggered when the state of the {@code IAssetDownload} changes.
     * @param oldState
     * @param newState
     */
    void onStateChanged(AssetDownloadState oldState, AssetDownloadState newState);
}
