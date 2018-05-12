package codersafterdark.reskillable.commands;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CmdResetSkill extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "resetskill";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "reskillable.command.resetskill.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        //going to make a resetskill command, (which I will also have it remove any attributes you have in that skill).
        // A setskilllevel. A command of some sort to toggle having specific traits, and then a command that basically just resets everything for a player
        //TODO Change usage and tab complete to be /reskillable resetskill <player> <skill>
        if (args.length == 0) {
            //TODO error message must enter a skill
            return;
        }
        ResourceLocation skillName = new ResourceLocation(args[0]);
        if (!ReskillableRegistries.SKILLS.containsKey(skillName)) {
            //TODO error message invalid skill
            return;
        }
        Skill skill = ReskillableRegistries.SKILLS.getValue(skillName);

    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        List<String> skillNames = new ArrayList<>();
        String partial = args.length == 0 ? "" : args[0];
        for (Skill skill : ReskillableRegistries.SKILLS.getValuesCollection()) {
            String skillName = skill.getKey();
            if (skillName.startsWith(partial)) {
                skillNames.add(skillName);
            }
        }
        return skillNames;
    }
}