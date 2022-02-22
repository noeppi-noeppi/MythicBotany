package mythicbotany;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import mythicbotany.alfheim.entity.AlfPixie;
import mythicbotany.alfheim.entity.AlfPixieRender;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

@RegisterClass
public class ModEntities {

    public static final EntityType<AlfPixie> alfPixie = EntityType.Builder.<AlfPixie>of(AlfPixie::new, MobCategory.CREATURE).sized(1, 1).setUpdateInterval(3).setTrackingRange(16).setShouldReceiveVelocityUpdates(true).build(MythicBotany.getInstance().modid + "_alf_pixie");

    public static void setup() {
        SpawnPlacements.register(alfPixie, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, AlfPixie::canSpawnAt);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void clientSetup() {
        EntityRenderers.register(alfPixie, AlfPixieRender::new);
    }
    
    public static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(alfPixie, AlfPixie.entityAttributes());
    }
}
