package flooferland.showbiz.client.entity.renderer;

import flooferland.showbiz.backend.entity.custom.InteractPartEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

// TODO: Render some text regarding the control's name (look at TextDisplayEntityRenderer)

@Environment(EnvType.CLIENT)
public class InteractPartEntityRenderer extends EntityRenderer<InteractPartEntity> {    
    public InteractPartEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
    }
    
    @Override
    public Identifier getTexture(InteractPartEntity entity) {
        //noinspection deprecation
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    // region | Label nameplate rendering
    @Override
    protected boolean hasLabel(InteractPartEntity entity) {
        return entity.name != null;
    }
    @Override
    protected void renderLabelIfPresent(InteractPartEntity entity, Text entityName, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta) {
        super.renderLabelIfPresent(entity, entityName, matrices, vertexConsumers, light, tickDelta);
    }
    // endregion
}
