package mythicbotany;

import mythicbotany.base.Registerable;
import mythicbotany.data.DataGenerators;
import mythicbotany.network.MythicNetwork;
import mythicbotany.pylon.PylonRepairables;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.ArrayList;
import java.util.List;

@Mod("mythicbotany")
public class MythicBotany {

    public static final String MODID = "mythicbotany";
    public static final String VERSION = "1.0";

    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.alfsteelSword);
        }
    };

    private static boolean registered = false;
    private static final List<Pair<String, Object>> registerables = new ArrayList<>();

    public MythicBotany() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::sendIMC);
        MinecraftForge.EVENT_BUS.addListener(this::serverStart);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGenerators::gatherData);

        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    private static void register() {
        ModItems.register();
        ModBlocks.register();
        ModRecipes.register();
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Loading MythicBotany v" + VERSION);
        MythicNetwork.registerPackets();

        PylonRepairables.register(new PylonRepairables.ItemPylonRepairable(), PylonRepairables.PRIORITY_ITEM_WITH_INTERFACE);
        PylonRepairables.register(new PylonRepairables.MendingPylonRepairable(), PylonRepairables.PRIORITY_MENDING);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        synchronized (registerables) {
            if (!registered) {
                registered = true;
                register();
            }
        }
        registerables.stream().filter(pair -> pair.getRight() instanceof Registerable).forEach(pair -> ((Registerable) pair.getRight()).registerClient(pair.getLeft()));
    }

    private void sendIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.RING.getMessageBuilder().size(3).build());

    }

    public static void register(String id, Object obj) {
        registerables.add(Pair.of(id, obj));
        if (obj instanceof Registerable) {
            ((Registerable) obj).getAdditionalRegisters().forEach(o -> register(id, o));
            ((Registerable) obj).getNamedAdditionalRegisters().forEach((str, o) -> register(id + "_" + str, o));
        }
    }

    public void serverStart(FMLServerStartingEvent event) {
        RecipeRemover.removeRecipes(event.getServer().getRecipeManager());
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            if (!registered) {
                registered = true;
                register();
            }
            registerables.stream().filter(pair -> pair.getRight() instanceof Item).forEach(pair -> {
                ((Item) pair.getRight()).setRegistryName(new ResourceLocation(MODID, pair.getLeft()));
                event.getRegistry().register((Item) pair.getRight());
            });
        }

        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            if (!registered) {
                registered = true;
                register();
            }
            registerables.stream().filter(pair -> pair.getRight() instanceof Block).forEach(pair -> {
                ((Block) pair.getRight()).setRegistryName(new ResourceLocation(MODID, pair.getLeft()));
                event.getRegistry().register((Block) pair.getRight());
            });
        }

        @SubscribeEvent
        public static void onTilesRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            synchronized (registerables) {
                if (!registered) {
                    registered = true;
                    register();
                }
            }
            registerables.stream().filter(pair -> pair.getRight() instanceof TileEntityType<?>).forEach(pair -> {
                ((TileEntityType<?>) pair.getRight()).setRegistryName(new ResourceLocation(MODID, pair.getLeft()));
                event.getRegistry().register((TileEntityType<?>) pair.getRight());
            });
        }

        @SubscribeEvent
        public static void onEnchantsRegistry(final RegistryEvent.Register<Enchantment> event) {
            synchronized (registerables) {
                if (!registered) {
                    registered = true;
                    register();
                }
            }
            registerables.stream().filter(pair -> pair.getRight() instanceof Enchantment).forEach(pair -> {
                ((Enchantment) pair.getRight()).setRegistryName(new ResourceLocation(MODID, pair.getLeft()));
                event.getRegistry().register((Enchantment) pair.getRight());
            });
        }

        @SubscribeEvent
        public static void onPotionsRegistry(final RegistryEvent.Register<Potion> event) {
            synchronized (registerables) {
                if (!registered) {
                    registered = true;
                    register();
                }
            }
            registerables.stream().filter(pair -> pair.getRight() instanceof Potion).forEach(pair -> {
                ((Potion) pair.getRight()).setRegistryName(new ResourceLocation(MODID, pair.getLeft()));
                event.getRegistry().register((Potion) pair.getRight());
            });
        }

        @SubscribeEvent
        public static void onEffectsRegistry(final RegistryEvent.Register<Effect> event) {
            synchronized (registerables) {
                if (!registered) {
                    registered = true;
                    register();
                }
            }
            registerables.stream().filter(pair -> pair.getRight() instanceof Effect).forEach(pair -> {
                ((Effect) pair.getRight()).setRegistryName(new ResourceLocation(MODID, pair.getLeft()));
                event.getRegistry().register((Effect) pair.getRight());
            });
        }

        @SubscribeEvent
        public static void onRecipeTypesRegistry(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
            synchronized (registerables) {
                if (!registered) {
                    registered = true;
                    register();
                }
            }
            registerables.stream().filter(pair -> pair.getRight() instanceof IRecipeSerializer<?>).forEach(pair -> {
                ((IRecipeSerializer<?>) pair.getRight()).setRegistryName(new ResourceLocation(MODID, pair.getLeft()));
                event.getRegistry().register((IRecipeSerializer<?>) pair.getRight());
            });
        }
    }
}
