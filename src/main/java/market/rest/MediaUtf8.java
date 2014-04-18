package market.rest;

import java.nio.charset.Charset;
import org.springframework.http.MediaType;

/**
 * 
 */
public class MediaUtf8 {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
}
