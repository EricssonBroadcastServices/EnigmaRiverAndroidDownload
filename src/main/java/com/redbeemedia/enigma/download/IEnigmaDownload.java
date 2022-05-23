package com.redbeemedia.enigma.download;

import android.content.Context;
import android.os.Handler;

import com.redbeemedia.enigma.core.session.ISession;
import com.redbeemedia.enigma.download.assetdownload.IAssetDownload;
import com.redbeemedia.enigma.download.resulthandler.IDownloadStartResultHandler;
import com.redbeemedia.enigma.download.resulthandler.IResultHandler;

import java.util.List;

public interface IEnigmaDownload {
    void isExpired(String assetId, ISession session, IResultHandler<Boolean> resultHandler);
    void getExpiryTime(String assetId, ISession session, IResultHandler<Long> resultHandler);

    void isAvailableToDownload(String assetId, ISession session, IResultHandler<Boolean> resultHandler);
    void isAvailableToDownload(String assetId, ISession session, IResultHandler<Boolean> resultHandler, Handler handler);

    void getDownloadableInfo(String assetId, ISession session, IResultHandler<IDownloadableInfo> resultHandler);
    void getDownloadableInfo(String assetId, ISession session, IResultHandler<IDownloadableInfo> resultHandler, Handler handler);

    void startAssetDownload(Context context, DownloadStartRequest request, IDownloadStartResultHandler resultHandler);
    void startAssetDownload(Context context, DownloadStartRequest request, IDownloadStartResultHandler resultHandler, Handler handler);

    void getDownloadedAssets(IResultHandler<List<DownloadedPlayable>> resultHandler);
    void getDownloadedAssets(IResultHandler<List<DownloadedPlayable>> resultHandler, Handler handler);

    void removeDownloadedAsset(DownloadedPlayable downloadedPlayable, IResultHandler<Void> resultHandler);
    void removeDownloadedAsset(DownloadedPlayable downloadedPlayable, IResultHandler<Void> resultHandler, Handler handler);

    void getDownloadsInProgress(IResultHandler<List<IAssetDownload>> resultHandler);
    void getDownloadsInProgress(IResultHandler<List<IAssetDownload>> resultHandler, Handler handler);
}
