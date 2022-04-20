package mythicbotany.data.patchouli.translate;

import com.google.gson.JsonObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TranslationManager {
    
    private final String prefix;
    private final Map<String, String> translations;
    
    public TranslationManager(String prefix) {
        this.prefix = prefix;
        translations = new HashMap<>();
    }
    
    public String add(String translated, String... nameElems) {
        String fullName = Stream.concat(Stream.of(prefix), Arrays.stream(nameElems)).filter(s -> !s.isEmpty()).collect(Collectors.joining("."));
        translations.put(fullName, translated);
        return fullName;
    }
    
    public JsonObject build() {
        JsonObject json = new JsonObject();
        for (String key : this.translations.keySet().stream().sorted().toList()) {
            json.addProperty(key, this.translations.get(key));
        }
        return json;
    }
}
