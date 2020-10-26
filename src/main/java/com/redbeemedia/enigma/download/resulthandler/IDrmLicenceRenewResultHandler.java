package com.redbeemedia.enigma.download.resulthandler;

import com.redbeemedia.enigma.core.error.EnigmaError;
import com.redbeemedia.enigma.core.util.IInternalCallbackObject;

public interface IDrmLicenceRenewResultHandler extends IInternalCallbackObject {
    /**
     * <p>Inspired by {@code org.hamcrest.Matcher} from JUnit lib.</p>
     * <br>
     * <p style="margin-left: 25px; font-weight:bold;">It's easy to ignore JavaDoc, but a bit harder to ignore compile errors .</p>
     * <p style="margin-left: 50px">-- Hamcrest source</p>
     */
    @Deprecated
    void _dont_implement_IDrmLicenceRenewResultHandler___instead_extend_BaseDrmLicenceRenewResultHandler_();

    void onSuccess();
    void onError(EnigmaError error);
}
