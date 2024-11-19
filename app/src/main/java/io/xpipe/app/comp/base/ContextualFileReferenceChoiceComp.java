package io.xpipe.app.comp.base;

import io.xpipe.app.browser.BrowserFileChooserSessionComp;
import io.xpipe.app.comp.Comp;
import io.xpipe.app.comp.CompStructure;
import io.xpipe.app.comp.SimpleCompStructure;
import io.xpipe.app.core.AppLayoutModel;
import io.xpipe.app.core.window.AppWindowHelper;
import io.xpipe.app.issue.ErrorEvent;
import io.xpipe.app.prefs.AppPrefs;
import io.xpipe.app.storage.ContextualFileReference;
import io.xpipe.app.storage.DataStorage;
import io.xpipe.app.storage.DataStoreEntryRef;
import io.xpipe.core.store.FileNames;
import io.xpipe.core.store.FileSystemStore;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import atlantafx.base.theme.Styles;
import lombok.Value;
import org.kordamp.ikonli.javafx.FontIcon;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ContextualFileReferenceChoiceComp extends Comp<CompStructure<HBox>> {

    @Value
    public static class PreviousFileReference {

        String displayName;
        Path path;
    }

    private final Property<DataStoreEntryRef<? extends FileSystemStore>> fileSystem;
    private final Property<String> filePath;
    private final boolean allowSync;
    private final List<PreviousFileReference> previousFileReferences;

    public <T extends FileSystemStore> ContextualFileReferenceChoiceComp(
            Property<DataStoreEntryRef<T>> fileSystem,
            Property<String> filePath,
            boolean allowSync,
            List<PreviousFileReference> previousFileReferences) {
        this.allowSync = allowSync;
        this.previousFileReferences = previousFileReferences;
        this.fileSystem = new SimpleObjectProperty<>();
        fileSystem.subscribe(val -> {
            this.fileSystem.setValue(val);
        });
        this.fileSystem.addListener((observable, oldValue, newValue) -> {
            fileSystem.setValue(newValue != null ? newValue.get().ref() : null);
        });
        this.filePath = filePath;
    }

    @Override
    public CompStructure<HBox> createBase() {
        var path = previousFileReferences.isEmpty() ? createTextField() : createComboBox();
        var fileBrowseButton = new ButtonComp(null, new FontIcon("mdi2f-folder-open-outline"), () -> {
                    BrowserFileChooserSessionComp.openSingleFile(
                            () -> fileSystem.getValue(),
                            fileStore -> {
                                if (fileStore == null) {
                                    filePath.setValue(null);
                                    fileSystem.setValue(null);
                                } else {
                                    filePath.setValue(fileStore.getPath());
                                    fileSystem.setValue(fileStore.getFileSystem());
                                }
                            },
                            false);
                })
                .styleClass(allowSync ? Styles.CENTER_PILL : Styles.RIGHT_PILL)
                .grow(false, true);

        var gitShareButton = new ButtonComp(null, new FontIcon("mdi2g-git"), () -> {
            if (!AppPrefs.get().enableGitStorage().get()) {
                AppLayoutModel.get().selectSettings();
                AppPrefs.get().selectCategory("sync");
                return;
            }

            var currentPath = filePath.getValue();
            if (currentPath == null || currentPath.isBlank()) {
                return;
            }

            if (ContextualFileReference.of(currentPath).isInDataDirectory()) {
                return;
            }

            try {
                var data = DataStorage.get().getDataDir();
                var f = data.resolve(FileNames.getFileName(currentPath.trim()));
                var source = Path.of(currentPath.trim());
                if (Files.exists(source)) {
                    var shouldCopy = AppWindowHelper.showConfirmationAlert(
                            "confirmGitShareTitle", "confirmGitShareHeader", "confirmGitShareContent");
                    if (!shouldCopy) {
                        return;
                    }

                    Files.copy(source, f, StandardCopyOption.REPLACE_EXISTING);
                    Platform.runLater(() -> {
                        filePath.setValue(f.toString());
                    });
                }
            } catch (Exception e) {
                ErrorEvent.fromThrowable(e).handle();
            }
        });
        gitShareButton.tooltipKey("gitShareFileTooltip");
        gitShareButton.styleClass(Styles.RIGHT_PILL).grow(false, true);

        var nodes = new ArrayList<Comp<?>>();
        nodes.add(path);
        nodes.add(fileBrowseButton);
        if (allowSync) {
            nodes.add(gitShareButton);
        }
        var layout = new HorizontalComp(nodes).apply(struc -> struc.get().setFillHeight(true));

        layout.apply(struc -> {
            struc.get().focusedProperty().addListener((observable, oldValue, newValue) -> {
                struc.get().getChildren().getFirst().requestFocus();
            });
        });

        return new SimpleCompStructure<>(layout.createStructure().get());
    }

    private Comp<?> createComboBox() {
        var items = previousFileReferences.stream()
                .map(previousFileReference -> previousFileReference.getPath().toString())
                .toList();
        var combo = new ComboTextFieldComp(filePath, items, param -> {
            return new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        return;
                    }

                    var display = previousFileReferences.stream()
                            .filter(ref -> ref.path.toString().equals(item))
                            .findFirst()
                            .map(previousFileReference -> previousFileReference.getDisplayName())
                            .orElse(item);
                    setText(display);
                }
            };
        });
        combo.hgrow();
        combo.styleClass(Styles.LEFT_PILL);
        combo.grow(false, true);
        return combo;
    }

    private Comp<?> createTextField() {
        var fileNameComp = new TextFieldComp(filePath)
                .apply(struc -> HBox.setHgrow(struc.get(), Priority.ALWAYS))
                .styleClass(Styles.LEFT_PILL)
                .grow(false, true);
        return fileNameComp;
    }
}