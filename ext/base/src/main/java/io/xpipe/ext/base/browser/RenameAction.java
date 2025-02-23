package io.xpipe.ext.base.browser;

import io.xpipe.app.browser.action.BrowserLeafAction;
import io.xpipe.app.browser.file.BrowserEntry;
import io.xpipe.app.browser.file.BrowserFileSystemTabModel;
import io.xpipe.app.core.AppI18n;
import io.xpipe.core.store.FileKind;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public class RenameAction implements BrowserLeafAction {

    @Override
    public void execute(BrowserFileSystemTabModel model, List<BrowserEntry> entries) {
        model.getFileList().getEditing().setValue(entries.getFirst());
    }

    @Override
    public Node getIcon(BrowserFileSystemTabModel model, List<BrowserEntry> entries) {
        return new FontIcon("mdi2r-rename-box");
    }

    @Override
    public Category getCategory() {
        return Category.MUTATION;
    }

    @Override
    public KeyCombination getShortcut() {
        return new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN);
    }

    @Override
    public ObservableValue<String> getName(BrowserFileSystemTabModel model, List<BrowserEntry> entries) {
        return AppI18n.observable("rename");
    }

    @Override
    public boolean isApplicable(BrowserFileSystemTabModel model, List<BrowserEntry> entries) {
        return entries.size() == 1 && entries.getFirst().getRawFileEntry().getKind() != FileKind.LINK;
    }

    @Override
    public boolean automaticallyResolveLinks() {
        return false;
    }
}
