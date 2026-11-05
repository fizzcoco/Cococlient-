package com.example.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;

/** Simple module menu: one toggle button per module. Esc closes it. */
public class ClickGuiScreen extends Screen {
    private static final int BTN_W = 110;
    private static final int BTN_H = 20;
    private static final int GAP = 6;
    private static final int COLUMNS = 3;

    public ClickGuiScreen() {
        super(Text.literal("Coconut Client"));
    }

    @Override
    protected void init() {
        List<Module> modules = CoconutClient.MODULES.getModules();
        int cols = Math.min(COLUMNS, Math.max(1, modules.size()));
        int totalW = cols * BTN_W + (cols - 1) * GAP;
        int startX = (this.width - totalW) / 2;
        int startY = 50;

        for (int i = 0; i < modules.size(); i++) {
            Module m = modules.get(i);
            int x = startX + (i % cols) * (BTN_W + GAP);
            int y = startY + (i / cols) * (BTN_H + GAP);

            ButtonWidget[] ref = new ButtonWidget[1];
            ref[0] = ButtonWidget.builder(label(m), btn -> {
                m.toggle();
                ref[0].setMessage(label(m));
            }).dimensions(x, y, BTN_W, BTN_H).build();
            this.addDrawableChild(ref[0]);
        }

        int rows = (modules.size() + cols - 1) / cols;
        int seedY = startY + rows * (BTN_H + GAP) + 10;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Auto Mine..."), btn ->
            this.client.setScreen(new AutoMineScreen())
        ).dimensions(startX, seedY, totalW, BTN_H).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Get Seed"), btn -> {
            SeedUtil.fetchSeed();
            this.close();
        }).dimensions(startX, seedY + BTN_H + GAP, totalW, BTN_H).build());
    }

    private static Text label(Module m) {
        return Text.literal(m.getName() + ": " + (m.isEnabled() ? "ON" : "OFF"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 25, 0xFFFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
