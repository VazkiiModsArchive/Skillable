package codersafterdark.reskillable.commands;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;

public class ReskillableCmd extends CommandTreeBase {
    //TODO ResetSkill and ResetAll should really make it so any skill locks or trait requirements no longer met get their state reset as well

    public ReskillableCmd() {
        addSubcommand(new CmdIncrementSkill());
        addSubcommand(new CmdResetAll());
        addSubcommand(new CmdResetSkill());
        addSubcommand(new CmdSetSkillLevel());
        addSubcommand(new CmdToggleTrait());
    }

    @Nonnull
    @Override
    public String getName() {
        return "reskillable";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "reskillable.command.usage";
    }
}