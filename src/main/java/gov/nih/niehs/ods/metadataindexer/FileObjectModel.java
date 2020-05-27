/**
 * 
 */
package gov.nih.niehs.ods.metadataindexer;

import java.util.ArrayList;
import java.util.List;

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

	@JsonProperty("metadataEntries")
	private List<MetadataEntry> metadataEntries = new ArrayList<>();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getParentPath() {
		return parentPath;
	}

	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	public boolean isFile() {
		return file;
	}

	public void setFile(boolean file) {
		this.file = file;
	}

	public long getDataSize() {
		return dataSize;
	}

	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public List<MetadataEntry> getMetadataEntries() {
		return metadataEntries;
	}

	public void setMetadataEntries(List<MetadataEntry> metadataEntries) {
		this.metadataEntries = metadataEntries;
	}

	@Override
	public String toString() {
		final int maxLen = 10;
		StringBuilder builder = new StringBuilder();
		builder.append("FileObjectModel [");
		if (url != null) {
			builder.append("url=").append(url).append(", ");
		}
		if (zoneName != null) {
			builder.append("zoneName=").append(zoneName).append(", ");
		}
		if (absolutePath != null) {
			builder.append("absolutePath=").append(absolutePath).append(", ");
		}
		if (fileName != null) {
			builder.append("fileName=").append(fileName).append(", ");
		}
		if (parentPath != null) {
			builder.append("parentPath=").append(parentPath).append(", ");
		}
		builder.append("file=").append(file).append(", dataSize=").append(dataSize).append(", ");
		if (mimeType != null) {
			builder.append("mimeType=").append(mimeType).append(", ");
		}
		if (lastModifiedDate != null) {
			builder.append("lastModifiedDate=").append(lastModifiedDate).append(", ");
		}
		if (metadataEntries != null) {
			builder.append("metadataEntries=")
					.append(metadataEntries.subList(0, Math.min(metadataEntries.size(), maxLen)));
		}
		builder.append("]");
		return builder.toString();
	}

}
