package mythicbotany;

import io.github.noeppi_noeppi.libx.annotation.RegisterClass;
import mythicbotany.alfheim.entity.AlfPixie;
import mythicbotany.alfheim.entity.AlfPixieRender;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

@RegisterClass
public class ModEntities {

    public static final EntityType<AlfPixie> alfPixie = EntityType.Builder.<AlfPixie>create(AlfPixie::new, EntityClassification.CREATURE).size(1, 1).setUpdateInterval(3).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).build(MythicBotany.getInstance().modid + "_alf_pixie");

    public static void setup() {
        GlobalEntityTypeAttributes.put(alfPixie, AlfPixie.entityAttributes());
        
        EntitySpawnPlacementRegistry.register(alfPixie, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AlfPixie::canSpawnAt);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        RenderingRegistry.registerEntityRenderingHandler(alfPixie, AlfPixieRender::new);
    }
}
