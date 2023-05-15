package mythicbotany;

import mythicbotany.advancement.ModCriteria;
import mythicbotany.alfheim.teleporter.AlfheimPortalHandler;
import mythicbotany.alfheim.worldgen.placement.AlfheimGroundModifier;
import mythicbotany.config.ClientConfig;
import mythicbotany.kvasir.WanderingTraderRuneInput;
import mythicbotany.loot.AlfsteelDisposeModifier;
import mythicbotany.loot.FimbultyrModifier;
import mythicbotany.mjoellnir.MjoellnirRuneOutput;
import mythicbotany.network.MythicNetwork;
import mythicbotany.patchouli.PageRitualInfo;
import mythicbotany.patchouli.PageRitualPattern;
import mythicbotany.pylon.PylonRepairables;
import mythicbotany.register.ModEnchantments;
import mythicbotany.register.ModEntities;
import mythicbotany.register.ModItems;
import mythicbotany.rune.RuneRitualRecipe;
import mythicbotany.util.density.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
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
import org.moddingx.libx.mod.ModXRegistration;
import org.moddingx.libx.registration.RegistrationBuilder;
import org.moddingx.libx.util.ClassUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.theillusivec4.curios.api.SlotTypePreset;
import vazkii.patchouli.client.book.ClientBookRegistry;

import javax.annotation.Nonnull;

// TODO textures
@Mod("mythicbotany")
public final class MythicBotany extends ModXRegistration {

    public static final Logger logger = LoggerFactory.getLogger("mythicbotany");
    
    private static MythicBotany instance;
    private static MythicNetwork network;

    public MythicBotany() {
        super(new CreativeModeTab("mythicbotany") {
            @Nonnull
            @Override
            public ItemStack makeIcon() {
                return new ItemStack(ModItems.alfsteelSword);
            }
        });

        instance = this;
        network = new MythicNetwork(this);

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
        
        Class<?> extra = ClassUtil.forName("mythicbotany.MythicBotanyExtra");
        if (extra != null) {
            try {
                extra.getMethod("init").invoke(null);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Nonnull
    public static MythicBotany getInstance() {
        return instance;
    }

    @Nonnull
    public static MythicNetwork getNetwork() {
        return network;
    }

    @Override
    protected void initRegistration(RegistrationBuilder builder) {
        // TODO 1.19.4 proper registry tracking support
    }
    
    private void registerMisc(RegisterEvent event) {
        event.register(Registry.DENSITY_FUNCTION_TYPE_REGISTRY, this.resource("smash"), DensitySmash.CODEC::codec);
        event.register(Registry.DENSITY_FUNCTION_TYPE_REGISTRY, this.resource("debug"), DensityDebug.CODEC::codec);
        event.register(Registry.DENSITY_FUNCTION_TYPE_REGISTRY, this.resource("lerp"), DensityLerp.CODEC::codec);
        event.register(Registry.DENSITY_FUNCTION_TYPE_REGISTRY, this.resource("clamp"), DensityClamp.CODEC::codec);
        event.register(Registry.PLACEMENT_MODIFIER_REGISTRY, this.resource("alfheim_ground"), () -> AlfheimGroundModifier.TYPE);
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
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.RING.getMessageBuilder().size(3).build());
        InterModComms.sendTo("apotheosis", "set_ench_hard_cap", () -> new EnchantmentInstance(ModEnchantments.hammerMobility, 5));
    }

    public void serverStart(ServerStartingEvent event) {
        RecipeRemover.removeRecipes(event.getServer().getRecipeManager());
    }
    
    public void datapacksReloaded(OnDatapackSyncEvent event) {
        RecipeRemover.removeRecipes(event.getPlayerList().getServer().getRecipeManager());
    }
}
