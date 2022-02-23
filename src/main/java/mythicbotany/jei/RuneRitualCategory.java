package mythicbotany.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mythicbotany.ModItems;
import mythicbotany.MythicBotany;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import vazkii.botania.client.gui.HUDHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RuneRitualCategory implements IRecipeCategory<RuneRitualRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(MythicBotany.getInstance().modid, "jei_category_rune_ritual");

    private final IDrawable background;
    private final IDrawable slot;
    private final Component localizedName;
    private final IDrawable icon;

    private final Map<ResourceLocation, Pair<Integer, Integer>> inputOutputSizes = new HashMap<>();

    public RuneRitualCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(MythicBotany.getInstance().modid, "textures/gui/jei_ritual.png"), 0, 0, 136, 196);
        this.slot = guiHelper.getSlotDrawable();
        this.localizedName = new TranslatableComponent("tooltip.mythicbotany.rune_ritual");
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM, new ItemStack(ModItems.fimbultyrTablet));
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Nonnull
    @Override
    public Class<? extends RuneRitualRecipe> getRecipeClass() {
        return RuneRitualRecipe.class;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Nonnull
    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(@Nonnull RuneRitualRecipe recipe, @Nonnull IIngredients ii) {
        ArrayList<Ingredient> inputs = new ArrayList<>();
        inputs.add(recipe.getCenterRune());
        for (RuneRitualRecipe.RunePosition rune : recipe.getRunes()) {
            inputs.add(rune.getRune());
        }
        inputs.addAll(recipe.getInputs());
        if (recipe.getSpecialInput() != null) {
            inputs.addAll(recipe.getSpecialInput().getJeiInputItems());
        }
        ii.setInputIngredients(inputs);

        ArrayList<ItemStack> outputs = new ArrayList<>();
        //noinspection CollectionAddAllCanBeReplacedWithConstructor
        outputs.addAll(recipe.getOutputs());
        if (recipe.getSpecialOutput() != null) {
            outputs.addAll(recipe.getSpecialOutput().getJeiOutputItems());
        }
        ii.setOutputs(VanillaTypes.ITEM, outputs);

        this.inputOutputSizes.put(recipe.getId(), Pair.of(inputs.size() - (1 + recipe.getRunes().size()), outputs.size()));
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout layout, @Nonnull RuneRitualRecipe recipe, @Nonnull IIngredients ii) {
        this.initRunePositioned(layout, 0, 0, 0, true);
        for (int i = 0; i < recipe.getRunes().size(); i++) {
            this.initRunePositioned(layout, i + 1, recipe.getRunes().get(i).getX(), recipe.getRunes().get(i).getZ(), recipe.getRunes().get(i).isConsumed());
        }
        int baseInputId = 1 + recipe.getRunes().size();
        int inputSize = ii.getInputs(VanillaTypes.ITEM).size() - baseInputId;
        int startInX = 68 - (inputSize * 9);
        for (int i = 0; i < inputSize; i++) {
            layout.getItemStacks().init(baseInputId + i, true, startInX + (i * 18), 137);
        }

        int outputSize = ii.getOutputs(VanillaTypes.ITEM).size();
        int startOutX = 68 - (outputSize * 9);
        for (int i = 0; i < outputSize; i++) {
            layout.getItemStacks().init(baseInputId + inputSize + i, false, startOutX + (i * 18), 169);
        }
        
        layout.getItemStacks().set(ii);
    }

    public void draw(RuneRitualRecipe recipe, @Nonnull PoseStack poseStack, double mouseX, double mouseY) {
        if (this.inputOutputSizes.containsKey(recipe.getId())) {
            Pair<Integer, Integer> sizes = this.inputOutputSizes.get(recipe.getId());
            
            int startInX = 68 - (sizes.getLeft() * 9);
            for (int i = 0; i < sizes.getLeft(); i++) {
                this.slot.draw(poseStack, startInX + (i * 18), 137);
            }
            
            int startOutX = 68 - (sizes.getRight() * 9);
            for (int i = 0; i < sizes.getRight(); i++) {
                this.slot.draw(poseStack, startOutX + (i * 18), 169);
            }
        }
        if (recipe.getMana() > 0) {
            HUDHandler.renderManaBar(poseStack, 17, 189, 0x0000FF, 0.75f, recipe.getMana(), 1000000);
        }
    }

    private void initRunePositioned(@Nonnull IRecipeLayout layout, int idx, int x, int z, boolean consume) {
        int realX = 2 + (12 * (x + 5));
        int realZ = 2 + (12 * ((-z) + 5));
        layout.getItemStacks().init(idx, true, LittleBoxItemRenderer.getRenderer(x, z, consume), realX, realZ, 12, 12, 0, 0);
    }
}
