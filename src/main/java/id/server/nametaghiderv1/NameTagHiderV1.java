
package id.server.nametaghiderv1;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class NameTagHiderV1 extends JavaPlugin implements Listener {

    private Scoreboard scoreboard;
    private Team hiddenTeam;

    @Override
    public void onEnable() {
        scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        hiddenTeam = scoreboard.getTeam("nthv1");

        if (hiddenTeam == null) {
            hiddenTeam = scoreboard.registerNewTeam("nthv1");
        }

        hiddenTeam.setOption(
                Team.Option.NAME_TAG_VISIBILITY,
                Team.OptionStatus.NEVER
        );

        Bukkit.getPluginManager().registerEvents(this, this);

        // Terapkan pengaturan secara berkala
        Bukkit.getScheduler().runTaskTimer(
                this,
                this::updateAllPlayers,
                1L,
                20L
        );

        getLogger().info("NameTagHiderV1 aktif!");
    }

    private void updateAllPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            hideNametag(player);
        }
    }

    private void hideNametag(Player player) {
        if (player.getScoreboard() != scoreboard) {
            player.setScoreboard(scoreboard);
        }

        Team currentTeam = scoreboard.getEntryTeam(player.getName());

        if (currentTeam != null && currentTeam != hiddenTeam) {
            currentTeam.removeEntry(player.getName());
        }

        hiddenTeam.addEntry(player.getName());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTaskLater(
                this,
                this::updateAllPlayers,
                1L
        );
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        Bukkit.getScheduler().runTaskLater(
                this,
                this::updateAllPlayers,
                1L
        );
    }
}
