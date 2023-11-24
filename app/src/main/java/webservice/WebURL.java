package webservice;


/**
 * Created by shakti on 10/6/2016.
 */
public class WebURL {
    public  static int CHECK_FOR_WORK_WITH_BT_OR_IN = 0;

    public  static String BT_DEVICE_NAME = "";
    public  static String BT_DEVICE_MAC_ADDRESS = "";


    public static final String BaseUrl = "BaseUrl";

    public static String sapBaseURL = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmapp_solar_pro/rms_base_url_api.htm";
    public static String rmsBaseURL ="https://shaktirms.com/";
    public static final String RetrievePumpCOde = "https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zemp_hr_portal/setting_parameter_app.htm?lv_sernr=";


    public static final String MOTOR_PERSMETER_LIST = "https://solar10.shaktisolarrms.com/RMSApp/MoterParamList?id=";
    public static final String syncOfflineData = "RMSApp/DeviceParamBLog";
    public static final String VerifyDeviceID = "RMSApp/deviceExist?deviceNo=";
    public static final String deviceOnOffAPI = "Home/DeviceONOFFAdmin";



    //Quality
//https://spquasrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zemp_hr_portal/setting_parameter_app.htm?lv_sernr=

    //Production
//https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zemp_hr_portal/setting_parameter_app.htm?lv_sernr=


}
