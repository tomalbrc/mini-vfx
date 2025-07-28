package de.tomalbrc.minivfx.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;

public class ModConfig {
    static Path CONFIG_FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve("mini-vfx.json");
    static ModConfig instance;
    public static GsonBuilder JSON = new GsonBuilder().setPrettyPrinting();

    @SerializedName("hurt-effect")
    public boolean hurtEffect;
    public Items item = new Items();
    public Cow cow = new Cow();
    public Sheep sheep = new Sheep();
    public Torch torch = new Torch();
    public Campfire campfire = new Campfire();
    public Jukebox jukebox = new Jukebox();
    public Explosion explosion = new Explosion();
    public Arrow arrow = new Arrow();
    @SerializedName("item-rarities")
    public Map<String, RarityOption> itemRarities = new Object2ObjectArrayMap<>(Map.of("common", new RarityOption(), "uncommon", new RarityOption(), "rare", new RarityOption(), "epic", new RarityOption()));
    @SerializedName("item-colors")
    public JsonElement itemColors = null;

    public static class RarityOption {
        @SerializedName("show-particles")
        public boolean showParticles = true;
    }

    public static class Cow {
        public boolean milk = true;
        public boolean shear = true;
        public boolean soup = true;
    }

    public static class Arrow {
        public boolean trail = true;
    }

    public static class Sheep {
        public boolean shear = true;
    }

    public static class Campfire {
        public boolean enabled = true;

        @SerializedName("soul-particles")
        public boolean soulParticles = true;
    }

    public static class Torch {
        public boolean enabled = true;
    }

    public static class Jukebox {
        public boolean enabled = true;
    }

    public static class Explosion {
        public boolean enabled = false;
    }

    public static class Items {
        public boolean enabled = true;
        @SerializedName("use-resource-pack")
        public boolean useRP = true;
        @SerializedName("always-use-model")
        public boolean alwaysUseModel = false;
    }

    public static ModConfig getInstance() {
        if (instance == null) {
            if (!load()) // only save if file wasn't just created
                save(); // save since newer versions may contain new options, also removes old options
        }
        return instance;
    }

    public static boolean load() {
        if (!CONFIG_FILE_PATH.toFile().exists()) {
            instance = new ModConfig();
            try {
                if (CONFIG_FILE_PATH.toFile().createNewFile()) {
                    FileOutputStream stream = new FileOutputStream(CONFIG_FILE_PATH.toFile());
                    stream.write(JSON.create().toJson(instance).getBytes(StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }

        try {
            ModConfig.instance = JSON.create().fromJson(new FileReader(ModConfig.CONFIG_FILE_PATH.toFile()), ModConfig.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static void save() {
        try (FileOutputStream stream = new FileOutputStream(CONFIG_FILE_PATH.toFile())) {
            stream.write(JSON.create().toJson(instance).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
