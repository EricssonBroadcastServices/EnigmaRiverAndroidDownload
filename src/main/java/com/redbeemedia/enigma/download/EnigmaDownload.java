package com.redbeemedia.enigma.download;

import android.content.Context;
import android.os.Handler;

import com.redbeemedia.enigma.core.businessunit.IBusinessUnit;
import com.redbeemedia.enigma.core.context.EnigmaRiverContext;
import com.redbeemedia.enigma.core.error.EnigmaError;
import com.redbeemedia.enigma.core.error.UnexpectedError;
import com.redbeemedia.enigma.core.http.AuthenticatedExposureApiCall;
import com.redbeemedia.enigma.core.json.JsonObjectResponseHandler;
import com.redbeemedia.enigma.core.session.ISession;
import com.redbeemedia.enigma.core.util.HandlerWrapper;
import com.redbeemedia.enigma.core.util.IHandler;
import com.redbeemedia.enigma.core.util.ProxyCallback;
import com.redbeemedia.enigma.core.util.UrlPath;
import com.redbeemedia.enigma.download.assetdownload.IAssetDownload;
import com.redbeemedia.enigma.download.resulthandler.IDownloadStartResultHandler;
import com.redbeemedia.enigma.download.resulthandler.IResultHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public final class EnigmaDownload implements IEnigmaDownload {
    private final IBusinessUnit businessUnit;

    public EnigmaDownload(IBusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Override
    public void isAvailableToDownload(String assetId, ISession session, IResultHandler<Boolean> resultHandler) {
        try {
            UrlPath endpoint = businessUnit.getApiBaseUrl("v2").append("/entitlement/").append(assetId).append("/downloadinfo");
            EnigmaRiverContext.getHttpHandler().doHttp(endpoint.toURL(), new AuthenticatedExposureApiCall("GET", session), new JsonObjectResponseHandler() {
                @Override
                protected void onSuccess(JSONObject jsonObject) throws JSONException {
                    resultHandler.onResult(true);
                }

                @Override
                protected void onError(EnigmaError error) {
                    resultHandler.onResult(false);
                }
            });
        } catch (Exception e) {
            resultHandler.onError(new UnexpectedError(e));
            return;
        }
    }

    @Override
    public void isAvailableToDownload(String assetId, ISession session, IResultHandler<Boolean> resultHandler, Handler handler) {
        isAvailableToDownload(assetId, session, resultHandler, new HandlerWrapper(handler));
    }

    /*package-protected*/ void isAvailableToDownload(String assetId, ISession session, IResultHandler<Boolean> resultHandler, IHandler handler) {
        IResultHandler<Boolean> proxiedResultHandler = ProxyCallback.createCallbackOnThread(handler, IResultHandler.class, resultHandler);
        isAvailableToDownload(assetId, session, proxiedResultHandler);
    }

    @Override
    public void getDownloadableInfo(String assetId, ISession session, IResultHandler<IDownloadableInfo> resultHandler) {
        try {
            UrlPath endpoint = businessUnit.getApiBaseUrl("v2").append("/entitlement/").append(assetId).append("/downloadinfo");
            EnigmaRiverContext.getHttpHandler().doHttp(endpoint.toURL(), new AuthenticatedExposureApiCall("GET", session), new JsonObjectResponseHandler() {
                @Override
                protected void onSuccess(JSONObject jsonObject) throws JSONException {
                    IDownloadableInfo downloadableInfo = new DownloadableInfo(jsonObject);
                    resultHandler.onResult(downloadableInfo);
                }

                @Override
                protected void onError(EnigmaError error) {
                    resultHandler.onError(error);
                }
            });
        } catch (Exception e) {
            resultHandler.onError(new UnexpectedError(e));
            return;
        }
    }

    @Override
    public void getDownloadableInfo(String assetId, ISession session,IResultHandler<IDownloadableInfo> resultHandler, Handler handler) {
        getDownloadableInfo(assetId, session, resultHandler, new HandlerWrapper(handler));
    }

    /*package-protected*/ void getDownloadableInfo(String assetId, ISession session,IResultHandler<IDownloadableInfo> resultHandler, IHandler handler) {
        IResultHandler<IDownloadableInfo> proxiedResultHandler = ProxyCallback.createCallbackOnThread(handler, IResultHandler.class, resultHandler);
        getDownloadableInfo(assetId, session, proxiedResultHandler);
    }

    @Override
    public void startAssetDownload(Context context, DownloadStartRequest request, IDownloadStartResultHandler resultHandler) {
        impl().startAssetDownload(context, businessUnit, request, resultHandler);
    }

    @Override
    public void startAssetDownload(Context context, DownloadStartRequest request, IDownloadStartResultHandler resultHandler, Handler handler) {
        startAssetDownload(context, request, resultHandler, new HandlerWrapper(handler));
    }

    /*package-protected*/ void startAssetDownload(Context context, DownloadStartRequest request, IDownloadStartResultHandler resultHandler, IHandler handler) {
        IDownloadStartResultHandler proxiedResultHandler = ProxyCallback.createCallbackOnThread(handler, IDownloadStartResultHandler.class, resultHandler);
        startAssetDownload(context, request, proxiedResultHandler);
    }

    @Override
    public void getDownloadedAssets(IResultHandler<List<DownloadedPlayable>> resultHandler) {
        impl().getDownloadedAssets(resultHandler);
    }

    @Override
    public void getDownloadedAssets(IResultHandler<List<DownloadedPlayable>> resultHandler, Handler handler) {
        getDownloadedAssets(resultHandler, new HandlerWrapper(handler));
    }

    /*package-protected*/ void getDownloadedAssets(IResultHandler<List<DownloadedPlayable>> resultHandler, IHandler handler) {
        IResultHandler<List<DownloadedPlayable>> proxiedResultHandler = ProxyCallback.createCallbackOnThread(handler, IResultHandler.class, resultHandler);
        getDownloadedAssets(proxiedResultHandler);
    }

    @Override
    public void removeDownloadedAsset(DownloadedPlayable downloadedPlayable, IResultHandler<Void> resultHandler) {
        impl().removeDownloadedAsset(downloadedPlayable.getDownloadData(), resultHandler);
    }

    @Override
    public void removeDownloadedAsset(DownloadedPlayable downloadedPlayable, IResultHandler<Void> resultHandler, Handler handler) {
        removeDownloadedAsset(downloadedPlayable, resultHandler, new HandlerWrapper(handler));
    }

    /*package-protected*/ void removeDownloadedAsset(DownloadedPlayable downloadedPlayable, IResultHandler<Void> resultHandler, IHandler handler) {
        IResultHandler<Void> proxiedResultHandler = ProxyCallback.createCallbackOnThread(handler, IResultHandler.class, resultHandler);
        removeDownloadedAsset(downloadedPlayable, proxiedResultHandler);
    }

    @Override
    public void getDownloadsInProgress(IResultHandler<List<IAssetDownload>> resultHandler) {
        impl().getDownloadsInProgress(resultHandler);
    }

    @Override
    public void getDownloadsInProgress(IResultHandler<List<IAssetDownload>> resultHandler, Handler handler) {
        getDownloadsInProgress(resultHandler, new HandlerWrapper(handler));
    }

    /*package-protected*/ void getDownloadsInProgress(IResultHandler<List<IAssetDownload>> resultHandler, IHandler handler) {
        IResultHandler<List<IAssetDownload>> proxiedResultHandler = ProxyCallback.createCallbackOnThread(handler, IResultHandler.class, resultHandler);
        getDownloadsInProgress(proxiedResultHandler);
    }


    private static IEnigmaDownloadImplementation implementation = null;

    private static IEnigmaDownloadImplementation impl() {
        final IEnigmaDownloadImplementation implementationSnapshot = implementation;
        if(implementationSnapshot != null) {
            return implementationSnapshot;
        } else {
            synchronized (EnigmaDownload.class) {
                if(implementation == null) {
                    throw new RuntimeException("No download implementation registered. Missing dependency?");
                }
                return implementation;
            }
        }
    }

    // Note: the method name "registerDownloadImplementation" must not change
    /*package-protected*/ static synchronized void registerDownloadImplementation(IEnigmaDownloadImplementation downloadImplementation) {
        implementation = downloadImplementation;
    }
}
