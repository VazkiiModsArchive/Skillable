package vazkii.skillable.skill.farming;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import vazkii.skillable.base.ConfigHandler;
import vazkii.skillable.skill.Skills;
import vazkii.skillable.skill.base.Trait;

public class TraitGreenThumb extends Trait {

	public TraitGreenThumb() {
		super("green_thumb", 3, 1, 8);
		addRequirement(Skills.farming, 16);
		addRequirement(Skills.magic, 16);
	}
	
	@Override
	public void onPlayerTick(PlayerTickEvent event) { 
		EntityPlayer player = event.player;
		BlockPos pos = player.getPosition();
		if(player.ticksExisted % 20 == 0) {
			int range = 6;
			int x = pos.getX() + player.world.rand.nextInt(range * 2 + 1) - range;
			int z = pos.getZ() + player.world.rand.nextInt(range * 2 + 1) - range;

			for(int i = 4; i > -2; i--) {
				int y = pos.getY() + i;
				BlockPos offPos = new BlockPos(x, y, z);
				if(player.world.isAirBlock(offPos))
					continue;

				if(isPlant(player.world, offPos)) {
					Block block = player.world.getBlockState(offPos).getBlock();
					player.world.scheduleUpdate(offPos, block, 1);
					player.world.playEvent(2005, offPos, 6 + player.world.rand.nextInt(4));

					break;
				}
			}
		}
	}
	
	private boolean isPlant(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		if(block == Blocks.GRASS || block == Blocks.LEAVES || block == Blocks.LEAVES2 || block instanceof BlockBush && !(block instanceof BlockCrops) && !(block instanceof BlockSapling))
			return false;

		Material mat = state.getMaterial();
		return mat != null && (mat == Material.PLANTS || mat == Material.CACTUS || mat == Material.GRASS || mat == Material.LEAVES || mat == Material.GOURD) && block instanceof IGrowable && ((IGrowable) block).canGrow(world, pos, world.getBlockState(pos), world.isRemote);
	}
	
}
