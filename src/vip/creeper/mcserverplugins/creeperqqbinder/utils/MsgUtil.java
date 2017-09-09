package vip.creeper.mcserverplugins.creeperqqbinder.utils;

import vip.creeper.mcserverplugins.creeperqqbinder.CreeperQqBinder;

import java.util.logging.Logger;

/**
 * Created by July_ on 2017/9/2.
 */
public class MsgUtil {
    private static Logger logger = CreeperQqBinder.getInstance().getLogger();

    public static void info(final String msg) {
        logger.info(msg);
    }

    public static void warring(final String msg) {
        logger.warning(msg);
    }
}
