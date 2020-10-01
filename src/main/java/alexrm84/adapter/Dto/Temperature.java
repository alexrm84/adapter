package alexrm84.adapter.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Temperature {
    private Air air;
    private Comfort comfort;
    private Water water;
}
