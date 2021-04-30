package mythicbotany.config;

import io.github.noeppi_noeppi.libx.annotation.RegisterConfig;
import io.github.noeppi_noeppi.libx.config.Config;
import io.github.noeppi_noeppi.libx.config.Group;
import io.github.noeppi_noeppi.libx.config.validator.DoubleRange;
import io.github.noeppi_noeppi.libx.config.validator.FloatRange;
import io.github.noeppi_noeppi.libx.config.validator.IntRange;
import mythicbotany.functionalflora.WitherAconite;

@RegisterConfig
public class MythicConfig {

    @Config({
            "Whether the alfheim dimension is enabled. When this is set to false, you'll still be able to use",
            "the mead of kvasir as usual but the portal to alfheim will not work."
    })
    public static boolean enableAlfheim = true;
    
    @Config("Whether to replace the recipe for the Gaia Pylon with a recipe that requires Alfsteel.")
    public static boolean replaceGaiaRecipe = true;
    
    public static class flowers {

        @Config("How much mana a wither aconite should generate per nether star.")
        @IntRange(min = 1)
        public static int witherAconiteMana = WitherAconite.DEFAULT_MANA_PER_STAR;
        
        @Group({
                "Can be used to tweak the multipliers for the raindeletia. All matching values are multiplied",
                "The result is the mana generated per tick."
        })
        public static class raindeletia {
            
            @Config("Base modifier. This one will always be applied")
            @FloatRange(min = 0)
            public static float base = 5;
            
            @Config("Modifier for normal rain, not for thunder")
            @FloatRange(min = 0)
            public static float rain = 0.09f;
            
            @Config("Modifier for thundering")
            @FloatRange(min = 0)
            public static float thunder = 3;
            
            @Config("Modifier for dry grass")
            @FloatRange(min = 0)
            public static float dry_grass = 0.5f;
            
            @Config("Modifier for vivid grass")
            @FloatRange(min = 0)
            public static float vivid_grass = 2;
            
            @Config("Modifier for enchanted soil")
            @FloatRange(min = 0)
            public static float enchanted_soil = 5;
        }
    }
    
    public static class alftools {
        
        @Config("Reach distance modifier for the alfsteel helmet")
        @DoubleRange(min = 0)
        public static double reach_modifier = 2;

        @Config("Knockback resistance modifier for the alfsteel chestplate")
        @DoubleRange(min = 0)
        public static double knockback_resistance_modifier = 1;

        @Config("Speed modifier for the alfsteel leggings")
        @DoubleRange(min = 0)
        public static double speed_modifier = 0.05;

        @Config("Jump boost modifier for the alfsteel boots")
        @FloatRange(min = 0)
        public static float jump_modifier = 0.025f;
    }
    
    
}
