package alexrm84.adapter.processors;

import alexrm84.adapter.Dto.MsgA;
import alexrm84.adapter.Dto.MsgB;
import alexrm84.adapter.services.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class GetTempProcessor implements Processor {
    private WeatherService weatherService;
    private MsgA msgA;
    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public void setWeatherService(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        msgA = exchange.getIn().getBody(MsgA.class);
        String date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
        MsgB msgB = null;
        msgB = new MsgB(msgA.getMsg(), date, weatherService.getTemperature(msgA.getCoordinates()));
//        msgB = new MsgB(msgA.getMsg(), date, 28);
        exchange.getIn().setBody(mapper.writeValueAsString(msgB));
        System.out.println(exchange.getIn().getBody(String.class));
    }
}
