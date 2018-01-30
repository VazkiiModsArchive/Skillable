package vazkii.skillable.skill.gathering;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.*;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import vazkii.skillable.skill.base.Trait;

public class TraitDropGuarantee extends Trait {

    public TraitDropGuarantee() {
        super("drop_guarantee", 1, 1, 8, "gathering:20,attack:8");
    }

    @Override
    public void onMobDrops(LivingDropsEvent event) {
        Entity e = event.getEntity();
        if (event.getDrops().isEmpty() && e.getEntityWorld().getGameRules().getBoolean("doMobLoot")) {
            ItemStack drop = null;
            if (e instanceof EntityCreeper)
                drop = new ItemStack(Items.GUNPOWDER);
            else if (e instanceof EntityZombie)
                drop = new ItemStack(Items.ROTTEN_FLESH);
            else if (e instanceof EntitySkeleton)
                drop = new ItemStack(Items.BONE);
            else if (e instanceof EntitySpider)
                drop = new ItemStack(Items.STRING);
            else if (e instanceof EntityBlaze)
                drop = new ItemStack(Items.BLAZE_ROD);
            else if (e instanceof EntityGuardian)
                drop = new ItemStack(Items.PRISMARINE_SHARD);
            else if (e instanceof EntityGhast)
                drop = new ItemStack(Items.GHAST_TEAR);
            else if (e instanceof EntityEnderman)
                drop = new ItemStack(Items.ENDER_PEARL);
            else if (e instanceof EntitySlime)
                drop = new ItemStack(Items.SLIME_BALL);
            else if (e instanceof EntityWitch)
                drop = e.getEntityWorld().rand.nextBoolean() ? new ItemStack(Items.REDSTONE) : new ItemStack(Items.GLOWSTONE_DUST);

            if (drop != null)
                event.getDrops().add(new EntityItem(e.getEntityWorld(), e.posX, e.posY, e.posZ, drop));
        }
    }

}

