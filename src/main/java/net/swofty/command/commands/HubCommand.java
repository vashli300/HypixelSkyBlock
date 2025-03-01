package net.swofty.command.commands;

import net.swofty.command.CommandParameters;
import net.swofty.command.SkyBlockCommand;
import net.swofty.user.SkyBlockPlayer;
import net.swofty.user.categories.Rank;

@CommandParameters(aliases = "h",
        description = "Sends the player to their hub",
        usage = "/hub",
        permission = Rank.DEFAULT,
        allowsConsole = false)
public class HubCommand extends SkyBlockCommand {
    @Override
    public void run(MinestomCommand command) {
        command.addSyntax((sender, context) -> {
            SkyBlockPlayer player = ((SkyBlockPlayer) sender);
            player.sendToHub();
        });
    }
}
