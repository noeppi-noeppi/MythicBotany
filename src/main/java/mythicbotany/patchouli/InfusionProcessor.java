package mythicbotany.patchouli;

import mythicbotany.MythicBotany;
import mythicbotany.infuser.IInfuserRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.resources.ResourceLocation;
import vazkii.botania.client.patchouli.processor.PetalApothecaryProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class InfusionProcessor extends PetalApothecaryProcessor {

    protected Recipe<?> recipe;

    public InfusionProcessor() {

    }

    public void setup(IVariableProvider variables) {
        ResourceLocation id = new ResourceLocation(variables.get("recipe").asString());
        this.recipe = null;
        //noinspection ConstantConditions
        Minecraft.getInstance().level.getRecipeManager().byKey(id).ifPresent(recipe -> this.recipe = recipe);
        if (this.recipe == null) {
            MythicBotany.logger.warn("Missing mythicbotany infusion recipe: " + id);
        } else if (!(this.recipe instanceof IInfuserRecipe)) {
            MythicBotany.logger.warn("Recipe is not a mythicbotany infusion recipe: " + id);
            this.recipe = null;
        }
    }

    @Override
    public IVariable process(String key) {
        if (this.recipe == null) {
            return null;
        } else {
            return switch (key) {
                case "output" -> IVariable.from(this.recipe.getResultItem());
                case "recipe" -> IVariable.wrap(this.recipe.getId().toString());
                case "heading" -> IVariable.from(this.recipe.getResultItem().getHoverName());
                case "mana" -> IVariable.wrap(((IInfuserRecipe) this.recipe).getManaUsage());
                default -> null;
            };
        }
    }
}
