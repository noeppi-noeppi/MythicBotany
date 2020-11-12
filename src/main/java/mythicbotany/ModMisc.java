package mythicbotany;

import mythicbotany.loot.AlfsteelDisposeModifier;

public class ModMisc {

    public static void register() {
        MythicBotany.getInstance().register("dispose", AlfsteelDisposeModifier.Serializer.INSTANCE);
    }
}
