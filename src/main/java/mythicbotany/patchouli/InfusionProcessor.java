package mythicbotany.patchouli;

import mythicbotany.infuser.IInfuserRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.patchouli.processor.PetalApothecaryProcessor;
import vazkii.botania.common.Botania;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class InfusionProcessor extends PetalApothecaryProcessor {

    protected IRecipe<?> recipe;

    public InfusionProcessor() {

    }

    public void setup(IVariableProvider variables) {
        ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
        this.recipe = null;
        //noinspection ConstantConditions
        Minecraft.getInstance().world.getRecipeManager().getRecipe(id).ifPresent(recipe -> this.recipe = recipe);
        if (this.recipe == null) {
            Botania.LOGGER.warn("Missing mythicbotany infusion recipe: " + id);
        } else if (!(this.recipe instanceof IInfuserRecipe)) {
            Botania.LOGGER.warn("Recipe is not a mythicbotany infusion recipe: " + id);
        }
    }

    @Override
    public IVariable process(String key) {
        if (this.recipe == null) {
            return null;
        } else {
            switch(key) {
                case "output":
                    return IVariable.from(this.recipe.getRecipeOutput());
                case "recipe":
                    return IVariable.wrap(this.recipe.getId().toString());
                case "heading":
                    return IVariable.from(this.recipe.getRecipeOutput().getDisplayName());
                case "mana":
                    return IVariable.wrap(((IInfuserRecipe) this.recipe).getManaUsage());
                default:
                    return null;
            }
        }
    }
}
