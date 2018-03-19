package codersafterdark.reskillable.compat.crafttweaker;

import codersafterdark.reskillable.base.LevelLockHandler;
import codersafterdark.reskillable.base.RequirementHolder;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.reskillable.Requirement")
@ZenRegister
public class RequirementTweaker {
    
    @ZenMethod
    public static void addRequirement(IItemStack item, String locked) {
        CraftTweakerAPI.apply(new IAction() {
            @Override
            public void apply() {
                if(locked == null || locked.isEmpty()) {
                    CraftTweakerAPI.logError("String: " + locked + " was found to be either null or empty!");
                    return;
                }
                if(item == null || item.isEmpty()) {
                    CraftTweakerAPI.logError("Itemstack: " + item + " was found to be either null or empty!");
                    return;
                }
                ItemStack realItem = CraftTweakerMC.getItemStack(item);
                RequirementHolder h = RequirementHolder.fromString(locked);
                LevelLockHandler.craftTweakerLocks.put(realItem, h);
            }
    
            @Override
            public String describe() {
                return "Setting the requirement of: " + item + " to: " + locked;
            }
        });
        
    }
}

