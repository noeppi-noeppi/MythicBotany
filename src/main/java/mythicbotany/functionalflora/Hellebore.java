package mythicbotany.functionalflora;

import mythicbotany.MythicBotany;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.block_entity.RadiusDescriptor;

import java.util.List;

public class Hellebore extends FunctionalFlowerBase {

    public static final int MANA_PER_ENTITY_AND_SECOND = 150;

    public Hellebore(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 0xCD3EBB, false);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!this.level.isClientSide) {
            int prevMana = this.mana;

            List<AbstractPiglin> piglins = this.level.getEntitiesOfClass(AbstractPiglin.class, new AABB(this.worldPosition.getX() - 5.5, this.worldPosition.getY() - 2, this.worldPosition.getZ() - 5.5, this.worldPosition.getX() + 6.5, this.worldPosition.getY() + 3, this.worldPosition.getZ() + 6.5));
            for (AbstractPiglin piglin : piglins) {
                if (this.mana >= MANA_PER_ENTITY_AND_SECOND && piglin.timeInOverworld > 20) {
                    this.mana -= MANA_PER_ENTITY_AND_SECOND;
                    piglin.timeInOverworld = 0;
                    MythicBotany.getNetwork().spawnParticle(this.level, ParticleTypes.FLAME, 10, piglin.getX(), piglin.getY(), piglin.getZ(), 0, 0.05, 0, 0.4, 0.8, 0.4, true);
                }
            }

            List<Hoglin> hoglins = this.level.getEntitiesOfClass(Hoglin.class, new AABB(this.worldPosition.getX() - 5.5, this.worldPosition.getY() - 2, this.worldPosition.getZ() - 5.5, this.worldPosition.getX() + 6.5, this.worldPosition.getY() + 3, this.worldPosition.getZ() + 6.5));
            for (Hoglin hoglin : hoglins) {
                if (this.mana >= MANA_PER_ENTITY_AND_SECOND && hoglin.timeInOverworld > 20) {
                    this.mana -= MANA_PER_ENTITY_AND_SECOND;
                    hoglin.timeInOverworld = 0;
                    MythicBotany.getNetwork().spawnParticle(this.level, ParticleTypes.FLAME, 10, hoglin.getX(), hoglin.getY(), hoglin.getZ(), 0, 0.08, 0, 0.8, 0.7, 0.8, true);
                }
            }

            if (prevMana != this.mana) {
                this.mana = Mth.clamp(this.mana, 0, this.maxMana);
                this.setChanged();
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public RadiusDescriptor getRadius() {
        return RadiusDescriptor.Rectangle.square(this.worldPosition, 5);
    }
}
