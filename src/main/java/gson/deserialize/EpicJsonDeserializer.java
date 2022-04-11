package gson.deserialize;

import com.google.gson.*;
import model.Epic;
import model.Status;
import model.Subtask;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicJsonDeserializer implements JsonDeserializer<Epic> {
    @Override
    public Epic deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Epic epic = new Epic(jsonObject.get("Name").getAsString());
        if (!jsonObject.get("Specification").getAsString().equals("null"))
            epic.setSpecification(jsonObject.get("Specification").getAsString());
        epic.setId(jsonObject.get("Id").getAsInt());
        epic.setStatus(Status.valueOf(jsonObject.get("Status").getAsString()));

        if (!jsonObject.get("Duration").getAsString().equals("null"))
            epic.setDuration(context.deserialize(jsonObject.get("Duration"), Duration.class));
        if (!jsonObject.get("StartTime").getAsString().equals("null"))
            epic.setStartTime((String) context.deserialize(jsonObject.get("StartTime"), LocalDateTime.class));

        JsonArray subtasks = jsonObject.getAsJsonArray("Subtasks");
        for (JsonElement subtask : subtasks) {
            epic.getSubtasks().add(context.deserialize(subtask, Subtask.class));
        }

        return epic;
    }
}
