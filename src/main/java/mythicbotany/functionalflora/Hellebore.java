package mythicbotany.functionalflora;

import mythicbotany.MythicBotany;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import vazkii.botania.api.subtile.RadiusDescriptor;

import java.util.List;

public class Hellebore extends FunctionalFlowerBase {

    public static final int MANA_PER_ENTITY_AND_SECOND = 150;

    public Hellebore(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 0xCD3EBB, false);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!level.isClientSide) {
            int prevMana = mana;

            List<AbstractPiglin> piglins = level.getEntitiesOfClass(AbstractPiglin.class, new AABB(worldPosition.getX() - 5.5, worldPosition.getY() - 2, worldPosition.getZ() - 5.5, worldPosition.getX() + 6.5, worldPosition.getY() + 3, worldPosition.getZ() + 6.5));
            for (AbstractPiglin piglin : piglins) {
                if (mana >= MANA_PER_ENTITY_AND_SECOND && piglin.timeInOverworld > 20) {
                    mana -= MANA_PER_ENTITY_AND_SECOND;
                    piglin.timeInOverworld = 0;
                    MythicBotany.getNetwork().spawnParticle(level, ParticleTypes.FLAME, 10, piglin.getX(), piglin.getY(), piglin.getZ(), 0, 0.05, 0, 0.4, 0.8, 0.4, true);
                }
            }

            List<Hoglin> hoglins = level.getEntitiesOfClass(Hoglin.class, new AABB(worldPosition.getX() - 5.5, worldPosition.getY() - 2, worldPosition.getZ() - 5.5, worldPosition.getX() + 6.5, worldPosition.getY() + 3, worldPosition.getZ() + 6.5));
            for (Hoglin hoglin : hoglins) {
                if (mana >= MANA_PER_ENTITY_AND_SECOND && hoglin.timeInOverworld > 20) {
                    mana -= MANA_PER_ENTITY_AND_SECOND;
                    hoglin.timeInOverworld = 0;
                    MythicBotany.getNetwork().spawnParticle(level, ParticleTypes.FLAME, 10, hoglin.getX(), hoglin.getY(), hoglin.getZ(), 0, 0.08, 0, 0.8, 0.7, 0.8, true);
                }
            }

            if (prevMana != mana) {
                mana = Mth.clamp(mana, 0, maxMana);
                setChanged();
            }
        }
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(worldPosition, 5);
    }
}
