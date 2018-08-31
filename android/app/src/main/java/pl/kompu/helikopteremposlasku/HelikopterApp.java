package pl.kompu.helikopteremposlasku;

import android.app.Application;

import pl.kompu.helikopteremposlasku.di.components.DaggerHelikopterCompnent;
import pl.kompu.helikopteremposlasku.di.components.DaggerNetComponent;
import pl.kompu.helikopteremposlasku.di.components.HelikopterCompnent;
import pl.kompu.helikopteremposlasku.di.components.NetComponent;
import pl.kompu.helikopteremposlasku.di.modules.AppModule;
import pl.kompu.helikopteremposlasku.di.modules.HelikopterModule;
import pl.kompu.helikopteremposlasku.di.modules.NetModule;

/**
 * Created by Kompu on 2016-04-02.
 */
public class HelikopterApp extends Application {

//    private static String HOST_IP = "http://192.168.1.101:8000";
    private static String HOST_IP = "http://helikopter.pythonanywhere.com";

    private NetComponent netComponent;
    private HelikopterCompnent helikopterCompnent;

    @Override
    public void onCreate() {
        super.onCreate();

        netComponent = DaggerNetComponent.builder()
                .appModule(new AppModule(this))
                .netModule(new NetModule(HOST_IP))
                .build();

        helikopterCompnent = DaggerHelikopterCompnent.builder()
                .netComponent(netComponent)
                .helikopterModule(new HelikopterModule())
                .build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }

    public HelikopterCompnent getHelikopterCompnent() {
        return helikopterCompnent;
    }
}
