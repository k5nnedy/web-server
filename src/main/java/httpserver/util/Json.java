//This is a custom JSON Class
package httpserver.util;
import java.io.IOException;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Json {

    // Uses jackson to pass JSON in java
    private static ObjectMapper myObjectMapper = defauObjectMapper() ;

    //creates new object mapper
    private static ObjectMapper defauObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    //Parses JSON string into JSON node
    public static JsonNode parse(String jsonSource) throws IOException {
        return myObjectMapper.readTree(jsonSource);
    }

    public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException, IllegalArgumentException {
        return myObjectMapper.treeToValue(node, clazz);
    }

    //Configuration file to JSON node method
    public static JsonNode toJson(Object obj) {
        return myObjectMapper.valueToTree(obj);
    }

    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateJSON(node, false);
    }
    //similar to prettyPrint
    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return generateJSON(node, true);
    }

    private static String generateJSON(Object o, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = myObjectMapper.writer();
        if (pretty) {
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return  objectWriter.writeValueAsString(o);
    }


}
