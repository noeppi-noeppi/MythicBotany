package mythicbotany.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import io.github.noeppi_noeppi.libx.LibX;
import io.github.noeppi_noeppi.libx.mod.ModX;
import io.github.noeppi_noeppi.libx.mod.registration.BlockTE;
import mythicbotany.network.MythicNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.BotaniaAPIClient;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

public class BlockTEManaHUD<T extends TileEntityMana> extends BlockTE<T> implements IWandHUD, IWandable {

    public static final int DEFAULT_COLOR = 0x4444FF;

    public final int color;

    public BlockTEManaHUD(ModX mod, Class<T> teClass, Properties properties) {
        super(mod, teClass, properties);
        this.color = DEFAULT_COLOR;
    }

    public BlockTEManaHUD(ModX mod, Class<T> teClass, Properties properties, Item.Properties itemProperties) {
        super(mod, teClass, properties, itemProperties);
        this.color = DEFAULT_COLOR;
    }

    public BlockTEManaHUD(ModX mod, Class<T> teClass, Properties properties, int color) {
        super(mod, teClass, properties);
        this.color = color;
    }

    public BlockTEManaHUD(ModX mod, Class<T> teClass, Properties properties, Item.Properties itemProperties, int color) {
        super(mod, teClass, properties, itemProperties);
        this.color = color;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void renderHUD(MatrixStack matrixStack, Minecraft minecraft, World world, BlockPos pos) {
        T te = getTile(world, pos);
        BotaniaAPIClient.instance().drawSimpleManaHUD(matrixStack, color, te.getCurrentMana(), te.maxMana, I18n.format(getTranslationKey()));
    }

    @Override
    public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
        if (world.isRemote) {
            LibX.getNetwork().requestTE(world, pos);
        }
        return true;
    }
}
