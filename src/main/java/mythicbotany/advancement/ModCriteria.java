package mythicbotany.advancement;

import net.minecraft.advancements.CriteriaTriggers;

public class ModCriteria {

    public static final AlfRepairTrigger ALF_REPAIR = new AlfRepairTrigger();
    public static final MjoellnirTrigger MJOELLNIR = new MjoellnirTrigger();

    public static void setup() {
        CriteriaTriggers.register(ALF_REPAIR);
        CriteriaTriggers.register(MJOELLNIR);
    }
}
