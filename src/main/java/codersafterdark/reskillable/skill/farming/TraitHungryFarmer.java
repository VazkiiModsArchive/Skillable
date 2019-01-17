package codersafterdark.reskillable.skill.farming;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TraitHungryFarmer extends Trait {
    private List<ItemStack> list = new ArrayList<>();
    private Map<ItemStack, Integer> hungerSaturationMap = new HashMap<>();

    public TraitHungryFarmer() {
        super(new ResourceLocation(LibMisc.MOD_NAME, "hungry_farmer"), 2, 3, new ResourceLocation(LibMisc.MOD_ID, "farming"), 8, "reskillable:farming|32");
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player == null) {
            return;
        }

        EntityPlayer entityPlayer = event.player;
        World world = entityPlayer.getEntityWorld();
        PlayerData data = PlayerDataHandler.get(entityPlayer);
        FoodStats foodStats = entityPlayer.getFoodStats();

        ItemStack currentStack = ItemStack.EMPTY;
        ItemFood food = null;

        int hungDif;
        int playerHunger = foodStats.getFoodLevel();

        if (data != null && data.getSkillInfo(getParentSkill()).isUnlocked(this)) {
            if (playerHunger < 20) {
                hungDif = 20 - playerHunger;
                NonNullList<ItemStack> inventoryList = entityPlayer.inventoryContainer.getInventory();

                for (ItemStack stack : inventoryList) {
                    if (stack.getItem() instanceof ItemFood) {
                        list.add(stack);
                    }
                }

                for (ItemStack stack : list) {
                    if (stack.getItem() instanceof  ItemFood) {
                        ItemFood internalFood = (ItemFood) stack.getItem();
                        int hunger = internalFood.getHealAmount(stack);
                        hungerSaturationMap.put(stack, hunger);
                    }
                }

                for (ItemStack stack : list) {
                    if (currentStack == ItemStack.EMPTY) {
                        currentStack = stack;
                    } else {
                        int hungerVal = hungerSaturationMap.get(stack);
                        int currentHungerVal = hungerSaturationMap.get(currentStack);

                        int trueVal = hungerVal - hungDif;
                        int trueBest = currentHungerVal - hungDif;

                        if (trueVal >= 0 && trueBest >= 0) {
                            if (trueVal < trueBest) {
                                currentStack = stack;
                            }
                        }
                    }
                }
                
                if (currentStack != ItemStack.EMPTY) {
                    food = (ItemFood) currentStack.getItem();
                }

                if (food != null) {
                    food.onItemUseFinish(currentStack, world, entityPlayer);
                }
            }
        }
    }
}