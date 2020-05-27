/**
 * 
 */
package gov.nih.niehs.ods.metadataindexer;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Jackson object representing a file or collection and its metadata in ES
 * 
 * @author conwaymc
 *
 */
public class FileObjectModel {

	@JsonProperty("url")
	private String url = "";

	@JsonProperty("zoneName")
	private String zoneName = "";

	@JsonProperty("absolutePath")
	private String absolutePath = "";

	@JsonProperty("fileName")
	private String fileName = "";

	@JsonProperty("parentPath")
	private String parentPath = "";

	@JsonProperty("isFile")
	private boolean file = true;

	@JsonProperty("dataSize")
	private long dataSize = 0L;

	@JsonProperty("mimeType")
	private String mimeType = "";

	@JsonProperty("lastModifiedDate")
	private String lastModifiedDate = "";

}
