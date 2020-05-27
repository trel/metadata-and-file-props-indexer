/**
 * 
 */
package gov.nih.niehs.ods.metadataindexer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Avu entry for ES
 * 
 * @author conwaymc
 *
 */
public class MetadataEntry {

	@JsonProperty("attribute")
	private String attribute = "";

	@JsonProperty("value")
	private String value = "";

	@JsonProperty("unit")
	private String unit = "";

	/**
	 * 
	 */
	public MetadataEntry() {
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetadataEntry [");
		if (attribute != null) {
			builder.append("attribute=").append(attribute).append(", ");
		}
		if (value != null) {
			builder.append("value=").append(value).append(", ");
		}
		if (unit != null) {
			builder.append("unit=").append(unit);
		}
		builder.append("]");
		return builder.toString();
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
