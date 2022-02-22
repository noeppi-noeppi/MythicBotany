package mythicbotany.rune;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mythicbotany.ModRecipes;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuneRitualRecipe implements Recipe<Container> {

    public static final int DEFAULT_TICKS = 200;
    
    private static final Map<ResourceLocation, SpecialRuneInput> specialInputs = new HashMap<>();
    private static final Map<ResourceLocation, SpecialRuneOutput> specialOutputs = new HashMap<>();
    
    public static void registerSpecialInput(SpecialRuneInput action) {
        synchronized (specialInputs) {
            if (specialInputs.containsKey(action.id)) {
                throw new IllegalStateException("Special rune ritual input registered twice: " + action.id);
            }
            specialInputs.put(action.id, action);
        }
    }
    
    public static void registerSpecialOutput(SpecialRuneOutput action) {
        synchronized (specialOutputs) {
            if (specialOutputs.containsKey(action.id)) {
                throw new IllegalStateException("Special rune ritual output registered twice: " + action.id);
            }
            specialOutputs.put(action.id, action);
        }
    }

    private final ResourceLocation id;
    private final Ingredient centerRune;
    private final List<RunePosition> runes;
    private final int mana;
    private final int ticks;
    private final List<Ingredient> inputs;
    private final List<ItemStack> outputs;
    @Nullable
    private final SpecialRuneInput specialInput;
    @Nullable
    private final SpecialRuneOutput specialOutput;

    public RuneRitualRecipe(ResourceLocation id, Ingredient centerRune, List<RunePosition> runes, int mana, int ticks, List<Ingredient> inputs, List<ItemStack> outputs, @Nullable SpecialRuneInput specialInput, @Nullable SpecialRuneOutput specialOutput) {
        this.id = id;
        this.centerRune = centerRune;
        this.runes = ImmutableList.copyOf(runes);
        this.mana = mana;
        this.ticks = ticks;
        this.inputs = ImmutableList.copyOf(inputs);
        this.outputs = ImmutableList.copyOf(outputs);
        this.specialInput = specialInput;
        this.specialOutput = specialOutput;
    }

    @Override
    public boolean matches(@Nonnull Container inv, @Nonnull Level level) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull Container inv) {
        return getResultItem();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        if (this.outputs.size() == 1) {
            return this.outputs.get(0);
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return ModRecipes.RUNE_RITUAL;
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.RUNE_RITUAL_SERIALIZER;
    }

    public Ingredient getCenterRune() {
        return centerRune;
    }

    public List<RunePosition> getRunes() {
        return runes;
    }
    
    public int getMana() {
        return mana;
    }

    public int getTicks() {
        return ticks;
    }

    public List<Ingredient> getInputs() {
        return inputs;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    @Nullable
    public SpecialRuneInput getSpecialInput() {
        return specialInput;
    }

    @Nullable
    public SpecialRuneOutput getSpecialOutput() {
        return specialOutput;
    }

    public static class RunePosition {

        private static final int HFLIP = 1;
        private static final int VFLIP = 1 << 1;
        private static final int ROTATE = 1 << 2;
        
        private final Ingredient rune;
        private final int x;
        private final int z;
        private final int[] xcoords;
        private final int[] zcoords;
        private final boolean consume;
        
        public RunePosition(Ingredient rune, int x, int z, boolean consume) {
            this.rune = rune;
            this.x = x;
            this.z = z;
            this.consume = (x == 0 && z == 0) || consume;
            this.xcoords = new int[8];
            this.zcoords = new int[8];
            xcoords[0] = x;
            zcoords[0] = z;
            xcoords[HFLIP] = -x;
            zcoords[HFLIP] = z;
            xcoords[VFLIP] = x;
            zcoords[VFLIP] = -z;
            xcoords[HFLIP | VFLIP] = -x;
            zcoords[HFLIP | VFLIP] = -z;
            for (int i = 0; i < (HFLIP | VFLIP); i++) {
                xcoords[i | ROTATE] = -zcoords[i];
                zcoords[i | ROTATE] = xcoords[i];
            }
        }

        public Ingredient getRune() {
            return rune;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }

        public boolean isConsumed() {
            return consume;
        }

        public int getX(int transformIdx) {
            if (transformIdx < 0 || transformIdx >= 8) {
                return getX();
            } else {
                return xcoords[transformIdx];
            }
        }

        public int getZ(int transformIdx) {
            if (transformIdx < 0 || transformIdx >= 8) {
                return getZ();
            } else {
                return zcoords[transformIdx];
            }
        }
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<RuneRitualRecipe> {

        @Nonnull
        @Override
        public RuneRitualRecipe fromJson(@Nonnull ResourceLocation recipeId, @Nonnull JsonObject json) {
            Ingredient centerRune = Ingredient.fromJson(json.get("center"));
            ImmutableList.Builder<RunePosition> runes = ImmutableList.builder();
            for (JsonElement elem : json.get("runes").getAsJsonArray()) {
                Ingredient rune = Ingredient.fromJson(elem.getAsJsonObject().get("rune"));
                int x = elem.getAsJsonObject().get("x").getAsInt();
                int z = elem.getAsJsonObject().get("z").getAsInt();
                boolean consume = elem.getAsJsonObject().has("consume") && elem.getAsJsonObject().get("consume").getAsBoolean();
                runes.add(new RunePosition(rune, x, z, consume));
            }

            int mana = json.has("mana") ? json.get("mana").getAsInt() : 0;
            int ticks = json.has("ticks") ? json.get("ticks").getAsInt() : DEFAULT_TICKS;

            ImmutableList.Builder<Ingredient> inputs = ImmutableList.builder();
            if (json.has("inputs")) {
                for (JsonElement elem : json.get("inputs").getAsJsonArray()) {
                    inputs.add(Ingredient.fromJson(elem));
                }
            }

            ImmutableList.Builder<ItemStack> outputs = ImmutableList.builder();
            if (json.has("outputs")) {
                for (JsonElement elem : json.get("outputs").getAsJsonArray()) {
                    outputs.add(CraftingHelper.getItemStack(elem.getAsJsonObject(), true));
                }
            }

            ResourceLocation specialInputId = (json.has("special_input") && !json.get("special_input").isJsonNull()) ? new ResourceLocation(json.get("special_input").getAsString()) : null;
            SpecialRuneInput specialInput = null;
            if (specialInputId != null) {
                if (!specialInputs.containsKey(specialInputId)) {
                    throw new IllegalStateException("Unknown special rune input: " + specialInputId);
                }
                specialInput = specialInputs.get(specialInputId);
            }
            
            ResourceLocation specialOutputId = (json.has("special_output") && !json.get("special_output").isJsonNull()) ? new ResourceLocation(json.get("special_output").getAsString()) : null;
            SpecialRuneOutput specialOutput = null;
            if (specialOutputId != null) {
                if (!specialOutputs.containsKey(specialOutputId)) {
                    throw new IllegalStateException("Unknown special rune output: " + specialOutputId);
                }
                specialOutput = specialOutputs.get(specialOutputId);
            }
            
            return new RuneRitualRecipe(recipeId, centerRune, runes.build(), mana, ticks, inputs.build(), outputs.build(), specialInput, specialOutput);
        }

        @Nullable
        @Override
        public RuneRitualRecipe fromNetwork(@Nonnull ResourceLocation recipeId, @Nonnull FriendlyByteBuf buffer) {
            Ingredient centerRune = Ingredient.fromNetwork(buffer);
            int size = buffer.readVarInt();
            ImmutableList.Builder<RunePosition> runes = ImmutableList.builder();
            for (int i = 0; i < size; i++) {
                Ingredient rune = Ingredient.fromNetwork(buffer);
                int x = buffer.readVarInt();
                int z = buffer.readVarInt();
                boolean consume = buffer.readBoolean();
                runes.add(new RunePosition(rune, x, z, consume));
            }

            int mana = buffer.readVarInt();
            int ticks = buffer.readVarInt();

            size = buffer.readVarInt();
            ImmutableList.Builder<Ingredient> inputs = ImmutableList.builder();
            for (int i = 0; i < size; i++) {
                inputs.add(Ingredient.fromNetwork(buffer));
            }

            size = buffer.readVarInt();
            ImmutableList.Builder<ItemStack> outputs = ImmutableList.builder();
            for (int i = 0; i < size; i++) {
                outputs.add(buffer.readItem());
            }

            ResourceLocation specialInputId = null;
            if (buffer.readBoolean()) {
                specialInputId = buffer.readResourceLocation();
            }
            SpecialRuneInput specialInput = null;
            if (specialInputId != null) {
                if (!specialInputs.containsKey(specialInputId)) {
                    throw new IllegalStateException("Unknown special rune input: " + specialInputId);
                }
                specialInput = specialInputs.get(specialInputId);
            }
            
            ResourceLocation specialOutputId = null;
            if (buffer.readBoolean()) {
                specialOutputId = buffer.readResourceLocation();
            }
            SpecialRuneOutput specialOutput = null;
            if (specialOutputId != null) {
                if (!specialOutputs.containsKey(specialOutputId)) {
                    throw new IllegalStateException("Unknown special rune output: " + specialOutputId);
                }
                specialOutput = specialOutputs.get(specialOutputId);
            }

            return new RuneRitualRecipe(recipeId, centerRune, runes.build(), mana, ticks, inputs.build(), outputs.build(), specialInput, specialOutput);

        }

        @Override
        public void toNetwork(@Nonnull FriendlyByteBuf buffer, @Nonnull RuneRitualRecipe recipe) {
            recipe.getCenterRune().toNetwork(buffer);
            
            buffer.writeVarInt(recipe.getRunes().size());
            for (RunePosition rune : recipe.getRunes()) {
                rune.getRune().toNetwork(buffer);
                buffer.writeVarInt(rune.getX());
                buffer.writeVarInt(rune.getZ());
                buffer.writeBoolean(rune.isConsumed());
            }
            
            buffer.writeVarInt(recipe.getMana());
            buffer.writeVarInt(recipe.getTicks());

            buffer.writeVarInt(recipe.getInputs().size());
            for (Ingredient input : recipe.getInputs()) {
                input.toNetwork(buffer);
            }

            buffer.writeVarInt(recipe.getOutputs().size());
            for (ItemStack output : recipe.getOutputs()) {
                buffer.writeItem(output);
            }
            
            if (recipe.getSpecialInput() != null) {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(recipe.getSpecialInput().id);
            } else {
                buffer.writeBoolean(false);
            }
            
            if (recipe.getSpecialOutput() != null) {
                buffer.writeBoolean(true);
                buffer.writeResourceLocation(recipe.getSpecialOutput().id);
            } else {
                buffer.writeBoolean(false);
            }
        }
    }
}
