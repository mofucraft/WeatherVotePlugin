package ine0056.github.com.weathervoteplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public final class Weathervoteplugin extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    private static int weatherchangetask;
    private static String votecomamnd;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // InGameのプレイヤーの場合のみ実行
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("mvote")) {
                if (args.length != 0) {

                    if (args[0].equalsIgnoreCase("day")||args[0].equalsIgnoreCase("sun")||args[0].equalsIgnoreCase("vote")) {
                        String pworld = player.getWorld().getName();
                        if ((votecomamnd == "nonvote") || (votecomamnd == null)) {
                            if (pworld == "world") {
                                Bukkit.broadcastMessage(ChatColor.AQUA + "1分後に天気を晴れにします。中断する場合は" + ChatColor.RED + "/mvote oppose " + ChatColor.AQUA + "と入力してください。");
                                votecomamnd = "voted";
                                World world = player.getWorld();
                        /*  1分後に以下のプログラムを実行
                            Bukkit.broadcastMessage("天候を変更しました");
                            String command = "weather clear";
                         */
                                weatherchangetask = Bukkit.getScheduler().runTaskLater(this, () -> { //指定したTick後に処理を実行する
                                    world.setStorm(false);
                                    world.setThundering(false);
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "天候を変更しました");
                                    votecomamnd = "nonvote";
                                }, 1200L).getTaskId();
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.RED + "このワールドでは天候を変更できません！");
                                sender.sendMessage("Player world is " + pworld);
                                return true;
                            }
                        } else if ((votecomamnd == "voted")) {
                            if (pworld == "world" ) {
                                sender.sendMessage(ChatColor.RED + "既に投票が開始済みです");
                                return true;
                            } else {
                                sender.sendMessage(ChatColor.RED + "すでに投票が開始済みかつこのワールドでは天候を変更できません！");
                                return true;
                            }
                        }
                    }

                    else if (args[0].equalsIgnoreCase("oppose")) {
                        if (votecomamnd == "voted") {
                            Bukkit.broadcastMessage(ChatColor.YELLOW + "反対があったため、天候の変更を中断しました");
                            //  タイマーを止める
                            Bukkit.getScheduler().cancelTask(weatherchangetask); //予約したタスクをキャンセルする
                            votecomamnd = "nonvote";
                        } else {
                          sender.sendMessage(ChatColor.RED + "投票が開始されていないため、このコマンドを実行できません。");
                        }
                        return true;
                    }

                    else if (args[0].equalsIgnoreCase("voteinfo")) {
                        if (votecomamnd == "voted") {
                            sender.sendMessage(ChatColor.DARK_AQUA + "現在投票は有効です");
                            return true;
                        }
                        else if (votecomamnd == "nonvote") {
                            sender.sendMessage(ChatColor.DARK_AQUA + "現在投票は無効です");
                            return true;
                        }
                    }
                    return false;
                }
            }
        } else {
            sender.sendMessage("このコマンドはゲーム内で実行してください！");
            return true;
        }
        return false;
    }
}
