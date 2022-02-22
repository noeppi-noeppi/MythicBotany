package mythicbotany.rune;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class SpecialRuneInput {
    
    public final ResourceLocation id;

    protected SpecialRuneInput(ResourceLocation id) {
        this.id = id;
    }
    
    // Should return a compound nbt if everything was applied correctly or a failure message if it failed.
    // The compound nbt can be used to restore state when it's canceled.
    public abstract Either<MutableComponent, CompoundTag> apply(Level world, BlockPos pos, RuneRitualRecipe recipe);
    
    public abstract void cancel(Level world, BlockPos pos, RuneRitualRecipe recipe, CompoundTag nbt);
    
    public List<Ingredient> getJeiInputItems() {
        return ImmutableList.of();
    }
}
