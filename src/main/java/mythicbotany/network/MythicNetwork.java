package mythicbotany.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.moddingx.libx.mod.ModX;
import org.moddingx.libx.network.NetworkX;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

public class MythicNetwork extends NetworkX {

    public MythicNetwork(ModX mod) {
        super(mod);
    }

    @Override
    protected Protocol getProtocol() {
        return Protocol.of("7");
    }

    @Override
    public void registerPackets() {
        this.registerGame(NetworkDirection.PLAY_TO_CLIENT, new ParticleMessage.Serializer(), () -> ParticleMessage.Handler::new);
        this.registerGame(NetworkDirection.PLAY_TO_CLIENT, new InfusionMessage.Serializer(), () -> InfusionMessage.Handler::new);
        this.registerGame(NetworkDirection.PLAY_TO_CLIENT, new PylonMessage.Serializer(), () -> PylonMessage.Handler::new);
        this.registerGame(NetworkDirection.PLAY_TO_CLIENT, new UpdatePortalTimeMessage.Serializer(), () -> UpdatePortalTimeMessage.Handler::new);
        this.registerGame(NetworkDirection.PLAY_TO_CLIENT, new MagnetImmunityMessage.Serializer(), () -> MagnetImmunityMessage.Handler::new);
        
        this.registerGame(NetworkDirection.PLAY_TO_SERVER, new AlfSwordLeftClickMessage.Serializer(), () -> AlfSwordLeftClickMessage.Handler::new);
    }

    public void spawnParticle(Level level, SimpleParticleType particle, int amount, double x, double y, double z, double xm, double ym, double zm, double xd, double yd, double zd) {
        this.spawnParticle(level, particle, amount, x, y, z, xm, ym, zm, xd, yd, zd, false);
    }

    public void spawnParticle(Level level, SimpleParticleType particle, int amount, double x, double y, double z, double xm, double ym, double zm, double xd, double yd, double zd, boolean randomizePosition) {
        if (level.isClientSide) {
            for (int i = 0; i < amount; i++) {
                if (randomizePosition) {
                    level.addParticle(particle,
                            x + (level.random.nextDouble() * 2 * xd) - xd,
                            y + (level.random.nextDouble() * 2 * yd) - yd,
                            z + (level.random.nextDouble() * 2 * zd) - zd,
                            xm, ym, zm);
                } else {
                    level.addParticle(particle, x, y, z,
                            xm + (level.random.nextDouble() * 2 * xd) - xd,
                            ym + (level.random.nextDouble() * 2 * yd) - yd,
                            zm + (level.random.nextDouble() * 2 * zd) - zd);
                }
            }
        } else {
            ResourceLocation id = ForgeRegistries.PARTICLE_TYPES.getKey(particle);
            if (id == null) return;
            this.channel.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(x, y, z, 100, level.dimension())),
                    new ParticleMessage(id, x, y, z, amount, xm, ym, zm, xd, yd, zd, randomizePosition));
        }
    }

    public void spawnInfusionParticles(Level level, BlockPos pos, double progress, int fromColor, int toColor) {
        if (!level.isClientSide) {
            this.channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), new InfusionMessage(pos, progress, fromColor, toColor));
        }
    }
    
    public void updatePortalTime(ServerPlayer player, int portalTime) {
        if (!player.getCommandSenderWorld().isClientSide) {
            this.channel.send(PacketDistributor.PLAYER.with(() -> player), new UpdatePortalTimeMessage(portalTime));
        }
    }
    
    public void setItemMagnetImmune(ItemEntity ie) {
        if (!ie.level.isClientSide && !ie.getPersistentData().getBoolean("PreventRemoteMovement")) {
            ie.getPersistentData().putBoolean("PreventRemoteMovement", true);
            this.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> ie), new MagnetImmunityMessage(ie.getId(), true, ie.getX(), ie.getY(), ie.getZ()));
        }
    }
    
    public void removeItemMagnetImmune(ItemEntity ie) {
        if (!ie.level.isClientSide && ie.getPersistentData().getBoolean("PreventRemoteMovement")) {
            ie.getPersistentData().putBoolean("PreventRemoteMovement", false);
            this.channel.send(PacketDistributor.TRACKING_ENTITY.with(() -> ie), new MagnetImmunityMessage(ie.getId(), false, ie.getX(), ie.getY(), ie.getZ()));
        }
    }
}
