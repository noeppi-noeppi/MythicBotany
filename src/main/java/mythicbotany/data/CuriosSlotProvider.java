package mythicbotany.data;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.server.packs.PackType;
import org.moddingx.libx.datagen.DatagenContext;
import org.moddingx.libx.datagen.PackTarget;
import org.moddingx.libx.mod.ModX;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class CuriosSlotProvider implements DataProvider {

    private final ModX mod;
    private final PackTarget target;
    
    public CuriosSlotProvider(DatagenContext ctx) {
        this.mod = ctx.mod();
        this.target = ctx.target();
    }
    
    @Nonnull
    @Override
    public final String getName() {
        return this.mod.modid + " curios slots";
    }

    @Nonnull
    @Override
    public CompletableFuture<?> run(@Nonnull CachedOutput cache) {
        Path path = this.target.path(PackType.SERVER_DATA).resolve(mod.modid).resolve("curios/slots/ring.json");
        JsonObject json = new JsonObject();
        json.addProperty("operation", "ADD");
        json.addProperty("size", 1);
        return DataProvider.saveStable(cache, json, path);
    }
}
