package gov.nih.niehs.ods.metadataindexer;

import org.irods.jargon.core.connection.ClientServerNegotiationPolicy.SslNegotiationPolicy;
import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.connection.SettableJargonProperties;
import org.irods.jargon.core.connection.SettableJargonPropertiesMBean;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.datautils.indexer.ConfigurableIndexerFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(scanBasePackages = { "gov.nih.*" }, exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class,
		ElasticsearchDataAutoConfiguration.class, ElasticsearchRepositoriesAutoConfiguration.class })
public class IndexerApp {

	public static final Logger log = LoggerFactory.getLogger(IndexerApp.class);

	@Autowired
	private IndexerConfiguration indexerConfiguration;

	@Autowired
	private IRODSAccessObjectFactory irodsAccessObjectFactory;

	@Autowired
	private ElasticSearchMetadataService elasticSearchService;

	public IndexerApp() {
	}

	public static void main(String[] args) {
		SpringApplication.run(IndexerApp.class, args);
	}

	// Only if I implement the CommandLineRunner interface...
	public void run(String... args) throws Exception {
		// host, port, zone, user, password, url prefix, dbhost, dbport, dbuid, dbpasswd
		log.info("Start of thunderstone scan...");
		SettableJargonPropertiesMBean settableJargonProperties = new SettableJargonProperties(
				irodsAccessObjectFactory.getJargonProperties());
		settableJargonProperties.setNegotiationPolicy(SslNegotiationPolicy.CS_NEG_REFUSE);
		irodsAccessObjectFactory.getIrodsSession().setJargonProperties(settableJargonProperties);
		IRODSAccount irodsAccount = IRODSAccount.instance(indexerConfiguration.getIrodsHost(),
				indexerConfiguration.getPort(), indexerConfiguration.getIrodsUser(),
				indexerConfiguration.getIrodsPassword(), "", indexerConfiguration.getIrodsZone(), "");

		FileObjectIndexerVisitor visitor = new FileObjectIndexerVisitor(irodsAccessObjectFactory, irodsAccount,
				indexerConfiguration);
		visitor.setElasticSearchSampleService(elasticSearchService);
		ConfigurableIndexerFilter filter = new ConfigurableIndexerFilter();
		filter.setIndexerName(indexerConfiguration.getIndexerName());
		filter.setIndexIfNoAvuOnCollection(true);
		filter.setIndexIfNoAvuOnDataObject(true);

		visitor.setIndexerFilter(filter);
		log.info("launching indexer");
		visitor.launch(indexerConfiguration.getScanRootDir());
		log.info("complete");

	}

	/**
	 * @return the elasticSearchService
	 */
	public ElasticSearchMetadataService getElasticSearchService() {
		return elasticSearchService;
	}

	/**
	 * @param elasticSearchService the elasticSearchService to set
	 */
	public void setElasticSearchService(ElasticSearchMetadataService elasticSearchService) {
		this.elasticSearchService = elasticSearchService;
	}

	public IndexerConfiguration getIndexerConfiguration() {
		return indexerConfiguration;
	}

	public void setIndexerConfiguration(IndexerConfiguration indexerConfiguration) {
		this.indexerConfiguration = indexerConfiguration;
	}

	public IRODSAccessObjectFactory getIrodsAccessObjectFactory() {
		return irodsAccessObjectFactory;
	}

	public void setIrodsAccessObjectFactory(IRODSAccessObjectFactory irodsAccessObjectFactory) {
		this.irodsAccessObjectFactory = irodsAccessObjectFactory;
	}

}
