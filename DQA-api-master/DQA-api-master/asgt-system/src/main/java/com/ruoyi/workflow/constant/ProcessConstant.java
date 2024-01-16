package com.ruoyi.workflow.constant;

/**
 * @author Omelette
 */
public class ProcessConstant {
    public static final String SOURCE_PROCESS_KEY = "SourceSupplierEntryProcess";

    public static final String POTENTIAL_PROCESS_KEY = "poentialSupplierEntryProcess";

    public static final String APPROVED_PROCESS_KEY = "approvedSupplierEntryProcess";

    public static final String RFI_PROCESS_KEY = "RFI";

    public static final String RFP_PROCESS_KEY = "RFP";

    public static final String RFQ_PROCESS_KEY = "RFQ";


    public static final String CHANGE_RECORDS_PROCESS_KEY = "SupplierModificationProcess";

    public static final String CLEAR_RECORDS_KEY = "supplierClearProcess";

    public static final String NOT_EXIST = "流程业务数据为空，无法执行流程操作！";

    public static final String START_CORRECT = "";


    public static final Integer SOURCE_SUPPLIER = 1;

    public static final Integer POTENTIAL_SUPPLIER = 2;

    public static final Integer APPROVED_SUPPLIER = 3;

    /** 四类不包含设计*/
    public static final Integer CATEGORY5 = 5;
    /** 五类不包含设计*/
    public static final Integer CATEGORY7 = 7;


    /** 0否1是*/
    public static final Integer YES = 1;
    public static final Integer NO = 0;

    public static final String SOURCE_SUPPLIER_ENTRY_PROCESS_NODE_ONE = "sid-D6BB6B8F-E513-4B30-A9BF-EF5C641BF3D2";
    public static final String SOURCE_SUPPLIER_ENTRY_PROCESS_NODE_TWO = "sid-761348E7-92C9-4895-AA48-EC0082711E79";
    public static final String SOURCE_SUPPLIER_ENTRY_PROCESS_NODE_THREE = "sid-A18E148B-C9C8-4968-894E-7CE8FDF219BB";
    public static final String POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_ONE = "sid-195AA2D7-E16C-43B6-9E17-CF7CAFAB0BD6";
    public static final String POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_TWO = "sid-A4F2D25A-306E-4A38-8E3C-68585C0E7121";
    public static final String POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_THREE = "sid-7C138BC4-383D-4987-9802-9B759D54839F";
    public static final String POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_FOUR = "sid-9F84DCC8-B391-41F3-B42F-A7E575921655";
    public static final String POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_FIVE = "sid-80EA1956-4618-4DB4-AF64-8F84D7C8BD2F";
    public static final String POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_SIX = "sid-B546E8C3-D3D7-47F4-AABF-F82FF622D647";
    public static final String POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_SEVEN = "sid-F8B0C10F-A4F4-4E62-A740-592519639404";
    public static final String POENTIAL_SUPPLIER_ENTRY_PROCESS_NODE_EIGHT = "sid-D15E034D-58D2-4A4B-B5B3-23B11D2E253B";
    public static final String APPROVED_SUPPLIER_ENTRY_PROCESS_NODE_ONE = "sid-F53B5253-08A6-43F0-9DB7-AF6C7F6F1764";
    public static final String APPROVED_SUPPLIER_ENTRY_PROCESS_NODE_TWO = "sid-D501AE9A-698F-497D-A92C-930212A79387";
    public static final String APPROVED_SUPPLIER_ENTRY_PROCESS_NODE_THREE = "sid-6575F30F-950E-47E8-A93C-6534E83463D0";
    public static final String APPROVED_SUPPLIER_ENTRY_PROCESS_NODE_FOUR = "sid-C9B11DE6-4C55-49DE-9526-927901EDB524";
    public static final String SUPPLIER_CLEAR_PROCESS_NODE_ONE = "sid-D1C2D489-F2A2-4811-AD06-6DF0D54921D3";
    public static final String SUPPLIER_CLEAR_PROCESS_NODE_TWO = "sid-64A081DF-5E45-4DFB-950B-360A767FA689";
    public static final String SUPPLIER_MODIFICATION_PROCESS_NODE_ONE = "sid-1A89C44E-4B84-4F2F-A1B7-492A58332E65";
    public static final String SUPPLIER_MODIFICATION_PROCESS_NODE_TWO = "sid-1E255582-81B4-480A-950C-1BDDEEF264E7";
    public static final String RFQ_NODE_ONE = "sid-0B2161CF-0C87-4E3D-B3C1-7EB7C03BEA3F";
    public static final String RFQ_NODE_TWO = "sid-25FC8296-EBF1-4973-A9E0-8EC136964FFF";
    public static final String RFQ_NODE_THREE = "sid-EE975668-84AA-466C-86AC-1E0B97D3DB6C";
    public static final String RFQ_NODE_FOUR = "sid-F79AA9D1-96A9-4A5F-B1DF-6A3D4083EA7C";
    public static final String RFQ_NODE_FIVE = "sid-9F26A648-6B35-4F52-AECD-151DA29F704A";
    public static final String RFQ_NODE_SIX = "sid-E9A7F3B1-73A6-467B-AB08-DAD9214F641C";
    public static final String RFP_NODE_ONE = "sid-635945CA-13FB-41B9-B4F9-02252EFCFE32";
    public static final String RFP_NODE_TWO = "sid-24D3FEAC-339A-4A43-8CE2-F97655A47DB2";
    public static final String RFP_NODE_THREE = "sid-7609BFDF-BB39-45E3-97AD-6E7A224897DC";
    public static final String RFP_NODE_FOUR = "sid-FA659477-BCAB-4C2C-96DF-99F07205303B";
    public static final String RFP_NODE_FIVE = "sid-8E6858BF-B2A8-4EC0-AB08-E98B305338EF";
    public static final String RFP_NODE_SIX = "sid-43438655-82F7-4652-9134-B96681C9A2B3";
    public static final String RFI_NODE_ONE = "sid-8E3BDC70-004A-4ACE-81F5-97EC9805E914";
    public static final String RFI_NODE_TWO = "sid-1CF8E3F0-AB61-4005-8246-5990BD8677C6";
    public static final String RFI_NODE_THREE = "sid-7069EA3D-FA73-459F-9028-54733AEE2AEC";
    public static final String RFI_NODE_FOUR = "sid-EA5A9279-CB17-475E-95C0-E210F67440AD";
    public static final String RFI_NODE_FIVE = "sid-9DF6C773-9167-4200-A37F-A95E11DE8DF3";
    public static final String RFI_NODE_SIX = "sid-344C495C-CEC2-4F53-945C-C2BC058C2918";
}
