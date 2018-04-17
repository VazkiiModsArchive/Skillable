package codersafterdark.reskillable.skill.mining;

import codersafterdark.reskillable.api.unlockable.Trait;
import codersafterdark.reskillable.base.ConditionHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import static codersafterdark.reskillable.lib.LibMisc.MOD_ID;

public class TraitObsidianSmasher extends Trait {

    public TraitObsidianSmasher() {
        super(new ResourceLocation(MOD_ID, "obsidian_smasher"), 1, 2, new ResourceLocation(MOD_ID, "mining"),
                4, "reskillable:mining|16");
    }

    @Override
    public void getBreakSpeed(BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = event.getState();

        if (state.getBlock() == Blocks.OBSIDIAN && ConditionHelper.hasRightTool(player, state, "pickaxe", ToolMaterial.DIAMOND.getHarvestLevel())) {
            event.setNewSpeed(event.getOriginalSpeed() * 10);
        }
    }

}
