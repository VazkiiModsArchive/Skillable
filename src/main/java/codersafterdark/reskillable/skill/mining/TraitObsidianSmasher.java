package codersafterdark.reskillable.skill.mining;

import codersafterdark.reskillable.base.ConditionHelper;
import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

public class TraitObsidianSmasher extends Trait {

    public TraitObsidianSmasher() {
        super("obsidian_smasher", 1, 2, 4, "mining:16");
    }

    @Override
    public void getBreakSpeed(BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = event.getState();

        if (state.getBlock() == Blocks.OBSIDIAN && ConditionHelper.hasRightTool(player, state, "pickaxe", ToolMaterial.DIAMOND.getHarvestLevel()))
            event.setNewSpeed(event.getOriginalSpeed() * 10);
    }

}
