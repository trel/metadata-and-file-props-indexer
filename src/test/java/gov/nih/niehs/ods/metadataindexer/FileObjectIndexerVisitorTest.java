package gov.nih.niehs.ods.metadataindexer;

import java.util.Properties;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.pub.CollectionAO;
import org.irods.jargon.core.pub.DataObjectAO;
import org.irods.jargon.core.pub.DataTransferOperations;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.domain.AvuData;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.datautils.indexer.ConfigurableIndexerFilter;
import org.irods.jargon.testutils.IRODSTestSetupUtilities;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.irods.jargon.testutils.filemanip.FileGenerator;
import org.irods.jargon.testutils.filemanip.ScratchFileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class FileObjectIndexerVisitorTest {

	private static Properties testingProperties = new Properties();
	private static TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	private static ScratchFileUtils scratchFileUtils = null;
	public static final String IRODS_TEST_SUBDIR_PATH = "FileObjectIndexerVisitorTest";
	private static IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static IRODSFileSystem irodsFileSystem;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
		scratchFileUtils = new ScratchFileUtils(testingProperties);
		scratchFileUtils.clearAndReinitializeScratchDirectory(IRODS_TEST_SUBDIR_PATH);
		irodsTestSetupUtilities = new IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		irodsFileSystem = IRODSFileSystem.instance();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@After
	public void afterEach() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Test
	public void testIndexMetadata() throws Exception {
		String runFolderName = "NS00001";
		String testFileName = "hithere.jpg";
		String testFileName2 = "hithere.fastq.gz";

		String testPi = "fred flintstone";
		String testProject = "Looking at how big a piece of pie should be";
		String testProjectId = "booo_baaah";
		String testSample = "apple-pie";
		String testSampleUnique = "apple-pie-abcd1";

		IRODSAccount irodsAccount = testingPropertiesHelper.buildIRODSAccountFromTestProperties(testingProperties);

		IndexerConfiguration config = new IndexerConfiguration();
		config.setIndexerName("testIndexRunFolder");
		config.setUrlPrefix(testingProperties.getProperty("web.interface.url"));

		String targetIrodsCollection = testingPropertiesHelper.buildIRODSCollectionAbsolutePathFromTestProperties(
				testingProperties, IRODS_TEST_SUBDIR_PATH + '/' + runFolderName);

		// make a run folder
		IRODSAccessObjectFactory accessObjectFactory = irodsFileSystem.getIRODSAccessObjectFactory();
		CollectionAO collectionAO = accessObjectFactory.getCollectionAO(irodsAccount);

		IRODSFile testFile = irodsFileSystem.getIRODSFileFactory(irodsAccount).instanceIRODSFile(targetIrodsCollection);
		testFile.deleteWithForceOption();
		testFile.mkdirs();

		AvuData avuData = AvuData.instance("PIName", testPi, "");
		collectionAO.addAVUMetadata(targetIrodsCollection, avuData);
		avuData = AvuData.instance("ProjectTitle", testProject, "");
		collectionAO.addAVUMetadata(targetIrodsCollection, avuData);

		// make the qc and raw data subfolders
		String qcCollection = targetIrodsCollection + "/qc";
		testFile = irodsFileSystem.getIRODSFileFactory(irodsAccount).instanceIRODSFile(qcCollection);
		testFile.mkdirs();

		String rawCollection = targetIrodsCollection + "/raw_data";
		testFile = irodsFileSystem.getIRODSFileFactory(irodsAccount).instanceIRODSFile(rawCollection);
		testFile.mkdirs();

		// make a couple fake files
		String absPath = scratchFileUtils.createAndReturnAbsoluteScratchPath(IRODS_TEST_SUBDIR_PATH);
		String localFileName1 = FileGenerator.generateFileOfFixedLengthGivenName(absPath, testFileName, 1);

		DataTransferOperations dto = irodsFileSystem.getIRODSAccessObjectFactory()
				.getDataTransferOperations(irodsAccount);

		dto.putOperation(localFileName1, qcCollection, "", null, null);

		String localFileName2 = FileGenerator.generateFileOfFixedLengthGivenName(absPath, testFileName2, 1);
		dto.putOperation(localFileName2, rawCollection, "", null, null);

		DataObjectAO dataObjectAO = irodsFileSystem.getIRODSAccessObjectFactory().getDataObjectAO(irodsAccount);
		String rawFileName = rawCollection + "/" + testFileName2;

		avuData = AvuData.instance("SampleName", testSample, "");
		dataObjectAO.addAVUMetadata(rawFileName, avuData);
		avuData = AvuData.instance("SampleName_unique", testSampleUnique, "");
		dataObjectAO.addAVUMetadata(rawFileName, avuData);
		avuData = AvuData.instance("ProjectTitle", testProjectId, "");
		dataObjectAO.addAVUMetadata(rawFileName, avuData);

		// do the indexing

		FileObjectIndexerVisitor visitor = new FileObjectIndexerVisitor(irodsFileSystem.getIRODSAccessObjectFactory(),
				irodsAccount, config);

		ElasticSearchMetadataService elasticSearchService = Mockito.mock(ElasticSearchMetadataService.class);

		visitor.setElasticSearchSampleService(elasticSearchService);
		ConfigurableIndexerFilter filter = new ConfigurableIndexerFilter();
		filter.setIndexerName(config.getIndexerName());
		filter.setIndexIfNoAvuOnCollection(true);
		filter.setIndexIfNoAvuOnDataObject(true);

		visitor.setIndexerFilter(filter);
		visitor.launch(targetIrodsCollection);

	}
}
