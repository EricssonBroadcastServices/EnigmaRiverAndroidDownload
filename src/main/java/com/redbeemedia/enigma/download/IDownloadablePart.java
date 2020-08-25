package com.redbeemedia.enigma.download;

import org.json.JSONObject;

public interface IDownloadablePart {
    String getName();

    /**
     * This method is not part of the public API.
     */
    JSONObject getRawJson();
}
