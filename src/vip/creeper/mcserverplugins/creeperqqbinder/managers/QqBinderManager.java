package vip.creeper.mcserverplugins.creeperqqbinder.managers;

import vip.creeper.mcserverplugins.creeperqqbinder.CreeperQqBinder;
import vip.creeper.mcserverplugins.creeperqqbinder.Settings;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by July_ on 2017/9/2.
 */
public class QqBinderManager {
    private SqlManager sqlManager;
    private Settings settings;

    public QqBinderManager(final CreeperQqBinder plugin) {
        this.sqlManager = plugin.getSqlManager();
        this.settings = plugin.getSettings();
    }

    // 根据QQ获取玩家名
    public String getQqByPlayerName(final String playerName) {
        try {
            java.sql.PreparedStatement preparedStatement = sqlManager.getCon().prepareStatement("select qq from " + settings.getTablePrefix() + "playerdata" + " where player_name = '" + playerName.toLowerCase() + "'");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
               return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 根据玩家名获取QQ
    public String getPlayerNameByQq(final String qq) {
        try {
            java.sql.PreparedStatement preparedStatement = sqlManager.getCon().prepareStatement("select player_name from " + settings.getTablePrefix() + "playerdata" + " where qq = '" + qq + "'");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    // 添加绑定验证请求
    public boolean addBindingRequest(final String playerName, final String qq) {
        try {
            java.sql.PreparedStatement preparedStatement = sqlManager.getCon().prepareStatement("insert into " + settings.getTablePrefix() + "verification" + "(player_name, qq) values(?,?)");
            preparedStatement.setString(1, playerName.toLowerCase());
            preparedStatement.setString(2, qq);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // 判断是否存在绑定验证请求
    public boolean isExistsBindingRequest(final String playerName) {
        try {
            java.sql.PreparedStatement preparedStatement = sqlManager.getCon().prepareStatement("select * from " + settings.getTablePrefix() + "verification" + " where player_name = '" + playerName.toLowerCase() + "'");
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 删除绑定验证请求
    public boolean removeBindingRequest(final String playerName) {
        return sqlManager.executeStatement("delete from " + settings.getTablePrefix() + "verification" + " where player_name = '" + playerName.toLowerCase() + "'");
    }

    // 为玩家绑定QQ
    public boolean bindQqForPlayer(final String playerName, final String qq) {
        try {
            java.sql.PreparedStatement preparedStatement = sqlManager.getCon().prepareStatement("insert into " + settings.getTablePrefix() + "playerdata" + "(player_name, qq) values(?,?)");
            preparedStatement.setString(1, playerName.toLowerCase());
            preparedStatement.setString(2, qq);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // 是否绑定了QQ
    public boolean isBoundQq(final String playerName) {
        return getQqByPlayerName(playerName) != null;
    }
}
