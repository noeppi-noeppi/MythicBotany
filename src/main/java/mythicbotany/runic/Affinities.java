package mythicbotany.runic;

import net.minecraft.client.resources.I18n;

public class Affinities {

    public final float fire;
    public final float air;
    public final int manaCost;

    public Affinities(float fire, float air, int manaCost) {
        this.fire = fire;
        this.air = air;
        this.manaCost = manaCost;
    }

    public String translatedString() {
        int absFire = Math.round(Math.abs(fire) * 100);
        int absAir = Math.round(Math.abs(air) * 100);
        StringBuilder str = new StringBuilder(I18n.format("tooltip.mythicbotany.affinity.name"));
        if (absFire != 0) {
            if (fire < 0) {
                str.append("  ").append(I18n.format("tooltip.mythicbotany.affinity.water")).append(": ");
            } else {
                str.append("  ").append(I18n.format("tooltip.mythicbotany.affinity.fire")).append(": ");
            }
            str.append(absFire).append("%");
        }
        if (absAir != 0) {
            if (air < 0) {
                str.append("  ").append(I18n.format("tooltip.mythicbotany.affinity.earth")).append(": ");
            } else {
                str.append("  ").append(I18n.format("tooltip.mythicbotany.affinity.air")).append(": ");
            }
            str.append(absAir).append("%");
        }
        return str.toString();
    }
}
