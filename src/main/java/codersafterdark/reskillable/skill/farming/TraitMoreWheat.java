package codersafterdark.reskillable.skill.farming;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitMoreWheat extends Trait {

    public TraitMoreWheat() {
        super(new ResourceLocation(MOD_ID, "more_wheat"), 1, 2, new ResourceLocation(MOD_ID, "farming"),
                6, "reskillable:farming|12");
    }

    @Override
    public void onBlockDrops(HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        IBlockState state = event.getState();

        if (state.getBlock() == Blocks.WHEAT && player.world.rand.nextInt(10) == 0) {
            event.getDrops().add(new ItemStack(Items.WHEAT));
        }
    }


}
