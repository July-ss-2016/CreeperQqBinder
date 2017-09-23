package vip.creeper.mcserverplugins.creeperqqbinder;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import vip.creeper.mcserverplugins.creeperqqbinder.commands.OpCommand;
import vip.creeper.mcserverplugins.creeperqqbinder.commands.PlayerCommand;
import vip.creeper.mcserverplugins.creeperqqbinder.managers.QqBinderManager;
import vip.creeper.mcserverplugins.creeperqqbinder.managers.SqlManager;
import vip.creeper.mcserverplugins.creeperqqbinder.utils.MsgUtil;
import vip.creeper.mcserverplugins.creeperqqbinder.utils.SqlUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by July_ on 2017/9/2.
 */
public class CreeperQqBinder extends JavaPlugin {
    private static CreeperQqBinder instance;
    private Settings settings;
    private SqlManager sqlManager;
    private QqBinderManager qqBinderManager;
    private String[] aSubTableName = {"playerdata", "verification"};

    public void onEnable() {
        instance = this;
        this.settings = new Settings();
        this.qqBinderManager = new QqBinderManager(this);

        loadConfig();

        if (!initSql()) {
            setEnabled(false);
            return;
        }

        registerCommands();
        runTaskReConnectTask();
        MsgUtil.warring("插件初始化完毕!");
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        sqlManager.closeSql();
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
        settings.setTablePrefix(config.getString("mysql.table_prefix"));
    }

    // 初始化MySQL
    public boolean initSql() {
        try {
            for (String subTableName : aSubTableName) {
                if (!sqlManager.isExistsTable(settings.getTablePrefix() + subTableName)) {
                    if (!sqlManager.executeStatement("create table " + settings.getTablePrefix() + subTableName + "(player_name varchar(32), qq varchar(32))")) {
                        MsgUtil.warring(subTableName + " 表创建失败!");
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        this.sqlManager = new SqlManager(SqlUtil.getSqlConnection(settings.getHost(), settings.getPort(), settings.getDatabase(), settings.getUsername(), settings.getPassword())); // 建立sql连接，将con传递SqlManager使用

        if (sqlManager.getCon() == null) {
            MsgUtil.warring("MySQL连接失败.");
            return false;
        }

        return true;
    }

    private void runTaskReConnectTask() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> Bukkit.getScheduler().runTask(instance, () -> {
            Connection con = SqlUtil.getSqlConnection(settings.getHost(), settings.getPort(), settings.getDatabase(), settings.getUsername(), settings.getPassword());

            if (con == null) {
                MsgUtil.warring("MySQL重连失败.");
            } else {
                sqlManager.setCon(con);
                MsgUtil.info("MySQL重连成功.");
            }
        }), 432000L,432000L);
    }

    private void registerCommands() {
        getCommand("cqb").setExecutor(new PlayerCommand(this));
        getCommand("ocqb").setExecutor(new OpCommand(this));
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
}
