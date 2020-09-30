package mythicbotany;

import mythicbotany.recipes.IInfuserRecipe;
import mythicbotany.recipes.InfuserRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;

public class ModRecipes {

    public static final IRecipeType<IInfuserRecipe> INFUSER = IRecipeType.register("infusion");

    public static final IRecipeSerializer<InfuserRecipe> INFUSER_SERIALIZER = new InfuserRecipe.Serializer();

    public static void register() {
        MythicBotany.register(INFUSER.toString(), INFUSER_SERIALIZER);
    }
}
