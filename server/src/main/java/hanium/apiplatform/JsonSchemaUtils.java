package hanium.apiplatform;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.github.fge.jackson.JsonLoader;

import org.json.JSONObject;

public class JsonSchemaUtils {
    public static boolean validateJsonAgainstSchema(JSONObject json, JSONObject schema) {
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        try {
            JsonNode jsonNode = JsonLoader.fromString(json.toString());
            JsonNode schemaNode = JsonLoader.fromString(schema.toString());

            JsonValidator validator = JsonSchemaFactory.byDefault().getValidator();
            return validator.validate(schemaNode, jsonNode).isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}