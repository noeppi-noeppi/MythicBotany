package mythicbotany.data.recipes;

import mythicbotany.data.recipes.extension.InfuserExtension;
import mythicbotany.register.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import org.moddingx.libx.datagen.provider.recipe.RecipeProviderBase;
import org.moddingx.libx.datagen.provider.recipe.SmithingExtension;
import org.moddingx.libx.mod.ModX;
import org.zeith.botanicadds.init.ItemsBA;
import vazkii.botania.common.item.BotaniaItems;
import vazkii.botania.common.lib.BotaniaTags;

import java.util.List;

public class BotanicAdditionsRecipeProvider extends RecipeProviderBase implements SmithingExtension, InfuserExtension {

    public BotanicAdditionsRecipeProvider(ModX mod, DataGenerator generator) {
        super(mod, generator);
    }

    @Override
    protected void setup() {
        this.infuser(ItemsBA.GAIASTEEL_INGOT)
                .addIngredient(BotaniaItems.dragonstone)
                .addIngredient(BotaniaItems.pixieDust)
                .addIngredient(BotaniaItems.gaiaIngot)
                .setManaCost(1000000)
                .setColors(0xEE008D, 0xFF0000)
                .build();
        
        this.infuser(BotaniaItems.overgrowthSeed)
                .addIngredient(BotaniaTags.Items.NUGGETS_TERRASTEEL)
                .addIngredient(BotaniaItems.grassSeeds)
                .addIngredient(ItemsBA.GAIA_SHARD)
                .setManaCost(250000)
                .setColors(0x33DD00, 0x00FF00)
                .build();
        
        this.smithing(ModItems.manaRingGreatest, ItemsBA.GAIASTEEL_INGOT, ItemsBA.MANA_RING_GAIA);
    }

    @Override
    protected List<ICondition> conditions() {
        return List.of(new ModLoadedCondition("botanicadds"));
    }
}
