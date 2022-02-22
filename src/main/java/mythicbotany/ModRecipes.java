package mythicbotany;

import mythicbotany.infuser.IInfuserRecipe;
import mythicbotany.infuser.InfuserRecipe;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.core.Registry;

import java.util.Objects;

public class ModRecipes {

    public static final RecipeType<IInfuserRecipe> INFUSER = RecipeType.register(MythicBotany.getInstance().modid + ":infusion");
    public static final RecipeType<RuneRitualRecipe> RUNE_RITUAL = RecipeType.register(MythicBotany.getInstance().modid + ":rune_ritual");

    public static final RecipeSerializer<InfuserRecipe> INFUSER_SERIALIZER = new InfuserRecipe.Serializer();
    public static final RecipeSerializer<RuneRitualRecipe> RUNE_RITUAL_SERIALIZER = new RuneRitualRecipe.Serializer();

    public static void register() {
        MythicBotany.getInstance().register(Objects.requireNonNull(Registry.RECIPE_TYPE.getKey(INFUSER)).getPath(), INFUSER_SERIALIZER);
        MythicBotany.getInstance().register(Objects.requireNonNull(Registry.RECIPE_TYPE.getKey(RUNE_RITUAL)).getPath(), RUNE_RITUAL_SERIALIZER);
        // TODO remove
        MythicBotany.getInstance().register(EmptyRecipe.ID.getPath(), EmptyRecipe.Serializer.INSTANCE);
    }
}
