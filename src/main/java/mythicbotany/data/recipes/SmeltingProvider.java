package mythicbotany.data.recipes;

import io.github.noeppi_noeppi.libx.data.provider.recipe.SmeltingProviderBase;
import io.github.noeppi_noeppi.libx.mod.ModX;
import mythicbotany.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import vazkii.botania.common.item.ModItems;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public class SmeltingProvider extends SmeltingProviderBase {

    public SmeltingProvider(ModX mod, DataGenerator generatorIn) {
        super(mod, generatorIn);
    }

    @Override
    protected void registerRecipes(@Nonnull Consumer<IFinishedRecipe> consumer) {
        blasting(consumer, ModBlocks.elementiumOre, ModItems.elementium, 0.7f, 200);
        blasting(consumer, ModBlocks.dragonstoneOre, ModItems.dragonstone, 0.7f, 200);
    }
}
