package alexrm84.adapter.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MsgA implements Serializable {
    private String msg;
    private String lng;
    private Coordinates coordinates;

    @Override
    public String toString() {
        return "{" +
                "\"msg\": \"" + msg + "\"," +
                "\"lng\": \"" + lng + "\"," +
                "\"coordinates\": {" + coordinates +
                "}}";
    }
}
