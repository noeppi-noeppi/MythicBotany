package mythicbotany.patchouli;

import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

public class MultipleInputsComponent implements ICustomComponent {

    @SerializedName("recipe_name")
    public String recipeName;

    protected transient List<Ingredient> ingredients;
    protected transient int x;
    protected transient int y;

    public void build(int componentX, int componentY, int pageNum) {
        this.x = componentX != -1 ? componentX : 17;
        this.y = componentY;
        this.ingredients = this.makeIngredients();
    }

    private List<Ingredient> makeIngredients() {
        AtomicReference<Recipe<?>> recipe = new AtomicReference<>(null);
        //noinspection ConstantConditions
        Minecraft.getInstance().level.getRecipeManager().byKey(new ResourceLocation(this.recipeName)).ifPresent(recipe::set);
        if (recipe.get() == null) {
            throw new RuntimeException("Missing recipe: " + this.recipeName);
        } else {
            return recipe.get().getIngredients();
        }
    }

    public void render(@Nonnull PoseStack poseStack, @Nonnull IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        int widthHalf = (20 * this.ingredients.size()) / 2;
        for (int i = 0; i < this.ingredients.size(); i++) {
            context.renderIngredient(poseStack, this.x - widthHalf + 9 + (20 * i), this.y, mouseX, mouseY, this.ingredients.get(i));
        }
    }

    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        this.recipeName = lookup.apply(IVariable.wrap(this.recipeName)).asString();
    }
}
