package mythicbotany.data.recipes;

import com.feywild.feywild.item.ModItems;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.provider.recipe.RecipeProviderBase;
import mythicbotany.register.ModBlocks;
import mythicbotany.data.recipes.extension.PetalExtension;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import java.util.List;

public class FeywildRecipeProvider extends RecipeProviderBase implements PetalExtension {

    public FeywildRecipeProvider(DatagenContext ctx) {
        super(ctx);
    }

    @Override
    protected void setup() {
        this.petalApothecary(ModBlocks.feysythia, this.petal(DyeColor.YELLOW), this.petal(DyeColor.YELLOW), this.petal(DyeColor.PURPLE), Ingredient.of(ModItems.lesserFeyGem));
    }

    @Override
    protected List<ICondition> conditions() {
        return List.of(new ModLoadedCondition("feywild"));
    }
}
