package mythicbotany.alfheim.placement;

import io.github.noeppi_noeppi.libx.annotation.registration.RegisterClass;
import mythicbotany.alfheim.carver.AlfheimCanyonCarver;
import mythicbotany.alfheim.carver.AlfheimCaveCarver;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

@RegisterClass
public class AlfheimCarvers {
    
    public static final WorldCarver<CaveCarverConfiguration> cave = new AlfheimCaveCarver(CaveCarverConfiguration.CODEC);   
    public static final WorldCarver<CanyonCarverConfiguration> canyon = new AlfheimCanyonCarver(CanyonCarverConfiguration.CODEC);   
}
