package vip.creeper.mcserverplugins.creeperqqbinder;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import vip.creeper.mcserverplugins.creeperqqbinder.managers.QqBinderManager;
import vip.creeper.mcserverplugins.creeperqqbinder.managers.SqlManager;
import vip.creeper.mcserverplugins.creeperqqbinder.utils.MsgUtil;
import vip.creeper.mcserverplugins.creeperqqbinder.utils.SqlUtil;

import java.sql.SQLException;

/**
 * Created by July_ on 2017/9/2.
 */
public class CreeperQqBinder extends JavaPlugin {
    private static CreeperQqBinder instance;
    private Settings settings;
    private SqlManager sqlManager;
    private QqBinderManager qqBinderManager;

    public void onEnable() {
        instance = this;
        this.settings = new Settings();
        this.sqlManager = new SqlManager(SqlUtil.getSqlConnection(settings.getHost(), settings.getPort(), settings.getDatabase(), settings.getUsername(), settings.getPassword())); // 建立sql连接，将con传递SqlManager使用
        this.qqBinderManager = new QqBinderManager(this);

        if (sqlManager.getCon() == null) {
            MsgUtil.info("MySQL连接失败!");
            setEnabled(false);
        }
    }

    public static CreeperQqBinder getInstance() {
        return instance;
    }

    public SqlManager getSqlManager() {
        return sqlManager;
    }

    public QqBinderManager getQqBinderManager() {
        return qqBinderManager;
    }

    public Settings getSettings() {
        return settings;
    }

    public void initSql() {
        try {
            if (!sqlManager.isExistsTable(settings.getTableName())) {

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig() {
        FileConfiguration config = getConfig();

        saveDefaultConfig();
        reloadConfig();
        settings.setHost(config.getString("mysql.host"));
        settings.setPort(config.getInt("mysql.port"));
        settings.setDatabase(config.getString("mysql.database"));
        settings.setUsername(config.getString("mysql.username"));
        settings.setPassword(config.getString("mysql.password"));
        settings.setTableName(config.getString("mysql.table_prefix"));
    }
}
