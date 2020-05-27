package gov.nih.niehs.ods.metadataindexer;

import org.irods.jargon.core.connection.IRODSProtocolManager;
import org.irods.jargon.core.connection.IRODSSession;
import org.irods.jargon.core.connection.IRODSSimpleProtocolManager;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSAccessObjectFactoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	@Bean
	public IRODSSession irodsSession(IRODSProtocolManager irodsProtocolManager) {
		IRODSSession irodsSession = new IRODSSession();
		irodsSession.setIrodsProtocolManager(irodsProtocolManager);
		return irodsSession;
	}

	@Bean
	public IRODSAccessObjectFactory irodsAccessObjectFactory(IRODSSession irodsSession) {
		IRODSAccessObjectFactory irodsAccessObjectFactory = new IRODSAccessObjectFactoryImpl(irodsSession);
		return irodsAccessObjectFactory;
	}

	@Bean(destroyMethod = "destroy")
	public IRODSProtocolManager irodsConnectionManager() {
		return new IRODSSimpleProtocolManager();
	}

}
