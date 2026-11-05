package com.example.client;

import com.example.client.modules.AutoAnchorSafeModule;
import com.example.client.modules.AutoMaceModule;
import com.example.client.modules.AutoTotemModule;
import com.example.client.modules.BlockEspModule;
import com.example.client.modules.ElytraHopModule;
import com.example.client.modules.ItemEspModule;
import com.example.client.modules.KillAuraModule;
import com.example.client.modules.PlayerEspModule;
import com.example.client.modules.XRayModule;
import com.example.client.modules.FlyModule;
import com.example.client.modules.FullbrightModule;
import com.example.client.modules.SprintModule;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class CoconutClient implements ClientModInitializer {
    public static final ModuleManager MODULES = new ModuleManager();
    private boolean tabWasDown = false;

    @Override
    public void onInitializeClient() {
        MODULES.register(new SprintModule());
        MODULES.register(new FullbrightModule());
        MODULES.register(new FlyModule());
        MODULES.register(new AutoMaceModule());
        MODULES.register(new PlayerEspModule());
        MODULES.register(new ItemEspModule());
        MODULES.register(new BlockEspModule());
        MODULES.register(new KillAuraModule());
        MODULES.register(new XRayModule());
        MODULES.register(new ElytraHopModule());
        MODULES.register(new AutoTotemModule());
        MODULES.register(new AutoAnchorSafeModule());

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            MODULES.tick();
            handleGuiKey(client);
        });

        // Usage: /toggle <module>   and   /modules
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("toggle")
                .then(ClientCommandManager.argument("module", StringArgumentType.word())
                    .suggests((ctx, builder) -> {
                        MODULES.getModules().forEach(m -> builder.suggest(m.getName()));
                        return builder.buildFuture();
                    })
                    .executes(ctx -> {
                        String name = StringArgumentType.getString(ctx, "module");
                        MODULES.get(name).ifPresentOrElse(m -> {
                            m.toggle();
                            ctx.getSource().sendFeedback(Text.literal(
                                m.getName() + (m.isEnabled() ? " enabled" : " disabled")));
                        }, () -> ctx.getSource().sendFeedback(Text.literal("No such module: " + name)));
                        return 1;
                    })));

            dispatcher.register(ClientCommandManager.literal("automine")
                .then(ClientCommandManager.literal("stop").executes(ctx -> {
                    BaritoneUtil.stop();
                    return 1;
                }))
                .then(ClientCommandManager.argument("item", StringArgumentType.greedyString())
                    .executes(ctx -> {
                        BaritoneUtil.mine(StringArgumentType.getString(ctx, "item"));
                        return 1;
                    })));

            dispatcher.register(ClientCommandManager.literal("modules").executes(ctx -> {
                MODULES.getModules().forEach(m -> ctx.getSource().sendFeedback(
                    Text.literal(m.getName() + ": " + (m.isEnabled() ? "ON" : "off"))));
                return 1;
            }));
        });
    }

    /** Opens the module menu on Tab (rising edge, only when no other screen is open). */
    private void handleGuiKey(MinecraftClient client) {
        if (client.player == null) return;
        boolean down = GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_TAB) == GLFW.GLFW_PRESS;
        if (down && !tabWasDown && client.currentScreen == null) {
            client.setScreen(new ClickGuiScreen());
        }
        tabWasDown = down;
    }
}
