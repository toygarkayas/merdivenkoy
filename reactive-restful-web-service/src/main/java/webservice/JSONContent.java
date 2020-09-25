package webservice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JSONContent {
	@JsonProperty("name")
	private String name;
	@JsonProperty("color")
	private String color;
	
	@JsonProperty("name")
	public String getName() {
		return name;
	}
	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}
	@JsonProperty("color")
	public String getColor() {
		return color;
	}
	@JsonProperty("color")
	public void setColor(String color) {
		this.color = color;
	}
}
