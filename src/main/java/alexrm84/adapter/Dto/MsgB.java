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
public class MsgB implements Serializable {
    private String txt;
    private String createdDt;
    private Integer currentTemp;
}
