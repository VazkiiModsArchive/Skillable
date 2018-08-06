package codersafterdark.reskillable.api.transmutations;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPrismarine;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;

import java.util.HashMap;
import java.util.Map;

public class TransmutationRegistry {
    private static Map<Item, Map<IBlockState, IBlockState>> reagentStateMap = new HashMap<>();

    public static Map<Item, Map<IBlockState, IBlockState>> getReagentStateMap() {
        return reagentStateMap;
    }

    public static Map<IBlockState, IBlockState> getStateMapByReagent(Item item) {
        return reagentStateMap.get(item);
    }

    public static void initDefaultMap() {
        Item item = Items.CHORUS_FRUIT;

        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.MELON_BLOCK, Blocks.PUMPKIN);

        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.MAGMA, Blocks.ICE);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.ICE, Blocks.MAGMA);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.STONE, Blocks.END_STONE);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.END_STONE, Blocks.STONE);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.CLAY, Blocks.HARDENED_CLAY);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.HARDENED_CLAY, Blocks.CLAY);

        TransmutationRegistry.addEntryToReagent(item, Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, true), Blocks.SPONGE.getDefaultState());
        TransmutationRegistry.addEntryToReagent(item, Blocks.SPONGE.getDefaultState(), Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, true));
        TransmutationRegistry.addEntryToReagent(item, Blocks.PRISMARINE.getDefaultState(), Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.BRICKS));
        TransmutationRegistry.addEntryToReagent(item, Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.BRICKS), Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.DARK));
        TransmutationRegistry.addEntryToReagent(item, Blocks.PRISMARINE.getDefaultState().withProperty(BlockPrismarine.VARIANT, BlockPrismarine.EnumType.DARK), Blocks.PRISMARINE.getDefaultState());

        for (EnumFacing face : EnumFacing.HORIZONTALS) {
            TransmutationRegistry.addEntryToReagent(item, Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, face), Blocks.MELON_BLOCK.getDefaultState());
        }

        for (int i = 0; i < 6; i++) {
            TransmutationRegistry.addEntryToReagent(item, Blocks.SAPLING.getStateFromMeta(i), Blocks.SAPLING.getStateFromMeta(i == 5 ? 0 : i + 1));
        }
        for (int i = 0; i < 16; i++) {
            TransmutationRegistry.addEntryToReagent(item, Blocks.WOOL.getStateFromMeta(i), Blocks.WOOL.getStateFromMeta(i == 15 ? 0 : i + 1));
            TransmutationRegistry.addEntryToReagent(item, Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(i), Blocks.STAINED_HARDENED_CLAY.getStateFromMeta(i == 15 ? 0 : i + 1));
            TransmutationRegistry.addEntryToReagent(item, Blocks.STAINED_GLASS.getStateFromMeta(i), Blocks.STAINED_GLASS.getStateFromMeta(i == 15 ? 0 : i + 1));
            TransmutationRegistry.addEntryToReagent(item, Blocks.STAINED_GLASS_PANE.getStateFromMeta(i), Blocks.STAINED_GLASS_PANE.getStateFromMeta(i == 15 ? 0 : i + 1));
            TransmutationRegistry.addEntryToReagent(item, Blocks.CARPET.getStateFromMeta(i), Blocks.CARPET.getStateFromMeta(i == 15 ? 0 : i + 1));
        }
    }

    public static boolean doesStateMapContainKeyState(IBlockState state, Map<IBlockState, IBlockState> stateMap) {
        return stateMap.containsKey(state);
    }

    public static boolean doesReagentStateMapContainReagentItem(Item item) {
        return reagentStateMap.containsKey(item);
    }

    ////////////////////////
    /// Addition Methods ///
    ////////////////////////

    public static void addEntryToReagentByBlockDefaultState(Item item, Block block1, Block block2) {
        if (reagentStateMap.containsKey(item)) {
            Map<IBlockState, IBlockState> map = reagentStateMap.get(item);
            map.put(block1.getDefaultState(), block2.getDefaultState());
        } else {
            Map<IBlockState, IBlockState> stateMap = new HashMap<>();
            stateMap.put(block1.getDefaultState(), block2.getDefaultState());
            reagentStateMap.put(item, stateMap);
        }
    }

    public static void addEntryToReagent(Item item, IBlockState state1, IBlockState state2) {
        if (reagentStateMap.containsKey(item)) {
            Map<IBlockState, IBlockState> map = reagentStateMap.get(item);
            map.put(state1, state2);
        } else {
            Map<IBlockState, IBlockState> stateMap = new HashMap<>();
            stateMap.put(state1, state2);
            reagentStateMap.put(item, stateMap);
        }
    }

    public static void addEntriesToReagent(Item item, Map<IBlockState, IBlockState> map) {
        if (reagentStateMap.containsKey(item)) {
            Map<IBlockState, IBlockState> mapster = reagentStateMap.get(item);
            for (Map.Entry<IBlockState, IBlockState> entry : map.entrySet()) {
                mapster.put(entry.getKey(), entry.getValue());
            }
        } else {
            Map<IBlockState, IBlockState> stateMap = new HashMap<>();
            for (Map.Entry<IBlockState, IBlockState> entry : map.entrySet()) {
                stateMap.put(entry.getKey(), entry.getValue());
            }
            reagentStateMap.put(item, stateMap);
        }
    }

    public static void addEntryReagentAgnostic(IBlockState state1, IBlockState state2) {
        for (Item key : reagentStateMap.keySet()) {
            Map<IBlockState, IBlockState> map = reagentStateMap.get(key);
            map.put(state1, state2);
        }
    }

    public static void addEntriesReagentAgnostic(Map<IBlockState, IBlockState> mapster) {
        for (Item key : reagentStateMap.keySet()) {
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
        for (Item reagent : reagentStateMap.keySet()) {
            reagentStateMap.get(reagent).remove(state);
        }
    }

    public static void removeEndStateFromReagentAgnostic(IBlockState state) {
        for (Item reagent : reagentStateMap.keySet()) {
            for (Map.Entry<IBlockState, IBlockState> stateEntry : reagentStateMap.get(reagent).entrySet()) {
                if (stateEntry.getValue() == state) {
                    reagentStateMap.get(reagent).remove(stateEntry.getKey());
                }
            }
        }
    }

    public static void removeStartStateFromReagent(Item reagent, IBlockState state) {
        reagentStateMap.get(reagent).remove(state);
    }

    public static void removeEndStateFromReagent(Item reagent, IBlockState state) {
        for (Map.Entry<IBlockState, IBlockState> stateEntry : reagentStateMap.get(reagent).entrySet()) {
            if (stateEntry.getValue() == state) {
                reagentStateMap.get(reagent).remove(stateEntry.getKey());
            }
        }
    }


    /////////////////////
    /// Clear Methods ///
    /////////////////////
    public static void clearMapOfReagent(Item stack) {
        reagentStateMap.remove(stack);
    }

    public static void clearReagentOfEntries(Item stack) {
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
