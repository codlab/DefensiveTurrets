//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dt.ajneb97.otros;

import dt.ajneb97.DefensiveTurrets;
import dt.ajneb97.Torreta;
import dt.ajneb97.TorretaConfig;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

public class TurretsGlobalCheck {
    int taskID;
    private DefensiveTurrets plugin;

    public TurretsGlobalCheck(DefensiveTurrets plugin) {
        this.plugin = plugin;
    }

    public void ejecutar(int time) {
        BukkitScheduler sh = Bukkit.getServer().getScheduler();
        this.taskID = sh.scheduleSyncRepeatingTask(this.plugin, new Runnable() {
            public void run() {
                TurretsGlobalCheck.this.checkearTorretas();
            }
        }, 0L, 20L * (long)time);
    }

    protected void checkearTorretas() {
        FileConfiguration messages = this.plugin.getMessages();
        FileConfiguration config = this.plugin.getConfig();
        ArrayList<Torreta> torretas = this.plugin.getTorretas();

        for(int i = 0; i < torretas.size(); ++i) {
            Location l1 = ((Torreta)torretas.get(i)).getBase().clone();
            Location l2 = ((Torreta)torretas.get(i)).getBase().clone().add(0.0D, 1.0D, 0.0D);
            Location l3 = ((Torreta)torretas.get(i)).getBase().clone().add(0.0D, 2.0D, 0.0D);
            int chunkX = l1.getBlockX() >> 4;
            int chunkZ = l1.getBlockZ() >> 4;
            if (l1.getWorld().isChunkLoaded(chunkX, chunkZ)
                    && (l1.getBlock() == null || l1.getBlock().getType().name().contains("AIR") || l2.getBlock() == null || l2.getBlock().getType().name().contains("AIR") || l3.getBlock() == null || l3.getBlock().getType().name().contains("AIR"))) {
                Player player = Bukkit.getPlayer(((Torreta)torretas.get(i)).getJugador());
                if (player != null) {
                    TorretaConfig configTorreta = this.plugin.getConfigTurret(((Torreta)torretas.get(i)).getTipo() + ".yml");
                    FileConfiguration tConfig = configTorreta.getConfig();
                    String name = tConfig.getString("Turret.name");
                    String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
                    player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretDestroyed").replace("%x_coord%", String.valueOf(((Torreta)torretas.get(i)).getBase().getBlockX())).replace("%y_coord%", String.valueOf(((Torreta)torretas.get(i)).getBase().getBlockY())).replace("%z_coord%", String.valueOf(((Torreta)torretas.get(i)).getBase().getBlockZ())).replace("%name%", name)));
                }

                Utilities.dropearBloqueTorreta(this.plugin, config, (Torreta)torretas.get(i));
                Utilidades.cerrarInventarioTorreta(this.plugin, (Torreta)torretas.get(i));
                this.plugin.eliminarTorreta(((Torreta)torretas.get(i)).getJugador(), l1);
                --i;
            }
        }

    }
}
