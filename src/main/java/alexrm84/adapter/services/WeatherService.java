package alexrm84.adapter.services;

import alexrm84.adapter.Dto.Coordinates;
import alexrm84.adapter.Dto.GisResponse;
import alexrm84.adapter.errors.GisServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WeatherService {

    public Integer getTemperature(Coordinates coord) throws IOException {
        String url = "http://127.0.0.1:8191/getTemperature?latitude="
                + coord.getLatitude() + "&longitude=" + coord.getLongitude();
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        get.setHeader("X-Gismeteo-Token", "56b30cb255.3443075");
        org.apache.http.HttpResponse response = null;

        try {
            response = client.execute(get);
        } catch (Exception e) {
            throw new GisServiceException("The weather server is unavailable");
        }
        GisResponse resp = null;
        if (response != null && response.getStatusLine().getStatusCode() == 200) {
            String json = EntityUtils.toString(response.getEntity(), "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            resp = mapper.readValue(json, GisResponse.class);
        }
        System.out.println(resp);
        System.out.println(resp.getTemperature().getAir().getC());
        return resp.getTemperature().getAir().getC();
    }
}
