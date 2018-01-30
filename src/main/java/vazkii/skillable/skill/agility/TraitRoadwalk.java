package vazkii.skillable.skill.agility;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import vazkii.skillable.skill.base.Trait;

public class TraitRoadwalk extends Trait {

    public TraitRoadwalk() {
        super("roadwalk", 1, 1, 6, "agility:12,building:8");
    }

    @Override
    public void onPlayerTick(PlayerTickEvent event) {
        BlockPos pos = event.player.getPosition().down();
        if (event.player.world.getBlockState(pos).getBlock() == Blocks.GRASS_PATH && event.player.onGround && event.player.moveForward > 0)
            event.player.moveRelative(0F, 0F, 1F, 0.05F);
    }


}
