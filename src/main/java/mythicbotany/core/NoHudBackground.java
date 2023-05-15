package mythicbotany.core;

import mythicbotany.config.ClientConfig;

public class NoHudBackground {
    
    public static boolean shouldRenderHudBackground() {
        return ClientConfig.hudBackgrounds.get();
    }
}
