package codersafterdark.reskillable.skill.farming;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.base.LevelLockHandler;
import codersafterdark.reskillable.lib.LibMisc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TraitHungryFarmer extends Trait {
    public TraitHungryFarmer() {
        super(new ResourceLocation(LibMisc.MOD_NAME, "hungry_farmer"), 2, 3, new ResourceLocation(LibMisc.MOD_ID, "farming"), 8, "reskillable:farming|32");
    }

    @Override
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (player == null || player.isCreative() || player.isSpectator()) {
            //If it is somehow null or they are not in a mode they can eat from don't do anything
            return;
        }
        PlayerData data = PlayerDataHandler.get(player);
        if (data != null && data.getSkillInfo(getParentSkill()).isUnlocked(this)) {
            int playerHunger = player.getFoodStats().getFoodLevel();
            if (playerHunger < 20) {
                NonNullList<ItemStack> inventoryList = player.inventoryContainer.getInventory();
                ItemStack currentStack = ItemStack.EMPTY;
                int hungerNeeded = 20 - playerHunger;
                int bestHungerPoints = 0;

                for (ItemStack stack : inventoryList) {
                    if (!stack.isEmpty() && stack.getItem() instanceof ItemFood && LevelLockHandler.canPlayerUseItem(player, stack)) {
                        int hungerPoints = ((ItemFood) stack.getItem()).getHealAmount(stack);
                        if (currentStack.isEmpty() || hungerPoints < bestHungerPoints && hungerPoints >= hungerNeeded ||
                                hungerPoints > bestHungerPoints && bestHungerPoints < hungerNeeded) {
                            //No food item yet OR
                            //The food is less filling but will still make you full OR
                            //The current piece won't fill you and this piece will fill you more
                            currentStack = stack;
                            bestHungerPoints = hungerPoints;
                        }
                    }
                }

                if (!currentStack.isEmpty()) {
                    currentStack.getItem().onItemUseFinish(currentStack, player.getEntityWorld(), player);
                }
            }
        }
    }
}