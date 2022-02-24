package mythicbotany.kvasir;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import io.github.noeppi_noeppi.libx.crafting.ingredient.NbtIngredient;
import io.github.noeppi_noeppi.libx.util.Misc;
import mythicbotany.MythicBotany;
import mythicbotany.rune.RuneRitualRecipe;
import mythicbotany.rune.SpecialRuneInput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TranslatableComponent;

public class WanderingTraderRuneInput extends SpecialRuneInput {

    public static final WanderingTraderRuneInput INSTANCE = new WanderingTraderRuneInput();
    
    private final ItemStack traderStack;

    private WanderingTraderRuneInput() {
        super(MythicBotany.getInstance().resource("wandering_trader"));
        this.traderStack = new ItemStack(Items.PLAYER_HEAD);
        CompoundTag nbt = this.traderStack.getOrCreateTag();
        CompoundTag skullOwner = new CompoundTag();
        skullOwner.putUUID("Id", UUID.fromString("3358ddae-3a41-4ba0-bdfa-ee54b6c55cf5"));
        CompoundTag properties = new CompoundTag();
        ListTag textures = new ListTag();
        CompoundTag texture = new CompoundTag();
        texture.putString("Value", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWYxMzc5YTgyMjkwZDdhYmUxZWZhYWJiYzcwNzEwZmYyZWMwMmRkMzRhZGUzODZiYzAwYzkzMGM0NjFjZjkzMiJ9fX0=");
        textures.add(texture);
        properties.put("textures", textures);
        skullOwner.put("Properties", properties);
        nbt.put("SkullOwner", skullOwner);
        CompoundTag display = new CompoundTag();
        ListTag tooltipNBT = new ListTag();
        tooltipNBT.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("tooltip.mythicbotany.sacrifice_entity1").withStyle(ChatFormatting.RESET).withStyle(Style.EMPTY.withItalic(false)).withStyle(ChatFormatting.AQUA))));
        tooltipNBT.add(StringTag.valueOf(Component.Serializer.toJson(new TranslatableComponent("tooltip.mythicbotany.sacrifice_entity2").withStyle(ChatFormatting.RESET).withStyle(Style.EMPTY.withItalic(false)).withStyle(ChatFormatting.AQUA))));
        display.put("Lore", tooltipNBT);
        nbt.put("display", display);
        this.traderStack.setHoverName(new TranslatableComponent("entity.minecraft.wandering_trader").withStyle(Style.EMPTY.withItalic(false)).withStyle(ChatFormatting.DARK_AQUA));
    }

    @Override
    public Either<MutableComponent, CompoundTag> apply(Level level, BlockPos pos, RuneRitualRecipe recipe) {
        Vec3 center = new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
        AABB aabb = new AABB(center, center).inflate(3);
        List<WanderingTrader> traders = level.getEntities(EntityType.WANDERING_TRADER, aabb, e -> true);
        if (traders.isEmpty()) {
            return Either.left(new TranslatableComponent("message.mythicbotany.ritual_no_trader"));
        }
        WanderingTrader trader = traders.get(0);
        if (trader.getVehicle() != null) {
            trader.stopRiding();
        }
        if (trader.isVehicle()) {
            trader.ejectPassengers();
        }
        CompoundTag traderData = trader.saveWithoutId(new CompoundTag());
        // Don't drop anything on death
        trader.lootTable = Misc.MISSIGNO;
        trader.kill();
        return Either.right(traderData);
    }

    @Override
    public void cancel(Level level, BlockPos pos, RuneRitualRecipe recipe, CompoundTag nbt) {
        WanderingTrader trader = new WanderingTrader(EntityType.WANDERING_TRADER, level);
        UUID uid = trader.getUUID();
        trader.setPos(pos.getX() + 0.5, pos.getY() + 0.3, pos.getZ() + 0.5);
        trader.load(nbt);
        trader.setUUID(uid);
        level.addFreshEntity(trader);
    }

    @Override
    public List<Ingredient> getJeiInputItems() {
        return ImmutableList.of(new NbtIngredient(this.traderStack));
    }
}
