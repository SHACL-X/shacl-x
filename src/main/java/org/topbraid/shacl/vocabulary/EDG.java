package org.topbraid.shacl.vocabulary;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class EDG {

	public final static String BASE_URI = "http://edg.topbraid.solutions/model/";

    public final static String NAME = "EDG";

    public final static String NS = BASE_URI;

    public final static String PREFIX = "edg";

    public final static Resource BigDataAsset = ResourceFactory.createResource(NS + "BigDataAsset");
    public final static Resource BusinessArea = ResourceFactory.createProperty(NS + "BusinessArea");
    public final static Resource DataAsset = ResourceFactory.createResource(NS + "DataAsset");
    public final static Resource DatabaseColumn = ResourceFactory.createResource(NS + "DatabaseColumn");
    public final static Resource DatabaseTable = ResourceFactory.createResource(NS + "DatabaseTable");
    public final static Resource DatabaseView = ResourceFactory.createResource(NS + "DatabaseView");
    public final static Resource DataSet = ResourceFactory.createResource(NS + "DataSet");
    public final static Resource DataSetElement = ResourceFactory.createResource(NS + "DataSetElement");
    public final static Resource DataSubjectArea = ResourceFactory.createResource(NS + "DataSubjectArea");
    public final static Resource DataValueRule = ResourceFactory.createResource(NS + "DataValueRule");
    public final static Resource Datatype = ResourceFactory.createResource(NS + "Datatype");
    public final static Resource EnumerationViewpoint = ResourceFactory.createResource(NS + "EnumerationViewpoint");
    public final static Resource EnterpriseAsset = ResourceFactory.createResource(NS + "EnterpriseAsset");
    public final static Resource EXCELdatatype = ResourceFactory.createResource(NS + "EXCELdatatype");
    public final static Resource FrequenciesRecord = ResourceFactory.createResource(NS + "FrequenciesRecord");
    public final static Resource GlossaryTerm = ResourceFactory.createResource(NS + "GlossaryTerm");
    public final static Resource JobTitle = ResourceFactory.createResource(EDG.NS + "JobTitle");
    public final static Resource LineageModel = ResourceFactory.createResource(NS + "LineageModel");
    public final static Resource NumericDataElement = ResourceFactory.createResource(NS + "NumericDataElement");
    public final static Resource PhysicalDataModel = ResourceFactory.createResource(NS + "PhysicalDataModel");
    public final static Resource PhysicalDatatype = ResourceFactory.createResource(NS + "PhysicalDatatype");
    public final static Resource QuantilesRecord = ResourceFactory.createResource(NS + "QuantilesRecord");
    public final static Resource RelationalDatabase = ResourceFactory.createResource(NS + "RelationalDatabase");
    public final static Resource RequirementsViewpoint = ResourceFactory.createResource(NS + "RequirementsViewpoint");
    public final static Resource SpreadsheetDataSet = ResourceFactory.createResource(NS + "SpreadsheetDataSet");
    public final static Resource SpreadsheetsWorkbook = ResourceFactory.createResource(EDG.NS + "SpreadsheetsWorkbook");
    public final static Resource SQLdatatype = ResourceFactory.createResource(NS + "SQLdatatype");
    public final static Resource Stewardship = ResourceFactory.createResource(EDG.NS + "Stewardship");
	public final static Resource StringDataElement = ResourceFactory.createResource(NS + "StringDataElement");
    public final static Resource TechnicalAsset = ResourceFactory.createResource(NS + "TechnicalAsset");
    public final static Resource WorkflowParticipantProperty = ResourceFactory.createResource(NS + "WorkflowParticipantProperty");

    public final static Property assignedJobTitle = ResourceFactory.createProperty(NS + "assignedJobTitle");
    public final static Property autoIncremented = ResourceFactory.createProperty(NS + "autoIncremented");
    public final static Property belongsTo = ResourceFactory.createProperty(NS + "belongsTo");
    public final static Property columnOf = ResourceFactory.createProperty(NS + "columnOf");
    public final static Property databaseRemarks = ResourceFactory.createProperty(NS + "databaseRemarks");
    public final static Property dataElementOf = ResourceFactory.createProperty(NS + "dataElementOf");
    public final static Property dataSample = ResourceFactory.createProperty(NS + "dataSample");
    public final static Property dataSource = ResourceFactory.createProperty(NS + "dataSource");
    public final static Property dataValue = ResourceFactory.createProperty(NS + "dataValue");
    public final static Property distinctValuesCount = ResourceFactory.createProperty(NS + "distinctValuesCount");
    public final static Property equivalentTeamworkPermissionRole = ResourceFactory.createProperty(NS + "equivalentTeamworkPermissionRole");
    public final static Property errorMsg = ResourceFactory.createProperty(NS + "errorMsg");
    public final static Property format = ResourceFactory.createProperty(NS + "format");
    public final static Property frequenciesStatistic = ResourceFactory.createProperty(NS + "frequenciesStatistic");
    public final static Property frequencyCount = ResourceFactory.createProperty(NS + "frequencyCount");
    public final static Property generatedValue = ResourceFactory.createProperty(NS + "generatedValue");
    public final static Property isNullable = ResourceFactory.createProperty(NS + "isNullable");
    public final static Property isPrimaryKey = ResourceFactory.createProperty(NS + "isPrimaryKey");
    public final static Property lastImported = ResourceFactory.createProperty(NS + "lastImported");
    public final static Property lastProfiled = ResourceFactory.createProperty(NS + "lastProfiled");
    public final static Property length = ResourceFactory.createProperty(NS + "length");
    public final static Property locationLink = ResourceFactory.createProperty(NS + "locationLink");
    public final static Property mapsToTerm = ResourceFactory.createProperty(NS + "mapsToTerm");
    public final static Property maxLength = ResourceFactory.createProperty(NS + "maxLength");
    public final static Property maxValue = ResourceFactory.createProperty(NS + "maxValue");
    public final static Property meanLength = ResourceFactory.createProperty(NS + "meanLength");
    public final static Property meanValue = ResourceFactory.createProperty(NS + "meanValue");
    public final static Property medianLength = ResourceFactory.createProperty(NS + "medianLength");
    public final static Property medianValue = ResourceFactory.createProperty(NS + "medianValue");
    public final static Property minLength = ResourceFactory.createProperty(NS + "minLength");
    public final static Property minValue = ResourceFactory.createProperty(NS + "minValue");
    public final static Property name = ResourceFactory.createProperty(NS + "name");
    public final static Property nonNullValuesCount = ResourceFactory.createProperty(NS + "nonNullValuesCount");
    public final static Property nullValuesCount = ResourceFactory.createProperty(NS + "nullValuesCount");
    public final static Property numberOfColumns = ResourceFactory.createProperty(NS + "numberOfColumns");
    public final static Property numberOfInstances = ResourceFactory.createProperty(NS + "numberOfInstances");
    public final static Property numberOfProperties = ResourceFactory.createProperty(NS + "numberOfProperties");
    public final static Property numberOfTables = ResourceFactory.createProperty(NS + "numberOfTables");
    public final static Property partOf = ResourceFactory.createProperty(NS + "partOf");
    public final static Property physicalDatatype = ResourceFactory.createProperty(NS + "physicalDatatype");
    public final static Property precision = ResourceFactory.createProperty(NS + "precision");
    public final static Property quantilePercentile = ResourceFactory.createProperty(NS + "quantilePercentile");
    public final static Property quantileStatistic = ResourceFactory.createProperty(NS + "quantileStatistic");
    public final static Property realizedAs = ResourceFactory.createProperty(NS + "realizedAs");
    public final static Property recordCount = ResourceFactory.createProperty(NS + "recordCount");
    public final static Property scale = ResourceFactory.createProperty(NS + "scale");
    public final static Property sizeInBytes = ResourceFactory.createProperty(NS + "sizeInBytes");
    public final static Property standardDeviation = ResourceFactory.createProperty(NS + "standardDeviation");
    public final static Property subArea = ResourceFactory.createProperty(NS + "subArea");
    public final static Property subjectArea = ResourceFactory.createProperty(NS + "subjectArea");
    public final static Property tableOf = ResourceFactory.createProperty(NS + "tableOf");
    public final static Property timestamp = ResourceFactory.createProperty(NS + "timestamp");
    public final static Property type = ResourceFactory.createProperty(NS + "type");
    public final static Resource userWorkflowParticipantRolesOnProjectGraph = ResourceFactory.createResource(NS + "userWorkflowParticipantRolesOnProjectGraph");
    public final static Property valuesSum = ResourceFactory.createProperty(NS + "valuesSum");
    public final static Property variance = ResourceFactory.createProperty(NS + "variance");
    public final static Property version = ResourceFactory.createProperty(NS + "version");
    public final static Property viewOf = ResourceFactory.createProperty(NS + "viewOf");
    public final static Property xsdDataType = ResourceFactory.createProperty(NS + "xsdDataType");
}
