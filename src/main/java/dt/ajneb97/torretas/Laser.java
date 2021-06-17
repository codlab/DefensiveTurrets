//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dt.ajneb97.torretas;

import dt.ajneb97.DefensiveTurrets;
import dt.ajneb97.Torreta;
import dt.ajneb97.otros.CooldownTorreta;
import dt.ajneb97.otros.Utilities;

import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class Laser extends Torreta {
    private double empuje;

    public Laser(double minDamage, double maxDamage, double cooldown, int level, String tipo, String jugador, String uuidJugador, Location base, double rango, double empuje, DefensiveTurrets plugin, boolean atacaMonstruos, boolean atacaJugadores, boolean atacaAnimales, List<String> jugadores, int ammo, boolean activada, boolean ammoInfinita) {
        super(minDamage, maxDamage, cooldown, level, tipo, jugador, uuidJugador, base, rango, plugin, atacaMonstruos, atacaJugadores, atacaAnimales, jugadores, ammo, activada, ammoInfinita);
        this.empuje = empuje;
    }

    public static void disparo(Entity enemigo, Location l, Laser torreta, FileConfiguration tConfig, DefensiveTurrets plugin) {
        double distance = enemigo.getLocation().distance(l);
        Vector p1 = enemigo.getLocation().toVector();
        p1.setY(p1.getY() + 1.25D);
        Vector p2 = l.toVector();
        Vector vector = p1.clone().subtract(p2).normalize().multiply(0.25D);
        double length = 0.0D;
        new Location(l.getWorld(), p2.getX(), p2.getY(), p2.getZ());
        String[] sep = tConfig.getString("Turret.sound_1").split(";");

        try {
            l.getWorld().playSound(l, Sound.valueOf(sep[0]), (float)Integer.valueOf(sep[1]), Float.valueOf(sep[2]));
        } catch (Exception var17) {
            FileConfiguration messages = plugin.getMessages();
            String prefix = ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.prefix") + " ");
            Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.translateAlternateColorCodes('&', messages.getString("Messages.soundError").replace("%name%", sep[0])));
        }

        while(length < distance) {
            Location actual = new Location(l.getWorld(), p2.getX(), p2.getY(), p2.getZ());
            Utilities.generarParticula("REDSTONE", actual, 0.0F, 0.0F, 0.0F, 0.0F, 1);
            length += 0.25D;
            p2.add(vector);
        }

        if (torreta.getEmpuje() > 0.0D) {
            Vector nuevoVector = vector.clone().setY(vector.getY() + 0.2D);
            nuevoVector.multiply(1.0D + torreta.getEmpuje());
            enemigo.setVelocity(nuevoVector);
        }

    }

    public double getEmpuje() {
        return this.empuje;
    }

    public void setEmpuje(double empuje) {
        this.empuje = empuje;
    }

    public void upgradeTorreta(double minDamage, double maxDamage, double cooldown, double rango, double power, DefensiveTurrets plugin) {
        this.cooldownTorreta.eliminar();
        this.aumentarNivel();
        this.setMinDamage(minDamage);
        this.setMaxDamage(maxDamage);
        this.setCooldown(cooldown);
        this.setRango(rango);
        this.setEmpuje(power);
        this.cooldownTorreta = new CooldownTorreta(plugin, this);
        this.cooldownTorreta.ejecucion();
    }
}
