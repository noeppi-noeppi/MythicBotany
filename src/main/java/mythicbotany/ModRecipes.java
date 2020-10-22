package mythicbotany;

import mythicbotany.infuser.IInfuserRecipe;
import mythicbotany.infuser.InfuserRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;

public class ModRecipes {

    public static final IRecipeType<IInfuserRecipe> INFUSER = IRecipeType.register("infusion");

    public static final IRecipeSerializer<InfuserRecipe> INFUSER_SERIALIZER = new InfuserRecipe.Serializer();

    public static void register() {
        MythicBotany.getInstance().register(INFUSER.toString(), INFUSER_SERIALIZER);
    }
}
