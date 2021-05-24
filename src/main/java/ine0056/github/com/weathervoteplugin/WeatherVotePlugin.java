package ine0056.github.com.weathervoteplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

enum VoteStatus {
    READY, VOTING, WAITING
}

class VoteController {
    // 0: OK, 1: Voting, 2: Waiting for 5 min
    private static final int TICK_PER_SEC = 20;
    private VoteStatus status;
    private final String broadcastMessage;
    private final String cancelMessage;
    private final Consumer<World> completedCommand;
    private int taskId;

    public VoteController(String broadcastMessage, String cancelMessage, Consumer<World> completedCommand) {
        this.status = VoteStatus.READY;
        this.broadcastMessage = broadcastMessage;
        this.cancelMessage = cancelMessage;
        this.completedCommand = completedCommand;
    }

    public void startVoting(@NotNull WeatherVotePlugin plugin, @NotNull CommandSender sender, @NotNull World world) {
        if (this.status != VoteStatus.READY) {
            var message = this.status == VoteStatus.VOTING
                    ? "既に投票が開始済みです"
                    : "前回の投票から5分経っていないため、投票を開始できません";
            sender.sendMessage(ChatColor.RED + message);
            return;
        }
        Bukkit.broadcastMessage(String.format(
                "%s1分後に%s建築ワールド%sの%sにします。中断する場合は%s/mvote o sun%sと入力してください。",
                ChatColor.AQUA, ChatColor.RED, ChatColor.AQUA, this.broadcastMessage, ChatColor.RED, ChatColor.AQUA)
        );
        this.status = VoteStatus.VOTING;
        this.taskId = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            this.status = VoteStatus.WAITING;
            this.completedCommand.accept(world);
        }, 60 * TICK_PER_SEC).getTaskId();
        Bukkit.getScheduler().runTaskLater(plugin, () -> this.status = VoteStatus.READY, 5 * 60 * TICK_PER_SEC).getTaskId();
    }

    public void cancelVoting(@NotNull CommandSender sender) {
        if (this.status == VoteStatus.VOTING) {
            Bukkit.broadcastMessage(ChatColor.YELLOW + "反対があったため、" + cancelMessage + "を中断しました");
            Bukkit.getScheduler().cancelTask(this.taskId);
        } else {
            sender.sendMessage(ChatColor.RED + "投票が開始されていないため、このコマンドを実行できません。");
        }
    }

    public VoteStatus getStatus() {
        return status;
    }
}

public final class WeatherVotePlugin extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        // Plugin startup logic
        sunController = new VoteController("天候を晴れ", "天候の変更", world -> {
            world.setStorm(false);
            world.setThundering(false);
            Bukkit.broadcastMessage(ChatColor.AQUA + "天候を変更しました");
        });
        dayController = new VoteController("時間を昼", "時間の変更", world -> {
            world.setTime(1000);
            Bukkit.broadcastMessage(ChatColor.AQUA + "時間を変更しました");
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private VoteController sunController;
    private VoteController dayController;



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, String[] args) {
        // InGameのプレイヤーの場合のみ実行
        if (!command.getName().equalsIgnoreCase("mvote")) return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはゲーム内で実行してください！");
            return true;
        }
        if (args.length == 0) {
            return false;
        }
        if (args[0].equalsIgnoreCase("oppose") || args[0].equalsIgnoreCase("o")) {
            if (args.length < 2) return false;
            if (args[1].equalsIgnoreCase("sun")) {
                sunController.cancelVoting(sender);
                return true;
            }
            if (args[1].equalsIgnoreCase("day")) {
                dayController.cancelVoting(sender);
                return true;
            }
            return false;
        }
        if (args[0].equalsIgnoreCase("voteinfo") || args[0].equalsIgnoreCase("info")) {
            var sunStatus = sunController.getStatus();
            var dayStatus = dayController.getStatus();
            if (sunStatus != VoteStatus.VOTING) {
                var message = String.format("天気：現在投票開始%sです", sunStatus == VoteStatus.READY ? "可能" : "無効");
                sender.sendMessage(ChatColor.DARK_AQUA + message);
            }
            if (dayStatus != VoteStatus.VOTING) {
                var message = String.format("時間：現在投票開始%sです", dayStatus == VoteStatus.READY ? "可能" : "無効");
                sender.sendMessage(ChatColor.DARK_AQUA + message);
            }
            return true;
        }
        World world = Bukkit.getServer().getWorld("world");
        assert world != null;
        if (args[0].equalsIgnoreCase("sun")) {
            sunController.startVoting(this, sender, world);
            return true;
        }
        if (args[0].equalsIgnoreCase("day")) {
            dayController.startVoting(this, sender, world);
            return true;
        }
        return false;
    }
}
