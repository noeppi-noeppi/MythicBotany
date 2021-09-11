package mythicbotany.functionalflora;

import com.google.common.collect.ImmutableList;
import mythicbotany.functionalflora.base.FunctionalFlowerBase;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class Feysythia extends FunctionalFlowerBase {

    public static final ResourceLocation FEY_DUST_ITEM = new ResourceLocation("feywild", "fey_dust");
    public static final List<ResourceLocation> FEY_GEM_ITEMS = ImmutableList.of(
            new ResourceLocation("feywild", "lesser_fey_gem"),
            new ResourceLocation("feywild", "greater_fey_gem"),
            new ResourceLocation("feywild", "shiny_fey_gem"),
            new ResourceLocation("feywild", "brilliant_fey_gem")
    );
    
    public Feysythia(TileEntityType<?> tileEntityType) {
        super(tileEntityType, 0x45FFAC, true);
    }

    @Override
    protected void tickFlower() {
        //noinspection ConstantConditions
        if (!world.isRemote && mana <= 0) {
            List<ItemEntity> items = world.getEntitiesWithinAABB(ItemEntity.class, new AxisAlignedBB(pos.add(-1, -1, -1), pos.add(2, 2, 2)));
            for (ItemEntity ie : items) {
                ItemStack stack = ie.getItem();
                if (!stack.isEmpty()) {
                    int level = 0;
                    if (FEY_DUST_ITEM.equals(stack.getItem().getRegistryName())) {
                        level = 1;
                    } else if (FEY_GEM_ITEMS.contains(stack.getItem().getRegistryName())) {
                        level = 1 + FEY_GEM_ITEMS.indexOf(stack.getItem().getRegistryName());
                    }
                    if (level > 0) {
                        stack.shrink(1);
                        if (stack.isEmpty()) {
                            ie.remove();
                        } else {
                            ie.setItem(stack);
                        }
                        mana = MathHelper.clamp((int) Math.ceil(Math.sqrt(level * 1.3) * 263), 0, maxMana);
                        didWork = true;
                    }
                }
            }
        }
    }
}
