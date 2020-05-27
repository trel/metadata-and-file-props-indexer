/**
 * 
 */
package gov.nih.niehs.ods.metadataindexer;

import java.io.IOException;
import java.io.InputStream;

import org.irods.jargon.core.exception.JargonRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * General json utils
 * 
 * @author conwaymc
 *
 */
public class JsonUtils {

	private ObjectMapper objectMapper = new ObjectMapper();
	public static final Logger log = LoggerFactory.getLogger(JsonUtils.class);

	/**
	 * 
	 */
	public JsonUtils() {
	}

	public FileObjectModel fileObjectFromJson(final InputStream jsonStream) {
		try {
			return objectMapper.readValue(jsonStream, FileObjectModel.class);
		} catch (IOException e) {
			log.error("error reading json", e);
			throw new JargonRuntimeException("error reading json", e);
		}

	}

	public String jsonStringFromFileModel(final FileObjectModel fileObjectModel) {
		try {
			return objectMapper.writeValueAsString(fileObjectModel);
		} catch (JsonProcessingException e) {
			log.error("error writing json", e);
			throw new JargonRuntimeException("error writing json", e);
		}
	}

}
