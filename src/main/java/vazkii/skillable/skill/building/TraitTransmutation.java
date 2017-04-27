package vazkii.skillable.skill.building;

import java.util.Map;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import vazkii.skillable.skill.base.Trait;

public abstract class TraitTransmutation extends Trait {
	
	private final ItemStack reagent;
	private final Map<IBlockState, IBlockState> stateMap;

	public TraitTransmutation(String name, int x, int y, int cost, ItemStack reagent, Map<IBlockState, IBlockState> stateMap, String reqs) {
		super(name, x, y, cost, reqs);
		this.reagent = reagent;
		this.stateMap = stateMap;
	}
	
	@Override
	public void onRightClickBlock(RightClickBlock event) { 
		ItemStack stack = event.getItemStack();
		if(ItemStack.areItemsEqual(stack, reagent)) {
			IBlockState state = event.getWorld().getBlockState(event.getPos());
			if(stateMap.containsKey(state)) {
				IBlockState placeState = stateMap.get(state);
				BlockPos pos = event.getPos();
				event.getWorld().setBlockState(pos, placeState);
				
				SoundEvent sound = placeState.getBlock().getSoundType().getPlaceSound();
				event.getWorld().playSound(null, pos, sound, SoundCategory.BLOCKS, 1F, 1F);
				if(event.getWorld().isRemote) {
					event.getEntityPlayer().swingArm(event.getHand());
					for(int i = 0; i < 20; i++)
						event.getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 0, 0);
				}
				
				stack.shrink(1);
			}
		}
	}

}
