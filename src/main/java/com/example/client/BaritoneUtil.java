package com.example.client;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Map;

/**
 * Drives the Baritone mod through its chat commands (#mine / #cancel).
 * This keeps Coconut free of a compile-time Baritone dependency, so it can't
 * break when Baritone's API changes. Baritone itself must be installed in /mods.
 */
public final class BaritoneUtil {
    private BaritoneUtil() {}

    /** Friendly item names -> the block(s) Baritone should mine. */
    private static final Map<String, String> ITEM_TO_BLOCKS = Map.ofEntries(
        Map.entry("diamond",  "diamond_ore deepslate_diamond_ore"),
        Map.entry("iron",     "iron_ore deepslate_iron_ore"),
        Map.entry("gold",     "gold_ore deepslate_gold_ore nether_gold_ore"),
        Map.entry("coal",     "coal_ore deepslate_coal_ore"),
        Map.entry("copper",   "copper_ore deepslate_copper_ore"),
        Map.entry("emerald",  "emerald_ore deepslate_emerald_ore"),
        Map.entry("lapis",    "lapis_ore deepslate_lapis_ore"),
        Map.entry("redstone", "redstone_ore deepslate_redstone_ore"),
        Map.entry("quartz",   "nether_quartz_ore"),
        Map.entry("netherite","ancient_debris"),
        Map.entry("debris",   "ancient_debris")
    );

    public static boolean isInstalled() {
        return FabricLoader.getInstance().isModLoaded("baritone");
    }

    public static void mine(String item) {
        String key = item.trim().toLowerCase().replace(' ', '_');
        if (key.isEmpty()) return;
        // Known item -> its ores; otherwise treat the input as a block id (e.g. oak_log, sand)
        String blocks = ITEM_TO_BLOCKS.getOrDefault(key, key);
        send("#mine " + blocks, "Mining: " + key);
    }

    public static void stop() {
        send("#cancel", "Stopped Baritone.");
    }

    private static void send(String baritoneCommand, String info) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        // Never send "#..." to a server's public chat if Baritone isn't there to catch it.
        if (!isInstalled()) {
            mc.inGameHud.getChatHud().addMessage(Text.literal(
                "[Coconut] Baritone isn't installed. Put the Baritone jar for 1.21.11 in your mods folder."));
            return;
        }
        mc.getNetworkHandler().sendChatMessage(baritoneCommand);
        mc.inGameHud.getChatHud().addMessage(Text.literal("[Coconut] " + info));
    }
}
