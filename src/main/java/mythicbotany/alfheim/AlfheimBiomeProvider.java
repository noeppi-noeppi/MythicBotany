package mythicbotany.alfheim;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.noeppi_noeppi.libx.world.WorldSeedHolder;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.gen.layer.RareBiomeLayer;
import net.minecraft.world.gen.layer.SmoothLayer;
import net.minecraft.world.gen.layer.ZoomLayer;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.function.LongFunction;

public class AlfheimBiomeProvider extends BiomeProvider {

    public static final Codec<AlfheimBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    Codec.LONG.fieldOf("seed").orElseGet(WorldSeedHolder::getSeed).stable().forGetter(p -> p.seed),
                    RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter(provider -> provider.biomeRegistry)
            ).apply(instance, instance.stable(AlfheimBiomeProvider::new)));

    private static final List<Biome> biomes = ImmutableList.of(
            Alfheim.alfheimPlains,
            Alfheim.alfheimHills,
            Alfheim.dreamwoodForest,
            Alfheim.goldenFields,
            Alfheim.alfheimLakes
    );

    private final Layer genBiomes;
    public final long seed;
    public final Registry<Biome> biomeRegistry;

    public AlfheimBiomeProvider(long seed, Registry<Biome> biomeRegistry) {
        super(biomes.stream().map(b -> () -> biomeRegistry.getOrThrow(
                RegistryKey.getOrCreateKey(
                        Registry.BIOME_KEY,
                        Objects.requireNonNull(b.getRegistryName(), "Biome not registered.")
                )
        )));
        this.seed = seed;
        this.biomeRegistry = biomeRegistry;
        IAreaFactory<LazyArea> areaFactory = createAreaFactory(biomeRegistry, seedModifier -> new LazyAreaLayerContext(25, seed, seedModifier), seed);
        this.genBiomes = new Layer(areaFactory);
    }

    @Nonnull
    @Override
    protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
        return CODEC;
    }

    @Nonnull
    @Override
    public BiomeProvider getBiomeProvider(long seed) {
        return new AlfheimBiomeProvider(seed, biomeRegistry);
    }

    @Nonnull
    @Override
    public Biome getNoiseBiome(int x, int y, int z) {
        return genBiomes.func_242936_a(biomeRegistry, x, z);
    }
    
    private static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> createAreaFactory(Registry<Biome> biomeRegistry, LongFunction<C> noiseRandom, long seed) {
        long modified = seed * (seed / 2);
        IAreaFactory<T> factory = new AlfheimBiomeLayer(biomeRegistry).apply(noiseRandom.apply(((modified >> 16) & 0xCCDACADDABABl) + "MythicBotany".hashCode()));
        factory = ZoomLayer.NORMAL.apply(noiseRandom.apply(((modified >> 13) & 0xACEDCECADADBl) + "Botania".hashCode()), factory);
        factory = ZoomLayer.NORMAL.apply(noiseRandom.apply(((modified >> 27) & 0xABBBCECBACAAl) + "noeppi_noeppi".hashCode()), factory);
        factory = ZoomLayer.NORMAL.apply(noiseRandom.apply(((modified) & 0xBAEADCADBEEBl) + "MelanX".hashCode()), factory);
        factory = ZoomLayer.NORMAL.apply(noiseRandom.apply(((modified >> 45) & 0xDDEBCCEBCDABl) + "Alfheim".hashCode()), factory);
        factory = SmoothLayer.INSTANCE.apply(noiseRandom.apply(((modified >> 6) & 0xAACBCADDEACEl) + "skateIEH".hashCode()), factory);
        factory = RareBiomeLayer.INSTANCE.apply(noiseRandom.apply(((modified >> 51) & 0xECEBBABAABDBl) + "Minecraft".hashCode()), factory);
        factory = ZoomLayer.NORMAL.apply(noiseRandom.apply(((modified >> 36) & 0xEAADADEDBBDBl) + "NorseMythology".hashCode()), factory);
        return factory;
    }
}
