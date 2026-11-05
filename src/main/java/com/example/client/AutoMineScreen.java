package com.example.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

/** Type an item (diamond, iron, ancient_debris, oak_log...) and hit Start. */
public class AutoMineScreen extends Screen {
    private TextFieldWidget input;

    public AutoMineScreen() {
        super(Text.literal("Auto Mine (Baritone)"));
    }

    @Override
    protected void init() {
        int w = 200;
        int x = (this.width - w) / 2;

        input = new TextFieldWidget(this.textRenderer, x, 70, w, 20, Text.literal("Item"));
        input.setMaxLength(64);
        input.setPlaceholder(Text.literal("diamond, iron, oak_log..."));
        this.addDrawableChild(input);
        this.setInitialFocus(input);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Start"), b -> {
            BaritoneUtil.mine(input.getText());
            this.close();
        }).dimensions(x, 100, 96, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Stop"), b -> {
            BaritoneUtil.stop();
            this.close();
        }).dimensions(x + 104, 100, 96, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 40, 0xFFFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
