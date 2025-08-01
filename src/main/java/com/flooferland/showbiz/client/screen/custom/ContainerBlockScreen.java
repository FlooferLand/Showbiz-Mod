package com.flooferland.showbiz.client.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ContainerBlockScreen extends HandledScreen<ContainerBlockScreenHandler> {
    private static final Identifier TEXTURE = Identifier.of("textures/gui/container/generic_54.png");

    public ContainerBlockScreen(ContainerBlockScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.backgroundHeight = 114 + 6 * 18;
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // this.textRenderer.draw(this.title.getString(), 8.0F, 6.0F, Colors.BLACK, true, context.getMatrices(), buff -> 4210752);
        // this.textRenderer.draw(context.getMatrices(), this.playerInventory.getDisplayName().asString(), 8.0F, (float)(this.backgroundHeight - 96 + 2), 4210752);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // Drawing the background
        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0f, 0f, backgroundWidth, backgroundHeight, 256, 256);
        
        // Drawing the slots
        if (this.client != null) {
            for (Slot slot : handler.slots) {
                this.drawSlot(context, slot);
            }
        }
    }
}
