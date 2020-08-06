package mythicbotany.network;

import mythicbotany.MythicBotany;
import mythicbotany.network.ParticleHandler.ParticleMessage;
import mythicbotany.network.TeUpdateHandler.TeUpdateMessage;
import mythicbotany.network.TeRequestHandler.TeRequestMessage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.Objects;
import java.util.Optional;

public class MythicNetwork {

    private static final String PROTOCOL_VERSION = "1";
    private static int discriminator = 0;
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MythicBotany.MODID, "netchannel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerPackets() {
        register(new ParticleHandler(), NetworkDirection.PLAY_TO_CLIENT);
        register(new TeUpdateHandler(), NetworkDirection.PLAY_TO_CLIENT);
        register(new InfusionHandler(), NetworkDirection.PLAY_TO_CLIENT);
        register(new PylonHandler(), NetworkDirection.PLAY_TO_CLIENT);

        register(new TeRequestHandler(), NetworkDirection.PLAY_TO_SERVER);
        register(new AlfSwordLeftClickHandler(), NetworkDirection.PLAY_TO_SERVER);
    }

    private static <T> void register(MythicHandler<T> handler, NetworkDirection direction) {
        INSTANCE.registerMessage(discriminator++, handler.messageClass(), handler::encode, handler::decode, handler::handle, Optional.of(direction));
    }

    public static void spawnParticle(World world, BasicParticleType particle, int amount, double x, double y, double z, double xm, double ym, double zm, double xd, double yd, double zd) {
        spawnParticle(world, particle, amount, x, y, z, xm, ym, zm, xd, yd, zd, false);
    }

    public static void spawnParticle(World world, BasicParticleType particle, int amount, double x, double y, double z, double xm, double ym, double zm, double xd, double yd, double zd, boolean randomizePosition) {
        if (world.isRemote) {
            for (int i = 0; i < amount; i++) {
                if (randomizePosition) {
                    world.addParticle(particle,
                            x + (world.rand.nextDouble() * 2 * xd) - xd,
                            y + (world.rand.nextDouble() * 2 * yd) - yd,
                            z + (world.rand.nextDouble() * 2 * zd) - zd,
                            xm, ym, zm);
                } else {
                    world.addParticle(particle, x, y, z,
                            xm + (world.rand.nextDouble() * 2 * xd) - xd,
                            ym + (world.rand.nextDouble() * 2 * yd) - yd,
                            zm + (world.rand.nextDouble() * 2 * zd) - zd);
                }
            }
        } else {
            MythicNetwork.INSTANCE.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(x, y, z, 100, world.func_234923_W_())),
                    new ParticleMessage(particle.getRegistryName(), world.func_234923_W_().getRegistryName(), x, y, z, amount, xm, ym, zm, xd, yd, zd, randomizePosition));
        }
    }

    public static void updateTE(World world, BlockPos pos) {
        if (!world.isRemote) {
            updateTE(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), world, pos);
        }
    }

    static void updateTE(PacketDistributor.PacketTarget target, World world, BlockPos pos) {
        if (!world.isRemote) {
            TileEntity te = world.getTileEntity(pos);
            if (te == null)
                return;
            CompoundNBT nbt = te.getUpdateTag();
            //noinspection ConstantConditions
            if (nbt == null)
                return;
            ResourceLocation id = te.getType().getRegistryName();
            if (id == null)
                return;
            MythicNetwork.INSTANCE.send(target,
                    new TeUpdateMessage(world.func_234923_W_().getRegistryName(), pos, id, nbt));
        }
    }

    public static void requestTE(World world, BlockPos pos) {
        if (world.isRemote) {
            MythicNetwork.INSTANCE.sendToServer(new TeRequestMessage(world.func_234923_W_().getRegistryName(), pos));
        }
    }

    public static void spawnInfusionParticles(World world, BlockPos pos, double progress, int fromColor, int toColor) {
        if (!world.isRemote) {
           MythicNetwork.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunkAt(pos)), new InfusionHandler.InfusionMessage(pos.getX(), pos.getY(), pos.getZ(), world.func_234923_W_().getRegistryName(), progress, fromColor, toColor));
        }
    }
}
