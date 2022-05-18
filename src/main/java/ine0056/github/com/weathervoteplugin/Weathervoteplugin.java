package ine0056.github.com.weathervoteplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;


public final class Weathervoteplugin extends JavaPlugin {

    // 変数の型とアクセス範囲の最適化
    private int weatherChangeTask;
    private int timeChangeTask;
    private boolean voteSunStatus; // false: nonvote, true: voted
    private boolean voteDayStatus; // false: nonvote, true: voted
    private long voteSunCooldownTime;
    private long voteDayCooldownTime;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // InGameのプレイヤーの場合のみ実行
        if (command.getName().equalsIgnoreCase("mvote")) {
            if (sender instanceof Player player) {
                if (args.length == 0) {
                    return false;
                } else {
                    World world = Bukkit.getServer().getWorld("world");

                    switch (args[0]) { // Switch式による可読性の向上
                        case "sun" -> {
                            if (!voteSunStatus && checkCooldown(voteSunCooldownTime)) {
                                Bukkit.broadcastMessage(ChatColor.AQUA + "----------------------------------------");
                                Bukkit.broadcastMessage(MessageManager.format(getMessage(player, "system.vote.start"),
                                        getMessage(player, "system.vote.weather"),
                                        getMessage(player, "weather.sun"),
                                        "/mvote o sun"));
                                Bukkit.broadcastMessage(ChatColor.AQUA + "----------------------------------------");

                                voteSunStatus = true;
                                voteSunCooldownTime = System.currentTimeMillis();

                                weatherChangeTask = Bukkit.getScheduler().runTaskLater(this, () -> { //指定したTick後に処理を実行する
                                    world.setStorm(false);
                                    world.setThundering(false);
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------");
                                    Bukkit.broadcastMessage(ChatColor.AQUA + MessageManager.format(getMessage(player, "system.vote.changed"), getMessage(player, "system.vote.weather")));
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------");

                                    voteSunStatus = false;
                                }, 1200L).getTaskId();
                            } else if (voteSunStatus) {
                                sender.sendMessage(getMessage(player, "system.vote.started"));
                            } else if (!checkCooldown(voteSunCooldownTime)) {
                                sender.sendMessage(getMessage(player, "system.vote.cooldown"));
                            }
                        }

                        case "day" -> {
                            if (!voteDayStatus && checkCooldown(voteDayCooldownTime)) {
                                Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------------------------");
                                Bukkit.broadcastMessage(MessageManager.format(getMessage(player, "system.vote.start"),
                                        getMessage(player, "system.vote.time"),
                                        getMessage(player, "weather.day"),
                                        "/mvote o day"));
                                Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------------------------");

                                voteDayStatus = true;
                                voteDayCooldownTime = System.currentTimeMillis();

                                timeChangeTask = Bukkit.getScheduler().runTaskLater(this, () -> { //指定したTick後に処理を実行する
                                    world.setTime(1000);
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------");
                                    Bukkit.broadcastMessage(ChatColor.AQUA + MessageManager.format(getMessage(player, "system.vote.changed"), getMessage(player, "system.vote.time")));
                                    Bukkit.broadcastMessage(ChatColor.AQUA + "------------------------------");

                                    voteDayStatus = false;
                                }, 1200L).getTaskId();
                            } else if (voteDayStatus) {
                                sender.sendMessage(getMessage(player, "system.vote.started"));
                            } else if (!checkCooldown(voteDayCooldownTime)) {
                                sender.sendMessage(getMessage(player, "system.vote.cooldown"));
                            }
                        }

                        case "oppose", "o" -> {
                            if (args.length == 1) {
                                return false;
                            } else {
                                // キャンセル処理の統一化
                                if (!(args[1].equalsIgnoreCase("sun") || args[1].equalsIgnoreCase("day")))
                                    return false;

                                boolean isDay = args[1].equalsIgnoreCase("day");
                                if ((!isDay && !voteSunStatus) || (isDay && !voteDayStatus)) {
                                    sender.sendMessage(getMessage(player, "system.vote.notstarted"));
                                    return true;
                                }

                                Bukkit.broadcastMessage(ChatColor.GOLD + "--------------------------------------");
                                Bukkit.broadcastMessage(MessageManager.format(getMessage(player, "system.vote.start.cancel"),
                                        player.getName(),
                                        isDay ? getMessage(player, "system.vote.time") : getMessage(player, "system.vote.weather")));
                                Bukkit.broadcastMessage(ChatColor.GOLD + "--------------------------------------");

                                // 予約したタスクをキャンセルする
                                if (isDay) {
                                    Bukkit.getScheduler().cancelTask(timeChangeTask);
                                    voteDayStatus = false;
                                } else {
                                    Bukkit.getScheduler().cancelTask(weatherChangeTask);
                                    voteSunStatus = false;
                                }
                            }
                        }

                        case "voteinfo", "info" -> {
                            // 天気投票の情報表示
                            sender.sendMessage(MessageManager.format(getMessage(player, "system.vote.status"),
                                    getMessage(player, "system.vote.weather"),
                                    getMessage(player, !voteSunStatus && checkCooldown(voteSunCooldownTime) ? "system.vote.enable" : "system.vote.disable")));

                            // 時間投票の情報表示
                            sender.sendMessage(MessageManager.format(getMessage(player, "system.vote.status"),
                                    getMessage(player, "system.vote.time"),
                                    getMessage(player, !voteDayStatus && checkCooldown(voteDayCooldownTime) ? "system.vote.enable" : "system.vote.disable")));
                        }

                        case "version" -> {
                            sender.sendMessage(ChatColor.AQUA + "====== " + ChatColor.WHITE + "WeatherVotePlugin " + ChatColor.AQUA + "=====");
                            sender.sendMessage(ChatColor.AQUA + "Version : " + ChatColor.WHITE + getDescription().getVersion());
                            sender.sendMessage("");
                            sender.sendMessage(ChatColor.AQUA + "===========================");
                        }

                        default -> {
                            return false;
                        }
                    }

                    return true;
                }
            } else {
                sender.sendMessage("このコマンドはゲーム内で実行してください！");
                return true;
            }
        }
        return false;
    }

    /**
     * 前回の実行から5分が経過したかを確認します。
     *
     * @param time 最後に実行した時間
     * @return 5分が経過しているかどうか
     */
    private boolean checkCooldown(long time) {
        return (System.currentTimeMillis() - time) / 1000 >= 300;
    }

    private String getMessage(Player player, String index) {
        if (player.hasPermission("mofucraft.english"))
            return ChatColor.translateAlternateColorCodes('&', MessageManager.getMessage("en_US", index));
        return ChatColor.translateAlternateColorCodes('&', MessageManager.getMessage(index));
    }
}
