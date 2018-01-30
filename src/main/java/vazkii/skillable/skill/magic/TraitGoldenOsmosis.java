package vazkii.skillable.skill.magic;

import com.google.common.collect.ImmutableSet;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import vazkii.skillable.base.ExperienceHelper;
import vazkii.skillable.skill.base.Trait;

public class TraitGoldenOsmosis extends Trait {

    public TraitGoldenOsmosis() {
        super("golden_osmosis", 3, 2, 10, "magic:20,mining:6,gathering:6,attack:6");
    }

    @Override
    public void onPlayerTick(PlayerTickEvent event) {
        ItemStack stack = event.player.getHeldItemMainhand();
        if (!event.player.world.isRemote && ImmutableSet.of(Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, Items.GOLDEN_SWORD).contains(stack.getItem()) && stack.getMetadata() > 3) {
            int xp = ExperienceHelper.getPlayerXP(event.player);
            if (xp > 0) {
                ExperienceHelper.drainPlayerXP(event.player, 1);
                stack.setItemDamage(stack.getMetadata() - 3);
            }
        }
    }

}
