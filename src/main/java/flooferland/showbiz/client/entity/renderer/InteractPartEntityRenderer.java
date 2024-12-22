package flooferland.showbiz.client.entity.renderer;

import flooferland.showbiz.backend.entity.custom.InteractPartEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
    
    /*@Override
    public Identifier getTexture(InteractPartEntity entity) {
        //noinspection deprecation
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }*/

    // region | Label nameplate rendering
    @Override
    protected boolean hasLabel(InteractPartEntity entity, double squaredDistanceToCamera) {
        var client = MinecraftClient.getInstance();
        var hitResult = client.crosshairTarget;

        return (hitResult != null && hitResult.squaredDistanceTo(entity) < 0.015f) && entity.name != null || ALWAYS_SHOW_INTERACTION_LABELS;
    }
    @Override
    protected void renderLabelIfPresent(InteractPartEntityRenderState state, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.renderLabelIfPresent(state, text, matrices, vertexConsumers, light);
    }
    // endregion
}
