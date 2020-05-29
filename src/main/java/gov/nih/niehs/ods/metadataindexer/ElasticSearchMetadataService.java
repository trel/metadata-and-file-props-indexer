/**
 * 
 */
package gov.nih.niehs.ods.metadataindexer;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.apache.http.HttpHost;
import org.bouncycastle.util.encoders.Base64;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.irods.jargon.core.exception.JargonRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Wrap elastic search operations (putting data into an index)
 * 
 * @author conwaymc
 *
 */
@Component
public class ElasticSearchMetadataService {

	public static final Logger log = LoggerFactory.getLogger(ElasticSearchMetadataService.class);
	private JsonUtils jsonUtils = new JsonUtils();
	private RestClient restClient;

	@Autowired
	private IndexerConfiguration indexerConfiguration;

	public ElasticSearchMetadataService() {

	}

	public IndexerConfiguration getIndexerConfiguration() {
		return indexerConfiguration;
	}

	public void setIndexerConfiguration(IndexerConfiguration indexerConfiguration) {
		this.indexerConfiguration = indexerConfiguration;
	}

	public void indexFileObject(FileObjectModel fileObjectModel) {
		log.info("indexFileObject()");
		String json = jsonUtils.jsonStringFromFileModel(fileObjectModel);

		/*
		 * formulate the call to ES
		 */

		StringBuilder sb = new StringBuilder();
		sb.append(indexerConfiguration.getElasticSearchIndexPath());

		byte[] bytesEncoded = Base64.encode(fileObjectModel.getAbsolutePath().getBytes());
		sb.append("/");
		sb.append(new String(bytesEncoded));
		String esPath = sb.toString();
		log.info("adding at es path:{}", esPath);

		Request request = new Request("POST", esPath);
		request.setJsonEntity(json);
		try {
			Response response = restClient.performRequest(request);
			log.info("response:{}", response);
		} catch (IOException e) {
			log.error("Error adding index", e);
			throw new JargonRuntimeException("Error adding index", e);
		}

	}

	@PostConstruct
	public void init() {
		restClient = RestClient
				.builder(new HttpHost(indexerConfiguration.getElasticSearchHost(),
						indexerConfiguration.getElasticSearchPort(), indexerConfiguration.getElasticSearchProtocol()))
				.build();
	}

	public void close() {
		try {
			restClient.close();
		} catch (IOException e) {
			log.error("error in closing restClient", e);
			throw new JargonRuntimeException("error closing restClient", e);
		}
	}

}
