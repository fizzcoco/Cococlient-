package com.example.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public final class SeedUtil {
    private SeedUtil() {}

    /**
     * Singleplayer / LAN host: reads the real seed from the integrated server,
     * prints it to chat and copies it to the clipboard.
     * Multiplayer: servers never send the seed to clients, so this runs /seed
     * (works only if the server lets you use that command).
     */
    public static void fetchSeed() {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        MinecraftServer server = mc.getServer();
        if (server != null && server.getOverworld() != null) {
            long seed = server.getOverworld().getSeed();
            mc.keyboard.setClipboard(String.valueOf(seed));
            mc.inGameHud.getChatHud().addMessage(
                Text.literal("[Coconut] Seed: " + seed + " (copied to clipboard)"));
        } else if (mc.getNetworkHandler() != null) {
            mc.inGameHud.getChatHud().addMessage(
                Text.literal("[Coconut] Multiplayer: asking the server with /seed..."));
            mc.getNetworkHandler().sendChatCommand("seed");
        }
    }
}
