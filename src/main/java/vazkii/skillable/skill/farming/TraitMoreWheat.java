package vazkii.skillable.skill.farming;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import vazkii.skillable.skill.base.Trait;

public class TraitMoreWheat extends Trait {

    public TraitMoreWheat() {
        super("more_wheat", 1, 2, 6, "farming:12");
    }

    @Override
    public void onBlockDrops(HarvestDropsEvent event) {
        EntityPlayer player = event.getHarvester();
        IBlockState state = event.getState();

        if (state.getBlock() == Blocks.WHEAT && player.world.rand.nextInt(10) == 0)
            event.getDrops().add(new ItemStack(Items.WHEAT));
    }


}
