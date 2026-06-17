package com.leclowndu93150.baguettelib;

import com.leclowndu93150.baguettelib.platform.Services;

public class CommonClass {
    public static void init() {
        Constants.LOG.info("BaguetteLib loaded on {}!", Services.PLATFORM.getPlatformName());
    }
}
