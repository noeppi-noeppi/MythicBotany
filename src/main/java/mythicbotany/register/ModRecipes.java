package mythicbotany.register;

import mythicbotany.infuser.InfuserRecipe;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.moddingx.libx.annotation.registration.RegisterClass;
import org.moddingx.libx.registration.Registerable;
import org.moddingx.libx.registration.RegistrationContext;

@RegisterClass(registry = "RECIPE_TYPE")
public class ModRecipes {

    public static final RecipeType<InfuserRecipe> infuser = new MythicRecipeType<>(InfuserRecipe.Serializer.INSTANCE);
    public static final RecipeType<RuneRitualRecipe> runeRitual = new MythicRecipeType<>(RuneRitualRecipe.Serializer.INSTANCE);
    
    private static class MythicRecipeType<T extends Recipe<?>> implements RecipeType<T>, Registerable {
        
        private ResourceLocation id;
        private final RecipeSerializer<T> serializer;

        private MythicRecipeType(RecipeSerializer<T> serializer) {
            this.id = null;
            this.serializer = serializer;
        }

        @Override
        public void registerAdditional(RegistrationContext ctx, EntryCollector builder) {
            this.id = ctx.id();
            builder.register(Registries.RECIPE_SERIALIZER, this.serializer);
        }

        @Override
        public String toString() {
            return this.id == null ? super.toString() : this.id.toString();
        }
    }
}
