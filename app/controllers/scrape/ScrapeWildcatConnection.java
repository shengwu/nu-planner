package controllers.scrape;

import java.io.File;
import java.io.IOException;
import java.util.*;
import play.*; // Play Framework
import play.mvc.*;
import play.data.*;
import play.db.ebean.*; // http://www.avaje.org/ebean/introquery.html
import models.*;
import views.html.*;
import org.joda.time.format.*; // DateTimeFormatter http://joda-time.sourceforge.net/apidocs/
import org.joda.time.DateTime; // http://joda-time.sourceforge.net/api-release/index.html
import org.jsoup.*; // http://jsoup.org/apidocs/
import org.jsoup.Connection.Method;
import org.jsoup.nodes.*; // Document, Element
import org.jsoup.select.Elements;
import java.io.BufferedReader; // java i/o
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.*; // json
import org.json.simple.JSONValue;
import com.google.gson.*;

/**
 * The ScrapeWildcatConnection class scrapes events within WildcatConnection
 * and stores them to the database.
 * @author Al Johri
*/
public class ScrapeWildcatConnection extends Scrape {
	/**
	 * connects to northwestern.collegiatelink.net/events
	 * extracts all the data from this link
	 * Store the data as Json objects
	 * @return json objects
	 */

    public static Result scrape_wildcatconnection() {
        String json = "{}";
        try {
            org.jsoup.Connection conn; org.jsoup.Connection.Response res;

            conn = Jsoup.connect("https://northwestern.collegiatelink.net/events");
            conn = conn.data("view", "calendar", "CurrentMonth", "5", "CurrentYear", "2013", "CurrentPage", "1");
            res = conn.method(Method.POST).ignoreContentType(true).execute();
            json = res.body();

            com.google.gson.JsonElement jelement = new JsonParser().parse(json);
            com.google.gson.JsonObject jobject = jelement.getAsJsonObject();
            com.google.gson.JsonArray jarray = jobject.getAsJsonArray("Events");

            for (com.google.gson.JsonElement object : jarray) {
                com.google.gson.JsonObject element = object.getAsJsonObject();
                String result = element.get("Name").toString();
                System.out.println(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok(json);
    }

}
