package mythicbotany.rune;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.world.World;

import java.util.List;

public abstract class SpecialRuneInput {
    
    public final ResourceLocation id;

    protected SpecialRuneInput(ResourceLocation id) {
        this.id = id;
    }
    
    // Should return a compound nbt if everything was applied correctly or a failure message if it failed.
    // The compound nbt can be used to restore state when it's canceled.
    public abstract Either<IFormattableTextComponent, CompoundNBT> apply(World world, BlockPos pos, RuneRitualRecipe recipe);
    
    public abstract void cancel(World world, BlockPos pos, RuneRitualRecipe recipe, CompoundNBT nbt);
    
    public List<Ingredient> getJeiInputItems() {
        return ImmutableList.of();
    }
}
