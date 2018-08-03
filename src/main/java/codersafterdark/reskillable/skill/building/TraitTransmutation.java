package codersafterdark.reskillable.skill.building;

import codersafterdark.reskillable.Reskillable;
import codersafterdark.reskillable.api.transmutations.TransmutationRegistry;
import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.Map;
import java.util.stream.IntStream;

public class TraitTransmutation extends Trait {

    public TraitTransmutation() {
        super(new ResourceLocation(LibMisc.MOD_ID, "transmutation"), 3, 2, new ResourceLocation(LibMisc.MOD_ID, "building"), 8, "reskillable:building|16", "reskillable:magic|16");
        TransmutationRegistry.initDefaultMap();
        for (ItemStack stack : TransmutationRegistry.getReagentStateMap().keySet()) {
            Reskillable.logger.error(stack.getDisplayName());
        }
    }

    @Override
    public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled()) {
            return;
        }
        ItemStack stack = event.getItemStack();
        if (TransmutationRegistry.doesStateMapContainReagentItemStack(stack)) {
            Map<IBlockState, IBlockState> stateMap = TransmutationRegistry.getStateMapByReagent(stack);
            IBlockState state = event.getWorld().getBlockState(event.getPos());
            if (stateMap.containsKey(state)) {
                IBlockState placeState = stateMap.get(state);
                BlockPos pos = event.getPos();
                event.getWorld().setBlockState(pos, placeState);
                SoundEvent sound = placeState.getBlock().getSoundType(placeState, event.getWorld(), pos, null).getPlaceSound();
                event.getWorld().playSound(null, pos, sound, SoundCategory.BLOCKS, 1F, 1F);
                if (event.getWorld().isRemote) {
                    event.getEntityPlayer().swingArm(event.getHand());
                    IntStream.range(0, 20).forEach(i -> event.getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0));
                }
                stack.shrink(1);
            }
        }
    }
}
