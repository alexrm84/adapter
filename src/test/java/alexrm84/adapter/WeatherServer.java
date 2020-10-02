package alexrm84.adapter;

import alexrm84.adapter.Dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.Parameter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WeatherServer {
//    private MockRestServiceServer mockGisServer;
//    private RestTemplate restTemplate;
    private ObjectMapper mapper = new ObjectMapper();

    private static ClientAndServer mockGisServer, mockServiceBServer;

//    @Autowired
//    public void setRestTemplate(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }

    @Before
    public void init() {
//        mockGisServer = MockRestServiceServer.createServer(restTemplate);
        mockGisServer = ClientAndServer.startClientAndServer(8191);
        mockServiceBServer = ClientAndServer.startClientAndServer(8192);
    }


    private void initGis() throws URISyntaxException, IOException {
        GisResponse resp = new GisResponse("Obs", new Temperature(
                new Air(28), new Comfort(26), new Water(21)));
//        mockGisServer.expect(ExpectedCount.once(),
//                requestTo(new URI("http://localhost:8191/getTemperature")))
//                .andExpect(method(HttpMethod.GET))
//                .andExpect(header("X-Gismeteo-Token", "56b30cb255.3443075"))
//                .andExpect(queryParam("latitude", "54.35"))
//                .andExpect(queryParam("longitude", "52.52"))
//                .andRespond(withStatus(HttpStatus.OK)
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(mapper.writeValueAsString(resp)));
        new MockServerClient("127.0.0.1", 8191)
                .when(
                    HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/getTemperature")
                        .withQueryStringParameters(new Parameter("latitude", "54.35"),
                                new Parameter("longitude", "52.52"))
                        .withHeaders(new Header("X-Gismeteo-Token", "56b30cb255.3443075"))
                ).respond(
                    HttpResponse.response()
                    .withStatusCode(200)
                    .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
                    .withBody(mapper.writeValueAsString(resp))
                );
    }

    private void initServiceB() {
        new MockServerClient("127.0.0.1", 8192)
        .when(
                HttpRequest.request()
                        .withMethod("POST")
                        .withPath("/message")
        ).respond(
                HttpResponse.response()
                        .withStatusCode(200)
                        .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
        );
    }

    private org.apache.http.HttpResponse requestToGisTest() {
        String url = "http://localhost:8191/getTemperature?latitude=54.35&longitude=52.52";
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url);
        get.addHeader("X-Gismeteo-Token", "56b30cb255.3443075");
        org.apache.http.HttpResponse response = null;

        try {
            response = client.execute(get);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(">>>>>Server activity test>>>>>  "+response);
        return response;
    }

    private org.apache.http.HttpResponse requestToAdapterTest() {
        MsgA msgA = new MsgA("Hello", "ru", new Coordinates("54.35", "52.52"));
        StringEntity se = new StringEntity(msgA.toString(), ContentType.APPLICATION_JSON);
        String url = "http://localhost:8190/camel/temp";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/json");
        post.setEntity(se);
        org.apache.http.HttpResponse response = null;

        try {
            response = client.execute(post);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(">>>>>  "+response);
        return response;
    }

    @Test
    public void mockServerTest() throws Exception {
        initGis();
        initServiceB();
//        Assert.assertEquals(200, requestToGisTest().getStatusLine().getStatusCode());
        Assert.assertEquals(200, requestToAdapterTest().getStatusLine().getStatusCode());
//        Assert.assertEquals(504, requestToAdapterTest().getStatusLine().getStatusCode());
    }
}
