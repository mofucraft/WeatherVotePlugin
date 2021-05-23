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
    private static String fivemintask;
    private static int fivemintasktask;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // InGameのプレイヤーの場合のみ実行
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("mvote")) {
                if (args.length != 0) {

                    if (args[0].equalsIgnoreCase("day") || args[0].equalsIgnoreCase("sun") || args[0].equalsIgnoreCase("vote")) {
                        if ((fivemintask == "ok") || (fivemintask == null)) {
                            if ((votecomamnd == "nonvote") || (votecomamnd == null)) {
                                Bukkit.broadcastMessage(ChatColor.AQUA + "1分後に" + ChatColor.RED + "建築ワールド" + ChatColor.AQUA + "の天気を晴れにします。中断する場合は" + ChatColor.RED + "/mvote oppose " + ChatColor.AQUA + "と入力してください。");
                                votecomamnd = "voted";
                                fivemintask = "no";
                                World world = Bukkit.getServer().getWorld("world");
                                /*  1分後に以下のプログラムを実行
                                    Bukkit.broadcastMessage("天候を変更しました");
                                    world のみ天候を晴れに変更
                                */
                                weatherchangetask = Bukkit.getScheduler().runTaskLater(this, () -> { //指定したTick後に処理を実行する
                                    world.setStorm(false);
                                    world.setThundering(false);
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "天候を変更しました");
                                    votecomamnd = "nonvote";
                                }, 1200L).getTaskId();
                                fivemintasktask = Bukkit.getScheduler().runTaskLater(this, () -> {
                                    fivemintask = "ok";
                                }, 6000L).getTaskId();
                                return true;
                            }
                        } else if ((fivemintask == "no") && (votecomamnd == "voted")) {
                            sender.sendMessage(ChatColor.RED + "既に投票が開始済みです");
                            return true;
                        } else if ((fivemintask == "no") && (votecomamnd == "nonvote")) {
                            sender.sendMessage(ChatColor.RED + "前回の投票から5分経っていないため、投票を開始できません");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("oppose")) {
                        if (votecomamnd == "voted") {
                            Bukkit.broadcastMessage(ChatColor.YELLOW + "反対があったため、天候の変更を中断しました");
                            //  タイマーを止める
                            Bukkit.getScheduler().cancelTask(weatherchangetask); //予約したタスクをキャンセルする
                            votecomamnd = "nonvote";
                        } else {
                            sender.sendMessage(ChatColor.RED + "投票が開始されていないため、このコマンドを実行できません。");
                        }
                        return true;
                    } else if (args[0].equalsIgnoreCase("voteinfo")) {
                        if ((votecomamnd == "nonvote") && (fivemintask == "ok")) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "現在投票可能です");
                            return true;
                        } else if ((votecomamnd == "voted") && (fivemintask == "ok")) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "例外：これが表示されたらおかしいよ（運営に連絡してください）");
                            return true;
                        } else if ((votecomamnd == "nonvote") && (fivemintask == "no")) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "現在投票を開始できません");
                            return true;
                        } else if ((votecomamnd == "voted") && (fivemintask == "no")) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "現在投票中です");
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
