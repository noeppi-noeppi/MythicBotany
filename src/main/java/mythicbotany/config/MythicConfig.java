package mythicbotany.config;

import org.moddingx.libx.annotation.config.RegisterConfig;
import org.moddingx.libx.config.Config;
import org.moddingx.libx.config.Group;
import org.moddingx.libx.config.validate.DoubleRange;
import org.moddingx.libx.config.validate.FloatRange;
import org.moddingx.libx.config.validate.IntRange;
import mythicbotany.functionalflora.WitherAconite;
import mythicbotany.mjoellnir.MjoellnirHoldRequirement;

@RegisterConfig
public class MythicConfig {

    @Config({
            "Whether the alfheim dimension is enabled. When this is set to false, you'll still be able to use",
            "the mead of kvasir as usual but the portal to alfheim will not work."
    })
    public static boolean enableAlfheim = true;
    
    @Config({
            "Whether players that manage to get to alfheim via another mod but have not drunk the mead of kvasir",
            "should get a blindness effect."
    })
    public static boolean lockAlfheim = true;
    
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
        
        @Group({
                "Durability stats for alfsteel tools.",
                "  max_durability      is the maximum durability, a tool has",
                "  mana_per_durability is the amount of mana consumed per durability loss"
        })
        public static class durability {
            @Config public static ToolDamageStats sword = new ToolDamageStats(4600, 200);
            @Config public static ToolDamageStats axe = new ToolDamageStats(4600, 200);
            @Config public static ToolDamageStats pickaxe = new ToolDamageStats(4600, 200);
            @Config public static ToolDamageStats armor = new ToolDamageStats(5200, 100);
        }
        
        public static class tool_values {
            
            @Config("The attack damage dealt by the alf blade")
            public static double sword_damage = 12;
            
            @Config("The attack speed modifier for the alf blade")
            public static double sword_speed = 2.4;
        }
        
        @Group("Armor stats for alfsteel armor.")
        public static class armor_values {
            @Config public static ArmorStats helmet = new ArmorStats(3, 3);
            @Config public static ArmorStats chestplate = new ArmorStats(8, 3);
            @Config public static ArmorStats leggings = new ArmorStats(6, 3);
            @Config public static ArmorStats boots = new ArmorStats(3, 3);
        }
    }
    
    public static class mjoellnir {
        
        @Config({
                "What is required for a player to hold mjoellnir.",
                "nothing  - Players will always be able to hold mjoellnir.",
                "effect   - Players need the absorption effect to hold mjoellnir.",
                "hearts   - Players need absorption hearts to hold mjoellnir.",
                "           If they run out of golden hearts but still have the effect, mjoellnir is dropped."
        })
        public static MjoellnirHoldRequirement requirement = MjoellnirHoldRequirement.EFFECT;
        
        @Config({
                "What is required for a player that holds the ring of thor to hold mjoellnir.",
                "If a player holds the ring of thor, this OR `mjoellnir.requirement` must be met."
        })
        public static MjoellnirHoldRequirement requirement_thor = MjoellnirHoldRequirement.NOTHING;
        
        @Config("The base damage for melee attacks.")
        @FloatRange(min = 1)
        public static float base_damage_melee = 25;
        
        @Config("The base damage for the main target on ranged attacks.")
        @FloatRange(min = 1)
        public static float base_damage_ranged = 25;
        
        @Config("Enchantment multiplier for sharpness and power enchantments.")
        @FloatRange(min = 1)
        public static float enchantment_multiplier = 5;
        
        @Config({"The damage dealt to secondary targets on ranged attacks.", "This value is multiplied with the damage to the main target."})
        @FloatRange(min = 0, max = 1)
        public static float secondary_target_multiplier = 0.4f;
        
        @Config("The chance for secondary targets to get lightning effects applied as well on ranged attacks.")
        @FloatRange(min = 0, max = 1)
        public static float secondary_lightning_chance = 0.25f;
        
        @Config("The base attack speed attribute for mjoellnir.")
        public static float base_attack_speed = -3.5f;
        
        @Config("The amount the attack speed increases per level of hammer mobility.")
        @FloatRange(min = 0)
        public static float attack_speed_multiplier = 0.2f;
        
        @Config("The cooldown in ticks after a ranged attack, before mjoellnir can be thrown again.")
        @IntRange(min = 0)
        public static int ranged_cooldown = 120;
    }
    
    public static class spawns {
        
        @Group("Spawn configuration for the alf pixie.")
        public static class pixies {
            @Config public static int weight = 5;
            @Config public static int min = 4;
            @Config public static int max = 10;
        }

        @Group("Spawn configuration for the witches in alfheim.")
        public static class witch {
            @Config public static int weight = 2;
            @Config public static int min = 1;
            @Config public static int max = 2;
        }
        
        @Group("Spawn configuration for the illusioner in alfheim.")
        public static class illusioner {
            @Config public static int weight = 1;
            @Config public static int min = 1;
            @Config public static int max = 1;
        }
    }
}
