package mythicbotany.alfheim.carver;

import com.mojang.serialization.Codec;
import mythicbotany.alfheim.placement.AlfheimWorldGen;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CanyonWorldCarver;

import javax.annotation.Nonnull;
import java.util.Random;

public class AlfheimCanyonCarver extends CanyonWorldCarver {
    
    private final Random random;

    public AlfheimCanyonCarver(Codec<CanyonCarverConfiguration> codec) {
        super(codec);
        this.random = new Random();
    }

    @Override
    protected boolean canReplaceBlock(@Nonnull BlockState state) {
        return AlfheimWorldGen.alfheimStone.test(state, this.random);
    }
}
