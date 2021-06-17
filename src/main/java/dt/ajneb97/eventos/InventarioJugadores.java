//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dt.ajneb97.eventos;

import dt.ajneb97.DefensiveTurrets;
import dt.ajneb97.JugadorInventario;
import dt.ajneb97.Torreta;
import dt.ajneb97.otros.Utilidades;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static dt.ajneb97.eventos.InventarioListener.from;
import static dt.ajneb97.eventos.InventarioListener.isOutdated;

public class InventarioJugadores implements Listener {
    public DefensiveTurrets plugin;

    public InventarioJugadores(DefensiveTurrets plugin) {
        this.plugin = plugin;
    }

    public static void crearInventario(Player jugador, Torreta torreta, DefensiveTurrets plugin) {
        FileConfiguration messages = plugin.getMessages();
        FileConfiguration config = plugin.getConfig();
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 45, ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretInventory")));
        ItemStack item = null;
        if (isOutdated()) {
            item = new ItemStack(from(160), 1, (short)7);
        } else {
            item = new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"));
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);

        int i;
        for(i = 0; i <= 8; ++i) {
            inv.setItem(i, item);
        }

        for(i = 36; i <= 44; ++i) {
            inv.setItem(i, item);
        }

        inv.setItem(9, item);
        inv.setItem(18, item);
        inv.setItem(26, item);
        inv.setItem(17, item);
        inv.setItem(27, item);
        inv.setItem(35, item);
        List<String> jugadores = torreta.getJugadores();
        List<String> lore = messages.getStringList("Messages.turretOptionPlayersPlayerLore");

        int pos;
        for(pos = 0; pos < lore.size(); ++pos) {
            lore.set(pos, ChatColor.translateAlternateColorCodes('&', (String)lore.get(pos)));
        }

        pos = 10;

        for(i = 0; i < jugadores.size(); ++i) {
            if (pos == 17 || pos == 26) {
                pos += 2;
            }

            if (pos == 35) {
                break;
            }

            if (isOutdated()) {
                item = new ItemStack(from(397), 1, (short)3);
            } else {
                item = new ItemStack(Material.valueOf("PLAYER_HEAD"));
            }

            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&e" + (String)jugadores.get(i)));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(pos, item);
            ++pos;
        }

        item = Utilidades.crearItem(config, "Config.inventory_back_item");
        inv.setItem(36, item);
        item = new ItemStack(Material.PAPER, 1);
        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionPlayersAddPlayer")));
        item.setItemMeta(meta);
        inv.setItem(40, item);
        jugador.openInventory(inv);
        plugin.agregarJugadorInventario(jugador, torreta, "players");
    }

    @EventHandler
    public void agregarJugador(AsyncPlayerChatEvent event) {
        final Player jugador = event.getPlayer();
        String name = ChatColor.stripColor(event.getMessage());
        JugadorInventario jugadorInventario = this.plugin.getJugadorInventario(jugador);
        if (jugadorInventario != null && jugadorInventario.getInventario().equals("adding_player")) {
            FileConfiguration messages = this.plugin.getMessages();
            String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
            event.setCancelled(true);
            if (!name.contains(" ")) {
                if (name.equalsIgnoreCase(jugador.getName())) {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionAddPlayerOwnName")));
                    return;
                }

                final Torreta torreta = jugadorInventario.getTorreta();
                List<String> jugadores = torreta.getJugadores();
                if (jugadores.contains(name)) {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionAddPlayerAlreadyAdded")));
                } else {
                    torreta.agregarJugador(name);
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionAddPlayerSuccess").replace("%name%", name)));
                    this.plugin.eliminarJugadorInventario(jugador);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                        public void run() {
                            InventarioJugadores.crearInventario(jugador, torreta, InventarioJugadores.this.plugin);
                        }
                    }, 2L);
                }
            } else {
                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionAddPlayerNotValid")));
            }
        }

    }
}
