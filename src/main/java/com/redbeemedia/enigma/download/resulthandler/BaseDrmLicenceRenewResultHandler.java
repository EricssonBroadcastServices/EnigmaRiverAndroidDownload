package com.redbeemedia.enigma.download.resulthandler;

import com.redbeemedia.enigma.core.error.EnigmaError;

public abstract class BaseDrmLicenceRenewResultHandler implements IDrmLicenceRenewResultHandler {
    @Override
    @Deprecated
    public final void _dont_implement_IDrmLicenceRenewResultHandler___instead_extend_BaseDrmLicenceRenewResultHandler_() {
    }

    @Override
    public void onSuccess() {
    }

    @Override
    public abstract void onError(EnigmaError error);
}
