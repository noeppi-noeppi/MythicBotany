package mythicbotany.data.recipes;

import org.moddingx.libx.datagen.provider.recipe.RecipeProviderBase;
import org.moddingx.libx.mod.ModX;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModItems;
import mythicbotany.data.recipes.extension.PetalExtension;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import java.util.List;

public class FeywildRecipeProvider extends RecipeProviderBase implements PetalExtension {

    public FeywildRecipeProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    protected void setup() {
        this.petalApothecary(ModBlocks.feysythia, this.petal(DyeColor.YELLOW), this.petal(DyeColor.YELLOW), this.petal(DyeColor.PURPLE), Ingredient.of(ModItems.dreamCherry));
    }

    @Override
    protected List<ICondition> conditions() {
        return List.of(new ModLoadedCondition("feywild"));
    }
}
