package com.redbeemedia.enigma.download;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.redbeemedia.enigma.core.businessunit.IBusinessUnit;
import com.redbeemedia.enigma.core.context.EnigmaRiverContext;
import com.redbeemedia.enigma.core.error.EnigmaError;
import com.redbeemedia.enigma.core.error.UnexpectedError;
import com.redbeemedia.enigma.core.http.AuthenticatedExposureApiCall;
import com.redbeemedia.enigma.core.http.HttpStatus;
import com.redbeemedia.enigma.core.json.JsonObjectResponseHandler;
import com.redbeemedia.enigma.core.session.ISession;
import com.redbeemedia.enigma.core.util.HandlerWrapper;
import com.redbeemedia.enigma.core.util.IHandler;
import com.redbeemedia.enigma.core.util.ProxyCallback;
import com.redbeemedia.enigma.core.util.UrlPath;
import com.redbeemedia.enigma.download.assetdownload.IAssetDownload;
import com.redbeemedia.enigma.download.resulthandler.BaseResultHandler;
import com.redbeemedia.enigma.download.resulthandler.IDownloadStartResultHandler;
import com.redbeemedia.enigma.download.resulthandler.IResultHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public final class EnigmaDownload implements IEnigmaDownload {
    private static final String EXPIRATION_TIME = "EXPIRATION_TIME";
    private static final String PUBLICATION_END = "PUBLICATION_END";
    private static final int SAVE_FORMAT_VERSION = 3;

    private final IBusinessUnit businessUnit;

    public EnigmaDownload(IBusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    @Override
    public void isExpired(String assetId, ISession session, IResultHandler<Boolean> resultHandler) {
        getExpiryTime(assetId, session, new BaseResultHandler<Long>() {
            @Override
            public void onResult(Long expiryTime) {
                if (expiryTime != -1) {
                    boolean isExpired = System.currentTimeMillis() > expiryTime;
                    resultHandler.onResult(isExpired);
                } else {
                    resultHandler.onResult(true);
                }
            }

            @Override
            public void onError(EnigmaError error) {
                resultHandler.onError(new UnexpectedError(error));
            }
        });
    }

    @Override
    public void getExpiryTime(String assetId, ISession session, IResultHandler<Long> resultHandler) {
        try {
            JSONObject storedJsonObject = getStoredJsonData(assetId);
            long playTokenExpirationInSeconds = storedJsonObject.optLong(EXPIRATION_TIME, -1);
            UrlPath endpoint = businessUnit.getApiBaseUrl("v2").append("/entitlement/").append(assetId).append("/downloadverified");
            EnigmaRiverContext.getHttpHandler().doHttp(endpoint.toURL(), new AuthenticatedExposureApiCall("GET", session), new JsonObjectResponseHandler() {
                protected void onError(EnigmaError error) {
                    getStoredExpiryTime(assetId, resultHandler);
                }

                @Override
                public void onResponse(HttpStatus status, InputStream inputStream) {
                    if (status.getResponseCode() == 403) {
                        resultHandler.onResult(-1L);
                    } else {
                        super.onResponse(status, inputStream);
                    }
                }

                protected void onSuccess(JSONObject jsonObject) {
                    try {
                        String publicationEnd = jsonObject.optString("publicationEnd", "");
                        if (!publicationEnd.isEmpty()) {
                            storedJsonObject.put(PUBLICATION_END, publicationEnd);
                            EnigmaDownloadContext.getMetadataManager().store(assetId, getJsonBytes(storedJsonObject));
                        }
                        calculateMinimumExpiryDate(resultHandler, playTokenExpirationInSeconds, publicationEnd);
                    } catch (Exception e) {
                        getStoredExpiryTime(assetId, resultHandler);
                    }
                }
            });
        } catch (Exception e) {
            getStoredExpiryTime(assetId, resultHandler);
        }
    }

    private void getStoredExpiryTime(String assetId, IResultHandler<Long> resultHandler) {
        try {
            JSONObject storedJsonObject = getStoredJsonData(assetId);
            long playTokenExpirationInSeconds = storedJsonObject.optLong(EXPIRATION_TIME,-1);
            String publicationEnd = storedJsonObject.optString(PUBLICATION_END,"");
            calculateMinimumExpiryDate(resultHandler, playTokenExpirationInSeconds, publicationEnd);
        } catch (Exception exception) {
            resultHandler.onError(new UnexpectedError(exception));
        }
    }

    @NonNull
    private JSONObject getStoredJsonData(String assetId) throws JSONException {
        try {
            byte[] data = EnigmaDownloadContext.getMetadataManager().load(assetId);
            String jsonData = new String(data, 1, data.length - 1, StandardCharsets.UTF_8);
            return new JSONObject(jsonData);
        } catch (Exception e){
            // if stored json data is empty
            return new JSONObject();
        }
    }

    public byte[] getJsonBytes(JSONObject jsonObject) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(SAVE_FORMAT_VERSION);
        byte[] jsonBytes = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
        baos.write(jsonBytes, 0, jsonBytes.length);
        return baos.toByteArray();
    }

    private void calculateMinimumExpiryDate(IResultHandler<Long> resultHandler, long playTokenExpirationInSeconds, String publicationEnd) throws ParseException {
        if (!publicationEnd.trim().isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date publicationEndDate = dateFormat.parse(publicationEnd);
            long publicationEndDateTimeStamp = publicationEndDate.getTime();
            long playTokenExpirationInMillis = playTokenExpirationInSeconds;
            if (playTokenExpirationInSeconds != -1) {
                playTokenExpirationInMillis = playTokenExpirationInSeconds * 1000;
            }
            resultHandler.onResult(Math.min(playTokenExpirationInMillis, publicationEndDateTimeStamp));
        }else{
            resultHandler.onResult(-1L);
        }
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
    public void getDownloadedAssets(ISession session, IResultHandler<List<DownloadedPlayable>> resultHandler) {
        impl().getDownloadedAssets(session, resultHandler);
    }

    @Override
    public void getDownloadedAssets(ISession session, IResultHandler<List<DownloadedPlayable>> resultHandler, Handler handler) {
        getDownloadedAssets(session, resultHandler, new HandlerWrapper(handler));
    }

    /*package-protected*/ void getDownloadedAssets(ISession session, IResultHandler<List<DownloadedPlayable>> resultHandler, IHandler handler) {
        IResultHandler<List<DownloadedPlayable>> proxiedResultHandler = ProxyCallback.createCallbackOnThread(handler, IResultHandler.class, resultHandler);
        getDownloadedAssets(session, proxiedResultHandler);
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
    public void getDownloadsInProgress(ISession session, IResultHandler<List<IAssetDownload>> resultHandler) {
        impl().getDownloadsInProgress(session, resultHandler);
    }

    @Override
    public void getDownloadsInProgress(ISession session, IResultHandler<List<IAssetDownload>> resultHandler, Handler handler) {
        getDownloadsInProgress(session,resultHandler, new HandlerWrapper(handler));
    }

    /*package-protected*/ void getDownloadsInProgress(ISession session, IResultHandler<List<IAssetDownload>> resultHandler, IHandler handler) {
        IResultHandler<List<IAssetDownload>> proxiedResultHandler = ProxyCallback.createCallbackOnThread(handler, IResultHandler.class, resultHandler);
        getDownloadsInProgress(session, proxiedResultHandler);
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
