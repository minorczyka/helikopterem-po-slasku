package pl.kompu.helikopteremposlasku.di.modules;

import android.content.SharedPreferences;

import dagger.Module;
import dagger.Provides;
import pl.kompu.helikopteremposlasku.di.scopes.UserScope;
import pl.kompu.helikopteremposlasku.network.CredentialsProvider;
import pl.kompu.helikopteremposlasku.network.HelikopterService;
import retrofit.Retrofit;

@Module
public class HelikopterModule {

    @Provides
    @UserScope
    HelikopterService provideHelikopterService(Retrofit retrofit) {
        return retrofit.create(HelikopterService.class);
    }

    @Provides
    @UserScope
    CredentialsProvider provideCredentialsProvider(SharedPreferences sharedPreferences) {
        return new CredentialsProvider(sharedPreferences);
    }
}
