//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dt.ajneb97.eventos;

import dt.ajneb97.DefensiveTurrets;
import dt.ajneb97.HologramaJugador;
import dt.ajneb97.Torreta;
import dt.ajneb97.TorretaConfig;
import dt.ajneb97.api.TurretKillEvent;
import dt.ajneb97.api.TurretPlaceEvent;
import dt.ajneb97.otros.UCMIHolograms;
import dt.ajneb97.otros.UHolographicDisplays;
import dt.ajneb97.otros.Utilidades;
import dt.ajneb97.otros.Utilities;
import dt.ajneb97.torretas.Burst;
import dt.ajneb97.torretas.Healing;
import dt.ajneb97.torretas.Laser;
import dt.ajneb97.torretas.Siege;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class BloqueListener implements Listener {
    public DefensiveTurrets plugin;

    public BloqueListener(DefensiveTurrets plugin) {
        this.plugin = plugin;
    }

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void ponerBloque(final BlockPlaceEvent event) {
        ItemStack block = event.getItemInHand();
        final Player jugador = event.getPlayer();
        if (!event.isCancelled()) {
            if (block != null && !block.getType().equals(Material.AIR) && block.hasItemMeta()) {
                ArrayList<TorretaConfig> configs = this.plugin.getConfigTurrets();
                Iterator var6 = configs.iterator();

                while(var6.hasNext()) {
                    final TorretaConfig tConfig = (TorretaConfig)var6.next();
                    String id = tConfig.getConfig().getString("Block.id");
                    if (block.getType().name().equals(id) && block.getItemMeta().hasDisplayName()) {
                        String name = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', tConfig.getConfig().getString("Block.name")));
                        String nameBlock = ChatColor.stripColor(block.getItemMeta().getDisplayName());
                        if (name.equals(nameBlock)) {
                            FileConfiguration messages = this.plugin.getMessages();
                            String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
                            if (!this.esMundoCorrecto(this.plugin.getConfig(), jugador)) {
                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.placeTurretWorldError")));
                                event.setCancelled(true);
                                return;
                            }

                            if (!this.pasaPermiso(tConfig.getPath(), this.plugin.getConfig(), jugador)) {
                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.placeTurretNoPermissions")));
                                event.setCancelled(true);
                                return;
                            }

                            if (!this.posicionCorrecta(event.getBlock().getLocation())) {
                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.notValidLocationError")));
                                event.setCancelled(true);
                                return;
                            }

                            if (!Bukkit.getVersion().contains("1.8") && !event.getHand().equals(EquipmentSlot.HAND)) {
                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.mainHandError")));
                                event.setCancelled(true);
                                return;
                            }

                            ArrayList<Torreta> torretas = this.plugin.getTorretas();
                            int limiteMaximo = this.pasaPermisoLimite(this.plugin.getConfig(), torretas, jugador);
                            if (limiteMaximo != 12345) {
                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretLimitError").replace("%limit%", String.valueOf(limiteMaximo))));
                                event.setCancelled(true);
                                return;
                            }

                            if (this.esDistanciaCorrecta(event.getBlock().getLocation(), this.plugin.getConfig(), this.plugin)) {
                                event.setCancelled(true);
                                TurretPlaceEvent eventT = new TurretPlaceEvent(jugador, tConfig.getPath().replace(".yml", ""), event.getBlock().getLocation());
                                this.plugin.getServer().getPluginManager().callEvent(eventT);
                                if (eventT.isCancelled()) {
                                    return;
                                }

                                ItemStack block2 = block.clone();
                                block2.setAmount(1);
                                final int upgrade = Utilities.getTorretaUpgrade(block2);
                                jugador.getInventory().removeItem(block2);
                                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                                    public void run() {
                                        BloqueListener.this.crearTorreta(event.getBlock().getLocation(), tConfig.getPath(), jugador, tConfig.getConfig(), BloqueListener.this.plugin.getConfig(), upgrade);
                                    }
                                }, 3L);
                                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretCreated")));
                                return;
                            }

                            jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.minDistanceError")));
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }

        }
    }

    private boolean esMundoCorrecto(FileConfiguration config, Player jugador) {
        List<String> mundos = config.getStringList("Config.blacklisted_worlds");

        for(int i = 0; i < mundos.size(); ++i) {
            if (jugador.getWorld().getName().equals(mundos.get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean posicionCorrecta(Location base) {
        Location loc2 = base.clone().add(0.0D, 1.0D, 0.0D);
        if (loc2.getBlock() != null && !loc2.getBlock().getType().name().contains("AIR")) {
            return false;
        } else {
            Location loc3 = base.clone().add(0.0D, 2.0D, 0.0D);
            return loc3.getBlock() == null || loc3.getBlock().getType().name().contains("AIR");
        }
    }

    private boolean pasaPermiso(String name, FileConfiguration config, Player jugador) {
        if (config.getString("Config.per_turret_permissions").equals("true")) {
            String real = name.replace(".yml", "");
            return jugador.isOp() || jugador.hasPermission("defensiveturrets.use." + real.toLowerCase());
        } else {
            return true;
        }
    }

    private int pasaPermisoLimite(FileConfiguration config, ArrayList<Torreta> torretas, Player jugador) {
        if (config.getString("Config.turret_limit_permissions.enabled").equals("true")) {
            if (jugador.isOp()) {
                return 12345;
            } else {
                Set<PermissionAttachmentInfo> permissions = jugador.getEffectivePermissions();
                int limiteActual = Utilidades.getNumeroTorretasJugador(jugador, torretas);
                Iterator var7 = permissions.iterator();

                PermissionAttachmentInfo p;
                String permiso;
                do {
                    if (!var7.hasNext()) {
                        int limiteMaximo = Integer.valueOf(config.getString("Config.turret_limit_permissions.default_limit"));
                        if (limiteActual >= limiteMaximo) {
                            return limiteMaximo;
                        }

                        return 12345;
                    }

                    p = (PermissionAttachmentInfo)var7.next();
                    permiso = p.getPermission();
                } while(!p.getValue() || !permiso.startsWith("defensiveturrets.limit."));

                String[] sep = permiso.split("\\.");
                int limiteMaximo = Integer.valueOf(sep[2]);
                if (limiteActual >= limiteMaximo) {
                    return limiteMaximo;
                } else {
                    return 12345;
                }
            }
        } else {
            return 12345;
        }
    }

    @EventHandler
    public void romperTorreta(BlockBreakEvent event) {
        Location loc = event.getBlock().getLocation();
        Player jugador = event.getPlayer();
        if (!event.getBlock().getType().equals(Material.AIR)) {
            Torreta torreta = this.plugin.getTorreta(loc);
            if (torreta != null) {
                event.setCancelled(true);
                FileConfiguration messages = this.plugin.getMessages();
                String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
                jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.cantDestroyTurret")));
                return;
            }
        }

    }

    @EventHandler
    public void clickearTorreta(PlayerInteractEvent event) {
        Player jugador = event.getPlayer();
        if (event.getClickedBlock() != null && !event.getClickedBlock().getType().equals(Material.AIR) && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Location loc = event.getClickedBlock().getLocation();
            Torreta torreta = this.plugin.getTorreta(loc);
            if (torreta != null) {
                boolean pasa = true;
                if (!Bukkit.getVersion().contains("1.8") && !event.getHand().equals(EquipmentSlot.HAND)) {
                    pasa = false;
                }

                if (pasa) {
                    event.setCancelled(true);
                    FileConfiguration messages = this.plugin.getMessages();
                    String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
                    if (!jugador.isOp() && !jugador.hasPermission("defensiveturrets.admin") && !jugador.hasPermission("defensiveturrets.admin.open") && !torreta.getJugador().equals(jugador.getName())) {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.notYourTurret")));
                    } else {
                        jugador.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.openingTurretInventory")));
                        InventarioListener.crearInventarioUpgrade(jugador, torreta, this.plugin);
                    }
                }
            }
        }

    }

    private boolean esDistanciaCorrecta(Location location, FileConfiguration config, DefensiveTurrets plugin) {
        int distancia = Integer.valueOf(config.getString("Config.min_distance_between_turrets"));
        ArrayList<Torreta> torretas = plugin.getTorretas();
        Iterator var7 = torretas.iterator();

        while(var7.hasNext()) {
            Torreta t = (Torreta)var7.next();
            Location l = t.getBase();
            if (l.getWorld().getName().equals(location.getWorld().getName())) {
                double distance = location.distance(l);
                if (distance <= (double)distancia) {
                    return false;
                }
            }
        }

        return true;
    }

    private void crearTorreta(Location location, String path, Player jugador, FileConfiguration bConfig, FileConfiguration config, int upgrade) {
        String name = path.replace(".yml", "");
        String[] primerUpgrade = Utilidades.getStatsSegunNivel(name, upgrade, this.plugin).split(";");
        String[] damage = primerUpgrade[1].split("-");
        double minDamage = Double.valueOf(damage[0]);
        double maxDamage = Double.valueOf(damage[1]);
        double cooldown = Double.valueOf(primerUpgrade[2]);
        double range = Double.valueOf(primerUpgrade[3]);
        boolean atacaMonstruos = Boolean.valueOf(config.getString("Config.default_turret_options.attack_monsters"));
        boolean atacaAnimales = Boolean.valueOf(config.getString("Config.default_turret_options.attack_animals"));
        boolean atacaJugadores = Boolean.valueOf(config.getString("Config.default_turret_options.attack_players"));
        if (name.equals("Burst")) {
            Burst.crearTorreta(location, bConfig);
            Burst torreta = new Burst(minDamage, maxDamage, cooldown, upgrade, name, jugador.getName(), jugador.getUniqueId().toString(), location, range, this.plugin, atacaMonstruos, atacaJugadores, atacaAnimales, new ArrayList(), 0, true, false);
            this.plugin.agregarTorreta(torreta);
        } else {
            double radio;
            if (name.equals("Laser")) {
                radio = Double.valueOf(primerUpgrade[4]);
                Laser.crearTorreta(location, bConfig);
                Laser torreta = new Laser(minDamage, maxDamage, cooldown, upgrade, name, jugador.getName(), jugador.getUniqueId().toString(), location, range, radio, this.plugin, atacaMonstruos, atacaJugadores, atacaAnimales, new ArrayList(), 0, true, false);
                this.plugin.agregarTorreta(torreta);
            } else if (name.equals("Siege")) {
                radio = Double.valueOf(primerUpgrade[4]);
                Siege.crearTorreta(location, bConfig);
                Siege torreta = new Siege(minDamage, maxDamage, cooldown, upgrade, name, jugador.getName(), jugador.getUniqueId().toString(), location, range, radio, this.plugin, atacaMonstruos, atacaJugadores, atacaAnimales, new ArrayList(), 0, true, false);
                this.plugin.agregarTorreta(torreta);
            } else if (name.equals("Healing")) {
                int maxTargets = Integer.valueOf(primerUpgrade[4]);
                Healing.crearTorreta(location, bConfig);
                Healing torreta = new Healing(minDamage, maxDamage, cooldown, upgrade, name, jugador.getName(), jugador.getUniqueId().toString(), location, range, maxTargets, this.plugin, false, true, false, new ArrayList(), 0, true, false);
                this.plugin.agregarTorreta(torreta);
            }
        }

    }

    @EventHandler
    public void mirarBloque(PlayerMoveEvent event) {
        Player jugador = event.getPlayer();

        try {
            Block block = jugador.getTargetBlock((Set)null, 5);
            if (block != null && !block.getType().equals(Material.AIR)) {
                Location blockloc = block.getLocation();
                Torreta torreta = this.plugin.getTorreta(blockloc);
                if (torreta != null) {
                    FileConfiguration config = this.plugin.getConfig();
                    if (config.getString("Config.information_holograms").equals("true")) {
                        HologramaJugador holograma = this.plugin.getHologramaJugador(jugador);
                        if (holograma == null) {
                            Location l = torreta.getBase().clone();
                            l.setY(l.getY() + 3.0D);
                            l.setX(l.getX() + 0.5D);
                            l.setZ(l.getZ() + 0.5D);
                            FileConfiguration messages = this.plugin.getMessages();
                            List<String> lineas = messages.getStringList("Messages.informationHologram");
                            l.setY(l.getY() + this.determinarY(l, lineas.size()));
                            String tipo = torreta.getTipo();
                            TorretaConfig configTorreta = this.plugin.getConfigTurret(tipo + ".yml");
                            FileConfiguration tConfig = configTorreta.getConfig();
                            String name = tConfig.getString("Turret.name");
                            boolean errorDisabled = !torreta.estaActivada();
                            boolean errorAmmo = false;
                            if (config.getString("Config.turrets_require_ammunition").equals("true") && torreta.getAmmo() == 0 && !torreta.tieneAmmoInfinita()) {
                                errorAmmo = true;
                            }

                            String damageType = "";
                            if (tipo.contains("Healing")) {
                                damageType = messages.getString("Messages.informationHologramTypeHeal");
                            } else {
                                damageType = messages.getString("Messages.informationHologramTypeDamage");
                            }

                            if (this.plugin.hologramas.equals("hd")) {
                                UHolographicDisplays.crearHologramaTorreta(jugador, this.plugin, l, true, lineas, torreta, messages, errorAmmo, errorDisabled, name, damageType);
                            } else if (this.plugin.hologramas.equals("cmi")) {
                                UCMIHolograms.crearHologramaTorreta(jugador, this.plugin, l, true, lineas, torreta, messages, errorAmmo, errorDisabled, name, damageType);
                            }
                        }
                    }

                    return;
                }
            }

            HologramaJugador holograma = this.plugin.getHologramaJugador(jugador);
            if (holograma != null) {
                long timestamp = holograma.getTimestamp();
                String name = holograma.getNombre();
                if (this.plugin.hologramas.equals("hd")) {
                    UHolographicDisplays.eliminarHolograma(this.plugin, timestamp, jugador);
                } else if (this.plugin.hologramas.equals("cmi")) {
                    UCMIHolograms.eliminarHolograma(this.plugin, name, jugador);
                }
            }

        } catch (Exception var18) {
        }
    }

    @EventHandler
    public void alDañarJugador(EntityDamageEvent event) {
        Entity entidad = event.getEntity();
        if (entidad instanceof Player) {
            final Player jugador = (Player)entidad;
            if (jugador.hasMetadata("TurretDamage")) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
                    public void run() {
                        jugador.removeMetadata("TurretDamage", BloqueListener.this.plugin);
                    }
                }, 1L);
            }
        }

    }

    @EventHandler
    public void torretaMataEnemigo(EntityDeathEvent event) {
        LivingEntity entidad = event.getEntity();
        if (entidad != null) {
            Player jugador = entidad.getKiller();
            if (jugador == null && entidad.hasMetadata("TurretDamage")) {
                List<MetadataValue> metadata = entidad.getMetadata("TurretDamage");
                String[] sep = ((MetadataValue)metadata.get(metadata.size() - 1)).asString().split(";");
                String player = sep[0];
                String tipo = sep[1];
                TurretKillEvent eventT = new TurretKillEvent(player, tipo, entidad);
                if (event instanceof PlayerDeathEvent) {
                    PlayerDeathEvent eventPlayer = (PlayerDeathEvent)event;
                    FileConfiguration messages = this.plugin.getMessages();
                    Player muerto = (Player)entidad;
                    TorretaConfig configTorreta = this.plugin.getConfigTurret(tipo + ".yml");
                    if (configTorreta != null) {
                        FileConfiguration tConfig = configTorreta.getConfig();
                        String nameTurret = tConfig.getString("Turret.name");
                        String mensaje = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.playerDeath").replace("%player%", muerto.getName()).replace("%killer%", player).replace("%turret%", nameTurret));
                        eventPlayer.setDeathMessage(mensaje);
                    }
                }

                this.plugin.getServer().getPluginManager().callEvent(eventT);
                if (eventT.isCancelled()) {
                    return;
                }
            }
        }

    }

    @EventHandler
    public void pistonBloqueoExtend(BlockPistonExtendEvent event) {
        List<Block> bloques = event.getBlocks();
        Iterator var4 = bloques.iterator();

        while(var4.hasNext()) {
            Block block = (Block)var4.next();
            if (block != null) {
                Torreta torreta = this.plugin.getTorreta(block.getLocation());
                if (torreta != null) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

    }

    @EventHandler
    public void pistonBloqueoRetract(BlockPistonRetractEvent event) {
        List<Block> bloques = event.getBlocks();
        Iterator var4 = bloques.iterator();

        while(var4.hasNext()) {
            Block block = (Block)var4.next();
            if (block != null) {
                Torreta torreta = this.plugin.getTorreta(block.getLocation());
                if (torreta != null) {
                    event.setCancelled(true);
                    return;
                }
            }
        }

    }

    @EventHandler
    public void alExplotar(EntityExplodeEvent event) {
        List<Block> blocks = event.blockList();
        FileConfiguration config = this.plugin.getConfig();

        for(int i = 0; i < blocks.size(); ++i) {
            if (blocks.get(i) != null) {
                Torreta torreta = this.plugin.getTorreta(((Block)blocks.get(i)).getLocation());
                if (torreta != null) {
                    String j = torreta.getJugador();
                    if (config.getString("Config.protect_turrets_from_explosions").equals("true")) {
                        blocks.remove(i);
                        --i;
                    } else {
                        Utilities.dropearBloqueTorreta(this.plugin, config, torreta);
                        Utilities.cerrarInventarioTorreta(this.plugin, torreta);
                        this.plugin.eliminarTorreta(j, ((Block)blocks.get(i)).getLocation());
                        Player player = Bukkit.getPlayer(j);
                        if (player != null) {
                            TorretaConfig configTorreta = this.plugin.getConfigTurret(torreta.getTipo() + ".yml");
                            FileConfiguration tConfig = configTorreta.getConfig();
                            String name = tConfig.getString("Turret.name");
                            FileConfiguration messages = this.plugin.getMessages();
                            String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
                            player.sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.turretDestroyed").replace("%x_coord%", String.valueOf(torreta.getBase().getBlockX())).replace("%y_coord%", String.valueOf(torreta.getBase().getBlockY())).replace("%z_coord%", String.valueOf(torreta.getBase().getBlockZ())).replace("%name%", name)));
                        }
                    }
                }
            }
        }

    }

    @EventHandler
    public void cabezaDestruida(BlockFromToEvent event) {
        Block bFrom = event.getBlock();
        Block bTo = event.getToBlock();
        if ((bTo.getType().name().contains("PLAYER_HEAD") || bTo.getType().name().contains("SKULL_ITEM") || bTo.getType().name().contains("SKULL")) && this.plugin.getTorreta(bTo.getLocation()) != null) {
            event.setCancelled(true);
        }

    }

    private double determinarY(Location l, int lineas) {
        double cantidad = (double)lineas * 0.15D;
        return cantidad;
    }
}
