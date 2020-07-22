package com.redbeemedia.enigma.download.resulthandler;

import com.redbeemedia.enigma.core.error.EnigmaError;

public abstract class BaseResultHandler<T> implements IResultHandler<T> {
    @Override
    @Deprecated
    public final void _dont_implement_IResultHandler___instead_extend_BaseResultHandler_() {
    }

    @Override
    public abstract void onResult(T result);

    @Override
    public abstract void onError(EnigmaError error);
}
