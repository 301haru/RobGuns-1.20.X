package net.robus.robguns.item.mod_items;

import com.google.common.collect.Lists;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.robus.robguns.entity.ModEntities;
import net.robus.robguns.entity.mod_entities.custom_entities.RoundBallProjectile;
import net.robus.robguns.item.ModItems;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.Random;

public class GunItem extends Item {
    private boolean twoHanded = false;
    private int chargeTime = 20;
    private float damage = 11f;
    private float projectileVelocity = 3;
    private boolean scoped = false;
    private float fovModifier = 0.25f;
    private float inaccuracy = 0.1f;
    private static final Random random = new Random();

    public GunItem(Properties pProperties) {
        super(pProperties);
    }

    public void shoot(LivingEntity entity, Level level) {
        RoundBallProjectile projectile = new RoundBallProjectile(level, entity);

        ItemStack itemStack = null;

        if (entity.getMainHandItem().getItem() instanceof GunItem) {
            itemStack = entity.getMainHandItem();
        } else {
            itemStack = entity.getOffhandItem();
        }

        projectile.setDamage(getAttackDamage(itemStack));

        Vec3 vec31 = entity.getUpVector(1.0F);
        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0 * ((float) Math.PI / 180F), vec31.x, vec31.y, vec31.z);
        Vec3 vec3 = entity.getViewVector(1.0F);
        Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);

        projectile.shoot(vector3f.x(), vector3f.y(), vector3f.z(), projectileVelocity, inaccuracy);
        projectile.setPos(entity.getEyePosition());

        level.addFreshEntity(projectile);

        shootParticle(entity, level);

        level.playSound(entity, entity.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.PLAYERS, 0.8f, 1.2f);
        level.playSound(entity, entity.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1, 1);
    }

    public void shootParticle(LivingEntity entity, Level level) {
        double playerX = entity.getEyePosition().x;
        double playerY = entity.getEyePosition().y;
        double playerZ = entity.getEyePosition().z;

        double lookX = entity.getLookAngle().x;
        double lookY = entity.getLookAngle().y;
        double lookZ = entity.getLookAngle().z;

        double spawnX = playerX + lookX * 1;
        double spawnY = playerY + lookY * 1;
        double spawnZ = playerZ + lookZ * 1;

        for (int i = 0; i < 20; i++) {
            double offsetX = random.nextDouble() * 0.6 - 0.3;
            double offsetY = random.nextDouble() * 0.6 - 0.3;
            double offsetZ = random.nextDouble() * 0.6 - 0.3;

            level.addParticle(ParticleTypes.SMOKE, spawnX + offsetX, spawnY + offsetY, spawnZ + offsetZ, 0.0, 0.0, 0.0);
        }
    }

    public ItemStack getProjectile(Player player, ItemStack itemStack) {
        int y =EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, itemStack);

        if (y != 0) {
            return new ItemStack(ModItems.ROUND_BALL.get());
        }

        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack itemstack1 = player.getInventory().getItem(i);
            if (itemstack1.getItem() instanceof RoundBallItem) {
                return itemstack1;
            }
        }

        return null;
    }

    public ItemStack getGunpowder(Player player, ItemStack itemStack) {
        int y = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack);

        if (y != 0) {
            return new ItemStack(Items.GUNPOWDER);
        }

        for (int i = 0; i < player.getInventory().getContainerSize(); ++i) {
            ItemStack itemstack1 = player.getInventory().getItem(i);
            if (itemstack1.getItem() == Items.GUNPOWDER) {
                return itemstack1;
            }
        }

        return null;
    }

    public static void addChargedProjectile(ItemStack pCrossbowStack, ItemStack pAmmoStack) {
        CompoundTag compoundtag = pCrossbowStack.getOrCreateTag();
        ListTag listtag;
        if (compoundtag.contains("ChargedProjectiles", 9)) {
            listtag = compoundtag.getList("ChargedProjectiles", 10);
        } else {
            listtag = new ListTag();
        }

        CompoundTag compoundtag1 = new CompoundTag();
        pAmmoStack.save(compoundtag1);
        listtag.add(compoundtag1);
        compoundtag.put("ChargedProjectiles", listtag);
    }

    public static List<ItemStack> getChargedProjectiles(ItemStack pCrossbowStack) {
        List<ItemStack> list = Lists.newArrayList();
        CompoundTag compoundtag = pCrossbowStack.getTag();
        if (compoundtag != null && compoundtag.contains("ChargedProjectiles", 9)) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 10);
            if (listtag != null) {
                for(int i = 0; i < listtag.size(); ++i) {
                    CompoundTag compoundtag1 = listtag.getCompound(i);
                    list.add(ItemStack.of(compoundtag1));
                }
            }
        }

        return list;
    }

    public static void clearChargedProjectiles(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getTag();
        if (compoundtag != null) {
            ListTag listtag = compoundtag.getList("ChargedProjectiles", 9);
            listtag.clear();
            compoundtag.put("ChargedProjectiles", listtag);
        }

    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.POWER_ARROWS || enchantment == Enchantments.INFINITY_ARROWS
        || enchantment == Enchantments.FLAMING_ARROWS || enchantment == Enchantments.UNBREAKING
        || enchantment == Enchantments.QUICK_CHARGE) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 10;
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        return 1;
    }

    public static boolean isCharged(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getTag();
        return compoundtag != null && compoundtag.getBoolean("Charged");
    }

    public void setCharged(ItemStack pCrossbowStack, boolean pIsCharged) {
        CompoundTag compoundtag = pCrossbowStack.getOrCreateTag();
        compoundtag.putBoolean("Charged", pIsCharged);
    }

    public static boolean isCharging(ItemStack pCrossbowStack) {
        CompoundTag compoundtag = pCrossbowStack.getTag();
        return compoundtag != null && compoundtag.getBoolean("Charging");
    }

    public static void setCharging(ItemStack pCrossbowStack, boolean pIsCharging) {
        CompoundTag compoundtag = pCrossbowStack.getOrCreateTag();
        compoundtag.putBoolean("Charging", pIsCharging);
    }

    public int getChargeTime(ItemStack itemStack) {
        int y = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, itemStack);

        int chargeDecrease = 5 * y;
        return chargeTime - chargeDecrease;
    }
    public void setChargeTime(int chargeTime) { this.chargeTime = chargeTime; }

    public float getAttackDamage(ItemStack itemStack) {
        int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack);

        float extraDamage = 0;

        if (i != 0) {
            extraDamage = (damage / 4) * (i + 1);
        }

        return damage + extraDamage;
    }

    public boolean isValidRepairItem(ItemStack pToRepair, ItemStack pRepair) {
        return pRepair.is(Items.IRON_INGOT) || super.isValidRepairItem(pToRepair, pRepair);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return true;
    }

    public void setAttackDamage(float damage) { this.damage = damage; }

    public boolean isTwoHanded() { return twoHanded; }
    public void setTwoHanded(boolean twoHanded) { this.twoHanded = twoHanded; }

    public float getProjectileVelocity() { return projectileVelocity; }
    public void setProjectileVelocity(float projectileVelocity) { this.projectileVelocity = projectileVelocity; }

    public float getInaccuracy() { return inaccuracy; }
    public void setInaccuracy(float inaccuracy) { this.inaccuracy = inaccuracy; }

    public float getFovModifier() { return fovModifier; }
    public void setFovModifier(float fovModifier) { this.fovModifier = fovModifier; }

    public void setScoped(boolean scoped) { this.scoped = scoped; }
    public boolean isScoped() { return scoped; }
}
