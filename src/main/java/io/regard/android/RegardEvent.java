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
            obj.put("time", System.currentTimeMillis());
            obj.put("user-id", userId);
            obj.put("session-id", sessionId);
            if (_data != null) {
                obj.put("data", new JSONObject(_data));
            }
            return obj;
        } catch (JSONException ex) {
            throw new RuntimeException(ex);
        }
    }
}
