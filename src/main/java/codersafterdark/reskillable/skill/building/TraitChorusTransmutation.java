package codersafterdark.reskillable.skill.building;

import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitChorusTransmutation extends TraitTransmutation {

    public TraitChorusTransmutation() {
        super(new ResourceLocation(MOD_ID, "chorus_transmute"), 3, 2, new ResourceLocation(MOD_ID, "building"),
                8, new ItemStack(Items.CHORUS_FRUIT), makeMap(), "reskillable:building|16", "reskillable:magic|16");
    }

    private static Map<IBlockState, IBlockState> makeMap() {
        Map<IBlockState, IBlockState> map = new HashMap<>();
        map.put(Blocks.STONE.getDefaultState(), Blocks.END_STONE.getDefaultState());

        for (EnumFacing face : EnumFacing.HORIZONTALS) {
            map.put(Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, face), Blocks.MELON_BLOCK.getDefaultState());
        }

        map.put(Blocks.MELON_BLOCK.getDefaultState(), Blocks.PUMPKIN.getDefaultState());
        map.put(Blocks.PRISMARINE.getDefaultState(), Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.DARK));

        for (int i = 0; i < 6; i++) {
            map.put(Blocks.SAPLING.getStateFromMeta(i), Blocks.SAPLING.getStateFromMeta(i == 5 ? 0 : i + 1));
        }
        for (int i = 0; i < 16; i++) {
            map.put(Blocks.WOOL.getStateFromMeta(i), Blocks.WOOL.getStateFromMeta(i == 15 ? 0 : i + 1));
            map.put(Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(i), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(i == 15 ? 0 : i + 1));
            map.put(Blocks.STAINED_GLASS.getStateFromMeta(i), Blocks.STAINED_GLASS.getStateFromMeta(i == 15 ? 0 : i + 1));
            map.put(Blocks.STAINED_GLASS_PANE.getStateFromMeta(i), Blocks.STAINED_GLASS_PANE.getStateFromMeta(i == 15 ? 0 : i + 1));
            map.put(Blocks.CARPET.getStateFromMeta(i), Blocks.CARPET.getStateFromMeta(i == 15 ? 0 : i + 1));
        }

        map.put(Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, true), Blocks.SPONGE.getDefaultState());
        map.put(Blocks.HARDENED_CLAY.getDefaultState(), Blocks.CLAY.getDefaultState());
        map.put(Blocks.MAGMA.getDefaultState(), Blocks.ICE.getDefaultState());

        return map;
    }

}
