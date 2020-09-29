package mythicbotany.recipes;

import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;

public class RecipeTypes {
    public static final IRecipeType<IInfuserRecipe> INFUSER_TYPE = IRecipeType.register(RecipeInfuser.TYPE_ID.toString());
    public static final IRecipeSerializer<RecipeInfuser> INFUSER_SERIALIZER = new RecipeInfuser.Serializer();

    public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        Registry.register(Registry.RECIPE_TYPE, RecipeInfuser.TYPE_ID, INFUSER_TYPE);
        event.getRegistry().register(INFUSER_SERIALIZER.setRegistryName(RecipeInfuser.TYPE_ID));
    }
}
