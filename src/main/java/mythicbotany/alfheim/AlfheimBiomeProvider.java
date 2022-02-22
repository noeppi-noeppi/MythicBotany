package mythicbotany.alfheim;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.noeppi_noeppi.libx.world.WorldSeedHolder;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryLookupCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.newbiome.context.BigContext;
import net.minecraft.world.level.newbiome.context.LazyAreaContext;
import net.minecraft.world.level.newbiome.area.Area;
import net.minecraft.world.level.newbiome.area.AreaFactory;
import net.minecraft.world.level.newbiome.area.LazyArea;
import net.minecraft.world.level.newbiome.layer.Layer;
import net.minecraft.world.level.newbiome.layer.RareBiomeSpotLayer;
import net.minecraft.world.level.newbiome.layer.SmoothLayer;
import net.minecraft.world.level.newbiome.layer.ZoomLayer;

import javax.annotation.Nonnull;
import java.util.function.LongFunction;

public class AlfheimBiomeProvider extends BiomeSource {

    public static final Codec<AlfheimBiomeProvider> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.LONG.fieldOf("seed").orElseGet(WorldSeedHolder::getSeed).forGetter(provider -> provider.seed),
                    RegistryLookupCodec.create(Registry.BIOME_REGISTRY).forGetter(provider -> provider.biomeRegistry)
            ).apply(instance, instance.stable(AlfheimBiomeProvider::new)));

    private final Layer genBiomes;
    public final long seed;
    public final Registry<Biome> biomeRegistry;

    public AlfheimBiomeProvider(long seed, Registry<Biome> biomeRegistry) {
        super(AlfheimBiomeManager.allBiomes().map(biomeId -> () -> biomeRegistry.getOrThrow(biomeId)));
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        AreaFactory<LazyArea> areaFactory = createAreaFactory(biomeRegistry, seedModifier -> new LazyAreaContext(25, seed, seedModifier), seed);
        this.genBiomes = new Layer(areaFactory);
    }

    @Nonnull
    @Override
    protected Codec<? extends BiomeSource> codec() {
        return CODEC;
    }

    @Nonnull
    @Override
    public BiomeSource withSeed(long seed) {
        return new AlfheimBiomeProvider(seed, biomeRegistry);
    }

    @Nonnull
    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return genBiomes.get(biomeRegistry, x, z);
    }
    
    private static <T extends Area, C extends BigContext<T>> AreaFactory<T> createAreaFactory(Registry<Biome> biomeRegistry, LongFunction<C> noiseRandom, long seed) {
        long modified = seed * (seed / 2);
        AreaFactory<T> factory = new AlfheimBiomeLayer(biomeRegistry).run(noiseRandom.apply(((modified >> 16) & 0xCCDACADDABABl) + "MythicBotany".hashCode()));
        factory = ZoomLayer.NORMAL.run(noiseRandom.apply(((modified >> 13) & 0xACEDCECADADBl) + "Botania".hashCode()), factory);
        factory = ZoomLayer.NORMAL.run(noiseRandom.apply(((modified >> 27) & 0xABBBCECBACAAl) + "noeppi_noeppi".hashCode()), factory);
        factory = ZoomLayer.NORMAL.run(noiseRandom.apply(((modified) & 0xBAEADCADBEEBl) + "MelanX".hashCode()), factory);
        factory = ZoomLayer.NORMAL.run(noiseRandom.apply(((modified >> 45) & 0xDDEBCCEBCDABl) + "Alfheim".hashCode()), factory);
        factory = SmoothLayer.INSTANCE.run(noiseRandom.apply(((modified >> 6) & 0xAACBCADDEACEl) + "skateIEH".hashCode()), factory);
        factory = RareBiomeSpotLayer.INSTANCE.run(noiseRandom.apply(((modified >> 51) & 0xECEBBABAABDBl) + "Minecraft".hashCode()), factory);
        factory = ZoomLayer.NORMAL.run(noiseRandom.apply(((modified >> 36) & 0xEAADADEDBBDBl) + "NorseMythology".hashCode()), factory);
        return factory;
    }
}
