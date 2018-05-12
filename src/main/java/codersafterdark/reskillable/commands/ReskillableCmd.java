package codersafterdark.reskillable.commands;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

import javax.annotation.Nonnull;

public class ReskillableCmd extends CommandTreeBase {
    //TODO @p and @a support for commands

    public ReskillableCmd() {
        addSubcommand(new CmdResetSkill());
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