//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package dt.ajneb97.versiones;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Skull;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class V1_17_R0 {
    public V1_17_R0() {

    }

    public void generarParticula(String particle, Location loc, float xOffset, float yOffset, float zOffset, float speed, int count) {
        if (particle.equals("REDSTONE")) {
            loc.getWorld().spawnParticle(Particle.valueOf("SPELL_WITCH"), loc, count, (double)xOffset, (double)yOffset, (double)zOffset, (double)speed);
        } else {
            loc.getWorld().spawnParticle(Particle.valueOf(particle), loc, count, (double)xOffset, (double)yOffset, (double)zOffset, (double)speed);
        }

    }

    public ItemStack setSkull(ItemStack item, String id, String textura) {
        if (textura.isEmpty()) {
            return item;
        } else {
            SkullMeta skullMeta = (SkullMeta)item.getItemMeta();
            GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
            profile.getProperties().put("textures", new Property("textures", textura));

            try {
                Method mtd = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                mtd.setAccessible(true);
                mtd.invoke(skullMeta, profile);
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException var7) {
                var7.printStackTrace();
            }

            item.setItemMeta(skullMeta);
            return item;
        }
    }

    public void setSkullBlock(Location locBloque, String id, String textura) {
        locBloque.getBlock().setType(Material.valueOf("PLAYER_HEAD"));
        Skull skullBlock = (Skull)locBloque.getBlock().getState();
        GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
        profile.getProperties().put("textures", new Property("textures", textura));

        try {
            Field profileField = skullBlock.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullBlock, profile);
        } catch (IllegalAccessException | NoSuchFieldException var7) {
            var7.printStackTrace();
        }

        skullBlock.update();
    }

    public ItemStack setTorretaUpgrade(ItemStack item, int upgrade) {
        net.minecraft.world.item.ItemStack itemstack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = itemstack.hasTag() ? itemstack.getTag() : new NBTTagCompound();
        tag.setInt("DT_upgrade", upgrade);
        itemstack.setTag(tag);
        return CraftItemStack.asBukkitCopy(itemstack);
    }

    public int getTorretaUpgrade(ItemStack item) {
        net.minecraft.world.item.ItemStack stack = CraftItemStack.asNMSCopy(item);
        new NBTTagCompound();
        NBTTagCompound tag = stack.getTag();
        return tag.hasKey("DT_upgrade") ? tag.getInt("DT_upgrade") : 1;
    }
}
