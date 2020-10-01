package alexrm84.adapter.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Coordinates implements Serializable {
    private String latitude;
    private String longitude;

    @Override
    public String toString() {
        return "\"latitude\": \"" + latitude + "\"," +
                "\"longitude\": \"" + longitude + "\"" ;
    }
}
