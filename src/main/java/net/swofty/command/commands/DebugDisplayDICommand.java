package net.swofty.command.commands;

import net.minestom.server.color.Color;
import net.minestom.server.command.builder.arguments.ArgumentBoolean;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.number.ArgumentInteger;
import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;
import net.swofty.utility.DamageIndicator;

import java.awt.*;

@CommandParameters(aliases = "dmgindicdisplaydebugcmd",
        description = "Display damage indic",
        usage = "/dmgindicdisplaydebugcmd <dmg> <crit>",
        permission = Rank.ADMIN,
        allowsConsole = false)
public class DebugDisplayDICommand extends SkyBlockCommand
{
      @Override
      public void run(MinestomCommand command) {
            ArgumentBoolean crit = ArgumentType.Boolean("critical");
            ArgumentInteger damage = ArgumentType.Integer("damage");

            command.addSyntax((sender, context) -> {
                  SkyBlockPlayer player = (SkyBlockPlayer) sender;

                  new DamageIndicator()
                          .damage(context.get(damage))
                          .pos(player.getPosition())
                          .critical(context.get(crit))
                          .display();

            }, damage, crit);
      }
}
