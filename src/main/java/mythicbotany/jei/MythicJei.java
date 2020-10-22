package mythicbotany.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mythicbotany.ModBlocks;
import mythicbotany.MythicBotany;
import mythicbotany.infuser.IInfuserRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@JeiPlugin
public class MythicJei implements IModPlugin {

    private static IJeiRuntime runtime = null;

    @Nonnull
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MythicBotany.getInstance().modid, "jeiplugin");
    }

    @Override
    public void registerCategories(@Nonnull IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(
                new InfusionCategory(registration.getJeiHelpers().getGuiHelper())
        );
    }

    @Override
    public void registerRecipes(@Nonnull IRecipeRegistration registration) {
        ClientWorld world = Minecraft.getInstance().world;
        RecipeManager recipes = Objects.requireNonNull(world).getRecipeManager();

        registration.addRecipes(recipes.getRecipes().stream().filter(recipe -> recipe instanceof IInfuserRecipe).collect(Collectors.toList()), InfusionCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(@Nonnull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.manaInfuser), InfusionCategory.UID);
    }

    @Override
    public void onRuntimeAvailable(@Nonnull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
    }

    public static void runtime(Consumer<IJeiRuntime> action) {
        if (runtime != null) {
            action.accept(runtime);
        }
    }

    public static <T> Optional<T> runtime(Function<IJeiRuntime, T> action) {
        if (runtime != null) {
            return Optional.of(action.apply(runtime));
        } else {
            return Optional.empty();
        }
    }
}
