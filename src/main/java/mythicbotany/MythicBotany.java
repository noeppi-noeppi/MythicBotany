package mythicbotany;

import io.github.noeppi_noeppi.libx.event.DatapacksReloadedEvent;
import io.github.noeppi_noeppi.libx.mod.registration.ModXRegistration;
import mythicbotany.advancement.ModCriteria;
import mythicbotany.alfheim.Alfheim;
import mythicbotany.alfheim.AlfheimFeatures;
import mythicbotany.alfheim.teleporter.AlfheimPortalHandler;
import mythicbotany.config.ClientConfig;
import mythicbotany.data.DataGenerators;
import mythicbotany.kvasir.WanderingTraderRuneInput;
import mythicbotany.mjoellnir.MjoellnirRuneOutput;
import mythicbotany.network.MythicNetwork;
import mythicbotany.patchouli.PageRitualInfo;
import mythicbotany.patchouli.PageRitualPattern;
import mythicbotany.pylon.PylonRepairables;
import mythicbotany.rune.RuneRitualRecipe;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypePreset;
import vazkii.patchouli.client.book.ClientBookRegistry;

import javax.annotation.Nonnull;

@Mod("mythicbotany")
public class MythicBotany extends ModXRegistration {

    private static MythicBotany instance;
    private static MythicNetwork network;

    public MythicBotany() {
        super("mythicbotany", new ItemGroup("mythicbotany") {
            @Nonnull
            @Override
            public ItemStack createIcon() {
                return new ItemStack(ModItems.alfsteelSword);
            }
        });

        instance = this;
        network = new MythicNetwork(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG);

        addRegistrationHandler(ModRecipes::register);
        addRegistrationHandler(ModMisc::register);
        addRegistrationHandler(Alfheim::register);
        addRegistrationHandler(AlfheimFeatures::register);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGenerators::gatherData);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModEntities::createAttributes);

        MinecraftForge.EVENT_BUS.addListener(this::serverStart);
        MinecraftForge.EVENT_BUS.addListener(this::datapacksReloaded);

        MinecraftForge.EVENT_BUS.register(new EventListener());
        //noinspection CodeBlock2Expr
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, AlfheimPortalHandler::renderGameOverlay);
        });
        MinecraftForge.EVENT_BUS.addListener(AlfheimPortalHandler::endTick);
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
    protected void setup(final FMLCommonSetupEvent event) {
        logger.info("Loading MythicBotany");
        
        event.enqueueWork(() -> {
            ModEntities.setup();
            ModCriteria.setup();
            
            PylonRepairables.register(new PylonRepairables.ItemPylonRepairable(), PylonRepairables.PRIORITY_ITEM_WITH_INTERFACE);
            PylonRepairables.register(new PylonRepairables.MendingPylonRepairable(), PylonRepairables.PRIORITY_MENDING);

            RuneRitualRecipe.registerSpecialInput(WanderingTraderRuneInput.INSTANCE);
            RuneRitualRecipe.registerSpecialOutput(MjoellnirRuneOutput.INSTANCE);
            
            Alfheim.setupBiomes();
        });
    }

    @Override
    protected void clientSetup(FMLClientSetupEvent event) {
        ModEntities.clientSetup();
        event.enqueueWork(() -> {
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(modid, "ritual_pattern"), PageRitualPattern.class);
            ClientBookRegistry.INSTANCE.pageTypes.put(new ResourceLocation(modid, "ritual_info"), PageRitualInfo.class);
        });
    }

    private void sendIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.RING.getMessageBuilder().size(3).build());
        InterModComms.sendTo("apotheosis", "set_ench_hard_cap", () -> new EnchantmentData(ModMisc.hammerMobility, 5));
    }

    public void serverStart(FMLServerStartingEvent event) {
        RecipeRemover.removeRecipes(event.getServer().getRecipeManager());
    }
    
    public void datapacksReloaded(DatapacksReloadedEvent event) {
        RecipeRemover.removeRecipes(event.getServer().getRecipeManager());
    }
}
