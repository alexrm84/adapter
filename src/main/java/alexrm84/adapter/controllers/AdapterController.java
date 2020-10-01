package alexrm84.adapter.controllers;

import alexrm84.adapter.Dto.MsgA;
import alexrm84.adapter.Dto.enums.Lang;
import alexrm84.adapter.errors.GisServiceException;
import alexrm84.adapter.processors.GetTempProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class AdapterController extends RouteBuilder {
    private GetTempProcessor processor;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public AdapterController(GetTempProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void configure() throws Exception {
        CamelContext context = new DefaultCamelContext();
        restConfiguration()
                .component("servlet")
                .contextPath("")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .host("http://localhost").port(8190);

//        onException(GisServiceException.class)
//                .process(ex -> {
//                    ex.removeProperty("CamelExceptionCaught");
//                    ex.getIn().setBody(mapper.writeValueAsString(new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE)));
//                })
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500));

        rest("/temp").consumes("application/json")
                .post().bindingMode(RestBindingMode.json).type(MsgA.class)
                .route().choice()
                    .when(exchange -> (exchange.getIn().getBody(MsgA.class).getLng().equals(Lang.ru.name())))
                        .process(processor)//.to("mock:serviceB");
                        .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                        .removeHeader(Exchange.HTTP_URI)
                        .to("http://localhost:8192/message")
                .endChoice().end().choice()
                    .when(exchange -> (exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE).equals(200)))
                        .log(">>>> answer OK")
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(200))
                    .when(exchange -> (!exchange.getIn().getHeader(Exchange.HTTP_RESPONSE_CODE).equals(200)))
                        .log(">>>> answer Error")
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(504));
    }
}
