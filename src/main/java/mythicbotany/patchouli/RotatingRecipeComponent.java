package mythicbotany.patchouli;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import javax.annotation.Nonnull;

public class RotatingRecipeComponent implements ICustomComponent {

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
        AtomicReference<IRecipe<?>> recipe = new AtomicReference<>(null);
        //noinspection ConstantConditions
        Minecraft.getInstance().world.getRecipeManager().getRecipe(new ResourceLocation(recipeName)).ifPresent(recipe::set);
        if (recipe.get() == null) {
            throw new RuntimeException("Missing recipe: " + this.recipeName);
        } else {
            return recipe.get().getIngredients();
        }
    }

    public void render(@Nonnull MatrixStack matrixStack, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        int degreePerInput = (int) (360.0F / (float) this.ingredients.size());
        int ticksElapsed = context.getTicksInBook();
        float currentDegree = ConfigHandler.CLIENT.lexiconRotatingItems.get() ? (Screen.hasShiftDown() ? (float) ticksElapsed : (float) ticksElapsed + pticks) : 0.0F;

        for (Ingredient ingredient : ingredients) {
            this.renderIngredientAtAngle(matrixStack, context, currentDegree, ingredient, mouseX, mouseY);
            currentDegree += (float) degreePerInput;
        }
    }

    private void renderIngredientAtAngle(MatrixStack matrixStack, IComponentRenderContext context, float angle, Ingredient ingredient, int mouseX, int mouseY) {
        if (!ingredient.hasNoMatchingItems()) {
            angle -= 90.0F;
            int radius = 32;
            double xPos = (double) this.x + Math.cos((double) angle * Math.PI / 180) * (double) radius + 32;
            double yPos = (double) this.y + Math.sin((double) angle * Math.PI / 180) * (double) radius + 32;
            matrixStack.push();
            matrixStack.translate(xPos - (double) ((int) xPos), yPos - (double) ((int) yPos), 0);
            context.renderIngredient(matrixStack, (int) xPos, (int) yPos, mouseX, mouseY, ingredient);
            matrixStack.pop();
        }
    }

    public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
        this.recipeName = lookup.apply(IVariable.wrap(this.recipeName)).asString();
    }
}
