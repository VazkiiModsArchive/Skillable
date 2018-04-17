package codersafterdark.reskillable.skill.agility;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitRoadWalk extends Trait {

    public TraitRoadWalk() {
        super(new ResourceLocation(MOD_ID, "roadwalk"), 1, 1, new ResourceLocation(MOD_ID, "agility"),
                6, "reskillable:agility|12", "reskillable:building|8");
    }

    @Override
    public void onPlayerTick(PlayerTickEvent event) {
        BlockPos pos = event.player.getPosition().down();
        if (event.player.world.getBlockState(pos).getBlock() == Blocks.GRASS_PATH && event.player.onGround && event.player.moveForward > 0) {
            event.player.moveRelative(0F, 0F, 1F, 0.05F);
        }
    }


}
