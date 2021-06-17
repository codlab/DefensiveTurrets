//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dt.ajneb97;

import dt.ajneb97.otros.CooldownTorreta;
import dt.ajneb97.otros.Utilidades;
import dt.ajneb97.otros.Utilities;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Torreta {
    protected int level;
    protected double maxDamage;
    protected double minDamage;
    protected double cooldown;
    protected String tipo;
    protected String jugador;
    protected String uuidJugador;
    protected Location base;
    protected double rango;
    protected CooldownTorreta cooldownTorreta;
    protected boolean atacaMonstruos;
    protected boolean atacaJugadores;
    protected boolean atacaAnimales;
    protected boolean ammoInfinita;
    protected List<String> jugadores;
    protected int ammo;
    private boolean activada;

    public Torreta(double minDamage, double maxDamage, double cooldown, int level, String tipo, String jugador, String uuidJugador, Location base, double rango, DefensiveTurrets plugin, boolean atacaMonstruos, boolean atacaJugadores, boolean atacaAnimales, List<String> jugadores, int ammo, boolean activada, boolean ammoInfinita) {
        this.cooldown = cooldown;
        this.level = level;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.tipo = tipo;
        this.jugador = jugador;
        this.uuidJugador = uuidJugador;
        this.base = base;
        this.rango = rango;
        this.cooldownTorreta = new CooldownTorreta(plugin, this);
        this.cooldownTorreta.ejecucion();
        this.atacaMonstruos = atacaMonstruos;
        this.atacaJugadores = atacaJugadores;
        this.atacaAnimales = atacaAnimales;
        this.jugadores = jugadores;
        this.ammo = ammo;
        this.ammoInfinita = ammoInfinita;
        this.activada = activada;
    }

    public boolean estaActivada() {
        return this.activada;
    }

    public void setActivada(boolean activada) {
        this.activada = activada;
    }

    public void setAmmo(int ammo) {
        this.ammo = ammo;
    }

    public void disminuirAmmo() {
        --this.ammo;
    }

    public int getAmmo() {
        return this.ammo;
    }

    public void setAmmoInfinita(boolean ammoInfinita) {
        this.ammoInfinita = ammoInfinita;
    }

    public boolean tieneAmmoInfinita() {
        return this.ammoInfinita;
    }

    public List<String> getJugadores() {
        return this.jugadores;
    }

    public void eliminarJugador(String name) {
        this.jugadores.remove(name);
    }

    public void agregarJugador(String name) {
        this.jugadores.add(name);
    }

    public String getTipo() {
        return this.tipo;
    }

    public int getLevel() {
        return this.level;
    }

    public double getCooldown() {
        return this.cooldown;
    }

    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    public double getMinDamage() {
        return this.minDamage;
    }

    public void setMinDamage(double minDamage) {
        this.minDamage = minDamage;
    }

    public double getMaxDamage() {
        return this.maxDamage;
    }

    public void setMaxDamage(double maxDamage) {
        this.maxDamage = maxDamage;
    }

    public void aumentarNivel() {
        ++this.level;
    }

    public String getJugador() {
        return this.jugador;
    }

    public String getUUIDJugador() {
        return this.uuidJugador;
    }

    public Location getBase() {
        return this.base;
    }

    public double getRango() {
        return this.rango;
    }

    public void setRango(double rango) {
        this.rango = rango;
    }

    public void eliminarTorreta() {
        this.cooldownTorreta.eliminar();
    }

    public boolean estaOwnerConectado() {
        Player p = Bukkit.getPlayer(this.jugador);
        return p != null && p.isOnline();
    }

    public boolean isAtacaMonstruos() {
        return this.atacaMonstruos;
    }

    public boolean isAtacaJugadores() {
        return this.atacaJugadores;
    }

    public boolean isAtacaAnimales() {
        return this.atacaAnimales;
    }

    public void setAtacaMonstruos(boolean atacaMonstruos) {
        this.atacaMonstruos = atacaMonstruos;
    }

    public void setAtacaJugadores(boolean atacaJugadores) {
        this.atacaJugadores = atacaJugadores;
    }

    public void setAtacaAnimales(boolean atacaAnimales) {
        this.atacaAnimales = atacaAnimales;
    }

    public static void crearTorreta(Location l, FileConfiguration configTorreta) {
        String block_1 = configTorreta.getString("Turret.block_1");
        String block_2 = configTorreta.getString("Turret.block_2");
        String[] sepHead = configTorreta.getString("Turret.head").split(";");
        Utilities.setBlockTorreta(block_1, l);
        Location l2 = l.clone();
        l2.setY(l.getY() + 1.0D);
        Utilities.setBlockTorreta(block_2, l2);
        Location l3 = l2.clone();
        l3.setY(l2.getY() + 1.0D);
        Utilities.setSkullBlock(l3, sepHead[0], sepHead[1]);
    }
}
