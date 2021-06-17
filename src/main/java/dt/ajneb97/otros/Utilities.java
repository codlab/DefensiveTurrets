package dt.ajneb97.otros;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import dt.ajneb97.DefensiveTurrets;
import dt.ajneb97.Torreta;
import dt.ajneb97.TorretaConfig;
import dt.ajneb97.versiones.V1_10;
import dt.ajneb97.versiones.V1_11;
import dt.ajneb97.versiones.V1_12;
import dt.ajneb97.versiones.V1_13;
import dt.ajneb97.versiones.V1_13_R2;
import dt.ajneb97.versiones.V1_14;
import dt.ajneb97.versiones.V1_15;
import dt.ajneb97.versiones.V1_16;
import dt.ajneb97.versiones.V1_16_R2;
import dt.ajneb97.versiones.V1_16_R3;
import dt.ajneb97.versiones.V1_17_R0;
import dt.ajneb97.versiones.V1_8_R1;
import dt.ajneb97.versiones.V1_8_R2;
import dt.ajneb97.versiones.V1_8_R3;
import dt.ajneb97.versiones.V1_9_R1;
import dt.ajneb97.versiones.V1_9_R2;

public class Utilities {
    public static ItemStack crearItem(FileConfiguration config, String path) {
        String id = config.getString(path + ".id");
        String[] idsplit = new String[2];
        ItemStack stack = null;
        int IDint;
        if (id.contains(":")) {
            idsplit = id.split(":");
            String stringDataValue = idsplit[1];
            int DataValue = Integer.valueOf(stringDataValue);
            Material mat = Material.getMaterial(idsplit[0].toUpperCase());

            stack = new ItemStack(mat, 1, (short) DataValue);
        } else {
            Material mat = Material.getMaterial(id.toUpperCase());
            stack = new ItemStack(mat, 1);
        }

        ItemMeta meta = stack.getItemMeta();
        if (config.contains(path + ".name")) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString(path + ".name")));
        }

        if (config.contains(path + ".lore")) {
            List<String> lore = config.getStringList(path + ".lore");

            for (int c = 0; c < lore.size(); ++c) {
                lore.set(c, ChatColor.translateAlternateColorCodes('&', (String) lore.get(c)));
            }

            meta.setLore(lore);
        }

        meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_POTION_EFFECTS});
        /*if (!Bukkit.getVersion().contains("1.15") && !Bukkit.getVersion().contains("1.16")) {
            meta.spigot().setUnbreakable(true);
        } else {
            meta.setUnbreakable(true);
        }*/
        meta.setUnbreakable(true);

        stack.setItemMeta(meta);
        if (config.contains(path + ".head")) {
            String[] head = config.getString(path + ".head").split(";");
            stack = setSkull(stack, head[0], head[1]);
        }

        return stack;
    }

    public static ItemStack setSkull(ItemStack item, String id, String textura) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        if (packageName.contains("1_17")) {
            V1_17_R0 u = new V1_17_R0();
            return u.setSkull(item, id, textura);
        } else {
            return Utilidades.setSkull(item, id, textura);
        }
    }


    public static void dropearBloqueTorreta(DefensiveTurrets plugin, FileConfiguration config, Torreta torreta) {
        String tipo = torreta.getTipo() + ".yml";
        if (config.getString("Config.drop_turret_on_destroy").equals("true")) {
            TorretaConfig configTorreta = plugin.getConfigTurret(tipo);
            if (configTorreta != null) {
                FileConfiguration tConfig = configTorreta.getConfig();
                ItemStack bloque = crearItem(tConfig, "Block");
                int upgrade = torreta.getLevel();
                String[] primerUpgrade = ((String) tConfig.getStringList("Turret.upgrades").get(upgrade - 1)).split(";");
                String[] damage = primerUpgrade[1].split("-");
                String minDamage = damage[0];
                String maxDamage = damage[1];
                String cooldown = primerUpgrade[2];
                String range = primerUpgrade[3];
                ItemMeta meta = bloque.getItemMeta();
                List<String> lore = meta.getLore();

                for (int i = 0; i < lore.size(); ++i) {
                    lore.set(i, ((String) lore.get(i)).replace("%min%", minDamage).replace("%max%", maxDamage).replace("%cooldown%", cooldown).replace("%range%", range));
                    String targets;
                    if (configTorreta.getPath().equals("Laser.yml")) {
                        targets = primerUpgrade[4];
                        lore.set(i, ((String) lore.get(i)).replace("%power%", targets));
                    } else if (configTorreta.getPath().equals("Siege.yml")) {
                        targets = primerUpgrade[4];
                        lore.set(i, ((String) lore.get(i)).replace("%radius%", targets));
                    } else if (configTorreta.getPath().equals("Healing.yml")) {
                        targets = primerUpgrade[4];
                        lore.set(i, ((String) lore.get(i)).replace("%targets%", targets));
                    }
                }

                meta.setLore(lore);
                bloque.setItemMeta(meta);
                ItemStack bloqueNuevo = setTorretaUpgrade(bloque, upgrade);
                torreta.getBase().getWorld().dropItemNaturally(torreta.getBase(), bloqueNuevo);
                int ammo = torreta.getAmmo();
                if (ammo > 0) {
                    ItemStack item = crearItem(tConfig, "Ammunition");

                    for (int i = 0; i < ammo; ++i) {
                        torreta.getBase().getWorld().dropItemNaturally(torreta.getBase(), item);
                    }
                }
            }
        }
    }


    public static void generarParticula(String particle, Location loc, float xOffset, float yOffset, float zOffset, float speed, int count) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        if (packageName.contains("1_17")) {
            V1_17_R0 u = new V1_17_R0();
            u.generarParticula(particle, loc, xOffset, yOffset, zOffset, speed, count);
        } else {
            Utilidades.generarParticula(particle, loc, xOffset, yOffset, zOffset, speed, count);
        }
    }

    public static ItemStack setTorretaUpgrade(ItemStack item, int upgrade) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        if (packageName.contains("1_17")) {
            V1_17_R0 u = new V1_17_R0();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_16_R3")) {
            V1_16_R3 u = new V1_16_R3();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_16_R2")) {
            V1_16_R2 u = new V1_16_R2();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_16_R1")) {
            V1_16 u = new V1_16();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_15_R1")) {
            V1_15 u = new V1_15();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_14_R1")) {
            V1_14 u = new V1_14();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_13_R2")) {
            V1_13_R2 u = new V1_13_R2();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_13_R1")) {
            V1_13 u = new V1_13();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_12_R1")) {
            V1_12 u = new V1_12();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_11_R1")) {
            V1_11 u = new V1_11();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_10_R1")) {
            V1_10 u = new V1_10();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_9_R2")) {
            V1_9_R2 u = new V1_9_R2();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_9_R1")) {
            V1_9_R1 u = new V1_9_R1();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_8_R3")) {
            V1_8_R3 u = new V1_8_R3();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_8_R2")) {
            V1_8_R2 u = new V1_8_R2();
            return u.setTorretaUpgrade(item, upgrade);
        } else if (packageName.contains("1_8_R1")) {
            V1_8_R1 u = new V1_8_R1();
            return u.setTorretaUpgrade(item, upgrade);
        } else {
            return null;
        }
    }

    public static int getTorretaUpgrade(ItemStack item) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        if (packageName.contains("1_17")) {
            V1_17_R0 u = new V1_17_R0();
            return u.getTorretaUpgrade(item);
        }
        return Utilidades.getTorretaUpgrade(item);
    }

    public static void cerrarInventarioTorreta(DefensiveTurrets plugin, Torreta torreta) {
        Utilidades.cerrarInventarioTorreta(plugin, torreta);
    }

    public static void setSkullBlock(Location l3, String s, String s1) {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        if (packageName.contains("1_17")) {
            V1_17_R0 u = new V1_17_R0();
            u.setSkullBlock(l3, s, s1);
        } else {
            Utilidades.setSkullBlock(l3, s, s1);
        }
    }

    public static void setBlockTorreta(String block, Location l) {
        Utilidades.setBlockTorreta(block, l);
    }
}
