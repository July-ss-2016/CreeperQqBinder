package vip.creeper.mcserverplugins.creeperqqbinder.utils;

/**
 * Created by July_ on 2017/9/9.
 */
public class Util {
    public static boolean isQqNum(final String str) {
        return str.matches("[1-9][0-9]{4,14}");
    }
}
