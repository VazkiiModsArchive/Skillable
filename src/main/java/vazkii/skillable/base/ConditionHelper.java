package vazkii.skillable.base;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public final class ConditionHelper {

    public static boolean hasRightTool(EntityPlayer player, IBlockState state, String toolClass, int reqLevel) {
        ItemStack stack = player.getHeldItemMainhand();
        return stack.getItem().getHarvestLevel(stack, toolClass, player, state) >= reqLevel;
    }

}
