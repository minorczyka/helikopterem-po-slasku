package pl.kompu.helikopteremposlasku.di.components;

import android.app.Application;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Component;
import pl.kompu.helikopteremposlasku.di.modules.AppModule;
import pl.kompu.helikopteremposlasku.di.modules.NetModule;
import retrofit.Retrofit;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface NetComponent {

    Retrofit retrofit();
    SharedPreferences sharedPreferences();
}
