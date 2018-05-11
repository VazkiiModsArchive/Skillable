package codersafterdark.reskillable.skill.agility;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitHillWalker extends Trait {
    public TraitHillWalker() {
        super(new ResourceLocation(MOD_ID, "hillwalker"), 2, 2, new ResourceLocation(MOD_ID, "agility"), 8, "reskillable:agility|32");
        if (FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @SubscribeEvent
    public void onPlayerUpdate(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        PlayerData data = PlayerDataHandler.get(player);
        if (data.getSkillInfo(getParentSkill()).isUnlocked(this)) {
            if (player.isSneaking()) {
                player.stepHeight = 0.9F;
            } else {
                player.stepHeight = 1.0F + (1F / 16F);
            }
        }
    }

}
