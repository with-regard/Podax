package io.regard.android;

import android.content.SharedPreferences;

import java.util.UUID;

public class AndroidRegardClient extends AbstractRegardClient {
    private final static String REGARD_USERID_KEY = "regard-user-id";

    public AndroidRegardClient(SharedPreferences preferences, String organization, String product) {
        super(getUserId(preferences), organization, product);
    }

    private static String getUserId(SharedPreferences preferences) {
        final String existingUserId = preferences.getString(REGARD_USERID_KEY, null);

        if (existingUserId != null){
            return existingUserId;
        }

        String newUserId = UUID.randomUUID().toString();
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(REGARD_USERID_KEY, newUserId);
        edit.apply();
        return newUserId;
    }
}
