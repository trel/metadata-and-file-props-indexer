/**
 * 
 */
package gov.nih.niehs.ods.metadataindexer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.exception.JargonRuntimeException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.io.IRODSFileImpl;
import org.irods.jargon.core.query.MetaDataAndDomainData;
import org.irods.jargon.core.utils.MiscIRODSUtils;
import org.irods.jargon.datautils.indexer.AbstractIndexerVisitor;
import org.irods.jargon.datautils.indexer.MetadataRollup;
import org.irods.jargon.datautils.visitor.HierComposite;
import org.irods.jargon.datautils.visitor.HierLeaf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author conwaymc
 *
 */

public class FileObjectIndexerVisitor extends AbstractIndexerVisitor {

	static int id = 0;

	public static final Logger log = LoggerFactory.getLogger(FileObjectIndexerVisitor.class);

	private IndexerConfiguration indexerConfiguration;

	private ElasticSearchMetadataService elasticSearchMetadataService;

	SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd - hh:mm:ss");

	public FileObjectIndexerVisitor(IRODSAccessObjectFactory irodsAccessObjectFactory, IRODSAccount irodsAccount,
			IndexerConfiguration indexerConfiguration) throws JargonException {
		super(irodsAccessObjectFactory, irodsAccount);
		this.indexerConfiguration = indexerConfiguration;
	}

	@Override
	public void launch(String startingCollectionPath) {
		super.launch(startingCollectionPath);
	}

	@Override
	public boolean visitEnterWithMetadata(HierComposite node, MetadataRollup metadataRollup) {

		log.info("visitEnterWithMetadata()");
		IRODSFileImpl coll = (IRODSFileImpl) node;
		log.info("coll:{}", coll);
		FileObjectModel model = new FileObjectModel();
		model.setAbsolutePath(node.getAbsolutePath());
		model.setDataSize(0);
		model.setFile(false);
		model.setFileName(node.getName());
		model.setLastModifiedDate(String.valueOf(coll.lastModified()));
		model.setMimeType("");
		model.setParentPath(coll.getParent());
		model.setUrl(generateUrl(coll.getAbsolutePath()));
		model.setZoneName(MiscIRODSUtils.getZoneInPath(node.getAbsolutePath()));

		List<MetaDataAndDomainData> currMetadata = metadataRollup.getMetadata().peek();
		log.debug("collection metadata:{}", currMetadata);

		MetadataEntry metadataEntry = null;
		for (MetaDataAndDomainData avu : currMetadata) {
			metadataEntry = new MetadataEntry();
			metadataEntry.setAttribute(avu.getAvuAttribute());
			metadataEntry.setValue(avu.getAvuValue());
			metadataEntry.setUnit(avu.getAvuUnit());
			model.getMetadataEntries().add(metadataEntry);
		}

		this.elasticSearchMetadataService.indexFileObject(model);

		return true;
	}

	@Override
	public boolean visitLeaveWithMetadata(HierComposite node, MetadataRollup metadataRollup, boolean visitorEntered) {
		IRODSFileImpl coll = (IRODSFileImpl) node;
		return true;

	}

	private String generateUrl(final String absolutePath) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.getIndexerConfiguration().getUrlPrefix());
		sb.append(absolutePath);
		return sb.toString();
	}

	@Override
	public boolean visitWithMetadata(HierLeaf node, MetadataRollup metadataRollup) {

		log.info("visitEnterWithMetadata()");
		IRODSFileImpl file = (IRODSFileImpl) node;
		log.info("file:{}", file);
		FileObjectModel model = new FileObjectModel();
		model.setAbsolutePath(node.getAbsolutePath());
		model.setDataSize(file.length());
		model.setFile(true);
		model.setFileName(node.getName());
		model.setLastModifiedDate(String.valueOf(file.lastModified()));
		model.setMimeType("");
		model.setParentPath(file.getParent());
		model.setUrl(generateUrl(file.getAbsolutePath()));
		model.setZoneName(MiscIRODSUtils.getZoneInPath(node.getAbsolutePath()));

		List<MetaDataAndDomainData> currMetadata = metadataRollup.getMetadata().peek();
		log.debug("file metadata:{}", currMetadata);

		MetadataEntry metadataEntry = null;
		for (MetaDataAndDomainData avu : currMetadata) {
			metadataEntry = new MetadataEntry();
			metadataEntry.setAttribute(avu.getAvuAttribute());
			metadataEntry.setValue(avu.getAvuValue());
			metadataEntry.setUnit(avu.getAvuUnit());
			model.getMetadataEntries().add(metadataEntry);
		}

		this.elasticSearchMetadataService.indexFileObject(model);
		return true;

	}

	private String parseDateTime(final String dateValue) {
		String myDate = checkAvuValueForEmpty(dateValue);
		if (myDate.isEmpty()) {
			return String.valueOf(new Date().getTime());
		}

		if (myDate.equals("0001-01-01T00:00:00Z")) {
			return String.valueOf(new Date().getTime());
		}

		try {
			Date convDate = format1.parse(myDate);
			return String.valueOf(convDate.getTime());
		} catch (ParseException e) {
			log.error("error parsing date:{}", myDate, e);
			throw new JargonRuntimeException("error processing date, e");
		}
		/* 2019/01/15 - 12:13:01 */

	}

	private String checkAvuValueForEmpty(final String avuValue) {
		if (avuValue.trim().equals("-empty-")) {
			return "";
		} else {
			return avuValue;
		}
	}

	private int checkAvuValueForInt(final String avuValue) {
		if (avuValue.trim().equals("-empty-")) {
			return 0;
		} else if (avuValue.isEmpty()) {
			return 0;
		} else {
			return Integer.parseInt(avuValue);
		}
	}

	private float checkAvuValueForFloat(final String avuValue) {
		if (avuValue.trim().equals("-empty-")) {
			return 0;
		} else if (avuValue.isEmpty()) {
			return 0;
		} else {
			return Float.parseFloat(avuValue);
		}
	}

	public IndexerConfiguration getIndexerConfiguration() {
		return indexerConfiguration;
	}

	public void setIndexerConfiguration(IndexerConfiguration indexerConfiguration) {
		this.indexerConfiguration = indexerConfiguration;
	}

	public ElasticSearchMetadataService getElasticSearchSampleService() {
		return elasticSearchMetadataService;
	}

	public void setElasticSearchSampleService(ElasticSearchMetadataService elasticSearchSampleService) {
		this.elasticSearchMetadataService = elasticSearchSampleService;
	}

}
