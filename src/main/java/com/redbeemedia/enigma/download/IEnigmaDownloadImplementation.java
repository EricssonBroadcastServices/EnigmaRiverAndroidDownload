package com.redbeemedia.enigma.download;

import android.content.Context;

import com.redbeemedia.enigma.core.businessunit.IBusinessUnit;
import com.redbeemedia.enigma.core.session.ISession;
import com.redbeemedia.enigma.download.assetdownload.IAssetDownload;
import com.redbeemedia.enigma.download.resulthandler.IDownloadStartResultHandler;
import com.redbeemedia.enigma.download.resulthandler.IResultHandler;

import java.util.List;

/**
 * <h3>NOTE</h3>
 * <p>This interface is not part of the public API.</p>
 */
public interface IEnigmaDownloadImplementation {
    void startAssetDownload(Context context, IBusinessUnit businessUnit, DownloadStartRequest request, IDownloadStartResultHandler resultHandler);

    void getDownloadedAssets(IResultHandler<List<DownloadedPlayable>> resultHandler);

    void getDownloadedAssets(ISession session, IResultHandler<List<DownloadedPlayable>> resultHandler);

    void removeDownloadedAsset(DownloadedPlayable.IInternalDownloadData downloadedData, IResultHandler<Void> resultHandler);

    void getDownloadsInProgress(IResultHandler<List<IAssetDownload>> resultHandler);

    void getDownloadsInProgress(ISession session,IResultHandler<List<IAssetDownload>> resultHandler);
}
