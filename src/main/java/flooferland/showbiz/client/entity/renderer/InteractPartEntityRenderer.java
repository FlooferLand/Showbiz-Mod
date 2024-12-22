package flooferland.showbiz.client.entity.renderer;

import flooferland.showbiz.backend.entity.custom.InteractPartEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardState;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import static flooferland.showbiz.backend.block.custom.ShowSelectorBlock.ALWAYS_SHOW_INTERACTION_LABELS;

// TODO: Render some text regarding the control's name (look at TextDisplayEntityRenderer)

@Environment(EnvType.CLIENT)
public class InteractPartEntityRenderer extends EntityRenderer<InteractPartEntity, InteractPartEntityRenderState> {    
    public InteractPartEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public InteractPartEntityRenderState createRenderState() {
        return new InteractPartEntityRenderState();
    }

    // region | Label nameplate rendering
    @Override
    public void updateRenderState(InteractPartEntity entity, InteractPartEntityRenderState state, float tickDelta) {
        super.updateRenderState(entity, state, tickDelta);
        state.entityPosition = entity.getPos();
        state.partName = entity.getName();
    }
    
    protected boolean isEntityHovered(InteractPartEntityRenderState state) {
        var client = MinecraftClient.getInstance();
        var hitResult = client.crosshairTarget;
        return (hitResult != null && (state.entityPosition != null ? hitResult.getPos().squaredDistanceTo(state.entityPosition) < 0.0055f : false));
    }

    @Override
    protected boolean hasLabel(InteractPartEntity entity, double squaredDistanceToCamera) {
        return false;
    }

    @Override
    public void render(InteractPartEntityRenderState state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        var text = (state.partName == Text.EMPTY) ? Text.of("ERROR") : state.partName;
        if (isEntityHovered(state) || ALWAYS_SHOW_INTERACTION_LABELS) {
            final float scale = 0.008f;
            final Vec3d offset = new Vec3d(0, 0.2, 0);
            final int backgroundColour = (int) (0.5 * 255) << 24;
            
            matrices.push();
            matrices.translate(offset.x, offset.y, offset.z);
            matrices.multiply(dispatcher.getRotation());
            matrices.scale(scale, -scale, scale);
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            
            TextRenderer textRenderer = getTextRenderer();
            float xOffset = (-textRenderer.getWidth(text)) / 2.0F;
            textRenderer.draw(text, xOffset, 0f, -2130706433, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, backgroundColour, light);
            textRenderer.draw(text, xOffset, 0f, -1, false, matrix4f, vertexConsumers, TextRenderer.TextLayerType.NORMAL, 0, LightmapTextureManager.applyEmission(light, 2));

            matrices.pop();
        }
    }
    // endregion
}
