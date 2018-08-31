package pl.kompu.helikopteremposlasku.di.components;

import dagger.Component;
import pl.kompu.helikopteremposlasku.activities.LoginActivity;
import pl.kompu.helikopteremposlasku.activities.MainActivity;
import pl.kompu.helikopteremposlasku.activities.RidesActivity;
import pl.kompu.helikopteremposlasku.di.modules.HelikopterModule;
import pl.kompu.helikopteremposlasku.di.scopes.UserScope;
import pl.kompu.helikopteremposlasku.fragments.HistoryFragment;
import pl.kompu.helikopteremposlasku.fragments.HomeFragment;
import pl.kompu.helikopteremposlasku.listeners.CalendarListener;

@UserScope
@Component(dependencies = NetComponent.class, modules = HelikopterModule.class)
public interface HelikopterCompnent {

    void inject(MainActivity activity);
    void inject(LoginActivity activity);
    void inject(RidesActivity activity);
    void inject(HomeFragment fragment);
    void inject(HistoryFragment fragment);
    void inject(CalendarListener calendarListener);
}
