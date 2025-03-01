package net.swofty.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.swofty.item.SkyBlockItem;

public class InventorySerializer<T> implements Serializer<T> {
    private final ObjectMapper mapper;
    private final Class<T> clazz;

    public InventorySerializer(Class<T> clazz) {
        this.mapper = new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.clazz = clazz;

        SimpleModule module = new SimpleModule();
        module.addSerializer(SkyBlockItem.class, new SkyBlockItemSerializer());
        module.addDeserializer(SkyBlockItem.class, new SkyBlockItemDeserializer());
        mapper.registerModule(module);
    }

    @Override
    public String serialize(T value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

    @Override
    public T deserialize(String json) throws JsonProcessingException {
        return mapper.readValue(json, clazz);
    }
}