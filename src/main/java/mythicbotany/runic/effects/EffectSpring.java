package mythicbotany.runic.effects;

import com.google.common.collect.ImmutableList;
import mythicbotany.runic.Affinities;
import mythicbotany.runic.RuneEffect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EffectSpring implements RuneEffect {

    public static final List<Block> SAPLINGS = ImmutableList.of(Blocks.ACACIA_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING);

    @Override
    public void perform(World world, PlayerEntity player, ItemStack stack, Affinities affinities, Set<Item> runes) {
        float treeChance = Math.max(0, affinities.air - 0.66f);
        float saplingCnce = Math.max(0, affinities.air - 0.66f);
    }
}
