package io.xpipe.app.core.check;

import io.xpipe.app.core.AppProperties;
import io.xpipe.app.prefs.AppPrefs;
import io.xpipe.app.util.PlatformState;

import javafx.application.ConditionalFeature;
import javafx.application.Platform;

public class AppGpuCheck {

    public static void check() {
        if (!AppProperties.get().isInitialLaunch()) {
            return;
        }

        // We might launch the platform due to an error early
        if (AppPrefs.get() == null) {
            return;
        }

        if (PlatformState.getCurrent() != PlatformState.RUNNING) {
            return;
        }

        if (Platform.isSupported(ConditionalFeature.SCENE3D)) {
            return;
        }

        AppPrefs.get().performanceMode.setValue(true);
    }
}
