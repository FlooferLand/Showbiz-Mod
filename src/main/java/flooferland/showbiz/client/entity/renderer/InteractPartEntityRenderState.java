package flooferland.showbiz.client.entity.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class InteractPartEntityRenderState extends EntityRenderState {
	public Vec3d entityPosition = null;
	public Text partName = Text.empty();
}
