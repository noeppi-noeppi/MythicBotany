package mythicbotany.data;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.noeppi_noeppi.libx.mod.ModX;
import net.minecraft.advancements.*;
import net.minecraft.advancements.criterion.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class AdvancementProviderBase implements IDataProvider {
    
   private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    protected final ModX mod;
    protected final DataGenerator generator;
    private final Map<ResourceLocation, Supplier<Advancement>> advancements = new HashMap<>();
    private String rootId = null;
    private Supplier<Advancement> rootSupplier = null;
    
    public AdvancementProviderBase(ModX mod, DataGenerator generator) {
        this.mod = mod;
        this.generator = generator;
    }
    
    public abstract void setup();

    @Nonnull
    @Override
    public String getName() {
        return mod.modid + " advancements";
    }
    
    @Override
    public void act(@Nonnull DirectoryCache cache) throws IOException {
        this.setup();
        for (Supplier<Advancement> supplier : advancements.values()) {
            Advancement advancement = supplier.get();
            Path path = generator.getOutputFolder().resolve("data/" + advancement.getId().getNamespace() + "/advancements/" + advancement.getId().getPath() + ".json");
            IDataProvider.save(GSON, cache, advancement.copy().serialize(), path);
        }
    }
    
    public AdvancementFactory root() {
        return root(mod.modid);
    }
    
    public AdvancementFactory root(String id) {
        return root(mod.modid, id);
    }
    
    public AdvancementFactory root(String namespace, String id) {
        if (id.equals("recipes")) {
            throw new IllegalStateException("Can't 'recipes' as root advancement id. Use a recipe provider to generate recipe advancements.");
        }
        if (rootId != null || rootSupplier != null) {
            throw new IllegalStateException("Can't use multiple root advancements in the same provider. Use multiple providers for this.");
        }
        if (!this.advancements.isEmpty()) {
            throw new IllegalStateException("The root advancement must be the first advancement that is configured.");
        }
        AdvancementFactory factory = new AdvancementFactory(namespace, id);
        if (this.advancements.put(factory.id, factory::build) != null) {
            throw new IllegalStateException("Duplicate root advancement: " + id);
        }
        rootId = id;
        rootSupplier = factory::build;
        return factory;
    }
    
    public void advancement(Advancement advancement) {
        if (this.advancements.put(advancement.getId(), () -> advancement) != null) {
            throw new IllegalStateException("Duplicate advancement: " + advancement.getId());
        }
    }
    
    public AdvancementFactory advancement(ResourceLocation id) {
        AdvancementFactory factory = new AdvancementFactory(id);
        if (this.advancements.put(id, factory::build) != null) {
            throw new IllegalStateException("Duplicate advancement: " + id);
        }
        return factory;
    }
    
    public AdvancementFactory advancement(String id) {
        return advancement(idFor(id));
    }
    
    public Advancement dummy(ResourceLocation id) {
        return dummy(id, false);
    }
    
    public Advancement dummy(ResourceLocation id, boolean hidden) {
        return new Advancement(id, null, new DisplayInfo(new ItemStack(Items.BARRIER), new StringTextComponent(""), new StringTextComponent(""), null, FrameType.TASK, true, true, hidden), AdvancementRewards.EMPTY, new HashMap<>(), new String[][]{});
    }
    
    private ResourceLocation idFor(String id) {
        if (rootId == null) {
            throw new IllegalStateException("On advancement providers without a root advancement only fully qualified resource locations are allowed, no plain ids.");
        }
        return new ResourceLocation(mod.modid, rootId + "/" + id);
    }
    
    public ICriterionInstance items(IItemProvider... items) {
        return items(Arrays.stream(items).map(item -> ItemPredicate.Builder.create().item(item).build()).toArray(ItemPredicate[]::new));
    }
    
    @SafeVarargs
    public final ICriterionInstance items(ITag<Item>... items) {
        return items(Arrays.stream(items).map(item -> ItemPredicate.Builder.create().tag(item).build()).toArray(ItemPredicate[]::new));
    }
    
    public ICriterionInstance items(ItemPredicate... items) {
        return InventoryChangeTrigger.Instance.forItems(items);
    }
    
    public TaskFactory itemTasks(IItemProvider... items) {
        return itemTasks(Arrays.stream(items).map(item -> ItemPredicate.Builder.create().item(item).build()).toArray(ItemPredicate[]::new));
    }
    
    @SafeVarargs
    public final TaskFactory itemTasks(ITag<Item>... items) {
        return itemTasks(Arrays.stream(items).map(item -> ItemPredicate.Builder.create().tag(item).build()).toArray(ItemPredicate[]::new));
    }
    
    public TaskFactory itemTasks(ItemPredicate... items) {
        return () -> Arrays.stream(items).map(item -> new ICriterionInstance[]{ items(item) }).toArray(ICriterionInstance[][]::new);
    }
    
    public ICriterionInstance eat(IItemProvider food) {
        return eat(ItemPredicate.Builder.create().item(food).build());
    }
    
    public ICriterionInstance eat(ITag<Item> food) {
        return eat(ItemPredicate.Builder.create().tag(food).build());
    }
    
    public ICriterionInstance eat(ItemPredicate food) {
        return new ConsumeItemTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, food);
    }
    
    public ICriterionInstance leave(RegistryKey<World> dimension) {
        return new ChangeDimensionTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, dimension, null);
    }
    
    public ICriterionInstance enter(RegistryKey<World> dimension) {
        return ChangeDimensionTrigger.Instance.toWorld(dimension);
    }
    
    public ICriterionInstance changeDim(RegistryKey<World> from, RegistryKey<World> to) {
        return new ChangeDimensionTrigger.Instance(EntityPredicate.AndPredicate.ANY_AND, from, to);
    }
    
    public EntityPredicate.AndPredicate entity(EntityPredicate entity) {
        return EntityPredicate.AndPredicate.serializePredicate(EntityHasProperty.func_237477_a_(LootContext.EntityTarget.THIS, entity).build());
    }
    
    public EntityPredicate.AndPredicate entity(EntityType<?> type) {
        return entity(EntityPredicate.Builder.create().type(type).build());
    }
    
    public ItemPredicate stack(IItemProvider item, Enchantment... enchs) {
        ItemPredicate.Builder builder = ItemPredicate.Builder.create().item(item);
        for (Enchantment ench : enchs) {
            builder.enchantment(new EnchantmentPredicate(ench, MinMaxBounds.IntBound.atLeast(1)));
        }
        return builder.build();
    }
    
    public ItemPredicate stack(ITag<Item> item, Enchantment... enchs) {
        ItemPredicate.Builder builder = ItemPredicate.Builder.create().tag(item);
        for (Enchantment ench : enchs) {
            builder.enchantment(new EnchantmentPredicate(ench, MinMaxBounds.IntBound.atLeast(1)));
        }
        return builder.build();
    }
    
    public ItemPredicate stack(Enchantment... enchs) {
        if (enchs.length == 0) {
            throw new IllegalStateException("Dont use stack() for an any predicate. Use ItemPredicate.ANY instead.");
        }
        ItemPredicate.Builder builder = ItemPredicate.Builder.create();
        for (Enchantment ench : enchs) {
            builder.enchantment(new EnchantmentPredicate(ench, MinMaxBounds.IntBound.atLeast(1)));
        }
        return builder.build();
    }
    
    public ItemPredicate stack(Enchantment ench, int min) {
        return ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(ench, MinMaxBounds.IntBound.atLeast(min))).build();
    }
    
    public class AdvancementFactory {
        
        private final ResourceLocation id;
        private final boolean root;
        private Supplier<Advancement> parent;
        private DisplayInfo display;
        private final List<List<Criterion>> criteria = new ArrayList<>();
        private AdvancementRewards reward = AdvancementRewards.EMPTY;

        private AdvancementFactory(String namespace, String rootId) {
            id = new ResourceLocation(namespace, rootId + "/root");
            root = true;
            this.parent = () -> null;
        }
        
        private AdvancementFactory(ResourceLocation id) {
            this.id = id;
            this.root = false;
            this.parent = () -> null;
        }
        
        public AdvancementFactory parent(Advancement parent) {
            if (root) throw new IllegalStateException("Can't set parent for root advancement.");
            this.parent = () -> parent;
            return this;
        }
        
        public AdvancementFactory parent(ResourceLocation id) {
            if (root) throw new IllegalStateException("Can't set parent for root advancement.");
            if (!advancements.containsKey(id)) {
                throw new IllegalStateException("Parent advancement unknown: " + id);
            }
            this.parent = advancements.get(id);
            return this;
        }
        
        public AdvancementFactory parent(String id) {
            if (root) throw new IllegalStateException("Can't set parent for root advancement.");
            return this.parent(idFor(id));
        }
        
        public AdvancementFactory display(IItemProvider icon) {
            return this.display(new ItemStack(icon));
        }
        
        public AdvancementFactory display(IItemProvider icon, FrameType frame) {
            return this.display(new ItemStack(icon), frame);
        }
        
        public AdvancementFactory display(IItemProvider icon, FrameType frame, boolean toast, boolean chat, boolean hidden) {
            return this.display(new ItemStack(icon), frame, toast, chat, hidden);
        }
        
        public AdvancementFactory display(ItemStack icon) {
            return this.display(icon, FrameType.TASK);
        }
        
        public AdvancementFactory display(ItemStack icon, FrameType frame) {
            return this.display(icon, frame, !root, !root, false);
        }
        
        public AdvancementFactory display(ItemStack icon, FrameType frame, boolean toast, boolean chat, boolean hidden) {
            return this.display(icon,
                    new TranslationTextComponent("advancements." + id.getNamespace() + "." + id.getPath().replace('/', '.') + ".title"),
                    new TranslationTextComponent("advancements." + id.getNamespace() + "." + id.getPath().replace('/', '.') + ".description"),
                    frame, toast, chat, hidden
            );
        }
        
        public AdvancementFactory display(IItemProvider icon, ITextComponent title, ITextComponent description) {
            return this.display(new ItemStack(icon), title, description);
        }

        public AdvancementFactory display(IItemProvider icon, ITextComponent title, ITextComponent description, FrameType frame) {
            return this.display(new ItemStack(icon), title, description, frame);
        }

        public AdvancementFactory display(IItemProvider icon, ITextComponent title, ITextComponent description, FrameType frame, boolean toast, boolean chat, boolean hidden) {
            return this.display(new ItemStack(icon), title, description, frame, toast, chat, hidden);
        }
        
        public AdvancementFactory display(ItemStack icon, ITextComponent title, ITextComponent description) {
            return this.display(icon, title, description, FrameType.TASK);
        }
        
        public AdvancementFactory display(ItemStack icon, ITextComponent title, ITextComponent description, FrameType frame) {
            return this.display(icon, title, description, frame, !root, !root, false);
        }
        
        public AdvancementFactory display(ItemStack icon, ITextComponent title, ITextComponent description, FrameType frame, boolean toast, boolean chat, boolean hidden) {
            this.display = new DisplayInfo(icon, title, description, null, frame, toast, chat, hidden);
            return this;
        }
        
        public AdvancementFactory task(ICriterionInstance... criteria) {
            if (criteria.length == 0) {
                throw new IllegalStateException("Can not add empty task to advancement.");
            }
            this.criteria.add(Arrays.stream(criteria).map(Criterion::new).collect(Collectors.toList()));
            return this;
        }
        
        public AdvancementFactory tasks(ICriterionInstance... criteria) {
            if (criteria.length == 0) {
                throw new IllegalStateException("Can not add empty task to advancement.");
            }
            for (ICriterionInstance instance : criteria) {
                this.criteria.add(ImmutableList.of(new Criterion(instance)));
            }
            this.criteria.add(Arrays.stream(criteria).map(Criterion::new).collect(Collectors.toList()));
            return this;
        }
        
        public AdvancementFactory tasks(TaskFactory factory) {
            for (ICriterionInstance[] task : factory.apply()) {
                this.task(task);
            }
            return this;
        }
        
        public AdvancementFactory reward(AdvancementRewards reward) {
            this.reward = reward;
            return this;
        }
        
        public Advancement build() {
            if (criteria.isEmpty()) {
                throw new IllegalStateException("Can not add advancement without tasks.");
            }
            Set<String> idsTaken = new HashSet<>();
            String[][] criteriaIds = new String[criteria.size()][];
            Map<String, Criterion> criteriaMap = new HashMap<>();
            for (int i = 0; i < criteria.size(); i++) {
                String[] criterionGroup = new String[criteria.get(i).size()];
                for (int j = 0; j < criteria.get(i).size(); j++) {
                    String baseName = Objects.requireNonNull(criteria.get(i).get(j).getCriterionInstance(), "Can't build advancement: Empty criterion").getId().getPath();
                    baseName = baseName.replace('.', '_').replace('/', '_');
                    String nextId = baseName;
                    int num = 2;
                    while ((idsTaken.contains(nextId))) {
                        nextId = baseName + (num++);
                    }
                    idsTaken.add(nextId);
                    criterionGroup[j] = nextId;
                    criteriaMap.put(nextId, criteria.get(i).get(j));
                }
                criteriaIds[i] = criterionGroup;
            }
            Advancement parentAdv = parent.get();
            if (root && parentAdv != null) {
                throw new IllegalStateException("Root advancement can not have a parent.");
            } else if (!root && parentAdv == null) {
                if (rootSupplier != null) {
                    parentAdv = rootSupplier.get();
                    if (parentAdv == null) {
                        throw new IllegalStateException("Root advancement configured wrongly. This is an error in LibX.");
                    }
                } else {
                    throw new IllegalStateException("This advancement provider has no default root and the advancement " + id + " has no root specified.");
                }
            }
            if (parentAdv != null && parentAdv.getDisplay() == null && display != null) {
                throw new IllegalStateException("Can't build advancement with display and display-less parent.");
            }
            if (parentAdv != null && parentAdv.getDisplay() != null && display != null && parentAdv.getDisplay().isHidden() && !display.isHidden()) {
                throw new IllegalStateException("Can't build visible advancement with hidden parent.");
            }
            return new Advancement(id, parentAdv, display, reward, criteriaMap, criteriaIds);
        }
    }
    
    public interface TaskFactory {
        
        ICriterionInstance[][] apply();
    }
}
