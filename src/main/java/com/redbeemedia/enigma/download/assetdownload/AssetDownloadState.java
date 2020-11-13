package com.redbeemedia.enigma.download.assetdownload;

public enum AssetDownloadState {
    IN_PROGRESS, PAUSED, FAILED, DONE, CANCELLED;

    public boolean isResolved() {
        return isResolved(this);
    }

    private static boolean isResolved(AssetDownloadState state) {
        return state == DONE
            || state == CANCELLED;
    }
}
