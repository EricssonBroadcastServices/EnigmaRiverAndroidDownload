package com.redbeemedia.enigma.download.resulthandler;

import com.redbeemedia.enigma.core.error.EnigmaError;

public abstract class BaseDownloadStartResultHandler implements IDownloadStartResultHandler {
    @Override
    @Deprecated
    public final void _dont_implement_IDownloadStartResultHandler___instead_extend_BaseDownloadStartResultHandler_() {
    }

    @Override
    public void onStarted() {
    }

    @Override
    public abstract void onError(EnigmaError error);
}
