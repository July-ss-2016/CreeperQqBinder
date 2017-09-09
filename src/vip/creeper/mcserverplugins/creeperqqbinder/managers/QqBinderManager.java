package vip.creeper.mcserverplugins.creeperqqbinder.managers;

import com.mysql.jdbc.PreparedStatement;
import vip.creeper.mcserverplugins.creeperqqbinder.CreeperQqBinder;
import vip.creeper.mcserverplugins.creeperqqbinder.Settings;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by July_ on 2017/9/2.
 */
public class QqBinderManager {
    private CreeperQqBinder plugin;
    private SqlManager sqlManager;
    private Settings settings;

    public QqBinderManager(final CreeperQqBinder plugin) {
        this.plugin = plugin;
        this.sqlManager = plugin.getSqlManager();
        this.settings = plugin.getSettings();
    }
    public String getQqByName(final String playerName) {
        try {
            java.sql.PreparedStatement preparedStatement = sqlManager.getCon().prepareStatement("select qq from " + settings.getTableName() + " where player_name = '" + playerName + "'");
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void getNameByQq(final String qq) {

    }

    public void setQq(final String playerName, final String qq) {

    }
}
