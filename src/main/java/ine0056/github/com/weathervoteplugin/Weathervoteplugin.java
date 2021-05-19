package ine0056.github.com.weathervoteplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Timer;
import java.util.TimerTask;


public final class Weathervoteplugin extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    Timer timer = new Timer();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("weathervote")) { // サブコマンドなしの場合の動作
            if (args.length == 0) {
                sender.sendMessage("使い方:/weathervote < vote | oppose >");
                return true;
            }
            else {
                if (args[0].equalsIgnoreCase("vote")) {
                    Bukkit.broadcastMessage("1分後に天気を晴れにします。中断する場合は/weathervote oppose と入力してください。");
                    TimerTask task = new TimerTask() {
                        public void run() {
                            Bukkit.broadcastMessage("天候を変更しました");
                            String command = "weather clear";
                        }
                    };
                    timer.schedule(task, 60000); // 1分後にrun内を作動させる
                }
                else if (args[0].equalsIgnoreCase("oppose")) {
                    Bukkit.broadcastMessage("反対があったため、天候の変更を中断しました");
                    timer.cancel();
                }
            }
            return true;
        }
        return false;
    }
}
