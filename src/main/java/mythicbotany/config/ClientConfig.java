package mythicbotany.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

    public static final ForgeConfigSpec CLIENT_CONFIG;
    private static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

    static {
        init(CLIENT_BUILDER);
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static ForgeConfigSpec.ConfigValue<Boolean> ringParticles;
    public static ForgeConfigSpec.ConfigValue<Boolean> hudBackgrounds;

    public static void init(ForgeConfigSpec.Builder builder) {
        ringParticles = builder.comment("Set to false to disable particles from the mythicbotany rings for your own player. You'll still see them from other players.").define("ring_particles", true);
        hudBackgrounds = builder.comment("Set to false to disable the background on Botania HUDs.").define("hud_backgrounds", true);
    }
}
