//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dt.ajneb97;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dt.ajneb97.otros.Check;
import dt.ajneb97.otros.Utilidades;
import dt.ajneb97.otros.Utilities;

public class Comando implements CommandExecutor {
    public DefensiveTurrets plugin;

    public Comando(DefensiveTurrets plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration tConfig;
        ItemStack item;
        String[] primerUpgrade;
        String minDamage;
        String maxDamage;
        String cooldown;
        TorretaConfig configTorreta;
        String name;
        String maxTargets;
        if (!(sender instanceof Player)) {
            FileConfiguration messages = this.plugin.getMessages();
            String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
            if (args.length >= 1) {
                Player player;
                String turret;
                if (args[0].equalsIgnoreCase("give")) {
                    if (!Check.checkTodo(this.plugin, sender)) {
                        return true;
                    }

                    if (args.length >= 3) {
                        player = Bukkit.getPlayer(args[2]);
                        turret = args[1] + ".yml";
                        if (player != null) {
                            configTorreta = this.plugin.getConfigTurret(turret);
                            if (configTorreta != null) {
                                tConfig = configTorreta.getConfig();
                                ItemStack bloque = Utilities.crearItem(tConfig, "Block");
                                primerUpgrade = ((String) tConfig.getStringList("Turret.upgrades").get(0)).split(";");
                                primerUpgrade = primerUpgrade[1].split("-");
                                name = primerUpgrade[0];
                                minDamage = primerUpgrade[1];
                                maxDamage = primerUpgrade[2];
                                cooldown = primerUpgrade[3];
                                ItemMeta meta = bloque.getItemMeta();
                                List<String> lore = meta.getLore();

                                for (int i = 0; i < lore.size(); ++i) {
                                    lore.set(i, ((String) lore.get(i)).replace("%min%", name).replace("%max%", minDamage).replace("%cooldown%", maxDamage).replace("%range%", cooldown));
                                    if (configTorreta.getPath().equals("Laser.yml")) {
                                        maxTargets = primerUpgrade[4];
                                        lore.set(i, ((String) lore.get(i)).replace("%power%", maxTargets));
                                    } else if (configTorreta.getPath().equals("Siege.yml")) {
                                        maxTargets = primerUpgrade[4];
                                        lore.set(i, ((String) lore.get(i)).replace("%radius%", maxTargets));
                                    } else if (configTorreta.getPath().equals("Healing.yml")) {
                                        maxTargets = primerUpgrade[4];
                                        lore.set(i, ((String) lore.get(i)).replace("%targets%", maxTargets));
                                    }
                                }

                                meta.setLore(lore);
                                bloque.setItemMeta(meta);
                                player.getInventory().addItem(new ItemStack[]{bloque});
                                name = tConfig.getString("Block.name");
                                player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveCommandSuccessPlayer").replace("%name%", name)));
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveCommandSuccessSender").replace("%name%", name).replace("%player%", player.getName())));
                            } else {
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretDoesNotExist")));
                            }
                        } else {
                            sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.playerNotOnline")));
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveCommandError")));
                    }
                } else if (args[0].equalsIgnoreCase("giveammo")) {
                    if (!Check.checkTodo(this.plugin, sender)) {
                        return true;
                    }

                    if (args.length >= 4) {
                        player = Bukkit.getPlayer(args[2]);
                        turret = args[1] + ".yml";
                        if (player != null) {
                            configTorreta = this.plugin.getConfigTurret(turret);
                            if (configTorreta != null) {
                                try {
                                    int amount = Integer.valueOf(args[3]);
                                    tConfig = configTorreta.getConfig();
                                    item = Utilities.crearItem(tConfig, "Ammunition");

                                    for (int i = 0; i < amount; ++i) {
                                        player.getInventory().addItem(new ItemStack[]{item});
                                    }

                                    name = tConfig.getString("Ammunition.name");
                                    player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveammoCommandSuccessPlayer").replace("%name%", name)));
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveammoCommandSuccessSender").replace("%name%", name).replace("%player%", player.getName())));
                                } catch (NumberFormatException var23) {
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.amountError")));
                                }
                            } else {
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretDoesNotExist")));
                            }
                        } else {
                            sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.playerNotOnline")));
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveammoCommandError")));
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    this.plugin.reloadConfig();
                    this.plugin.reloadMessages();
                    ArrayList<TorretaConfig> configs = this.plugin.getConfigTurrets();
                    Iterator var35 = configs.iterator();

                    while (var35.hasNext()) {
                        TorretaConfig t = (TorretaConfig) var35.next();
                        t.reloadTurretConfig();
                    }

                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.configReloaded")));
                } else if (args[0].equalsIgnoreCase("removeturrets")) {
                    if (args.length >= 2) {
                        int eliminadas;
                        if (args[1].equalsIgnoreCase("world")) {
                            if (args.length >= 3) {
                                eliminadas = this.plugin.eliminarTorretasMundo(args[2]);
                                if (eliminadas == 0) {
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsNoRemoved")));
                                } else {
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsRemoved").replace("%turrets%", String.valueOf(eliminadas))));
                                }
                            } else {
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsCommandError")));
                            }
                        } else if (args[1].equalsIgnoreCase("player")) {
                            if (args.length >= 3) {
                                eliminadas = this.plugin.eliminarTorretasJugador(args[2]);
                                if (eliminadas == 0) {
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsNoRemoved")));
                                } else {
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsRemoved").replace("%turrets%", String.valueOf(eliminadas))));
                                }
                            } else {
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsCommandError")));
                            }
                        } else if (args[1].equalsIgnoreCase("all")) {
                            eliminadas = this.plugin.eliminarTorretas();
                            if (eliminadas == 0) {
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsNoRemoved")));
                            } else {
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsRemoved").replace("%turrets%", String.valueOf(eliminadas))));
                            }
                        } else {
                            sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsCommandError")));
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsCommandError")));
                    }
                }
            }

            return false;
        } else {
            Player jugador = (Player) sender;
            FileConfiguration messages = this.plugin.getMessages();
            String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
            if (args.length >= 1) {
                Player player;
                String turret;
                if (args[0].equalsIgnoreCase("give")) {
                    if (!Check.checkTodo(this.plugin, sender)) {
                        return true;
                    }

                    if (!jugador.isOp() && !jugador.hasPermission("defensiveturrets.admin")) {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.noPermissions")));
                    } else if (args.length >= 3) {
                        player = Bukkit.getPlayer(args[2]);
                        turret = args[1] + ".yml";
                        if (player != null) {
                            configTorreta = this.plugin.getConfigTurret(turret);
                            if (configTorreta != null) {
                                tConfig = configTorreta.getConfig();
                                item = Utilities.crearItem(tConfig, "Block");
                                primerUpgrade = ((String) tConfig.getStringList("Turret.upgrades").get(0)).split(";");
                                String[] damage = primerUpgrade[1].split("-");
                                minDamage = damage[0];
                                maxDamage = damage[1];
                                cooldown = primerUpgrade[2];
                                String range = primerUpgrade[3];
                                ItemMeta meta = item.getItemMeta();
                                List<String> lore = meta.getLore();

                                for (int i = 0; i < lore.size(); ++i) {
                                    lore.set(i, ((String) lore.get(i)).replace("%min%", minDamage).replace("%max%", maxDamage).replace("%cooldown%", cooldown).replace("%range%", range));
                                    if (configTorreta.getPath().equals("Laser.yml")) {
                                        maxTargets = primerUpgrade[4];
                                        lore.set(i, ((String) lore.get(i)).replace("%power%", maxTargets));
                                    } else if (configTorreta.getPath().equals("Siege.yml")) {
                                        maxTargets = primerUpgrade[4];
                                        lore.set(i, ((String) lore.get(i)).replace("%radius%", maxTargets));
                                    } else if (configTorreta.getPath().equals("Healing.yml")) {
                                        maxTargets = primerUpgrade[4];
                                        lore.set(i, ((String) lore.get(i)).replace("%targets%", maxTargets));
                                    }
                                }

                                meta.setLore(lore);
                                item.setItemMeta(meta);
                                player.getInventory().addItem(new ItemStack[]{item});
                                maxTargets = tConfig.getString("Block.name");
                                player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveCommandSuccessPlayer").replace("%name%", maxTargets)));
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveCommandSuccessSender").replace("%name%", maxTargets).replace("%player%", player.getName())));
                            } else {
                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretDoesNotExist")));
                            }
                        } else {
                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.playerNotOnline")));
                        }
                    } else {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveCommandError")));
                    }
                } else if (args[0].equalsIgnoreCase("reload")) {
                    if (!jugador.isOp() && !jugador.hasPermission("defensiveturrets.admin")) {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.noPermissions")));
                    } else {
                        this.plugin.reloadConfig();
                        this.plugin.reloadMessages();
                        ArrayList<TorretaConfig> configs = this.plugin.getConfigTurrets();
                        Iterator var34 = configs.iterator();

                        while (var34.hasNext()) {
                            configTorreta = (TorretaConfig) var34.next();
                            configTorreta.reloadTurretConfig();
                        }

                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.configReloaded")));
                    }
                } else if (args[0].equalsIgnoreCase("giveammo")) {
                    if (!Check.checkTodo(this.plugin, sender)) {
                        return true;
                    }

                    if (!jugador.isOp() && !jugador.hasPermission("defensiveturrets.admin")) {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.noPermissions")));
                    } else if (args.length >= 4) {
                        player = Bukkit.getPlayer(args[2]);
                        turret = args[1] + ".yml";
                        if (player != null) {
                            configTorreta = this.plugin.getConfigTurret(turret);
                            if (configTorreta != null) {
                                try {
                                    int amount = Integer.valueOf(args[3]);
                                    tConfig = configTorreta.getConfig();
                                    item = Utilities.crearItem(tConfig, "Ammunition");

                                    for (int i = 0; i < amount; ++i) {
                                        player.getInventory().addItem(new ItemStack[]{item});
                                    }

                                    name = tConfig.getString("Ammunition.name");
                                    player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveammoCommandSuccessPlayer").replace("%name%", name)));
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveammoCommandSuccessSender").replace("%name%", name).replace("%player%", player.getName())));
                                } catch (NumberFormatException var24) {
                                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.amountError")));
                                }
                            } else {
                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretDoesNotExist")));
                            }
                        } else {
                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.playerNotOnline")));
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.giveammoCommandError")));
                    }
                } else if (args[0].equalsIgnoreCase("removeturrets")) {
                    if (!jugador.isOp() && !jugador.hasPermission("defensiveturrets.admin")) {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.noPermissions")));
                    } else if (args.length >= 2) {
                        int eliminadas;
                        if (args[1].equalsIgnoreCase("world")) {
                            if (args.length >= 3) {
                                eliminadas = this.plugin.eliminarTorretasMundo(args[2]);
                                if (eliminadas == 0) {
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsNoRemoved")));
                                } else {
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsRemoved").replace("%turrets%", String.valueOf(eliminadas))));
                                }
                            } else {
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsCommandError")));
                            }
                        } else if (args[1].equalsIgnoreCase("player")) {
                            if (args.length >= 3) {
                                eliminadas = this.plugin.eliminarTorretasJugador(args[2]);
                                if (eliminadas == 0) {
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsNoRemoved")));
                                } else {
                                    sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsRemoved").replace("%turrets%", String.valueOf(eliminadas))));
                                }
                            } else {
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsCommandError")));
                            }
                        } else if (args[1].equalsIgnoreCase("all")) {
                            eliminadas = this.plugin.eliminarTorretas();
                            if (eliminadas == 0) {
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsNoRemoved")));
                            } else {
                                sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsRemoved").replace("%turrets%", String.valueOf(eliminadas))));
                            }
                        } else {
                            sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsCommandError")));
                        }
                    } else {
                        sender.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.removeTurretsCommandError")));
                    }
                } else if (!jugador.isOp() && !jugador.hasPermission("defensiveturrets.admin")) {
                    jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.noPermissions")));
                } else {
                    mensajeAyuda(jugador);
                }
            } else if (!jugador.isOp() && !jugador.hasPermission("defensiveturrets.admin")) {
                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.noPermissions")));
            } else {
                mensajeAyuda(jugador);
            }

            return true;
        }
    }

    public static void mensajeAyuda(Player jugador) {
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m                                                                    "));
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "      &a&lDefensive&9&lTurrets &7Commands"));
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8- &a/dt give <turret> <player> &eGives a turret to the player."));
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8- &a/dt giveammo <turret> <player> <amount> &eGives ammo to the player."));
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8- &a/dt removeturrets <world/player/all> <value> &eRemoves placed turrets."));
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8- &a/dt reload &eReloads the Config."));
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8- &a/dt help &eShows this message."));
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a&m                                                                    "));
    }
}
