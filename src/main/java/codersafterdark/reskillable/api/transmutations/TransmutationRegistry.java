package codersafterdark.reskillable.api.transmutations;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

public class TransmutationRegistry {
    private static Map<ItemStack, Map<IBlockState, IBlockState>> reagentStateMap = new HashMap<>();

    public static Map<ItemStack, Map<IBlockState, IBlockState>> getReagentStateMap(){
        return reagentStateMap;
    }

    public static Map<IBlockState, IBlockState> getStateMapByReagent(ItemStack stack) {
        return reagentStateMap.get(stack);
    }

    public static void initDefaultMap() {
        ItemStack stack = new ItemStack(Items.CHORUS_FRUIT);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(stack, Blocks.MAGMA, Blocks.ICE);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(stack, Blocks.STONE, Blocks.END_STONE);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(stack, Blocks.MELON_BLOCK, Blocks.PUMPKIN);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(stack, Blocks.HARDENED_CLAY, Blocks.CLAY);

        TransmutationRegistry.addEntryToReagent(stack, Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, true), Blocks.SPONGE.getDefaultState());
        TransmutationRegistry.addEntryToReagent(stack, Blocks.PRISMARINE.getDefaultState(), Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.DARK));

        for (EnumFacing face : EnumFacing.HORIZONTALS) {
            TransmutationRegistry.addEntryToReagent(stack, Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, face), Blocks.MELON_BLOCK.getDefaultState());
        }

        for (int i = 0; i < 6; i++) {
            TransmutationRegistry.addEntryToReagent(stack, Blocks.SAPLING.getStateFromMeta(i), Blocks.SAPLING.getStateFromMeta(i == 5 ? 0 : i + 1));
        }
        for (int i = 0; i < 16; i++) {
            TransmutationRegistry.addEntryToReagent(stack, Blocks.WOOL.getStateFromMeta(i), Blocks.WOOL.getStateFromMeta(i == 15 ? 0 : i + 1));
            TransmutationRegistry.addEntryToReagent(stack, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(i), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(i == 15 ? 0 : i + 1));
            TransmutationRegistry.addEntryToReagent(stack, Blocks.STAINED_GLASS.getStateFromMeta(i), Blocks.STAINED_GLASS.getStateFromMeta(i == 15 ? 0 : i + 1));
            TransmutationRegistry.addEntryToReagent(stack, Blocks.STAINED_GLASS_PANE.getStateFromMeta(i), Blocks.STAINED_GLASS_PANE.getStateFromMeta(i == 15 ? 0 : i + 1));
            TransmutationRegistry.addEntryToReagent(stack, Blocks.CARPET.getStateFromMeta(i), Blocks.CARPET.getStateFromMeta(i == 15 ? 0 : i + 1));
        }
    }

    public static boolean doesStateMapContainReagentItemStack(ItemStack stack) {
        boolean returnValue = false;
        for (ItemStack stack1 : reagentStateMap.keySet()) {
            if (ItemStack.areItemsEqual(stack, stack1)) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    ////////////////////////
    /// Addition Methods ///
    ////////////////////////

    public static void addEntryToReagentByBlockDefaultState(ItemStack stack, Block block1, Block block2) {
        if (reagentStateMap.containsKey(stack)) {
            Map<IBlockState, IBlockState> map = reagentStateMap.get(stack);
            map.put(block1.getDefaultState(), block2.getDefaultState());
        } else {
            Map<IBlockState, IBlockState> stateMap = new HashMap<>();
            stateMap.put(block1.getDefaultState(), block2.getDefaultState());
            reagentStateMap.put(stack, stateMap);
        }
    }

    public static void addEntryToReagent(ItemStack stack, IBlockState state1, IBlockState state2) {
        if (reagentStateMap.containsKey(stack)) {
            Map<IBlockState, IBlockState> map = reagentStateMap.get(stack);
            map.put(state1, state2);
        } else {
            Map<IBlockState, IBlockState> stateMap = new HashMap<>();
            stateMap.put(state1, state2);
            reagentStateMap.put(stack, stateMap);
        }
    }

    public static void addEntriesToReagent(ItemStack stack, Map<IBlockState, IBlockState> map) {
        if (reagentStateMap.containsKey(stack)) {
            Map<IBlockState, IBlockState> mapster = reagentStateMap.get(stack);
            for (Map.Entry<IBlockState, IBlockState> entry : map.entrySet()) {
                mapster.put(entry.getKey(), entry.getValue());
            }
        } else {
            Map<IBlockState, IBlockState> stateMap = new HashMap<>();
            for (Map.Entry<IBlockState, IBlockState> entry : map.entrySet()) {
                stateMap.put(entry.getKey(), entry.getValue());
            }
            reagentStateMap.put(stack, stateMap);
        }
    }

    public static void addEntryReagentAgnostic(IBlockState state1, IBlockState state2) {
        for (ItemStack key : reagentStateMap.keySet()) {
            Map<IBlockState, IBlockState> map = reagentStateMap.get(key);
            map.put(state1, state2);
        }
    }

    public static void addEntriesReagentAgnostic(Map<IBlockState, IBlockState> mapster) {
        for (ItemStack key : reagentStateMap.keySet()) {
            Map<IBlockState, IBlockState> map = reagentStateMap.get(key);
            for (Map.Entry<IBlockState, IBlockState> entry : mapster.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
    }



    ///////////////////////
    /// Removal Methods ///
    ///////////////////////

    public static void removeStartStateFromReagentAgnostic(IBlockState state) {
        for (ItemStack reagent : reagentStateMap.keySet()) {
            reagentStateMap.get(reagent).remove(state);
        }
    }

    public static void removeEndStateFromReagentAgnostic(IBlockState state) {
        for (ItemStack reagent : reagentStateMap.keySet()) {
            for (Map.Entry<IBlockState, IBlockState> stateEntry : reagentStateMap.get(reagent).entrySet()) {
                if (stateEntry.getValue() == state) {
                    reagentStateMap.get(reagent).remove(stateEntry.getKey());
                }
            }
        }
    }

    public static void removeStartStateFromReagent(ItemStack reagent, IBlockState state) {
        reagentStateMap.get(reagent).remove(state);
    }

    public static void removeEndStateFromReagent(ItemStack reagent, IBlockState state) {
        for (Map.Entry<IBlockState, IBlockState> stateEntry : reagentStateMap.get(reagent).entrySet()) {
            if (stateEntry.getValue() == state) {
                reagentStateMap.get(reagent).remove(stateEntry.getKey());
            }
        }
    }



    /////////////////////
    /// Clear Methods ///
    /////////////////////
    public static void clearMapOfReagent(ItemStack stack) {
        reagentStateMap.remove(stack);
    }

    public static void clearReagentOfEntries(ItemStack stack) {
        reagentStateMap.get(stack).clear();
    }

    ////////////////////////////////////////////////////////////////
    //         Clear's the entire Transmutation Map               //
    // Do Not Use This Unless You Have A Very Specific Reasoning! //
    ////////////////////////////////////////////////////////////////
    public static void clearReagentMap() {
        reagentStateMap.clear();
    }
}
