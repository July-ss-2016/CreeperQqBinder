package vip.creeper.mcserverplugins.creeperqqbinder.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import vip.creeper.mcserverplugins.creeperqqbinder.CreeperQqBinder;
import vip.creeper.mcserverplugins.creeperqqbinder.managers.QqBinderManager;
import vip.creeper.mcserverplugins.creeperqqbinder.utils.MsgUtil;
import vip.creeper.mcserverplugins.creeperqqbinder.utils.Util;

/**
 * Created by July_ on 2017/9/2.
 */
public class PlayerCommand implements CommandExecutor {
    private QqBinderManager qqBinderManager;

    public PlayerCommand(final CreeperQqBinder plugin) {
        this.qqBinderManager = plugin.getQqBinderManager();
    }

    public boolean onCommand(CommandSender cs, Command cmd, String lable, String[] args) {
        if (!cs.hasPermission("CreeperQqBinder.use")) {
            MsgUtil.sendMsg(cs, "无权限.");
            return true;
        }

        if (!(cs instanceof Player)) {
            MsgUtil.sendMsg(cs, "指令执行者必须为玩家.");
            return true;
        }

        Player player = (Player) cs;
        String playerName = player.getName();

        if (args.length == 2) {
            switch (args[0]) {
                case "bind":
                    // 已经绑定QQ
                    if (qqBinderManager.isBoundQq(playerName)) {
                        MsgUtil.sendMsg(player, "&c你的账户已经绑定了 &eQQ: " + qqBinderManager.getQqByPlayerName(playerName) + "&c.");
                        return true;
                    }

                    // 拒绝请求重复
                    if (!qqBinderManager.removeBindingRequest(playerName)) {
                        MsgUtil.sendMsg(player, "&c处理旧请求失败,请联系管理员!");
                        return true;
                    }

                    // 正则检测QQ是否合法
                    if (!Util.isQqNum(args[1])) {
                        MsgUtil.sendMsg(player, "&cQQ格式不合法,请检查其正确性!");
                        return true;
                    }

                    // 绑定QQ
                    if (qqBinderManager.addBindingRequest(playerName, args[1])) {
                        MsgUtil.sendMsg(player, "绑定请求已记录,使用 &e你的QQ: " + args[1] + "&b 将以下消息(不区分大小写,不含前缀)私聊发送给 &e群管理QQ: 3573826293 &b来完成绑定!");
                        MsgUtil.sendMsg(player, "&c#bd " + playerName);
                        return true;
                    }

                    MsgUtil.sendMsg(player,"&c绑定请求注册失败,请联系管理员!");
                    return true;
                case "unbind":


            }
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("info")) {
                String qq = qqBinderManager.getQqByPlayerName(playerName);

                if (qq != null) {
                    MsgUtil.sendMsg(player, "&d你已绑定 &eQQ: " + qq + "&d.");
                    return true;
                }

                MsgUtil.sendMsg(player, "&c你还没有绑定QQ.");
                return true;
            }
        }

        MsgUtil.sendMsg(player, "&f/cqb bind <qq> &7- &f绑定QQ");
        MsgUtil.sendMsg(player, "&f/cqb info &7- &f查看绑定状态");
        return false;
    }
}
