package io.regard.android;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegardEvent {
    private final String _type;
    private final HashMap<String, String> _data;

    public RegardEvent(String type, HashMap<String, String> data) {
        _type = type;
        _data = data;
    }

    protected JSONObject AsJSONObject(String userId, String sessionId) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("event-type", _type);
            //TODO: put in the data map thing

            obj.put("time", System.currentTimeMillis());
            obj.put("user-id", userId);
            obj.put("session-id", sessionId);
            return obj;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }

    public String getType() {
        return _type;
    }

    public HashMap<String, String> getData() {
        return _data;
    }
}
