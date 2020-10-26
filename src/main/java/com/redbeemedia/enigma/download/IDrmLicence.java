package com.redbeemedia.enigma.download;

import android.os.Handler;

import com.redbeemedia.enigma.core.session.ISession;
import com.redbeemedia.enigma.download.resulthandler.IDrmLicenceRenewResultHandler;

public interface IDrmLicence {
    int EXPIRY_TIME_UNKNOWN = -1;

    /**
     * @return The expiry time for the drm licence as a
     * unix timestamp in milliseconds or {@link IDrmLicence#EXPIRY_TIME_UNKNOWN} if unknown
     */
    long getExpiryTime();

    /**
     * Sends a request to renew the drm licence.
     *
     * @param session
     * @param resultHandler
     */
    void renew(ISession session, IDrmLicenceRenewResultHandler resultHandler);
    /**
     * Sends a request to renew the drm licence.
     *
     * @param session
     * @param resultHandler
     * @param handler Handler to use for <code>resultHandler</code> callbacks
     */
    void renew(ISession session, IDrmLicenceRenewResultHandler resultHandler, Handler handler);
}
