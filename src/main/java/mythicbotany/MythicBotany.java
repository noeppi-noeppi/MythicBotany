package mythicbotany;

import io.github.noeppi_noeppi.libx.mod.registration.ModXRegistration;
import mythicbotany.data.DataGenerators;
import mythicbotany.network.MythicNetwork;
import mythicbotany.pylon.PylonRepairables;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypePreset;

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

        addRegistrationHandler(ModBlocks::register);
        addRegistrationHandler(ModItems::register);
        addRegistrationHandler(ModRecipes::register);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendIMC);
        MinecraftForge.EVENT_BUS.addListener(this::serverStart);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGenerators::gatherData);

        MinecraftForge.EVENT_BUS.register(new EventListener());
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

        PylonRepairables.register(new PylonRepairables.ItemPylonRepairable(), PylonRepairables.PRIORITY_ITEM_WITH_INTERFACE);
        PylonRepairables.register(new PylonRepairables.MendingPylonRepairable(), PylonRepairables.PRIORITY_MENDING);
    }

    @Override
    protected void clientSetup(FMLClientSetupEvent fmlClientSetupEvent) {
        //
    }

    private void sendIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.RING.getMessageBuilder().size(3).build());

    }

    public void serverStart(FMLServerStartingEvent event) {
        RecipeRemover.removeRecipes(event.getServer().getRecipeManager());
    }
}
