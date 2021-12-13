package mythicbotany;

import mythicbotany.infuser.IInfuserRecipe;
import mythicbotany.infuser.InfuserRecipe;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;

public class ModRecipes {

    public static final IRecipeType<IInfuserRecipe> INFUSER = IRecipeType.register(MythicBotany.getInstance().modid + ":infusion");
    public static final IRecipeType<RuneRitualRecipe> RUNE_RITUAL = IRecipeType.register(MythicBotany.getInstance().modid + ":rune_ritual");

    public static final IRecipeSerializer<InfuserRecipe> INFUSER_SERIALIZER = new InfuserRecipe.Serializer();
    public static final IRecipeSerializer<RuneRitualRecipe> RUNE_RITUAL_SERIALIZER = new RuneRitualRecipe.Serializer();

    public static void register() {
        MythicBotany.getInstance().register(INFUSER.toString(), INFUSER_SERIALIZER);
        MythicBotany.getInstance().register(RUNE_RITUAL.toString(), RUNE_RITUAL_SERIALIZER);
        MythicBotany.getInstance().register(EmptyRecipe.ID.getPath(), EmptyRecipe.Serializer.INSTANCE);
    }
}
