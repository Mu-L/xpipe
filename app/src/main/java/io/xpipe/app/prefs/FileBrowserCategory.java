package io.xpipe.app.prefs;

import io.xpipe.app.comp.Comp;
import io.xpipe.app.util.OptionsBuilder;

public class FileBrowserCategory extends AppPrefsCategory {

    @Override
    protected String getId() {
        return "fileBrowser";
    }

    @Override
    protected Comp<?> create() {
        var prefs = AppPrefs.get();
        return new OptionsBuilder()
                .addTitle("fileBrowser")
                .sub(new OptionsBuilder()
                        .pref(prefs.enableTerminalDocking)
                        .addToggle(prefs.enableTerminalDocking)
                        .pref(prefs.confirmAllDeletions)
                        .addToggle(prefs.confirmAllDeletions)
                        .pref(prefs.downloadsDirectory)
                        .addString(prefs.downloadsDirectory))
                .buildComp();
    }
}
