package mythicbotany;

import com.google.common.collect.ImmutableSet;
import mythicbotany.base.Registerable;
import mythicbotany.data.DataGenerators;
import mythicbotany.infuser.InfuserRecipe;
import mythicbotany.network.MythicNetwork;
import mythicbotany.pylon.RenderAlfsteelPylon;
import mythicbotany.runic.RunicSpellRecipe;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraAxe;
import vazkii.botania.common.item.equipment.tool.terrasteel.ItemTerraPick;
import vazkii.botania.common.lib.ResourceLocationHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mod("mythicbotany")
public class MythicBotany {

    public static final String MODID = "mythicbotany";
    public static final String VERSION = "1.0";

    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup TAB = new ItemGroup(MODID) {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.alfsteelIngot);
        }
    };

    private static boolean registered = false;
    private static final Set<Pair<String, Object>> registerables = new HashSet<>();

    public MythicBotany() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        MinecraftForge.EVENT_BUS.addListener(this::serverStart);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(DataGenerators::gatherData);

        MinecraftForge.EVENT_BUS.register(new EventListener());
    }

    private static void register() {
        ModItems.register();
        ModBlocks.register();
        Set<Pair<String, Object>> r = registerables;
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Loading MythicBotany v" + VERSION);
        MythicNetwork.registerPackets();

        InfuserRecipe.add(new ItemStack(vazkii.botania.common.item.ModItems.terrasteel),
                500000, 0x0000FF, 0x00FF00,
                new ItemStack(vazkii.botania.common.item.ModItems.manaSteel),
                new ItemStack(vazkii.botania.common.item.ModItems.manaDiamond),
                new ItemStack(vazkii.botania.common.item.ModItems.manaPearl));

        InfuserRecipe.add(new ItemStack(ModItems.alfsteelIngot),
                2000000, 0xFF008D, 0xFF9600,
                new ItemStack(vazkii.botania.common.item.ModItems.elementium),
                new ItemStack(vazkii.botania.common.item.ModItems.dragonstone),
                new ItemStack(vazkii.botania.common.item.ModItems.pixieDust));

        InfuserRecipe.add(new RunicSpellRecipe(100000, 0xFFFFFF, 0xFFFFFF));
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        synchronized (registerables) {
            if (!registered) {
                registered = true;
                register();
            }
        }
        registerables.stream().filter(pair -> pair.getRight() instanceof Registerable).forEach(pair -> ((Registerable) pair.getRight()).registerClient(pair.getLeft()));

        ClientRegistry.bindTileEntityRenderer(ModBlocks.alfsteelPylon.getTileType(), RenderAlfsteelPylon::new);

        ItemModelsProperties.func_239418_a_(ModItems.alfsteelAxe, new ResourceLocation(MODID, "active"), (stack, world, entity) -> entity instanceof PlayerEntity && !ItemTerraAxe.shouldBreak((PlayerEntity) entity) ? 0.0F : 1.0F);
        ItemModelsProperties.func_239418_a_(ModItems.alfsteelPick, new ResourceLocation(MODID, "tipped"), (stack, world, entity) -> ItemTerraPick.isTipped(stack) ? 1.0F : 0.0F);
        ItemModelsProperties.func_239418_a_(ModItems.alfsteelPick, new ResourceLocation(MODID, "active"), (stack, world, entity) -> ItemTerraPick.isEnabled(stack) ? 1.0F : 0.0F);
    }

    public static void register(String id, Object obj) {
        registerables.add(Pair.of(id, obj));
        if (obj instanceof Registerable) {
            ((Registerable) obj).getAdditionalRegisters().forEach(o -> register(id, o));
        }
    }

    public void serverStart(FMLServerStartingEvent event) {
        RecipeRemover.removeRecipes(event.getServer().getRecipeManager());
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
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
        public static void onBiomesRegistry(final RegistryEvent.Register<Biome> event) {
            synchronized (registerables) {
                if (!registered) {
                    registered = true;
                    register();
                }
            }
            registerables.stream().filter(pair -> pair.getRight() instanceof Biome).forEach(pair -> {
                ((Biome) pair.getRight()).setRegistryName(new ResourceLocation(MODID, pair.getLeft()));
                event.getRegistry().register((Biome) pair.getRight());
            });
        }
    }
}
