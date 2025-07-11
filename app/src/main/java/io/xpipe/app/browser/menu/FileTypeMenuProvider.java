package io.xpipe.app.browser.menu;

import io.xpipe.app.browser.file.BrowserEntry;
import io.xpipe.app.browser.file.BrowserFileSystemTabModel;
import io.xpipe.app.browser.icon.BrowserIconFileType;
import io.xpipe.app.browser.icon.BrowserIcons;
import io.xpipe.app.util.LabelGraphic;

import java.util.List;

public interface FileTypeMenuProvider extends BrowserMenuItemProvider {

    @Override
    default LabelGraphic getIcon(BrowserFileSystemTabModel model, List<BrowserEntry> entries) {
        return new LabelGraphic.CompGraphic(BrowserIcons.createContextMenuIcon(getType()));
    }

    @Override
    default boolean isApplicable(BrowserFileSystemTabModel model, List<BrowserEntry> entries) {
        var t = getType();
        return entries.stream().allMatch(entry -> t.matches(entry.getRawFileEntry()));
    }

    BrowserIconFileType getType();
}
