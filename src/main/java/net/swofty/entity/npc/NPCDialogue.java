package net.swofty.entity.npc;

import lombok.Builder;
import net.minestom.server.MinecraftServer;
import net.minestom.server.timer.Scheduler;
import net.minestom.server.timer.TaskSchedule;
import net.swofty.entity.villager.NPCVillagerDialogue;
import net.swofty.entity.villager.NPCVillagerParameters;
import net.swofty.entity.villager.SkyBlockVillagerNPC;
import net.swofty.user.SkyBlockPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class NPCDialogue extends SkyBlockNPC {
    HashMap<SkyBlockPlayer, Map.Entry<DialogueSet, CompletableFuture<String>>> dialogueSets = new HashMap<>();

    protected NPCDialogue(NPCParameters parameters) {
        super(parameters);
    }

    public abstract DialogueSet[] getDialogueSets();

    public Boolean isInDialogue(SkyBlockPlayer player) {
        Map.Entry<DialogueSet, CompletableFuture<String>> dialogueSet = dialogueSets.get(player);
        return dialogueSet != null;
    }

    public CompletableFuture<String> setDialogue(SkyBlockPlayer player, String key) {
        CompletableFuture<String> future = new CompletableFuture<>();

        for (DialogueSet dialogueSet : getDialogueSets()) {
            if (dialogueSet.key().equals(key)) {
                dialogueSets.put(player, Map.entry(dialogueSet, future));
                handleLineSendingLoop(player, dialogueSet);
                return future;
            }
        }

        future.completeExceptionally(new NullPointerException("Dialogue set with key '" + key + "' not found."));
        return future;
    }

    private void handleLineSendingLoop(SkyBlockPlayer player, DialogueSet dialogueSet) {
        sendMessage(player, dialogueSet.lines()[0]);

        String[] newLines = new String[dialogueSet.lines().length - 1];
        System.arraycopy(dialogueSet.lines(), 1, newLines, 0, dialogueSet.lines().length - 1);

        if (newLines.length == 0) {
            dialogueSets.get(player).getValue().complete(dialogueSet.key());
            dialogueSets.remove(player);
            return;
        }

        Scheduler scheduler = MinecraftServer.getSchedulerManager();
        scheduler.buildTask(() -> {
            handleLineSendingLoop(player, DialogueSet.builder().key(dialogueSet.key()).lines(newLines).build());
        }).delay(TaskSchedule.seconds(2)).schedule();
    }

    public static void remove(SkyBlockPlayer player) {
        for (SkyBlockNPC npc : SkyBlockNPC.getNpcs().keySet()) {
            if (npc instanceof NPCDialogue) {
                NPCDialogue dialogue = (NPCDialogue) npc;
                if (dialogue.isInDialogue(player)) {
                    dialogue.dialogueSets.get(player).getValue().complete(null);
                    dialogue.dialogueSets.remove(player);
                }
            }
        }
    }

    @Builder
    public record DialogueSet(String key, String[] lines) { }

}
