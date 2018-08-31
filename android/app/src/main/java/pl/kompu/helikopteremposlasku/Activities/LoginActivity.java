package pl.kompu.helikopteremposlasku.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.squareup.okhttp.Credentials;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.kompu.helikopteremposlasku.HelikopterApp;
import pl.kompu.helikopteremposlasku.network.HelikopterService;
import pl.kompu.helikopteremposlasku.model.User;
import pl.kompu.helikopteremposlasku.R;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;

public class LoginActivity extends AppCompatActivity implements Callback<ArrayList<User>> {

    public static final String EXTRA_USERS = "users";
    public static final String EXTRA_LOGGED_USER = "logged_user";
    public static final String EXTRA_AUTO_LOGIN = "auto_login";

    public static final String PREF_LOGIN = "login";
    public static final String PREF_PASSWORD = "password";

    @Bind(R.id.input_login) TextView inputLogin;
    @Bind(R.id.input_password) TextView inputPassword;
    @Bind(R.id.btn_login) ActionProcessButton buttonLogin;

    @Inject SharedPreferences sharedPreferences;
    @Inject HelikopterService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((HelikopterApp) getApplication()).getHelikopterCompnent().inject(this);

        buttonLogin.setMode(ActionProcessButton.Mode.ENDLESS);

        boolean autoLogin = getIntent().getBooleanExtra(EXTRA_AUTO_LOGIN, true);
        loadCredentials(autoLogin);
    }

    @OnClick(R.id.btn_login)
    public void login() {
        buttonLogin.setProgress(1);
        buttonLogin.setEnabled(false);
        inputLogin.setEnabled(false);
        inputPassword.setEnabled(false);

        String login = inputLogin.getText().toString();
        String password = inputPassword.getText().toString();
        String auth = Credentials.basic(login, password);

        Call<ArrayList<User>> users = service.getPeople(auth);
        users.enqueue(this);
    }

    @Override
    public void onResponse(Response<ArrayList<User>> response) {
        if (response.code() == 200) {
            ArrayList<User> list = response.body();
            buttonLogin.setProgress(100);
            saveCredentials();

            User loggedUser = getLoggedUser(list, inputLogin.getText().toString());
            if (loggedUser != null) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putParcelableArrayListExtra(EXTRA_USERS, list);
                intent.putExtra(EXTRA_LOGGED_USER, loggedUser);
                startActivity(intent);
            }
        } else if (response.code() == 401) {
            onFailure(new Throwable(response.message()));
        }
    }

    @Override
    public void onFailure(Throwable t) {
        buttonLogin.setProgress(-1);

        buttonLogin.setEnabled(true);
        inputLogin.setEnabled(true);
        inputPassword.setEnabled(true);
    }

    private void saveCredentials() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_LOGIN, inputLogin.getText().toString());
        editor.putString(PREF_PASSWORD, inputPassword.getText().toString());
        editor.commit();
    }

    private void loadCredentials(boolean autoLogin) {
        String login = sharedPreferences.getString(PREF_LOGIN, "");
        String password = sharedPreferences.getString(PREF_PASSWORD, "");

        if (!login.equals("")) {
            inputLogin.setText(login);
            inputPassword.setText(password);

            if (autoLogin) {
                login();
            }
        }
    }

    private User getLoggedUser(ArrayList<User> users, String username) {
        for (User user : users) {
            if (user.username.equals(username)) {
                return user;
            }
        }
        return null;
    }
}
