package codersafterdark.reskillable.skill.magic;

import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.base.ExperienceHelper;
import com.google.common.collect.ImmutableSet;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitGoldenOsmosis extends Trait {
    public TraitGoldenOsmosis() {
        super(new ResourceLocation(MOD_ID, "golden_osmosis"), 3, 2, new ResourceLocation(MOD_ID, "magic"),
                10, "reskillable:magic|20", "reskillable:mining|6", "reskillable:gathering|6", "reskillable:attack|6");
    }

    @Override
    public void onPlayerTick(PlayerTickEvent event) {
        ItemStack stack = event.player.getHeldItemMainhand();
        if (!event.player.world.isRemote && ImmutableSet.of(Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_SWORD).contains(stack.getItem()) && stack.getMetadata() > 3) {
            if (ExperienceHelper.getPlayerXP(event.player) > 0) {
                ExperienceHelper.drainPlayerXP(event.player, 1);
                stack.setItemDamage(stack.getMetadata() - 3);
            }
        }
    }
}