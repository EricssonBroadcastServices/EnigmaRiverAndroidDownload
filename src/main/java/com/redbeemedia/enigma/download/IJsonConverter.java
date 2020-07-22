package com.redbeemedia.enigma.download;

import org.json.JSONException;
import org.json.JSONObject;

/*package-protected*/ interface IJsonConverter<T> {
    T convert(JSONObject jsonObject) throws JSONException;
}
