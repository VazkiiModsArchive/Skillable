package codersafterdark.reskillable.skill.building;

import codersafterdark.reskillable.skill.base.Trait;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class TraitPerfectRecover extends Trait {
    private Item glowstone;
    private Item lantern;

    public TraitPerfectRecover() {
        super("perfect_recover", 1, 1, 4, "building:8,gathering:4,mining:6");
    }

    @Override
    public void onBlockDrops(HarvestDropsEvent event) {
        if (event.getState().getBlock() == Blocks.GLOWSTONE) {
            boolean hasGlowstoneBlock = false;
            for (ItemStack stack : event.getDrops()) {
                hasGlowstoneBlock |= stack.getItem() == this.getGlowstone();
            }
            if (!hasGlowstoneBlock) {
                event.getDrops().removeIf((s) -> s.getItem() == Items.GLOWSTONE_DUST);
                event.getDrops().add(new ItemStack(Items.GLOWSTONE_DUST, 4));
            }
        } else if (event.getState().getBlock() == Blocks.SEA_LANTERN) {
            boolean hasSeaLantern = false;
            for (ItemStack stack : event.getDrops()) {
                hasSeaLantern |= stack.getItem() == this.getSeaLantern();
            }
            if (!hasSeaLantern) {
                event.getDrops().removeIf((s) -> s.getItem() == Items.PRISMARINE_CRYSTALS);
                event.getDrops().add(new ItemStack(Items.PRISMARINE_CRYSTALS, 5));
                event.getDrops().add(new ItemStack(Items.PRISMARINE_SHARD, 4));
            }
        }
    }

    private Item getGlowstone() {
        if (glowstone == null) {
            glowstone = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:glowstone"));
        }
        return glowstone;
    }

    private Item getSeaLantern() {
        if (lantern == null) {
            lantern = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:sea_lantern"));
        }
        return lantern;
    }
}