package mythicbotany.runic;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import mythicbotany.ModItems;
import mythicbotany.runic.effects.EffectSpring;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RuneSpell {

    private RuneSpell() {

    }

    // Triple (fire, air, manaCost)
    // negative fire = water
    // negative air = earth
    // Range: -1 to 1   (except tier 1 runes have -1.5 or 1.5)

    public static final List<Item> RUNE_IDS = ImmutableList.of(
            vazkii.botania.common.item.ModItems.runeWater,
            vazkii.botania.common.item.ModItems.runeFire,
            vazkii.botania.common.item.ModItems.runeEarth,
            vazkii.botania.common.item.ModItems.runeAir,
            vazkii.botania.common.item.ModItems.runeSpring,
            vazkii.botania.common.item.ModItems.runeSummer,
            vazkii.botania.common.item.ModItems.runeAutumn,
            vazkii.botania.common.item.ModItems.runeWinter,
            vazkii.botania.common.item.ModItems.runeLust,
            vazkii.botania.common.item.ModItems.runeGluttony,
            vazkii.botania.common.item.ModItems.runeGreed,
            vazkii.botania.common.item.ModItems.runeSloth,
            vazkii.botania.common.item.ModItems.runeWrath,
            vazkii.botania.common.item.ModItems.runeEnvy,
            vazkii.botania.common.item.ModItems.runePride,
            ModItems.vanaheimRune,
            ModItems.helheimRune,
            ModItems.muspelheimRune,
            ModItems.niflheimRune,
            ModItems.joetunheimRune,
            ModItems.alfheimRune,
            ModItems.asgardRune,
            ModItems.nidavellirRune,
            ModItems.midgardRune
    );

    public static final Map<Item, Triple<Float, Float, Integer>> RUNE_AFFINITIES = ImmutableMap.<Item, Triple<Float, Float, Integer>>builder()
            .put(vazkii.botania.common.item.ModItems.runeWater, Triple.of(-1.5f, 0f, 1000))
            .put(vazkii.botania.common.item.ModItems.runeFire, Triple.of(1.5f, 0f, 1000))
            .put(vazkii.botania.common.item.ModItems.runeEarth, Triple.of(0f, -1.5f, 1000))
            .put(vazkii.botania.common.item.ModItems.runeAir, Triple.of(0f, 1.5f, 1000))
            .put(vazkii.botania.common.item.ModItems.runeSpring, Triple.of(0f, -0.5f, 2000))
            .put(vazkii.botania.common.item.ModItems.runeSummer, Triple.of(0.5f, 0f, 2000))
            .put(vazkii.botania.common.item.ModItems.runeAutumn, Triple.of(0f, 0.5f, 2000))
            .put(vazkii.botania.common.item.ModItems.runeWinter, Triple.of(-0.5f, 0f, 2000))
            .put(vazkii.botania.common.item.ModItems.runeLust, Triple.of(0.25f, 0.5f, 3000))
            .put(vazkii.botania.common.item.ModItems.runeGluttony, Triple.of(0.25f, 0f, 3000))
            .put(vazkii.botania.common.item.ModItems.runeGreed, Triple.of(-0.5f, -0.25f, 3000))
            .put(vazkii.botania.common.item.ModItems.runeSloth, Triple.of(0f, 0.75f, 3000))
            .put(vazkii.botania.common.item.ModItems.runeWrath, Triple.of(-0.25f, -0.5f, 3000))
            .put(vazkii.botania.common.item.ModItems.runeEnvy, Triple.of(-0.75f, 0f, 3000))
            .put(vazkii.botania.common.item.ModItems.runePride, Triple.of(0.75f, 0f, 3000))
            .put(ModItems.vanaheimRune, Triple.of(0.25f, -0.45f, 4000))
            .put(ModItems.helheimRune, Triple.of(0.05f, 0.15f, 4000))
            .put(ModItems.muspelheimRune, Triple.of(0.3f, -0.15f, 4000))
            .put(ModItems.niflheimRune, Triple.of(-0.3f, -0.15f, 4000))
            .put(ModItems.joetunheimRune, Triple.of(0.15f, -0.15f, 4000))
            .put(ModItems.alfheimRune, Triple.of(0.45f, 0.45f, 4000))
            .put(ModItems.asgardRune, Triple.of(0.25f, 0.45f, 4000))
            .put(ModItems.nidavellirRune, Triple.of(-0.15f, -0.059f, 4000))
            .put(ModItems.midgardRune, Triple.of(-0.15f, -0.45f, 4000))
            .build();

    public static final Map<Item, RuneEffect> RUNE_EFFECTS = ImmutableMap.<Item, RuneEffect>builder()
            .put(vazkii.botania.common.item.ModItems.runeSpring, new EffectSpring())
            .build();

    public static Affinities getAffinities(int rune1, int rune2, int rune3) {
        return getAffinities(
                getRuneById(rune1),
                getRuneById(rune2),
                getRuneById(rune3)
        );
    }

    @Nullable
    public static Affinities getAffinities(@Nullable Item rune1, @Nullable Item rune2, @Nullable Item rune3) {
        if (rune1 == null || rune2 == null || rune3 == null || !RUNE_AFFINITIES.containsKey(rune1)
                || !RUNE_AFFINITIES.containsKey(rune2) || !RUNE_AFFINITIES.containsKey(rune3)
                || rune1 == rune2 || rune1 == rune3 || rune2 == rune3
                || (!RUNE_EFFECTS.containsKey(rune1) && !RUNE_EFFECTS.containsKey(rune2)
                    && !RUNE_EFFECTS.containsKey(rune3))) {
            return null;
        } else {
            float affinityFire = 0;
            float affinityAir = 0;
            int manaCost = 0;
            Triple<Float, Float, Integer> pair1 = RUNE_AFFINITIES.get(rune1);
            Triple<Float, Float, Integer> pair2 = RUNE_AFFINITIES.get(rune2);
            Triple<Float, Float, Integer> pair3 = RUNE_AFFINITIES.get(rune3);
            affinityFire += pair1.getLeft();
            affinityFire += pair2.getLeft();
            affinityFire += pair3.getLeft();
            affinityAir += pair1.getMiddle();
            affinityAir += pair2.getMiddle();
            affinityAir += pair3.getMiddle();
            manaCost += pair1.getRight();
            manaCost += pair2.getRight();
            manaCost += pair3.getRight();
            if (affinityFire == 0 && affinityAir == 0)
                return null;
            return new Affinities(MathHelper.clamp(affinityFire / 3f, -1, 1), MathHelper.clamp(affinityAir / 3f, -1, 1), Math.max(manaCost, 5000));
        }
    }

    @Nullable
    public static Item getRuneById(int rune) {
        if (rune < 0 || rune >= RUNE_IDS.size()) {
            return null;
        } else {
            return RUNE_IDS.get(rune);
        }
    }

    // -1 if item invalid
    public static int getIdFromRune(Item rune) {
        return RUNE_IDS.indexOf(rune);
    }

    public static boolean perform(World world, PlayerEntity entity, ItemStack stack, int rune1, int rune2, int rune3, Affinities affinities) {
        return perform(
                world, entity, stack,
                getRuneById(rune1),
                getRuneById(rune2),
                getRuneById(rune3),
                affinities
        );
    }

    public static boolean perform(World world, PlayerEntity entity, ItemStack stack, @Nullable Item rune1, @Nullable Item rune2, @Nullable Item rune3, Affinities affinities) {
        if (rune1 == null || rune2 == null || rune3 == null || !RUNE_AFFINITIES.containsKey(rune1)
                || !RUNE_AFFINITIES.containsKey(rune2) || !RUNE_AFFINITIES.containsKey(rune3)
                || rune1 == rune2 || rune1 == rune3 || rune2 == rune3
                || (!RUNE_EFFECTS.containsKey(rune1) && !RUNE_EFFECTS.containsKey(rune2)
                    && !RUNE_EFFECTS.containsKey(rune3)))
            return false;

        Set<Item> runes = ImmutableSet.of(rune1, rune2, rune3);
        runes.stream().filter(RUNE_EFFECTS::containsKey).map(RUNE_EFFECTS::get).forEach(effect -> effect.perform(world, entity, stack, affinities, runes));
        return true;
    }
}
