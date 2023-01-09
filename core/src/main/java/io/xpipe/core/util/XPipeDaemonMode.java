package io.xpipe.core.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public enum XPipeDaemonMode {
    @JsonProperty("background")
    BACKGROUND("background", List.of("base", "background")),

    @JsonProperty("tray")
    TRAY("tray", List.of("tray", "taskbar")),

    @JsonProperty("gui")
    GUI("gui", List.of("gui", "desktop", "interface"));

    public static XPipeDaemonMode get(String name) {
        return Arrays.stream(XPipeDaemonMode.values())
                .filter(xPipeDaemonMode ->
                        xPipeDaemonMode.getNameAlternatives().contains(name.toLowerCase(Locale.ROOT)))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Unknown mode: " + name + ". Possible values: "
                        + Arrays.stream(values())
                                .map(XPipeDaemonMode::getDisplayName)
                                .collect(Collectors.joining(", "))));
    }

    @Getter
    private final String displayName;

    @Getter
    private final List<String> nameAlternatives;

    XPipeDaemonMode(String displayName, List<String> nameAlternatives) {
        this.displayName = displayName;
        this.nameAlternatives = nameAlternatives;
    }
}
