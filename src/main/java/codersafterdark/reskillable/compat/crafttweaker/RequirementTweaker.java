package codersafterdark.reskillable.compat.crafttweaker;

import codersafterdark.reskillable.base.RequirementHolder;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.HashMap;
import java.util.Map;

@ZenClass("mods.reskillable.Requirement")
@ZenRegister
public class RequirementTweaker {
    private static final Map<ItemStack, RequirementHolder> locks = new HashMap();

    @ZenMethod
    public static void addRequirement(IItemStack item, String locked){
        if (locked == null || locked.isEmpty()){
            CraftTweakerAPI.logError("String: " + locks + " was found to be either null or empty!");
        } else if (item == null || item.isEmpty()){
            CraftTweakerAPI.logError("Itemstack: " + item + " was found to be either null or empty!");
        } else {
            ItemStack realItem = CraftTweakerMC.getItemStack(item);
            locks.clear();
            lock = locked;

            if (locked == null){
                return;
            }

            for (String s : locked) {
                String[] tokens = s.split("=");
                if (tokens.length == 2) {
                    RequirementHolder h = RequirementHolder.fromString(tokens[1]);
                    locks.put(realItem, h);
                }
            }
        }
    }
}

