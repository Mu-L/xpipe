package io.xpipe.core.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.xpipe.core.process.ProcessControlProvider;
import io.xpipe.core.process.ShellDialects;
import io.xpipe.core.process.ShellProcessControl;
import io.xpipe.core.store.*;
import io.xpipe.core.util.JacksonizedValue;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@JsonTypeName("local")
public class LocalStore extends JacksonizedValue implements FileSystemStore, MachineStore {

    private static ShellProcessControl local;

    public static ShellProcessControl getShell() throws Exception {
        if (local == null) {
            local = new LocalStore().create().start();
        }

        return local;
    }

    @Override
    public boolean isLocal() {
        return true;
    }

    @Override
    public FileSystem createFileSystem() {
        return new ConnectionFileSystem(ShellStore.local().create()) {

            @Override
            public InputStream openInput(String file) throws Exception {
                var p = wrap(file);
                return Files.newInputStream(p);
            }

            @Override
            public OutputStream openOutput(String file) throws Exception {
                var p = wrap(file);
                return Files.newOutputStream(p);
            }

            private Path wrap(String file) {
                for (var e : System.getenv().entrySet()) {
                    file = file.replace(
                            ShellDialects.getPlatformDefault().environmentVariable(e.getKey()),
                            e.getValue());
                }
                return Path.of(file);
            }

//            @Override
//            public boolean exists(String file) {
//                return Files.exists(wrap(file));
//            }
//
//            @Override
//            public void delete(String file) throws Exception {
//                Files.delete(wrap(file));
//            }
//
//            @Override
//            public void copy(String file, String newFile) throws Exception {
//                Files.copy(wrap(file), wrap(newFile), StandardCopyOption.REPLACE_EXISTING);
//            }
//
//            @Override
//            public void move(String file, String newFile) throws Exception {
//                Files.move(wrap(file), wrap(newFile), StandardCopyOption.REPLACE_EXISTING);
//            }
//
//            @Override
//            public boolean mkdirs(String file) throws Exception {
//                try {
//                    Files.createDirectories(wrap(file));
//                    return true;
//                } catch (Exception ex) {
//                    return false;
//                }
//            }
//
//            @Override
//            public void touch(String file) throws Exception {
//                if (exists(file)) {
//                    return;
//                }
//
//                Files.createFile(wrap(file));
//            }
//
//            @Override
//            public boolean isDirectory(String file) throws Exception {
//                return Files.isDirectory(wrap(file));
//            }
//
//            @Override
//            public Stream<FileEntry> listFiles(String file) throws Exception {
//                return Files.list(wrap(file)).map(path -> {
//                    try {
//                        var date = Files.getLastModifiedTime(path);
//                        var size = Files.isDirectory(path) ? 0 : Files.size(path);
//                        return new FileEntry(
//                                this,
//                                path.toString(),
//                                date.toInstant(),
//                                Files.isDirectory(path),
//                                Files.isHidden(path),
//                                Files.isExecutable(path),
//                                size);
//                    } catch (IOException e) {
//                        throw new UncheckedIOException(e);
//                    }
//                });
//            }
//
//            @Override
//            public List<String> listRoots() throws Exception {
//                return StreamSupport.stream(
//                                FileSystems.getDefault().getRootDirectories().spliterator(), false)
//                        .map(path -> path.toString())
//                        .toList();
//            }
        };
    }

    @Override
    public ShellProcessControl createControl() {
        return ProcessControlProvider.createLocal();
    }
}
