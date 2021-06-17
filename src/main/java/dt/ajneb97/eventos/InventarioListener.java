//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dt.ajneb97.eventos;

import dt.ajneb97.DefensiveTurrets;
import dt.ajneb97.JugadorInventario;
import dt.ajneb97.Torreta;
import dt.ajneb97.TorretaConfig;
import dt.ajneb97.otros.Utilidades;
import dt.ajneb97.otros.Utilities;
import dt.ajneb97.torretas.Burst;
import dt.ajneb97.torretas.Healing;
import dt.ajneb97.torretas.Laser;
import dt.ajneb97.torretas.Siege;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventarioListener implements Listener {
    public DefensiveTurrets plugin;

    public InventarioListener(DefensiveTurrets plugin) {
        this.plugin = plugin;
    }

    public static void crearInventarioOpciones(Player jugador, Torreta torreta, DefensiveTurrets plugin) {
        FileConfiguration messages = plugin.getMessages();
        FileConfiguration config = plugin.getConfig();
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 45, ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretInventory")));
        ItemStack item = null;

        //new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"), 160, 1, (short) 7);
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
        if (isOutdated()) {
            item = new ItemStack(from(160), 1, (short)5);
        } else {
            item = new ItemStack(Material.valueOf("LIME_STAINED_GLASS_PANE"));
        }

        meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        inv.setItem(10, item);
        inv.setItem(28, item);
        inv.setItem(16, item);
        inv.setItem(34, item);
        ItemStack opcionEnabled = null;
        ItemStack opcionDisabled = null;
        if (isOutdated()) {
            opcionEnabled = new ItemStack(from(159), 1, (short)5);
            opcionDisabled = new ItemStack(from(159), 1, (short)14);
        } else {
            opcionEnabled = new ItemStack(Material.valueOf("GREEN_TERRACOTTA"));
            opcionDisabled = new ItemStack(Material.valueOf("RED_TERRACOTTA"));
        }

        String optionAttackMonsters = "";
        String optionAttackPlayers = "";
        String optionAttackAnimals = "";
        if (torreta instanceof Healing) {
            optionAttackMonsters = messages.getString("Messages.turretOptionHealMonsters");
            optionAttackPlayers = messages.getString("Messages.turretOptionHealPlayers");
            optionAttackAnimals = messages.getString("Messages.turretOptionHealAnimals");
        } else {
            optionAttackMonsters = messages.getString("Messages.turretOptionAttackMonsters");
            optionAttackPlayers = messages.getString("Messages.turretOptionAttackPlayers");
            optionAttackAnimals = messages.getString("Messages.turretOptionAttackAnimals");
        }

        ArrayList lore;
        if (torreta.isAtacaMonstruos()) {
            meta = opcionEnabled.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', optionAttackMonsters));
            lore = new ArrayList();
            lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionEnabled")));
            meta.setLore(lore);
            opcionEnabled.setItemMeta(meta);
            inv.setItem(21, opcionEnabled);
        } else {
            meta = opcionDisabled.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', optionAttackMonsters));
            lore = new ArrayList();
            lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionDisabled")));
            meta.setLore(lore);
            opcionDisabled.setItemMeta(meta);
            inv.setItem(21, opcionDisabled);
        }

        if (torreta.isAtacaJugadores()) {
            meta = opcionEnabled.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', optionAttackPlayers));
            lore = new ArrayList();
            lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionEnabled")));
            meta.setLore(lore);
            opcionEnabled.setItemMeta(meta);
            inv.setItem(22, opcionEnabled);
        } else {
            meta = opcionDisabled.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', optionAttackPlayers));
            lore = new ArrayList();
            lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionDisabled")));
            meta.setLore(lore);
            opcionDisabled.setItemMeta(meta);
            inv.setItem(22, opcionDisabled);
        }

        if (torreta.isAtacaAnimales()) {
            meta = opcionEnabled.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', optionAttackAnimals));
            lore = new ArrayList();
            lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionEnabled")));
            meta.setLore(lore);
            opcionEnabled.setItemMeta(meta);
            inv.setItem(23, opcionEnabled);
        } else {
            meta = opcionDisabled.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', optionAttackAnimals));
            lore = new ArrayList();
            lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionDisabled")));
            meta.setLore(lore);
            opcionDisabled.setItemMeta(meta);
            inv.setItem(23, opcionDisabled);
        }

        item = Utilities.crearItem(config, "Config.inventory_back_item");
        inv.setItem(36, item);
        if (isOutdated()) {
            item = new ItemStack(from(397), 1, (short)3);
        } else {
            item = new ItemStack(Material.valueOf("PLAYER_HEAD"));
        }

        meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionPlayers")));
        lore = new ArrayList();
        List<String> jugadores = torreta.getJugadores();
        if (jugadores.isEmpty()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionPlayersListNone")));
        } else {
            for(i = 0; i < jugadores.size(); ++i) {
                lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionPlayersList").replace("%name%", (CharSequence)jugadores.get(i))));
            }
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(31, item);
        if (torreta.estaActivada()) {
            if (isOutdated()) {
                item = new ItemStack(from(351), 1, (short)10);
            } else {
                item = new ItemStack(Material.valueOf("LIME_DYE"));
            }

            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretEnabled")));
        } else {
            if (isOutdated()) {
                item = new ItemStack(from(351), 1, (short)8);
            } else {
                item = new ItemStack(Material.valueOf("GRAY_DYE"));
            }

            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretDisabled")));
        }

        lore = new ArrayList();
        lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretStatusMessage")));
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(8, item);
        if (config.getString("Config.turrets_require_ammunition").equals("true")) {
            item = new ItemStack(Material.DISPENSER);
            meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionAmmunitionStorage")));
            lore = new ArrayList();
            String ammo = String.valueOf(torreta.getAmmo());
            if (torreta.tieneAmmoInfinita()) {
                ammo = "∞";
            }

            lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionAmmunitionCurrentAmmo").replace("%ammo%", ammo)));
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(44, item);
        }

        jugador.openInventory(inv);
        plugin.agregarJugadorInventario(jugador, torreta, "options");
    }

    public static void crearInventarioUpgrade(Player jugador, Torreta torreta, DefensiveTurrets plugin) {
        FileConfiguration messages = plugin.getMessages();
        FileConfiguration config = plugin.getConfig();
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretInventory")));
        TorretaConfig configTorreta = plugin.getConfigTurret(torreta.getTipo() + ".yml");
        FileConfiguration tConfig = configTorreta.getConfig();
        ItemStack item = null;
        if (isOutdated()) {
            item = new ItemStack(from(160), 1, (short)7);
        } else {
            item = new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"));
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);

        int level;
        for(level = 0; level <= 7; ++level) {
            inv.setItem(level, item);
        }

        for(level = 18; level <= 26; ++level) {
            inv.setItem(level, item);
        }

        inv.setItem(9, item);
        inv.setItem(17, item);
        if (isOutdated()) {
            item = new ItemStack(from(160), 1, (short)5);
        } else {
            item = new ItemStack(Material.valueOf("LIME_STAINED_GLASS_PANE"));
        }

        meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        inv.setItem(10, item);
        inv.setItem(16, item);
        if (isOutdated()) {
            item = new ItemStack(from(160), 1);
        } else {
            item = new ItemStack(Material.valueOf("WHITE_STAINED_GLASS_PANE"));
        }

        meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);
        inv.setItem(11, item);
        inv.setItem(12, item);
        inv.setItem(14, item);
        inv.setItem(15, item);
        item = Utilities.crearItem(config, "Config.turret_options_item");
        inv.setItem(8, item);
        level = torreta.getLevel();
        String lineaUpgrade = Utilidades.getStatsSegunNivel(torreta.getTipo(), level + 1, plugin);
        if (lineaUpgrade.equals("error")) {
            item = Utilities.crearItem(tConfig, "LastUpgrade");
            meta = item.getItemMeta();
            String currentLevel = Utilidades.getFormatoLevel(level, Utilidades.getUpgradeSize(torreta.getTipo(), plugin));
            List<String> lore = meta.getLore();

            for(int i = 0; i < lore.size(); ++i) {
                lore.set(i, ChatColor.translateAlternateColorCodes('&', ((String)lore.get(i)).replace("%current_level%", currentLevel).replace("%current_min%", String.valueOf(torreta.getMinDamage())).replace("%current_max%", String.valueOf(torreta.getMaxDamage())).replace("%current_cooldown%", String.valueOf(torreta.getCooldown())).replace("%current_range%", String.valueOf(torreta.getRango()))));
                if (torreta.getTipo().equals("Laser")) {
                    Laser laser = (Laser)torreta;
                    lore.set(i, ChatColor.translateAlternateColorCodes('&', ((String)lore.get(i)).replace("%current_power%", String.valueOf(laser.getEmpuje()))));
                } else if (torreta.getTipo().equals("Siege")) {
                    Siege siege = (Siege)torreta;
                    lore.set(i, ChatColor.translateAlternateColorCodes('&', ((String)lore.get(i)).replace("%current_radius%", String.valueOf(siege.getRadio()))));
                } else if (torreta.getTipo().equals("Healing")) {
                    Healing healing = (Healing)torreta;
                    lore.set(i, ChatColor.translateAlternateColorCodes('&', ((String)lore.get(i)).replace("%current_targets%", String.valueOf(healing.getMaxTargets()))));
                }
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(13, item);
        } else {
            item = Utilities.crearItem(tConfig, "Upgrade");
            meta = item.getItemMeta();
            String[] nextUpgrade = lineaUpgrade.split(";");
            String[] damage = nextUpgrade[1].split("-");
            double minDamage = Double.valueOf(damage[0]);
            double maxDamage = Double.valueOf(damage[1]);
            double cooldown = Double.valueOf(nextUpgrade[2]);
            double range = Double.valueOf(nextUpgrade[3]);
            String price = nextUpgrade[0];
            String currentLevel = Utilidades.getFormatoLevel(level, Utilidades.getUpgradeSize(torreta.getTipo(), plugin));
            String nextLevel = Utilidades.getFormatoLevel(level + 1, Utilidades.getUpgradeSize(torreta.getTipo(), plugin));
            List<String> lore = meta.getLore();

            for(int i = 0; i < lore.size(); ++i) {
                lore.set(i, ChatColor.translateAlternateColorCodes('&', ((String)lore.get(i)).replace("%current_level%", currentLevel).replace("%current_min%", String.valueOf(torreta.getMinDamage())).replace("%current_max%", String.valueOf(torreta.getMaxDamage())).replace("%current_cooldown%", String.valueOf(torreta.getCooldown())).replace("%current_range%", String.valueOf(torreta.getRango())).replace("%next_level%", nextLevel).replace("%next_min%", String.valueOf(minDamage)).replace("%next_max%", String.valueOf(maxDamage)).replace("%next_cooldown%", String.valueOf(cooldown)).replace("%next_range%", String.valueOf(range)).replace("%price%", price)));
                double radio;
                if (torreta.getTipo().equals("Laser")) {
                    Laser laser = (Laser)torreta;
                    radio = Double.valueOf(nextUpgrade[4]);
                    lore.set(i, ChatColor.translateAlternateColorCodes('&', ((String)lore.get(i)).replace("%current_power%", String.valueOf(laser.getEmpuje())).replace("%next_power%", String.valueOf(radio))));
                } else if (torreta.getTipo().equals("Siege")) {
                    Siege siege = (Siege)torreta;
                    radio = Double.valueOf(nextUpgrade[4]);
                    lore.set(i, ChatColor.translateAlternateColorCodes('&', ((String)lore.get(i)).replace("%current_radius%", String.valueOf(siege.getRadio())).replace("%next_radius%", String.valueOf(radio))));
                } else if (torreta.getTipo().equals("Healing")) {
                    Healing healing = (Healing)torreta;
                    int maxTargets = Integer.valueOf(nextUpgrade[4]);
                    lore.set(i, ChatColor.translateAlternateColorCodes('&', ((String)lore.get(i)).replace("%current_targets%", String.valueOf(healing.getMaxTargets())).replace("%next_targets%", String.valueOf(maxTargets))));
                }
            }

            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(13, item);
        }

        item = Utilities.crearItem(config, "Config.delete_turret_item");
        inv.setItem(26, item);
        jugador.openInventory(inv);
        plugin.agregarJugadorInventario(jugador, torreta, "main");
    }

    @EventHandler
    public void alSalir(PlayerQuitEvent event) {
        Player jugador = event.getPlayer();
        this.plugin.eliminarJugadorInventario(jugador);
    }

    @EventHandler
    public void alCerrarInventario(InventoryCloseEvent event) {
        Player jugador = (Player)event.getPlayer();
        JugadorInventario jugadorInventario = this.plugin.getJugadorInventario(jugador);
        if (jugadorInventario != null && jugadorInventario.getInventario().equals("ammo")) {
            Inventory inv = jugador.getOpenInventory().getTopInventory();
            int ammo = 0;

            for(int i = 0; i <= 35; ++i) {
                ItemStack item = inv.getItem(i);
                if (item != null && !item.getType().equals(Material.AIR)) {
                    ammo += item.getAmount();
                }
            }

            jugadorInventario.getTorreta().setAmmo(ammo);
        }

        this.plugin.eliminarJugadorInventario(jugador);
    }

    @EventHandler
    public void clickearInventario(InventoryClickEvent event) {
        Player jugador = (Player)event.getWhoClicked();
        JugadorInventario jugadorInventario = this.plugin.getJugadorInventario(jugador);
        if (jugadorInventario != null) {
            String optionAttackPlayers;
            ItemStack itemStack;
            FileConfiguration fileConfiguration;
            ItemMeta itemMeta;
            String optionAttackMonsters;
            if (jugadorInventario.getInventario().equals("ammo")) {
                if (event.getClickedInventory() != null && event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
                    int slot = event.getSlot();
                    Torreta torreta;
                    if (slot == 36) {
                        event.setCancelled(true);
                        torreta = jugadorInventario.getTorreta();
                        Inventory inv = jugador.getOpenInventory().getTopInventory();
                        int ammo = 0;

                        for(int i = 0; i <= 35; ++i) {
                            ItemStack item = inv.getItem(i);
                            if (item != null && !item.getType().equals(Material.AIR)) {
                                ammo += item.getAmount();
                            }
                        }

                        jugadorInventario.getTorreta().setAmmo(ammo);
                        crearInventarioOpciones(jugador, torreta, this.plugin);
                    } else if (slot >= 37 && slot <= 43) {
                        event.setCancelled(true);
                    } else if (slot == 44) {
                        event.setCancelled(true);
                        if (jugador.isOp() || jugador.hasPermission("defensiveturrets.admin") || jugador.hasPermission("defensiveturrets.infiniteammo")) {
                            torreta = jugadorInventario.getTorreta();
                            optionAttackPlayers = null;
                            FileConfiguration messages = this.plugin.getMessages();
                            ItemStack item;
                            if (torreta.tieneAmmoInfinita()) {
                                torreta.setAmmoInfinita(false);
                                if (isOutdated()) {
                                    item = new ItemStack(from(351), 1, (short)8);
                                } else {
                                    item = new ItemStack(Material.valueOf("GRAY_DYE"));
                                }

                                itemMeta = item.getItemMeta();
                                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretInfiniteAmmoDisabled")));
                            } else {
                                torreta.setAmmoInfinita(true);
                                if (isOutdated()) {
                                    item = new ItemStack(from(351), 1, (short)10);
                                } else {
                                    item = new ItemStack(Material.valueOf("LIME_DYE"));
                                }

                                itemMeta = item.getItemMeta();
                                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretInfiniteAmmoEnabled")));
                            }

                            List<String> lore = new ArrayList();
                            lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretInfiniteAmmoStatusMessage")));
                            itemMeta.setLore(lore);
                            item.setItemMeta(itemMeta);
                            event.getClickedInventory().setItem(44, item);
                        }
                    } else if (jugadorInventario.getTorreta().estaActivada()) {
                        FileConfiguration messages = this.plugin.getMessages();
                        optionAttackMonsters = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
                        event.setCancelled(true);
                        jugador.sendMessage(optionAttackMonsters + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionAmmunitionDisabledError")));
                    }
                } else {
                    itemStack = event.getCurrentItem();
                    if (itemStack != null && event.getSlotType() != null && !itemStack.getType().equals(Material.AIR)) {
                        ItemStack itemC = itemStack.clone();
                        if (event.getClick().equals(ClickType.NUMBER_KEY)) {
                            event.setCancelled(true);
                        } else {
                            itemMeta = itemC.getItemMeta();
                            /*if (!Bukkit.getVersion().contains("1.15") && !Bukkit.getVersion().contains("1.16")) {
                                item.spigot().setUnbreakable(true);
                            } else {
                                item.setUnbreakable(true);
                            }*/
                            itemMeta.setUnbreakable(true);

                            itemC.setItemMeta(itemMeta); //necessary ?
                            optionAttackPlayers = jugadorInventario.getTorreta().getTipo() + ".yml";
                            TorretaConfig configTorreta = this.plugin.getConfigTurret(optionAttackPlayers);
                            FileConfiguration messages;
                            if (configTorreta != null) {
                                messages = configTorreta.getConfig();
                                itemStack = Utilities.crearItem(messages, "Ammunition");
                                if (itemC.isSimilar(itemStack)) {
                                    if (jugadorInventario.getTorreta().estaActivada()) {
                                        fileConfiguration = this.plugin.getMessages();
                                        String prefix = ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString("Messages.prefix") + " ");
                                        event.setCancelled(true);
                                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', fileConfiguration.getString("Messages.turretOptionAmmunitionDisabledError")));
                                    }

                                    return;
                                }
                            }

                            messages = this.plugin.getMessages();
                            String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
                            event.setCancelled(true);
                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionAmmunitionError")));
                        }
                    }
                }
            } else {
                if (event.getCurrentItem() == null) {
                    return;
                }

                if (event.getCurrentItem().getType() == Material.AIR || event.getSlotType() == null) {
                    return;
                }

                event.setCancelled(true);
                if (event.getClickedInventory().equals(jugador.getOpenInventory().getTopInventory())) {
                    FileConfiguration messages = this.plugin.getMessages();
                    String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
                    String optionAttackAnimals;
                    if (jugadorInventario.getInventario().equals("main")) {
                        int ammo;
                        if (event.getSlot() == 26) {
                            FileConfiguration config = this.plugin.getConfig();
                            Location l = jugadorInventario.getTorreta().getBase();
                            optionAttackAnimals = jugadorInventario.getTorreta().getJugador();
                            String tipo = jugadorInventario.getTorreta().getTipo() + ".yml";
                            jugador.closeInventory();
                            ammo = jugadorInventario.getTorreta().getAmmo();
                            int upgrade = jugadorInventario.getTorreta().getLevel();
                            jugadorInventario.getTorreta().eliminarTorreta();
                            this.plugin.eliminarTorreta(optionAttackAnimals, l);
                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretRemoved")));
                            if (config.getString("Config.receive_turret_on_destroy").equals("true")) {
                                TorretaConfig configTorreta = this.plugin.getConfigTurret(tipo);
                                if (configTorreta != null) {
                                    FileConfiguration tConfig = configTorreta.getConfig();
                                    ItemStack bloque = Utilities.crearItem(tConfig, "Block");
                                    String[] primerUpgrade = ((String)tConfig.getStringList("Turret.upgrades").get(upgrade - 1)).split(";");
                                    String[] damage = primerUpgrade[1].split("-");
                                    String minDamage = damage[0];
                                    String maxDamage = damage[1];
                                    String cooldown = primerUpgrade[2];
                                    String range = primerUpgrade[3];
                                    itemMeta = bloque.getItemMeta();
                                    List<String> lore = itemMeta.getLore();

                                    for(int i = 0; i < lore.size(); ++i) {
                                        lore.set(i, ((String)lore.get(i)).replace("%min%", minDamage).replace("%max%", maxDamage).replace("%cooldown%", cooldown).replace("%range%", range));
                                        String targets;
                                        if (configTorreta.getPath().equals("Laser.yml")) {
                                            targets = primerUpgrade[4];
                                            lore.set(i, ((String)lore.get(i)).replace("%power%", targets));
                                        } else if (configTorreta.getPath().equals("Siege.yml")) {
                                            targets = primerUpgrade[4];
                                            lore.set(i, ((String)lore.get(i)).replace("%radius%", targets));
                                        } else if (configTorreta.getPath().equals("Healing.yml")) {
                                            targets = primerUpgrade[4];
                                            lore.set(i, ((String)lore.get(i)).replace("%targets%", targets));
                                        }
                                    }

                                    itemMeta.setLore(lore);
                                    bloque.setItemMeta(itemMeta);
                                    ItemStack bloqueNuevo = Utilities.setTorretaUpgrade(bloque, upgrade);
                                    jugador.getInventory().addItem(bloqueNuevo);
                                    if (ammo > 0) {
                                        ItemStack item = Utilities.crearItem(tConfig, "Ammunition");

                                        for(int i = 0; i < ammo; ++i) {
                                            jugador.getInventory().addItem(new ItemStack[]{item});
                                        }
                                    }
                                }
                            }
                        } else if (event.getSlot() == 13) {
                            try {
                                Economy econ = this.plugin.getEconomy();
                                Torreta torreta = jugadorInventario.getTorreta();
                                optionAttackAnimals = Utilidades.getStatsSegunNivel(torreta.getTipo(), torreta.getLevel() + 1, this.plugin);
                                if (!optionAttackAnimals.equals("error")) {
                                    String[] nextUpgrade = optionAttackAnimals.split(";");
                                    ammo = Integer.valueOf(nextUpgrade[0]);
                                    if (econ.getBalance(jugador) >= (double) ammo) {
                                        econ.withdrawPlayer(jugador, (double) ammo);
                                        String[] damage = nextUpgrade[1].split("-");
                                        double minDamage = Double.valueOf(damage[0]);
                                        double maxDamage = Double.valueOf(damage[1]);
                                        double cooldown = Double.valueOf(nextUpgrade[2]);
                                        double range = Double.valueOf(nextUpgrade[3]);
                                        if (torreta.getTipo().equals("Burst")) {
                                            Burst burst = (Burst) torreta;
                                            burst.upgradeTorreta(minDamage, maxDamage, cooldown, range, this.plugin);
                                        } else {
                                            double radio;
                                            if (torreta.getTipo().equals("Laser")) {
                                                radio = Double.valueOf(nextUpgrade[4]);
                                                Laser laser = (Laser) torreta;
                                                laser.upgradeTorreta(minDamage, maxDamage, cooldown, range, radio, this.plugin);
                                            } else if (torreta.getTipo().equals("Siege")) {
                                                radio = Double.valueOf(nextUpgrade[4]);
                                                Siege siege = (Siege) torreta;
                                                siege.upgradeTorreta(minDamage, maxDamage, cooldown, range, radio, this.plugin);
                                            } else if (torreta.getTipo().equals("Healing")) {
                                                int maxTargets = Integer.valueOf(nextUpgrade[4]);
                                                Healing healing = (Healing) torreta;
                                                healing.upgradeTorreta(minDamage, maxDamage, cooldown, range, maxTargets, this.plugin);
                                            }
                                        }

                                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretUpgraded").replace("%level%", String.valueOf(torreta.getLevel()))));
                                        crearInventarioUpgrade(jugador, torreta, this.plugin);
                                    } else {
                                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.noEnoughMoney")));
                                    }
                                }
                            } catch(NoClassDefFoundError exception) {
                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.economyPluginNotInstalled")));
                            }
                        } else if (event.getSlot() == 8) {
                            Torreta torreta = jugadorInventario.getTorreta();
                            crearInventarioOpciones(jugador, torreta, this.plugin);
                        }
                    } else {
                        Torreta torreta;
                        if (jugadorInventario.getInventario().equals("options")) {
                            optionAttackMonsters = "";
                            optionAttackPlayers = "";
                            optionAttackAnimals = "";
                            torreta = jugadorInventario.getTorreta();
                            if (torreta instanceof Healing) {
                                optionAttackMonsters = "Messages.turretOptionHealMonsters";
                                optionAttackPlayers = "Messages.turretOptionHealPlayers";
                                optionAttackAnimals = "Messages.turretOptionHealAnimals";
                            } else {
                                optionAttackMonsters = "Messages.turretOptionAttackMonsters";
                                optionAttackPlayers = "Messages.turretOptionAttackPlayers";
                                optionAttackAnimals = "Messages.turretOptionAttackAnimals";
                            }

                            if (event.getSlot() == 36) {
                                crearInventarioUpgrade(jugador, torreta, this.plugin);
                            } else if (event.getSlot() == 21) {
                                if (!jugador.isOp() && !jugador.hasPermission("defensiveturrets.options.attackmonsters")) {
                                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionPermissionsError")));
                                    return;
                                }

                                if (!torreta.isAtacaMonstruos()) {
                                    ponerItemOption(event.getClickedInventory(), event.getSlot(), messages, optionAttackMonsters, "Messages.turretOptionEnabled");
                                    torreta.setAtacaMonstruos(true);
                                } else {
                                    ponerItemOption(event.getClickedInventory(), event.getSlot(), messages, optionAttackMonsters, "Messages.turretOptionDisabled");
                                    torreta.setAtacaMonstruos(false);
                                }
                            } else if (event.getSlot() == 22) {
                                if (!jugador.isOp() && !jugador.hasPermission("defensiveturrets.options.attackplayers")) {
                                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionPermissionsError")));
                                    return;
                                }

                                if (!torreta.isAtacaJugadores()) {
                                    ponerItemOption(event.getClickedInventory(), event.getSlot(), messages, optionAttackPlayers, "Messages.turretOptionEnabled");
                                    torreta.setAtacaJugadores(true);
                                } else {
                                    ponerItemOption(event.getClickedInventory(), event.getSlot(), messages, optionAttackPlayers, "Messages.turretOptionDisabled");
                                    torreta.setAtacaJugadores(false);
                                }
                            } else if (event.getSlot() == 23) {
                                if (!jugador.isOp() && !jugador.hasPermission("defensiveturrets.options.attackanimals")) {
                                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionPermissionsError")));
                                    return;
                                }

                                if (!torreta.isAtacaAnimales()) {
                                    ponerItemOption(event.getClickedInventory(), event.getSlot(), messages, optionAttackAnimals, "Messages.turretOptionEnabled");
                                    torreta.setAtacaAnimales(true);
                                } else {
                                    ponerItemOption(event.getClickedInventory(), event.getSlot(), messages, optionAttackAnimals, "Messages.turretOptionDisabled");
                                    torreta.setAtacaAnimales(false);
                                }
                            } else if (event.getSlot() == 31) {
                                InventarioJugadores.crearInventario(jugador, torreta, this.plugin);
                            } else if (event.getSlot() == 44) {
                                FileConfiguration config = this.plugin.getConfig();
                                if (config.getString("Config.turrets_require_ammunition").equals("true")) {
                                    InventarioAmmo.crearInventario(jugador, torreta, this.plugin);
                                }
                            } else if (event.getSlot() == 8) {
                                itemStack = null;
                                itemMeta = null;
                                if (torreta.estaActivada()) {
                                    torreta.setActivada(false);
                                    if (isOutdated()) {
                                        itemStack = new ItemStack(from(351), 1, (short)8);
                                    } else {
                                        itemStack = new ItemStack(Material.valueOf("GRAY_DYE"));
                                    }

                                    itemMeta = itemStack.getItemMeta();
                                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretDisabled")));
                                } else {
                                    torreta.setActivada(true);
                                    if (isOutdated()) {
                                        itemStack = new ItemStack(from(351), 1, (short)10);
                                    } else {
                                        itemStack = new ItemStack(Material.valueOf("LIME_DYE"));
                                    }

                                    itemMeta = itemStack.getItemMeta();
                                    itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretEnabled")));
                                }

                                List<String> lore = new ArrayList();
                                lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretStatusMessage")));
                                itemMeta.setLore(lore);
                                itemStack.setItemMeta(itemMeta);
                                event.getClickedInventory().setItem(8, itemStack);
                            }
                        } else if (jugadorInventario.getInventario().equals("players")) {
                            int slot = event.getSlot();
                            if (slot >= 10 && slot <= 16 || slot >= 19 && slot <= 25 || slot >= 28 && slot <= 34) {
                                if (event.getCurrentItem().hasItemMeta() && event.getClick().equals(ClickType.RIGHT)) {
                                    itemMeta = event.getCurrentItem().getItemMeta();
                                    optionAttackAnimals = ChatColor.stripColor(itemMeta.getDisplayName());
                                    torreta = jugadorInventario.getTorreta();
                                    torreta.eliminarJugador(optionAttackAnimals);
                                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionPlayerRemoved").replace("%name%", optionAttackAnimals)));
                                    InventarioJugadores.crearInventario(jugador, torreta, this.plugin);
                                }
                            } else if (slot == 40) {
                                torreta = jugadorInventario.getTorreta();
                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretOptionPlayersAddPlayerInfo")));
                                jugador.closeInventory();
                                this.plugin.agregarJugadorInventario(jugador, torreta, "adding_player");
                            } else if (event.getSlot() == 36) {
                                torreta = jugadorInventario.getTorreta();
                                crearInventarioOpciones(jugador, torreta, this.plugin);
                            }
                        }
                    }
                }
            }
        }

    }

    public static void ponerItemOption(Inventory inv, int slot, FileConfiguration messages, String path, String pathEnabledDisabled) {
        ItemStack item = null;
        if (!Bukkit.getVersion().contains("1.13") && !Bukkit.getVersion().contains("1.14") && !Bukkit.getVersion().contains("1.15") && !Bukkit.getVersion().contains("1.16")) {
            if (pathEnabledDisabled.equals("Messages.turretOptionEnabled")) {
                item = new ItemStack(from(159), 1, (short)5);
            } else {
                item = new ItemStack(from(159), 1, (short)14);
            }
        } else if (pathEnabledDisabled.equals("Messages.turretOptionEnabled")) {
            item = new ItemStack(Material.valueOf("GREEN_TERRACOTTA"));
        } else {
            item = new ItemStack(Material.valueOf("RED_TERRACOTTA"));
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', messages.getString(path)));
        List<String> lore = new ArrayList();
        lore.add(ChatColor.translateAlternateColorCodes('&', messages.getString(pathEnabledDisabled)));
        meta.setLore(lore);
        item.setItemMeta(meta);
        inv.setItem(slot, item);
    }

    @EventHandler
    public void bloquearYunque(InventoryClickEvent event) {
        Inventory inv = event.getView().getTopInventory();
        if (inv != null && inv.getType().equals(InventoryType.ANVIL)) {
            ItemStack item = event.getCurrentItem();
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName()) {
                    ArrayList<TorretaConfig> torretasConfig = this.plugin.getConfigTurrets();
                    Iterator var7 = torretasConfig.iterator();

                    while(var7.hasNext()) {
                        TorretaConfig torreta = (TorretaConfig)var7.next();
                        FileConfiguration tConfig = torreta.getConfig();
                        ItemStack bloque = Utilities.crearItem(tConfig, "Block");
                        if (bloque.getItemMeta().getDisplayName().equals(meta.getDisplayName())) {
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }

    }

    public static Material from(String id) {
        try {
            return Material.valueOf(id);
        } catch(IllegalArgumentException exception) {
            try {
                Material[] values = Material.values();
                for (Material material : values) {
                    if (material.getId() == Integer.parseInt(id)) {
                        return material;
                    }
                }
                return Material.REDSTONE_BLOCK;
            } catch(Exception e) {
                return Material.REDSTONE_BLOCK;
            }
        }
    }

    public static Material from(int id) {
        return from( "" +id);
    }

    public static boolean isOutdated() {
        return !Bukkit.getVersion().contains("1.13")
                && !Bukkit.getVersion().contains("1.14")
                && !Bukkit.getVersion().contains("1.15")
                && !Bukkit.getVersion().contains("1.16")
                && !Bukkit.getVersion().contains("1.17");
    }
}
