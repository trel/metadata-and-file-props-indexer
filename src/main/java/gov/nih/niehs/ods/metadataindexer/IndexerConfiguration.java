/**
 * 
 */
package gov.nih.niehs.ods.metadataindexer;

import org.irods.jargon.core.connection.AuthScheme;
import org.irods.jargon.core.exception.JargonRuntimeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Mike Conway - NIEHS
 *
 */

@PropertySources({ @PropertySource(value = "classpath:testing.properties", ignoreResourceNotFound = true),
		@PropertySource("file:///etc/irods-ext/metadata-es.properties") })

@Component
public class IndexerConfiguration {

	@Value("${irods.host}")
	private String irodsHost;

	@Value("${irods.zone}")
	private String irodsZone;

	@Value("${irods.port}")
	private int port;

	@Value("${irods.user}")
	private String irodsUser;

	@Value("${irods.password}")
	private String irodsPassword;

	@Value("${web.interface.url}")
	private String urlPrefix;

	@Value("${auth.type}")
	private String authScheme;

	@Value("${ssl.negotiation.policy}")
	private String sslNegotiationPolicy;

	@Value("${irods.root.dir}")
	private String scanRootDir;

	@Value("${indexer.name}")
	private String indexerName = "metadata-es";

	@Value("${es.protocol}")
	private String elasticSearchProtocol;

	@Value("${es.host}")
	private String elasticSearchHost;

	@Value("${es.port}")
	private int elasticSearchPort;

	@Value("${es.path}")
	private String elasticSearchIndexPath;

	public String getIrodsUser() {
		return irodsUser;
	}

	public void setIrodsUser(String irodsUser) {
		this.irodsUser = irodsUser;
	}

	public String getIrodsPassword() {
		return irodsPassword;
	}

	public void setIrodsPassword(String irodsPassword) {
		this.irodsPassword = irodsPassword;
	}

	public AuthScheme translateAuthSchemeToEnum() {
		String authSchemeStr = authScheme;
		if (authSchemeStr == null || authSchemeStr.isEmpty()) {
			return AuthScheme.STANDARD;
		} else if (authSchemeStr.equals(AuthScheme.STANDARD.toString())) {
			return AuthScheme.STANDARD;
		} else if (authSchemeStr.equals(AuthScheme.PAM.toString())) {
			return AuthScheme.PAM;
		} else {
			throw new JargonRuntimeException("unknown authscheme");
		}
	}

	public String getIrodsHost() {
		return irodsHost;
	}

	public void setIrodsHost(String irodsHost) {
		this.irodsHost = irodsHost;
	}

	public String getIrodsZone() {
		return irodsZone;
	}

	public void setIrodsZone(String irodsZone) {
		this.irodsZone = irodsZone;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public String getAuthScheme() {
		return authScheme;
	}

	public void setAuthScheme(String authScheme) {
		this.authScheme = authScheme;
	}

	public void setSslNegotiationPolicy(String sslNegotiationPolicy) {
		this.sslNegotiationPolicy = sslNegotiationPolicy;
	}

	public String getSslNegotiationPolicy() {
		return sslNegotiationPolicy;
	}

	public String getScanRootDir() {
		return scanRootDir;
	}

	public void setScanRootDir(String scanRootDir) {
		this.scanRootDir = scanRootDir;
	}

	public String getIndexerName() {
		return indexerName;
	}

	public void setIndexerName(String indexerName) {
		this.indexerName = indexerName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IndexerConfiguration [");
		if (irodsHost != null) {
			builder.append("irodsHost=").append(irodsHost).append(", ");
		}
		if (irodsZone != null) {
			builder.append("irodsZone=").append(irodsZone).append(", ");
		}
		builder.append("port=").append(port).append(", ");
		if (irodsUser != null) {
			builder.append("irodsUser=").append(irodsUser).append(", ");
		}
		if (irodsPassword != null) {
			builder.append("irodsPassword=").append(irodsPassword).append(", ");
		}
		if (urlPrefix != null) {
			builder.append("urlPrefix=").append(urlPrefix).append(", ");
		}
		if (authScheme != null) {
			builder.append("authScheme=").append(authScheme).append(", ");
		}
		if (sslNegotiationPolicy != null) {
			builder.append("sslNegotiationPolicy=").append(sslNegotiationPolicy).append(", ");
		}
		if (scanRootDir != null) {
			builder.append("scanRootDir=").append(scanRootDir).append(", ");
		}
		if (indexerName != null) {
			builder.append("indexerName=").append(indexerName).append(", ");
		}
		if (elasticSearchProtocol != null) {
			builder.append("elasticSearchProtocol=").append(elasticSearchProtocol).append(", ");
		}
		if (elasticSearchHost != null) {
			builder.append("elasticSearchHost=").append(elasticSearchHost).append(", ");
		}
		builder.append("elasticSearchPort=").append(elasticSearchPort).append(", ");
		if (elasticSearchIndexPath != null) {
			builder.append("elasticSearchIndexPath=").append(elasticSearchIndexPath);
		}
		builder.append("]");
		return builder.toString();
	}

	public String getElasticSearchProtocol() {
		return elasticSearchProtocol;
	}

	public void setElasticSearchProtocol(String elasticSearchProtocol) {
		this.elasticSearchProtocol = elasticSearchProtocol;
	}

	public String getElasticSearchHost() {
		return elasticSearchHost;
	}

	public void setElasticSearchHost(String elasticSearchHost) {
		this.elasticSearchHost = elasticSearchHost;
	}

	public int getElasticSearchPort() {
		return elasticSearchPort;
	}

	public void setElasticSearchPort(int elasticSearchPort) {
		this.elasticSearchPort = elasticSearchPort;
	}

	public String getElasticSearchIndexPath() {
		return elasticSearchIndexPath;
	}

	public void setElasticSearchIndexPath(String elasticSearchIndexPath) {
		this.elasticSearchIndexPath = elasticSearchIndexPath;
	}
}
