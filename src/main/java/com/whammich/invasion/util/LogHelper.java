package com.whammich.invasion.util;

import com.whammich.invasion.ConfigHandler;
import com.whammich.invasion.Reference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper {

    private static Logger logger = LogManager.getLogger(Reference.NAME);

    public static void info(Object info) {
        if (ConfigHandler.enableLogging)
            logger.info(info);
    }

    public static void error(Object error) {
        if (ConfigHandler.enableLogging)
            logger.error(error);
    }

    public static void debug(Object debug) {
        if (ConfigHandler.enableLogging)
            logger.debug(debug);
    }

}
