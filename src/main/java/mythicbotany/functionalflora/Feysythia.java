package mythicbotany.functionalflora;

import com.google.common.collect.ImmutableList;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class Feysythia extends FunctionalFlowerBase {

    public static final ResourceLocation FEY_DUST_ITEM = new ResourceLocation("feywild", "fey_dust");
    public static final List<ResourceLocation> FEY_GEM_ITEMS = ImmutableList.of(
            new ResourceLocation("feywild", "lesser_fey_gem"),
            new ResourceLocation("feywild", "greater_fey_gem"),
            new ResourceLocation("feywild", "shiny_fey_gem"),
            new ResourceLocation("feywild", "brilliant_fey_gem")
    );
    
    public Feysythia(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 0x45FFAC, true);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!this.level.isClientSide && this.mana <= 0) {
            List<ItemEntity> items = this.level.getEntitiesOfClass(ItemEntity.class, new AABB(this.worldPosition.offset(-1, -1, -1), this.worldPosition.offset(2, 2, 2)));
            for (ItemEntity ie : items) {
                ItemStack stack = ie.getItem();
                if (!stack.isEmpty()) {
                    int level = 0;
                    ResourceLocation id = ForgeRegistries.ITEMS.getKey(stack.getItem());
                    if (FEY_DUST_ITEM.equals(id)) {
                        level = 1;
                    } else if (FEY_GEM_ITEMS.contains(id)) {
                        level = 1 + FEY_GEM_ITEMS.indexOf(id);
                    }
                    if (level > 0) {
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            ie.remove(Entity.RemovalReason.DISCARDED);
                        } else {
                            ie.setItem(stack);
                        }
                        this.mana = Mth.clamp((int) Math.ceil(Math.sqrt(level * 1.3) * 263), 0, this.maxMana);
                        this.didWork = true;
                    }
                }
            }
        }
    }
}
