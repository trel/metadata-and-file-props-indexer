/**
 * 
 */
package gov.nih.niehs.ods.metadataindexer;

import org.irods.jargon.core.connection.IRODSAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author conwaymc
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = { "gov.nih.*" })

public class IndexerApp implements CommandLineRunner {

	public static final Logger log = LoggerFactory.getLogger(IndexerApp.class);

	private IRODSAccount irodsAccount;

	public IndexerApp() {
	}

	public static void main(String[] args) {
		SpringApplication.run(IndexerApp.class, args);
	}

	// Only if I implement the CommandLineRunner interface...
	@Override
	public void run(String... args) throws Exception {
		// host, port, zone, user, password, url prefix, dbhost, dbport, dbuid, dbpasswd

		if (args.length != 10) {
			throw new IllegalArgumentException(
					"need host, port, zone, user, password, urlprefix, dbhost, dbport, dbuid, dbpasswd");
		}

	}
}
