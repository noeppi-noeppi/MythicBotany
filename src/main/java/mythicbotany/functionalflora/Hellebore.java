package mythicbotany.functionalflora;

import mythicbotany.MythicBotany;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.monster.piglin.AbstractPiglinEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import vazkii.botania.api.subtile.RadiusDescriptor;

import java.util.List;

public class Hellebore extends FunctionalFlowerBase {

    public static final int MANA_PER_ENTITY_AND_SECOND = 150;

    public Hellebore(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn, 0xCD3EBB, false);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!world.isRemote) {
            int prevMana = mana;

            List<AbstractPiglinEntity> piglins = world.getEntitiesWithinAABB(AbstractPiglinEntity.class, new AxisAlignedBB(pos.getX() - 5.5, pos.getY() - 2, pos.getZ() - 5.5, pos.getX() + 6.5, pos.getY() + 3, pos.getZ() + 6.5));
            for (AbstractPiglinEntity piglin : piglins) {
                if (mana >= MANA_PER_ENTITY_AND_SECOND && piglin.field_242334_c > 20) {
                    mana -= MANA_PER_ENTITY_AND_SECOND;
                    piglin.field_242334_c = 0;
                    MythicBotany.getNetwork().spawnParticle(world, ParticleTypes.FLAME, 10, piglin.getPosX(), piglin.getPosY(), piglin.getPosZ(), 0, 0.05, 0, 0.4, 0.8, 0.4, true);
                }
            }

            List<HoglinEntity> hoglins = world.getEntitiesWithinAABB(HoglinEntity.class, new AxisAlignedBB(pos.getX() - 5.5, pos.getY() - 2, pos.getZ() - 5.5, pos.getX() + 6.5, pos.getY() + 3, pos.getZ() + 6.5));
            for (HoglinEntity hoglin : hoglins) {
                if (mana >= MANA_PER_ENTITY_AND_SECOND && hoglin.field_234358_by_ > 20) {
                    mana -= MANA_PER_ENTITY_AND_SECOND;
                    hoglin.field_234358_by_ = 0;
                    MythicBotany.getNetwork().spawnParticle(world, ParticleTypes.FLAME, 10, hoglin.getPosX(), hoglin.getPosY(), hoglin.getPosZ(), 0, 0.08, 0, 0.8, 0.7, 0.8, true);
                }
            }

            if (prevMana != mana) {
                mana = MathHelper.clamp(mana, 0, maxMana);
                markDirty();
            }
        }
    }

    @Override
    public RadiusDescriptor getRadius() {
        return new RadiusDescriptor.Square(pos, 5);
    }
}
