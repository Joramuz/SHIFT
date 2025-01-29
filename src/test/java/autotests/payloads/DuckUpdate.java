package autotests.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@Accessors(fluent = true)
public class DuckUpdate {
    @JsonProperty("color")
    private String color;
    @JsonProperty("height")
    private Double height;
    @JsonProperty("material")
    private String material;
    @JsonProperty("sound")
    private String sound;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("wingsState")
    private WingsState wingsState;
}
