package codersafterdark.reskillable.skill.farming;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitGreenThumb extends Trait {

    public TraitGreenThumb() {
        super(new ResourceLocation(MOD_ID, "green_thumb"), 3, 1, new ResourceLocation(MOD_ID, "farming"),
                8, "reskillable:farming|16", "reskillable:magic|16");
    }

    @Override
    public void onPlayerTick(PlayerTickEvent event) {
        EntityPlayer player = event.player;
        BlockPos pos = player.getPosition();
        if (player.ticksExisted % 20 == 0) {
            int range = 6;
            int x = pos.getX() + player.world.rand.nextInt(range * 2 + 1) - range;
            int z = pos.getZ() + player.world.rand.nextInt(range * 2 + 1) - range;

            for (int i = 4; i > -2; i--) {
                int y = pos.getY() + i;
                BlockPos offPos = new BlockPos(x, y, z);
                if (player.world.isAirBlock(offPos)) {
                    continue;
                }

                if (isPlant(player.world, offPos)) {
                    ItemStack item = new ItemStack(Items.DYE, 1, 15);
                    ItemDye.applyBonemeal(item, player.world, offPos);
                    player.world.playEvent(2005, offPos, 6 + player.world.rand.nextInt(4));
                    break;
                }
            }
        }
    }

    private boolean isPlant(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        if (block == Blocks.GRASS || block == Blocks.LEAVES || block == Blocks.LEAVES2 || block instanceof BlockBush && !(block instanceof BlockCrops) && !(block instanceof BlockSapling)) {
            return false;
        }

        Material mat = state.getMaterial();
        return mat != null && (mat == Material.PLANTS || mat == Material.CACTUS || mat == Material.GRASS || mat == Material.LEAVES || mat == Material.GOURD) && block instanceof IGrowable && ((IGrowable) block).canGrow(world, pos, world.getBlockState(pos), world.isRemote);
    }

}
