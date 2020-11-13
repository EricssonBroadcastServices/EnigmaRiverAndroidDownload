package com.redbeemedia.enigma.download.listener;

import com.redbeemedia.enigma.download.assetdownload.AssetDownloadState;

public abstract class BaseAssetDownloadListener implements IAssetDownloadListener {
    @Deprecated
    @Override
    public final  void _dont_implement_IAssetDownloadListener___instead_extend_BaseAssetDownloadListener_() {
    }

    @Override
    public void onProgressChanged(float oldProgress, float newProgress) {
    }

    @Override
    public void onStateChanged(AssetDownloadState oldState, AssetDownloadState newState) {
    }
}
