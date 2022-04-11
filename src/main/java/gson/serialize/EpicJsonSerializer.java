package gson.serialize;

import com.google.gson.*;
import model.Epic;
import model.Subtask;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;

public class EpicJsonSerializer implements JsonSerializer<Epic> {
    @Override
    public JsonElement serialize(Epic epic, Type type, JsonSerializationContext context) {
        JsonObject result = new JsonObject();

        result.addProperty("Name", epic.getName());

        if (epic.getSpecification() == null)
            result.addProperty("Specification", "null");
        else
            result.addProperty("Specification", epic.getSpecification());

        result.addProperty("Id", epic.getId());
        result.addProperty("Status", String.valueOf(epic.getStatus()));
        if (epic.getDuration() == null)
            result.addProperty("Duration", "null");
        else
            result.add("Duration", context.serialize(epic.getDuration().toMinutes()));

        if (epic.getStartTime() == null)
            result.addProperty("StartTime", "null");
        else
            result.add("StartTime",
                    context.serialize(epic.getStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy|HH:mm"))));

        JsonArray subtasks = new JsonArray();
        result.add("Subtasks", subtasks);

        for (Subtask subtask : epic.getSubtasks()) {
            subtasks.add(context.serialize(subtask));
        }
        return result;
    }
}
