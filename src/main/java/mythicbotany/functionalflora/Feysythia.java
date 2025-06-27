package mythicbotany.functionalflora;

import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import mythicbotany.register.tags.ModItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class Feysythia extends FunctionalFlowerBase {

    public static final List<TagKey<Item>> FEYSYTHIA_LEVELS = List.of(
            ModItemTags.FEYSYTHIA_LEVEL_1,
            ModItemTags.FEYSYTHIA_LEVEL_2,
            ModItemTags.FEYSYTHIA_LEVEL_3,
            ModItemTags.FEYSYTHIA_LEVEL_4,
            ModItemTags.FEYSYTHIA_LEVEL_5
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
                    for (int i = 0; i < FEYSYTHIA_LEVELS.size(); i++) {
                        if (stack.is(FEYSYTHIA_LEVELS.get(i))) {
                            level = i + 1;
                        }
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
