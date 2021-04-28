package mythicbotany.kvasir;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import io.github.noeppi_noeppi.libx.crafting.ingredient.NbtIngredient;
import io.github.noeppi_noeppi.libx.util.BoundingBoxUtils;
import io.github.noeppi_noeppi.libx.util.Misc;
import mythicbotany.MythicBotany;
import mythicbotany.rune.RuneRitualRecipe;
import mythicbotany.rune.SpecialRuneInput;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class WanderingTraderRuneInput extends SpecialRuneInput {

    public static final WanderingTraderRuneInput INSTANCE = new WanderingTraderRuneInput();
    
    private final ItemStack traderStack;

    private WanderingTraderRuneInput() {
        super(new ResourceLocation(MythicBotany.getInstance().modid, "wandering_trader"));
        traderStack = new ItemStack(Items.PLAYER_HEAD);
        CompoundNBT nbt = traderStack.getOrCreateTag();
        CompoundNBT skullOwner = new CompoundNBT();
        skullOwner.putUniqueId("Id", UUID.fromString("3358ddae-3a41-4ba0-bdfa-ee54b6c55cf5"));
        CompoundNBT properties = new CompoundNBT();
        ListNBT textures = new ListNBT();
        CompoundNBT texture = new CompoundNBT();
        texture.putString("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzc5YTgyMjkwZDdhYmUxZWZhYWJiYzcwNzEwZmYyZWMwMmRkMzRhZGUzODZiYzAwYzkzMGM0NjFjZjkzMiJ9fX0=");
        textures.add(texture);
        properties.put("textures", textures);
        skullOwner.put("Properties", properties);
        nbt.put("SkullOwner", skullOwner);
        CompoundNBT display = new CompoundNBT();
        ListNBT tooltipNBT = new ListNBT();
        tooltipNBT.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(new TranslationTextComponent("tooltip.mythicbotany.sacrifice_entity1").mergeStyle(TextFormatting.RESET).mergeStyle(Style.EMPTY.setItalic(false)).mergeStyle(TextFormatting.AQUA))));
        tooltipNBT.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(new TranslationTextComponent("tooltip.mythicbotany.sacrifice_entity2").mergeStyle(TextFormatting.RESET).mergeStyle(Style.EMPTY.setItalic(false)).mergeStyle(TextFormatting.AQUA))));
        display.put("Lore", tooltipNBT);
        nbt.put("display", display);
        traderStack.setDisplayName(new TranslationTextComponent("entity.minecraft.wandering_trader").mergeStyle(Style.EMPTY.setItalic(false)).mergeStyle(TextFormatting.DARK_AQUA));
    }

    @Override
    public Either<IFormattableTextComponent, CompoundNBT> apply(World world, BlockPos pos, RuneRitualRecipe recipe) {
        List<WanderingTraderEntity> traders = world.getEntitiesWithinAABB(EntityType.WANDERING_TRADER, BoundingBoxUtils.expand(new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5), 3), e -> true);
        if (traders.isEmpty()) {
            return Either.left(new TranslationTextComponent("message.mythicbotany.ritual_no_trader"));
        }
        WanderingTraderEntity trader = traders.get(0);
        if (trader.getRidingEntity() != null) {
            trader.stopRiding();
        }
        if (trader.isBeingRidden()) {
            trader.removePassengers();
        }
        CompoundNBT traderData = trader.writeWithoutTypeId(new CompoundNBT());
        // Don't drop anything on death
        trader.deathLootTable = Misc.MISSIGNO;
        trader.onKillCommand();
        return Either.right(traderData);
    }

    @Override
    public void cancel(World world, BlockPos pos, RuneRitualRecipe recipe, CompoundNBT nbt) {
        WanderingTraderEntity trader = new WanderingTraderEntity(EntityType.WANDERING_TRADER, world);
        UUID uid = trader.getUniqueID();
        trader.setPosition(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5);
        trader.read(nbt);
        trader.setUniqueId(uid);
        trader.setWorld(world);
        world.addEntity(trader);
    }

    @Override
    public List<Ingredient> getJeiInputItems() {
        return ImmutableList.of(new NbtIngredient(traderStack));
    }
}
