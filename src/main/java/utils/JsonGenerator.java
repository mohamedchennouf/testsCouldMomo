package utils;

import com.google.appengine.repackaged.com.google.gson.JsonElement;
import com.google.appengine.repackaged.com.google.gson.JsonObject;
import com.google.appengine.repackaged.com.google.gson.JsonParser;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class JsonGenerator {
    static private JsonGenerator instance;

    private JsonGenerator() {}

    static public JsonGenerator getInstance() {
        if (instance == null) {
            instance = new JsonGenerator();
        }
        return instance;
    }

    static public JsonObject requestToJson(HttpServletRequest req) throws IOException {
        StringBuffer buff = new StringBuffer();
        String line = null;
        
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
            	buff.append(line);
            }
        } catch (Exception e) { /*report an error*/ }

        try {
            JsonParser jparser = new JsonParser();
            JsonObject json = ((JsonElement) jparser.parse(buff.toString())).getAsJsonObject();
            return json;
        } catch (Exception e) { /*report an error*/ }
        
		return null;
    }
}
