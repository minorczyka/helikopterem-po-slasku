package pl.kompu.helikopteremposlasku.network;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.squareup.okhttp.Credentials;

import pl.kompu.helikopteremposlasku.activities.LoginActivity;

public class CredentialsProvider {

    private SharedPreferences sharedPreferences;

    public CredentialsProvider(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public String getCredentials() {
        String login = sharedPreferences.getString(LoginActivity.PREF_LOGIN, "");
        String password = sharedPreferences.getString(LoginActivity.PREF_PASSWORD, "");
        String auth = Credentials.basic(login, password);
        return auth;
    }
}
