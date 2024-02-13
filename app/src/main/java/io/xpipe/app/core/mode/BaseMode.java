package io.xpipe.app.core.mode;

import io.xpipe.app.browser.BrowserModel;
import io.xpipe.app.comp.store.StoreViewState;
import io.xpipe.app.core.*;
import io.xpipe.app.core.check.AppAvCheck;
import io.xpipe.app.core.check.AppCertutilCheck;
import io.xpipe.app.core.check.AppShellCheck;
import io.xpipe.app.ext.ActionProvider;
import io.xpipe.app.issue.TrackEvent;
import io.xpipe.app.prefs.AppPrefs;
import io.xpipe.app.storage.DataStorage;
import io.xpipe.app.update.XPipeDistributionType;
import io.xpipe.app.util.*;
import io.xpipe.core.util.JacksonMapper;

public class BaseMode extends OperationMode {

    private boolean initialized;

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public String getId() {
        return "background";
    }

    @Override
    public void onSwitchTo() throws Throwable {
        if (initialized) {
            return;
        }

        // For debugging
        // if (true) throw new IllegalStateException();

        TrackEvent.info("Initializing base mode components ...");
        AppExtensionManager.init(true);
        JacksonMapper.initModularized(AppExtensionManager.getInstance().getExtendedLayer());
        // Load translations before storage initialization to localize store error messages
        // Also loaded before antivirus alert to localize that
        AppI18n.init();
        LicenseProvider.get().init();
        AppPrefs.init();
        AppCertutilCheck.check();
        AppAvCheck.check();
        LocalShell.init();
        XPipeDistributionType.init();
        AppShellCheck.check();
        AppPrefs.setDefaults();
        // Initialize socket server before storage
        // as we should be prepared for git askpass commands
        AppSocketServer.init();
        UnlockAlert.showIfNeeded();
        DataStorage.init();
        AppFileWatcher.init();
        FileBridge.init();
        ActionProvider.initProviders();
        TrackEvent.info("Finished base components initialization");
        initialized = true;

        var sec = new VaultKeySecretValue(new char[] {1, 2, 3});
        sec.getSecret();
    }

    @Override
    public void onSwitchFrom() {}

    @Override
    public void finalTeardown() {
        TrackEvent.info("Background mode shutdown started");
        BrowserModel.DEFAULT.reset();
        StoreViewState.reset();
        DataStorage.reset();
        AppPrefs.reset();
        AppResources.reset();
        AppExtensionManager.reset();
        AppDataLock.unlock();
        // Shut down socket server last to keep a non-daemon thread running
        AppSocketServer.reset();
        TrackEvent.info("Background mode shutdown finished");
    }
}
