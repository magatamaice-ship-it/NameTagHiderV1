
package id.server.nametaghiderv1;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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

        for (Player player : Bukkit.getOnlinePlayers()) {
            hideNametag(player);
        }

        getLogger().info("NameTagHiderV1 aktif!");
    }

    private void hideNametag(Player player) {
        player.setScoreboard(scoreboard);

        Team currentTeam = scoreboard.getEntryTeam(player.getName());
        if (currentTeam != null && currentTeam != hiddenTeam) {
            currentTeam.removeEntry(player.getName());
        }

        hiddenTeam.addEntry(player.getName());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        hideNametag(event.getPlayer());
    }
}
