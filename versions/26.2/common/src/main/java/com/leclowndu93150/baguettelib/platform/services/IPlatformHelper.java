package com.leclowndu93150.baguettelib.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {
    String getPlatformName();
    boolean isModLoaded(String modId);
    boolean isDevelopmentEnvironment();

    Path getConfigDir();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
