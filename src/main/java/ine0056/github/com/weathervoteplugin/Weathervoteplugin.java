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
    private static String sunvotecomamnd;
    private static String sunfivemintask;
    private static int sunfivemintasktask;
    private static String dayfivemintask;
    private static String dayvotecomamnd;
    private static int timechangetask;
    private static int dayfivemintasktask;
    private static String playerrr;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // InGameのプレイヤーの場合のみ実行
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("mvote")) {
                if (args.length != 0) {


                    if (args[0].equalsIgnoreCase("sun")) {
                        if ((sunfivemintask == "ok") || (sunfivemintask == null)) {
                            if ((sunvotecomamnd == "nonvote") || (sunvotecomamnd == null)) {
                                Bukkit.broadcastMessage(ChatColor.AQUA + "----------------------------------------");
                                Bukkit.broadcastMessage(ChatColor.AQUA + "1分後に" + ChatColor.RED + "建築ワールド" + ChatColor.AQUA + "の天気を晴れにします。中断する場合は" + ChatColor.RED + "/mvote o sun" + ChatColor.AQUA + "と入力してください。");
                                Bukkit.broadcastMessage(ChatColor.AQUA + "----------------------------------------");
                                sunvotecomamnd = "voted";
                                sunfivemintask = "no";
                                World world = Bukkit.getServer().getWorld("world");
                                /*  1分後に以下のプログラムを実行
                                    Bukkit.broadcastMessage("天候を変更しました");
                                    world のみ天候を晴れに変更
                                */
                                weatherchangetask = Bukkit.getScheduler().runTaskLater(this, () -> { //指定したTick後に処理を実行する
                                    world.setStorm(false);
                                    world.setThundering(false);
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------");
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "天候を変更しました");
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------");
                                    sunvotecomamnd = "nonvote";
                                }, 1200L).getTaskId();
                                sunfivemintasktask = Bukkit.getScheduler().runTaskLater(this, () -> {
                                    sunfivemintask = "ok";
                                }, 6000L).getTaskId();
                                return true;
                            }
                        } else if ((sunfivemintask == "no") && (sunvotecomamnd == "voted")) {
                            sender.sendMessage(ChatColor.RED + "既に投票が開始済みです");
                            return true;
                        } else if ((sunfivemintask == "no") && (sunvotecomamnd == "nonvote")) {
                            sender.sendMessage(ChatColor.RED + "前回の投票から5分経っていないため、投票を開始できません");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("day")) {
                        if ((dayfivemintask == "ok") || (dayfivemintask == null)) {
                            if ((dayvotecomamnd == "nonvote") || (dayvotecomamnd == null)) {
                                Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------------------------");
                                Bukkit.broadcastMessage(ChatColor.AQUA + "1分後に" + ChatColor.RED + "建築ワールド" + ChatColor.AQUA + "の時間を昼にします。中断する場合は" + ChatColor.RED + "/mvote o day" + ChatColor.AQUA + "と入力してください。");
                                Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------------------------");
                                dayvotecomamnd = "voted";
                                dayfivemintask = "no";
                                World world = Bukkit.getServer().getWorld("world");
                                /*  1分後に以下のプログラムを実行
                                    Bukkit.broadcastMessage("天候を変更しました");
                                    world のみ天候を晴れに変更
                                */
                                timechangetask = Bukkit.getScheduler().runTaskLater(this, () -> { //指定したTick後に処理を実行する
                                    world.setTime(1000);
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------");
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "時間を変更しました");
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------");
                                    dayvotecomamnd = "nonvote";
                                }, 1200L).getTaskId();
                                dayfivemintasktask = Bukkit.getScheduler().runTaskLater(this, () -> {
                                    dayfivemintask = "ok";
                                }, 6000L).getTaskId();
                                return true;
                            }
                        } else if ((dayfivemintask == "no") && (dayvotecomamnd == "voted")) {
                            sender.sendMessage(ChatColor.RED + "既に投票が開始済みです");
                            return true;
                        } else if ((dayfivemintask == "no") && (dayvotecomamnd == "nonvote")) {
                            sender.sendMessage(ChatColor.RED + "前回の投票から5分経っていないため、投票を開始できません");
                            return true;
                        }
                    } else if (args[0].equalsIgnoreCase("oppose") || args[0].equalsIgnoreCase("o")) {

                        if (args.length == 1) {
                            return false;
                        } else {
                            if (args[1].equalsIgnoreCase("sun")) {
                                if (sunvotecomamnd == "voted") {
                                    playerrr = ((Player) sender).getPlayer().getName();
                                    Bukkit.broadcastMessage(ChatColor.GOLD + "--------------------------------------");
                                    Bukkit.broadcastMessage(ChatColor.AQUA + playerrr + ChatColor.YELLOW + "さんの" + ChatColor.GOLD + "反対" + ChatColor.YELLOW + "により、" + ChatColor.GOLD + "天候" + ChatColor.YELLOW + "の変更を中断しました");
                                    Bukkit.broadcastMessage(ChatColor.GOLD + "--------------------------------------");
                                    //  タイマーを止める
                                    Bukkit.getScheduler().cancelTask(weatherchangetask); //予約したタスクをキャンセルする
                                    sunvotecomamnd = "nonvote";
                                } else {
                                    sender.sendMessage(ChatColor.RED + "投票が開始されていないため、このコマンドを実行できません。");
                                }
                                return true;
                            } else if (args[1].equalsIgnoreCase("day")) {
                                if (dayvotecomamnd == "voted") {
                                    playerrr = ((Player) sender).getPlayer().getName();
                                    Bukkit.broadcastMessage(ChatColor.GOLD + "--------------------------------------");
                                    Bukkit.broadcastMessage(ChatColor.AQUA + playerrr + ChatColor.YELLOW + "さんの" + ChatColor.GOLD + "反対" + ChatColor.YELLOW + "により、" + ChatColor.GOLD + "時間" + ChatColor.YELLOW + "の変更を中断しました");
                                    Bukkit.broadcastMessage(ChatColor.GOLD + "--------------------------------------");
                                    //  タイマーを止める
                                    Bukkit.getScheduler().cancelTask(timechangetask); //予約したタスクをキャンセルする
                                    dayvotecomamnd = "nonvote";
                                } else {
                                    sender.sendMessage(ChatColor.RED + "投票が開始されていないため、このコマンドを実行できません。");
                                }
                                return true;
                            }
                        }
                    }


                    else if (args[0].equalsIgnoreCase("voteinfo") || args[0].equalsIgnoreCase("info")) {
                        if (((sunvotecomamnd == "nonvote") || (sunvotecomamnd == null)) && ((sunfivemintask == "ok") || (sunfivemintask == null))) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "天気：現在投票開始" + ChatColor.BLUE + "可能"+ ChatColor.DARK_AQUA + "です");
                            if (((dayvotecomamnd == "nonvote") || (dayvotecomamnd == null)) && ((dayfivemintask == "ok") || (dayfivemintask == null))) {
                                sender.sendMessage(ChatColor.DARK_AQUA + "時間：現在投票開始" + ChatColor.BLUE + "可能"+ ChatColor.DARK_AQUA + "です");
                                return true;
                            } else if (dayvotecomamnd == "voted" || dayfivemintask == "no") {
                                sender.sendMessage(ChatColor.DARK_AQUA + "時間：現在投票開始" + ChatColor.RED + "無効" + ChatColor.DARK_AQUA + "です");
                                return true;
                            }
                            return false;
                        } else if ((sunvotecomamnd == "voted") || (sunfivemintask == "no")) {
                            sender.sendMessage(ChatColor.DARK_AQUA + "天気：現在投票開始" + ChatColor.RED + "無効" + ChatColor.DARK_AQUA + "です");
                            if (((dayvotecomamnd == "nonvote") || (dayvotecomamnd == null)) && ((dayfivemintask == "ok") || (dayfivemintask == null))) {
                                sender.sendMessage(ChatColor.DARK_AQUA + "時間：現在投票開始" + ChatColor.BLUE + "可能"+ ChatColor.DARK_AQUA + "です");
                                return true;
                            } else if (dayvotecomamnd == "voted" || dayfivemintask == "no") {
                                sender.sendMessage(ChatColor.DARK_AQUA + "時間：現在投票開始" + ChatColor.RED + "無効" + ChatColor.DARK_AQUA + "です");
                                return true;
                            }
                        }
                        return false;
                    }


                    else if (args[0].equalsIgnoreCase("version")) {
                        sender.sendMessage(ChatColor.AQUA + "====== " + ChatColor.WHITE + "WeatherVotePlugin " + ChatColor.AQUA + "=====");
                        sender.sendMessage(ChatColor.AQUA + "Version : " + ChatColor.WHITE + "1.0.4.1");
                        sender.sendMessage("");
                        sender.sendMessage(ChatColor.AQUA + "===========================");
                        return true;
                    }


                }
            } else {
                sender.sendMessage("このコマンドはゲーム内で実行してください！");
                return true;
            }
        }
        return false;
    }
}
