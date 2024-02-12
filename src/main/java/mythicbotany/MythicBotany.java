package mythicbotany;

import mythicbotany.advancement.ModCriteria;
import mythicbotany.alfheim.datagen.*;
import mythicbotany.alfheim.teleporter.AlfheimPortalHandler;
import mythicbotany.alfheim.worldgen.placement.AlfheimGroundModifier;
import mythicbotany.config.ClientConfig;
import mythicbotany.config.MythicConfig;
import mythicbotany.data.AdvancementProvider;
import mythicbotany.data.BlockStateProvider;
import mythicbotany.data.CuriosSlotProvider;
import mythicbotany.data.ItemModelProvider;
import mythicbotany.data.lexicon.LexiconProvider;
import mythicbotany.data.loot.*;
import mythicbotany.data.recipes.RecipeProvider;
import mythicbotany.data.tags.BiomeLayerTagsProvider;
import mythicbotany.data.tags.BiomeTagsProvider;
import mythicbotany.data.tags.CommonTagsProvider;
import mythicbotany.kvasir.WanderingTraderRuneInput;
import mythicbotany.loot.AlfsteelDisposeModifier;
import mythicbotany.loot.FimbultyrModifier;
import mythicbotany.mjoellnir.MjoellnirRuneOutput;
import mythicbotany.network.MythicNetwork;
import mythicbotany.patchouli.PageRitualInfo;
import mythicbotany.patchouli.PageRitualPattern;
import mythicbotany.pylon.PylonRepairables;
import mythicbotany.register.ModBlocks;
import mythicbotany.register.ModEnchantments;
import mythicbotany.register.ModEntities;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.moddingx.libx.datagen.DatagenSystem;
import org.moddingx.libx.datagen.PackTarget;
import org.moddingx.libx.datapack.DynamicPacks;
import org.moddingx.libx.mod.ModXRegistration;
import org.moddingx.libx.registration.RegistrationBuilder;
import org.moddingx.libx.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vazkii.patchouli.client.book.ClientBookRegistry;

import javax.annotation.Nonnull;
import java.util.Map;

@Mod("mythicbotany")
public final class MythicBotany extends ModXRegistration {

    public static final Logger logger = LoggerFactory.getLogger("mythicbotany");
    
    private static MythicBotany instance;
    private static MythicNetwork network;
    private static MythicTab creativeTab;

    public MythicBotany() {
        instance = this;
        network = new MythicNetwork(this);
        creativeTab = new MythicTab(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerMisc);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModEntities::createAttributes);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModEntities::createSpawnPlacement);

        MinecraftForge.EVENT_BUS.addListener(this::serverStart);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOW, this::datapacksReloaded);

        MinecraftForge.EVENT_BUS.register(new EventListener());
        //noinspection CodeBlock2Expr
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, AlfheimPortalHandler::renderGameOverlay);
        });
        MinecraftForge.EVENT_BUS.addListener(AlfheimPortalHandler::serverStarted);
        MinecraftForge.EVENT_BUS.addListener(AlfheimPortalHandler::endTick);

        if (MythicConfig.addExtraRingSlot) {
            DynamicPacks.DATA_PACKS.enablePack(this.modid, "curios");
        }
        
        Class<?> extra = ClassUtil.forName("mythicbotany.MythicBotanyExtra");
        if (extra != null) {
            try {
                extra.getMethod("init").invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        
        DatagenSystem.create(this, system -> {
            system.addRegistryProvider(AlfheimFeatures::new);
            system.addRegistryProvider(AlfheimPlacements::new);
            system.addRegistryProvider(AlfheimTemplates::new);
            system.addRegistryProvider(AlfheimStructures::new);
            system.addRegistryProvider(AlfheimStructureSets::new);
            system.addRegistryProvider(AlfheimBiomes::new);
            system.addRegistryProvider(AlfheimNoise::new);
            system.addRegistryProvider(AlfheimDimensionTypes::new);
            system.addRegistryProvider(AlfheimBiomeLayers::new);
            system.addExtensionProvider(AlfheimSurface::new);
            system.addExtensionProvider(AlfheimDimension::new);
            system.addDataProvider(LexiconProvider::new);
            system.addDataProvider(AdvancementProvider::new);
            system.addDataProvider(RecipeProvider::new);
            system.addDataProvider(BlockLootProvider::new);
            system.addDataProvider(EntityLootProvider::new);
            system.addDataProvider(ChestLootProvider::new);
            system.addDataProvider(EntityAdditionLootProvider::new);
            system.addDataProvider(CommonTagsProvider::new);
            system.addDataProvider(BiomeTagsProvider::new);
            system.addDataProvider(BiomeLayerTagsProvider::new);
            system.addDataProvider(BlockStateProvider::new);
            system.addDataProvider(ItemModelProvider::new);
            system.addDataProvider(GlobalLootProvider::new);

            PackTarget curiosTarget = system.dynamic("curios", PackType.SERVER_DATA);
            system.addDataProvider(curiosTarget, CuriosSlotProvider::new);
        });
    }

    @Nonnull
    public static MythicBotany getInstance() {
        return instance;
    }

    @Nonnull
    public static MythicNetwork getNetwork() {
        return network;
    }

    @Nonnull
    public static MythicTab getCreativeTab() {
        return creativeTab;
    }

    @Override
    protected void initRegistration(RegistrationBuilder builder) {
        builder.disableRegistryTracking(); // Registry tracking seems to have no future anyway.
    }
    
    private void registerMisc(RegisterEvent event) {
        event.register(Registries.PLACEMENT_MODIFIER_TYPE, this.resource("alfheim_ground"), () -> AlfheimGroundModifier.TYPE);
        event.register(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, this.resource("dispose"), () -> AlfsteelDisposeModifier.CODEC);
        event.register(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, this.resource("fimbultyr"), () -> FimbultyrModifier.CODEC);
    }

    @Override
    protected void setup(final FMLCommonSetupEvent event) {
        logger.info("Loading MythicBotany");
        
        event.enqueueWork(() -> {
            ModCriteria.setup();
            
            PylonRepairables.register(new PylonRepairables.ItemPylonRepairable(), PylonRepairables.PRIORITY_ITEM_WITH_INTERFACE);
            PylonRepairables.register(new PylonRepairables.MendingPylonRepairable(), PylonRepairables.PRIORITY_MENDING);

            RuneRitualRecipe.registerSpecialInput(WanderingTraderRuneInput.INSTANCE);
            RuneRitualRecipe.registerSpecialOutput(MjoellnirRuneOutput.INSTANCE);
        });
    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ModEntities.clientSetup();
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(this.modid, "ritual_pattern"), PageRitualPattern.class);
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(this.modid, "ritual_info"), PageRitualInfo.class);
        });
    }

    private void sendIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("apotheosis", "set_ench_hard_cap", () -> new EnchantmentInstance(ModEnchantments.hammerMobility, 5));
        InterModComms.sendTo("apotheosis", "loot_category_override", () -> Map.entry(ModBlocks.mjoellnir.asItem(), "sword"));
    }

    public void serverStart(ServerStartingEvent event) {
        RecipeRemover.removeRecipes(event.getServer().getRecipeManager());
    }
    
    public void datapacksReloaded(OnDatapackSyncEvent event) {
        RecipeRemover.removeRecipes(event.getPlayerList().getServer().getRecipeManager());
    }
}
