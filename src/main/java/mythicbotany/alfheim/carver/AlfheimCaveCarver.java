package mythicbotany.alfheim.carver;

import com.mojang.serialization.Codec;
import mythicbotany.alfheim.placement.AlfheimWorldGen;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.carver.CaveCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CaveWorldCarver;

import javax.annotation.Nonnull;
import java.util.Random;

public class AlfheimCaveCarver extends CaveWorldCarver {
    
    private final Random random;

    public AlfheimCaveCarver(Codec<CaveCarverConfiguration> codec) {
        super(codec);
        this.random = new Random();
    }

    @Override
    protected boolean canReplaceBlock(@Nonnull BlockState state) {
        return AlfheimWorldGen.alfheimStone.test(state, this.random);
    }
}
