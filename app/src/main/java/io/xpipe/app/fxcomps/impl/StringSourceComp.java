package io.xpipe.app.fxcomps.impl;

import io.xpipe.app.fxcomps.SimpleComp;
import io.xpipe.app.storage.DataStoreEntryRef;
import io.xpipe.app.util.StringSource;
import io.xpipe.core.store.ShellStore;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;

public class StringSourceComp extends SimpleComp {

    private final Property<DataStoreEntryRef<? extends ShellStore>> fileSystem;
    private final Property<StringSource> stringSource;

    public <T extends ShellStore> StringSourceComp(ObservableValue<DataStoreEntryRef<T>> fileSystem, Property<StringSource> stringSource) {
        this.stringSource = stringSource;
        this.fileSystem = new SimpleObjectProperty<>();
        fileSystem.subscribe(val -> {
            this.fileSystem.setValue(val);
        });
    }

    @Override
    protected Region createSimple() {
        var tab = new TabPane();

        var inPlace = new SimpleObjectProperty<>(stringSource.getValue() instanceof StringSource.InPlace i ? i.get() : null);
        var stringField = new TextAreaComp(inPlace);
        tab.getTabs().add(new Tab());

        var fs = stringSource.getValue() instanceof StringSource.File f ? f.getFile() : null;
        var file = new SimpleObjectProperty<>(stringSource.getValue() instanceof StringSource.File f ? f.getFile() : null);
        // new ContextualFileReferenceChoiceComp(fileSystem, file);
        return null;
    }
}
