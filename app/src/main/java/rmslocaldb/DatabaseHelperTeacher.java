package rmslocaldb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



import java.util.ArrayList;

import activity.BeanVk.SettingModelResponse;

/**
 * Created by Sayem on 12/5/2017.
 */

public class DatabaseHelperTeacher extends SQLiteOpenHelper {


/*    private OldKLPResponse mOldKLPResponse;
    private KLPGridModelResponse mKLPGridModelResponse;
    private SAJResponse mSAJResponse;
    private KLPTotEnergyResponse mKLPTotEnergyResponse;
    private KLPHybrideResponse mKLPHybrideResponse;
    private NikolaResponse mNikolaResponse;
    private ShimaResponse mShimaResponse;
    private UspcEnrgyResponse mUspcEnrgyResponse;
    private DynamicBTNResponse mDynamicBTNResponse;
    private ViechiDataResponse mViechiDataResponse;
    private AOneSSResponse mAOneSSResponse;*/


    public static String DATABASE_NAME_RMS = "rms_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USER = "teachers";

    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "name";
    private static final String KEY_COURSE = "course";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    ////////////********************************Table name  Login*********************///////////////////////
    private static final String TABLE_USER_LOGIN = "userlogin";

    /////////////****************************Login AUTHModelData parameter*******************//////////
    private static final String LOGIN_KEY_IDD = "idd";
    private static final String LOGIN_KEY_ID = "id";
    private static final String LOGIN_USER_NAME = "username";
    private static final String LOGIN_USER_PASSWORD = "password";
    private static final String LOGIN_USER_ID = "userid";
    private static final String LOGIN_PARENT_ID = "parentid";
    private static final String LOGIN_MOBILE_NO = "phone";
    private static final String LOGIN_CLIENT_ID = "clientid";
    private static final String IS_LOGIN = "isLogin";
    private static final String LOGIN_STATUS = "status";
    private static final String LOGIN_ACTIVE = "active";

    private static final String LOGIN_TEST1 = "LoginTest1";
    private static final String LOGIN_TEST2 = "LoginTest2";
    private static final String LOGIN_TEST3 = "LoginTest3";
    private static final String LOGIN_TEST4 = "LoginTest4";


    ///*********************************end ****************//////////

    private static final String CREATE_TABLE_TEACHERS = "CREATE TABLE "
            + TABLE_USER_LOGIN + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            LOGIN_USER_ID + " TEXT NOT NULL, " +
            LOGIN_PARENT_ID + " TEXT NOT NULL, " +
            LOGIN_USER_NAME + " TEXT NOT NULL, " +
            LOGIN_USER_PASSWORD + " TEXT NOT NULL, " +
            LOGIN_MOBILE_NO + " VARCHAR, " +
            LOGIN_CLIENT_ID + " TEXT NOT NULL, " +
            IS_LOGIN + " TEXT NOT NULL, " +
            LOGIN_STATUS + " TEXT NOT NULL, " +
            LOGIN_ACTIVE + " TEXT NOT NULL, " +
            LOGIN_TEST1 + " VARCHAR, " +
            LOGIN_TEST2 + " VARCHAR, " +
            LOGIN_TEST3 + " VARCHAR " +
            "); ";


    ////////////////////this is for deviec list

    // private ArrayList<Customer_GPS_Search> Customer_GPS_SearchList;
  //  private List<UspcModelResponse> mUspcModelResponseList;

    /* "id": 4,
             "image": "http://localhost:8086/Home/images/USPCIMG/http://localhost:8086/Home/images/USPCIMG/Motor-Pump Set",
             "title": "Motor Pump Set",
             "bgColor": "white",
             "status": true
*/


    private ArrayList<SettingModelResponse> mSettingModelResponseList;
  //  private ArrayList<ProductStatusResponse> mProductStatusResponseList;
    // private List<ProductStatusResponse> mProductStatusResponse;

    private SettingModelResponse mSettingModelResponse;
   // private ProductStatusResponse mProductStatusResponse;

    /////////////////////////////device parameter
    private static final String DEVICE_PARA_ADDRESS = "Address";
    private static final String DEVICE_PARA_DIVISIBLE = "Divisible";
    private static final String DEVICE_PARA_DEVICENO = "MDeviceNo";
    private static final String DEVICE_PARA_MPID = "MPId";
    private static final String DEVICE_PARA_MPINDEX = "MPIndex";
    private static final String DEVICE_PARA_MPNAME = "MPName";
    private static final String DEVICE_PARA_STATUS = "Status";
    private static final String DEVICE_PARA_UNIT = "Unit";
    private static final String DEVICE_PARA_PMIN = "PMin";
    private static final String DEVICE_PARA_PMAX = "PMax";
    private static final String DEVICE_PARA_MODADDRESS = "MODAddress";
    private static final String DEVICE_PARA_DEVICE_TYPE = "DeviceType";///offset
    private static final String DEVICE_PARA_DEVICE_TEST1 = "Offset";
    private static final String DEVICE_PARA_DEVICE_TEST2 = "ParaTest2";
    private static final String DEVICE_PARA_DEVICE_TEST3 = "ParaTest3";
    private static final String DEVICE_PARA_DEVICE_TEST4 = "ParaTest4";

    private static final String DEVICE_PARAMETER_TABLE_NAME = "deviceparameterlist";////////////////table name

    private static final String CREATE_TABLE_DEVICE_PARAMETER = "CREATE TABLE "
            + DEVICE_PARAMETER_TABLE_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_PARA_ADDRESS + " TEXT NOT NULL, " +
            DEVICE_PARA_DIVISIBLE + " TEXT NOT NULL, " +
            DEVICE_PARA_DEVICENO + " TEXT NOT NULL, " +
            DEVICE_PARA_MPID + " TEXT NOT NULL, " +
            DEVICE_PARA_MPINDEX + " VARCHAR, " +
            DEVICE_PARA_MPNAME + " VARCHAR, " +
            DEVICE_PARA_STATUS + " VARCHAR, " +
            DEVICE_PARA_UNIT + " VARCHAR, " +
            DEVICE_PARA_PMIN + " VARCHAR, " +
            DEVICE_PARA_PMAX + " VARCHAR, " +
            DEVICE_PARA_MODADDRESS + " VARCHAR, " +
            DEVICE_PARA_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_PARA_DEVICE_TEST1 + " VARCHAR, " +
            DEVICE_PARA_DEVICE_TEST2 + " VARCHAR, " +
            DEVICE_PARA_DEVICE_TEST3 + " VARCHAR, " +
            DEVICE_PARA_DEVICE_TEST4 + " VARCHAR " +
            "); ";


    /////////////////////////////device Status indicator
    private static final String DEVICE_STATUS_MODE = "SMode";
    private static final String DEVICE_STATUS_NAME = "SName";
    private static final String DEVICE_STATUS_COLOR = "SColor";
    private static final String DEVICE_STATUS_DEVICE_TYPE = "DeviceType";
    private static final String DEVICE_STATUS_TEST1 = "TestOne";
    private static final String DEVICE_STATUS_TEST2 = "TestTwo";
    private static final String DEVICE_STATUS_TEST3 = "TestThree";
    private static final String DEVICE_STATUS_TABLE_NAME = "devicestatustable";////////////////table name



    ///////////////////////////////////Grid Tie//////////////

    private static final String DEVICE_KLPGRID_TotalFault = "TotalFault";
    private static final String DEVICE_KLPGRID_Fault = "Fault";
    private static final String DEVICE_KLPGRID_TOTGRHName = "TOTGRHName";
    private static final String DEVICE_KLPGRID_TOTGRHValue = "TOTGRHValue";
    private static final String DEVICE_KLPGRID_TOTGRHUnit = "TOTGRHUnit";
    private static final String DEVICE_KLPGRID_TDGRHName = "TDGRHName";
    private static final String DEVICE_KLPGRID_TDGRHValue = "TDGRHValue";
    private static final String DEVICE_KLPGRID_TDGRHUnit = "TDGRHUnit";
    private static final String DEVICE_KLPGRID_TOTGEnergyName = "TOTGEnergyName";

    private static final String DEVICE_KLPGRID_TOTMGEnergyName = "TOTMGEnergyName";
    private static final String DEVICE_KLPGRID_TOTMGEnergyValue = "TOTMGEnergyValue";
    private static final String DEVICE_KLPGRID_TOTMGEnergyUnit = "TOTMGEnergyUnit";

    private static final String DEVICE_KLPGRID_TOTGEnergyValue = "TOTGEnergyValue";
    private static final String DEVICE_KLPGRID_TOTGEnergyUnit = "TOTGEnergyUnit";
    private static final String DEVICE_KLPGRID_TDGEnergyName = "TDGEnergyName";
    private static final String DEVICE_KLPGRID_TDGEnergyValue = "TDGEnergyValue";
    private static final String DEVICE_KLPGRID_TDGEnergyUnit = "TDGEnergyUnit";
    private static final String DEVICE_KLPGRID_TEST1 = "klpgridtest1";
    private static final String DEVICE_KLPGRID_TEST2 = "klpgridtest2";
    private static final String DEVICE_KLPGRID_TEST3 = "klpgridtest3";
    private static final String DEVICE_KLPGRID_DEVICE_TYPE = "DeviceType";


    private static final String DEVICE_KLPGRID_TABLENAME = "deviceklpgridtableble";////////////////table name

    private static final String CREATE_TABLE_KLPGRID = "CREATE TABLE "
            + DEVICE_KLPGRID_TABLENAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_KLPGRID_TotalFault + " VARCHAR, " +
            DEVICE_KLPGRID_Fault + " VARCHAR, " +
            DEVICE_KLPGRID_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_KLPGRID_TOTGRHName + " VARCHAR, " +
            DEVICE_KLPGRID_TOTGRHValue + " VARCHAR, " +
            DEVICE_KLPGRID_TOTGRHUnit + " VARCHAR, " +
            DEVICE_KLPGRID_TDGRHName + " VARCHAR, " +
            DEVICE_KLPGRID_TDGRHValue + " VARCHAR, " +
            DEVICE_KLPGRID_TDGRHUnit + " VARCHAR, " +
            DEVICE_KLPGRID_TOTGEnergyName + " VARCHAR, " +

            DEVICE_KLPGRID_TOTMGEnergyName + " VARCHAR, " +
            DEVICE_KLPGRID_TOTMGEnergyValue + " VARCHAR, " +
            DEVICE_KLPGRID_TOTMGEnergyUnit + " VARCHAR, " +

            DEVICE_KLPGRID_TOTGEnergyValue + " VARCHAR, " +
            DEVICE_KLPGRID_TOTGEnergyUnit + " VARCHAR, " +
            DEVICE_KLPGRID_TDGEnergyName + " VARCHAR, " +
            DEVICE_KLPGRID_TDGEnergyValue + " VARCHAR, " +
            DEVICE_KLPGRID_TDGEnergyUnit + " VARCHAR, " +
            DEVICE_KLPGRID_TEST1 + " VARCHAR, " +
            DEVICE_KLPGRID_TEST2 + " VARCHAR, " +
            DEVICE_KLPGRID_TEST3 + " VARCHAR " +
            "); ";







    /////////////////////////////device Status old klp indicator

    private static final String DEVICE_TotalFaultOldKLP = "TotalFault";
    private static final String DEVICE_FaultOldKLP = "Fault";
    private static final String DEVICE_TOTGRHNameOldKLP = "TOTGRHName";
    private static final String DEVICE_TOTGRHValueOldKLP = "TOTGRHValue";
    private static final String DEVICE_TOTGRHUnitOldKLP = "TOTGRHUnit";
    private static final String DEVICE_TDGRHNameOldKLP = "TDGRHName";
    private static final String DEVICE_TDGRHValueOldKLP = "TDGRHValue";
    private static final String DEVICE_TDGRHUnitOldKLP = "TDGRHUnit";
    private static final String DEVICE_TOTGEnergyNameOldKLP = "TOTGEnergyName";
    private static final String DEVICE_TOTGEnergyValueOldKLP = "TOTGEnergyValue";
    private static final String DEVICE_TOTGEnergyUnitOldKLP = "TOTGEnergyUnit";
    private static final String DEVICE_TDGEnergyNameOldKLP = "TDGEnergyName";
    private static final String DEVICE_TDGEnergyValueOldKLP = "TDGEnergyValue";
    private static final String DEVICE_TDGEnergyUnitOldKLP = "TDGEnergyUnit";
    private static final String DEVICE_TOTMRHNameOldKLP = "TOTMRHName";
    private static final String DEVICE_TOTMRHValueOldKLP = "TOTMRHValue";
    private static final String DEVICE_TOTMRHUnitOldKLP = "TOTMRHUnit";
    private static final String DEVICE_TDMRHNameOldKLP = "TDMRHName";
    private static final String DEVICE_TDMRHValueOldKLP = "TDMRHValue";
    private static final String DEVICE_TDMRHUnitOldKLP = "TDMRHUnit";
    private static final String DEVICE_TOTMFlowNameOldKLP = "TOTMFlowName";
    private static final String DEVICE_TOTMFlowValueOldKLP = "TOTMFlowValue";
    private static final String DEVICE_TOTMFlowUnitOldKLP = "TOTMFlowUnit";
    private static final String DEVICE_TDMFlowNameOldKLP = "TDMFlowName";
    private static final String DEVICE_TDMFlowValueOldKLP = "TDMFlowValue";
    private static final String DEVICE_TDMFlowUnitOldKLP = "TDMFlowUnit";
    private static final String DEVICE_OldKLP_DEVICE_TYPE = "DeviceType";

    private static final String DEVICE_OldKLP_TEST1 = "TestOne";
    private static final String DEVICE_OldKLP_TEST2 = "TestTwo";
    private static final String DEVICE_OldKLP_TEST3 = "TestThree";


    private static final String DEVICE_OldKLP_NAME = "deviceoldklptableble";////////////////table name

    private static final String CREATE_TABLE_OldKLP = "CREATE TABLE "
            + DEVICE_OldKLP_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_TotalFaultOldKLP + " VARCHAR, " +
            DEVICE_FaultOldKLP + " VARCHAR, " +
            DEVICE_OldKLP_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_TOTGRHNameOldKLP + " VARCHAR, " +
            DEVICE_TOTGRHValueOldKLP + " VARCHAR, " +
            DEVICE_TOTGRHUnitOldKLP + " VARCHAR, " +
            DEVICE_TDGRHNameOldKLP + " VARCHAR, " +
            DEVICE_TDGRHValueOldKLP + " VARCHAR, " +
            DEVICE_TDGRHUnitOldKLP + " VARCHAR, " +
            DEVICE_TOTGEnergyNameOldKLP + " VARCHAR, " +
            DEVICE_TOTGEnergyValueOldKLP + " VARCHAR, " +
            DEVICE_TOTGEnergyUnitOldKLP + " VARCHAR, " +
            DEVICE_TDGEnergyNameOldKLP + " VARCHAR, " +
            DEVICE_TDGEnergyValueOldKLP + " VARCHAR, " +
            DEVICE_TDGEnergyUnitOldKLP + " VARCHAR, " +
            DEVICE_TOTMRHNameOldKLP + " VARCHAR, " +
            DEVICE_TOTMRHValueOldKLP + " VARCHAR, " +
            DEVICE_TOTMRHUnitOldKLP + " VARCHAR, " +
            DEVICE_TDMRHNameOldKLP + " VARCHAR, " +
            DEVICE_TDMRHValueOldKLP + " VARCHAR, " +
            DEVICE_TDMRHUnitOldKLP + " VARCHAR, " +
            DEVICE_TOTMFlowNameOldKLP + " VARCHAR, " +
            DEVICE_TOTMFlowValueOldKLP + " VARCHAR, " +
            DEVICE_TOTMFlowUnitOldKLP + " VARCHAR, " +
            DEVICE_TDMFlowNameOldKLP + " VARCHAR, " +
            DEVICE_TDMFlowValueOldKLP + " VARCHAR, " +
            DEVICE_TDMFlowUnitOldKLP + " VARCHAR, " +
            DEVICE_OldKLP_TEST1 + " VARCHAR, " +
            DEVICE_OldKLP_TEST2 + " VARCHAR, " +
            DEVICE_OldKLP_TEST3 + " VARCHAR " +
            "); ";



    ////////////////////////////SAJ LOCL DB CODE

    /////////////////////////////device Status klp indicator
    private static final String DEVICE_TotalFaultSAJ = "TotalFault";
    private static final String DEVICE_FaultSAJ = "Fault";
    private static final String DEVICE_TOTGRHNameSAJ = "TOTGRHName";
    private static final String DEVICE_TOTGRHValueSAJ = "TOTGRHValue";
    private static final String DEVICE_TOTGRHUnitSAJ = "TOTGRHUnit";
    private static final String DEVICE_TDGRHNameSAJ = "TDGRHName";
    private static final String DEVICE_TDGRHValueSAJ = "TDGRHValue";
    private static final String DEVICE_TDGRHUnitSAJ = "TDGRHUnit";
    private static final String DEVICE_TOTGEnergyNameSAJ = "TOTGEnergyName";
    private static final String DEVICE_TOTGEnergyValueSAJ = "TOTGEnergyValue";
    private static final String DEVICE_TOTGEnergyUnitSAJ = "TOTGEnergyUnit";
    private static final String DEVICE_TDGEnergyNameSAJ = "TDGEnergyName";
    private static final String DEVICE_TDGEnergyValueSAJ = "TDGEnergyValue";
    private static final String DEVICE_TDGEnergyUnitSAJ = "TDGEnergyUnit";

    private static final String DEVICE_SAJ_DEVICE_TYPE = "DeviceType";

    private static final String DEVICE_SAJ_TEST1 = "TestOne";
    private static final String DEVICE_SAJ_TEST2 = "TestTwo";
    private static final String DEVICE_SAJ_TEST3 = "TestThree";

    private static final String DEVICE_SAJ_NAME = "devicesajble";////////////////table name


    private static final String CREATE_TABLE_SAJ = "CREATE TABLE "
            + DEVICE_SAJ_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_TotalFaultSAJ + " VARCHAR, " +
            DEVICE_FaultSAJ + " VARCHAR, " +
            DEVICE_SAJ_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_TOTGRHNameSAJ + " VARCHAR, " +
            DEVICE_TOTGRHValueSAJ + " VARCHAR, " +
            DEVICE_TOTGRHUnitSAJ + " VARCHAR, " +
            DEVICE_TDGRHNameSAJ + " VARCHAR, " +
            DEVICE_TDGRHValueSAJ + " VARCHAR, " +
            DEVICE_TDGRHUnitSAJ + " VARCHAR, " +
            DEVICE_TOTGEnergyNameSAJ + " VARCHAR, " +
            DEVICE_TOTGEnergyValueSAJ + " VARCHAR, " +
            DEVICE_TOTGEnergyUnitSAJ + " VARCHAR, " +
            DEVICE_TDGEnergyNameSAJ + " VARCHAR, " +
            DEVICE_TDGEnergyValueSAJ + " VARCHAR, " +
            DEVICE_TDGEnergyUnitSAJ + " VARCHAR, " +
            DEVICE_SAJ_TEST1 + " VARCHAR, " +
            DEVICE_SAJ_TEST2 + " VARCHAR, " +
            DEVICE_SAJ_TEST3 + " VARCHAR " +
            "); ";



    /////////////////////////////device Status klp indicator
    private static final String DEVICE_TotalFaultKLP = "TotalFault";
    private static final String DEVICE_FaultKLP = "Fault";
    private static final String DEVICE_TOTGRHNameKLP = "TOTGRHName";
    private static final String DEVICE_TOTGRHValueKLP = "TOTGRHValue";
    private static final String DEVICE_TOTGRHUnitKLP = "TOTGRHUnit";
    private static final String DEVICE_TDGRHNameKLP = "TDGRHName";
    private static final String DEVICE_TDGRHValueKLP = "TDGRHValue";
    private static final String DEVICE_TDGRHUnitKLP = "TDGRHUnit";
    private static final String DEVICE_TOTGEnergyNameKLP = "TOTGEnergyName";
    private static final String DEVICE_TOTGEnergyValueKLP = "TOTGEnergyValue";
    private static final String DEVICE_TOTGEnergyUnitKLP = "TOTGEnergyUnit";
    private static final String DEVICE_TDGEnergyNameKLP = "TDGEnergyName";
    private static final String DEVICE_TDGEnergyValueKLP = "TDGEnergyValue";
    private static final String DEVICE_TDGEnergyUnitKLP = "TDGEnergyUnit";
    private static final String DEVICE_TOTMRHNameKLP = "TOTMRHName";
    private static final String DEVICE_TOTMRHValueKLP = "TOTMRHValue";
    private static final String DEVICE_TOTMRHUnitKLP = "TOTMRHUnit";
    private static final String DEVICE_TDMRHNameKLP = "TDMRHName";
    private static final String DEVICE_TDMRHValueKLP = "TDMRHValue";
    private static final String DEVICE_TDMRHUnitKLP = "TDMRHUnit";
    private static final String DEVICE_TOTMEnergyNameKLP = "TOTMEnergyName";
    private static final String DEVICE_TOTMEnergyValueKLP = "TOTMEnergyValue";
    private static final String DEVICE_TOTMEnergyUnitKLP = "TOTMEnergyUnit";
    private static final String DEVICE_TDMEnergyNameKLP = "TDMEnergyName";
    private static final String DEVICE_TDMEnergyValueKLP = "TDMEnergyValue";
    private static final String DEVICE_TDMEnergyUnitKLP = "TDMEnergyUnit";
    private static final String DEVICE_TOTMFlowNameKLP = "TOTMFlowName";
    private static final String DEVICE_TOTMFlowValueKLP = "TOTMFlowValue";
    private static final String DEVICE_TOTMFlowUnitKLP = "TOTMFlowUnit";
    private static final String DEVICE_TDMFlowNameKLP = "TDMFlowName";
    private static final String DEVICE_TDMFlowValueKLP = "TDMFlowValue";
    private static final String DEVICE_TDMFlowUnitKLP = "TDMFlowUnit";
    private static final String DEVICE_KLP_DEVICE_TYPE = "DeviceType";


    private static final String DEVICE_KLP_TEST1 = "TestOne";
    private static final String DEVICE_KLP_TEST2 = "TestTwo";
    private static final String DEVICE_KLP_TEST3 = "TestThree";

    private static final String DEVICE_KLP_NAME = "deviceklpble";////////////////table name


    private static final String CREATE_TABLE_KLP = "CREATE TABLE "
            + DEVICE_KLP_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_TotalFaultKLP + " VARCHAR, " +
            DEVICE_FaultKLP + " VARCHAR, " +
            DEVICE_KLP_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_TOTGRHNameKLP + " VARCHAR, " +
            DEVICE_TOTGRHValueKLP + " VARCHAR, " +
            DEVICE_TOTGRHUnitKLP + " VARCHAR, " +
            DEVICE_TDGRHNameKLP + " VARCHAR, " +
            DEVICE_TDGRHValueKLP + " VARCHAR, " +
            DEVICE_TDGRHUnitKLP + " VARCHAR, " +
            DEVICE_TOTGEnergyNameKLP + " VARCHAR, " +
            DEVICE_TOTGEnergyValueKLP + " VARCHAR, " +
            DEVICE_TOTGEnergyUnitKLP + " VARCHAR, " +
            DEVICE_TDGEnergyNameKLP + " VARCHAR, " +
            DEVICE_TDGEnergyValueKLP + " VARCHAR, " +
            DEVICE_TDGEnergyUnitKLP + " VARCHAR, " +
            DEVICE_TOTMRHNameKLP + " VARCHAR, " +
            DEVICE_TOTMRHValueKLP + " VARCHAR, " +
            DEVICE_TOTMRHUnitKLP + " VARCHAR, " +
            DEVICE_TDMRHNameKLP + " VARCHAR, " +
            DEVICE_TDMRHValueKLP + " VARCHAR, " +
            DEVICE_TDMRHUnitKLP + " VARCHAR, " +
            DEVICE_TOTMEnergyNameKLP + " VARCHAR, " +
            DEVICE_TOTMEnergyValueKLP + " VARCHAR, " +
            DEVICE_TOTMEnergyUnitKLP + " VARCHAR, " +
            DEVICE_TDMEnergyNameKLP + " VARCHAR, " +
            DEVICE_TDMEnergyValueKLP + " VARCHAR, " +
            DEVICE_TDMEnergyUnitKLP + " VARCHAR, " +
            DEVICE_TOTMFlowNameKLP + " VARCHAR, " +
            DEVICE_TOTMFlowValueKLP + " VARCHAR, " +
            DEVICE_TOTMFlowUnitKLP + " VARCHAR, " +
            DEVICE_TDMFlowNameKLP + " VARCHAR, " +
            DEVICE_TDMFlowValueKLP + " VARCHAR, " +
            DEVICE_TDMFlowUnitKLP + " VARCHAR, " +
            DEVICE_KLP_TEST1 + " VARCHAR, " +
            DEVICE_KLP_TEST2 + " VARCHAR, " +
            DEVICE_KLP_TEST3 + " VARCHAR " +
            "); ";

    ////////////////////////////////insert Devic


    /////////////////////////////device Status Nikola
    private static final String DEVICE_TotalFaultNikola = "TotalFault";
    private static final String DEVICE_FaultNikola = "Fault";
    private static final String DEVICE_TOTGRHNameNikola = "TOTGRHName";
    private static final String DEVICE_TOTGRHValueNikola = "TOTGRHValue";
    private static final String DEVICE_TOTGRHUnitNikola = "TOTGRHUnit";
    private static final String DEVICE_TDGRHNameNikola = "TDGRHName";
    private static final String DEVICE_TDGRHValueNikola = "TDGRHValue";
    private static final String DEVICE_TDGRHUnitNikola = "TDGRHUnit";
    private static final String DEVICE_TOTGEnergyNameNikola = "TOTGEnergyName";
    private static final String DEVICE_TOTGEnergyValueNikola = "TOTGEnergyValue";
    private static final String DEVICE_TOTGEnergyUnitNikola = "TOTGEnergyUnit";
    private static final String DEVICE_TDGEnergyNameNikola = "TDGEnergyName";
    private static final String DEVICE_TDGEnergyValueNikola = "TDGEnergyValue";
    private static final String DEVICE_TDGEnergyUnitNikola = "TDGEnergyUnit";

    private static final String DEVICE_TOTLEnergyNameNikola = "TOTLEnergyName";
    private static final String DEVICE_TOTLEnergyValueNikola = "TOTLEnergyValue";
    private static final String DEVICE_TOTLEnergyUnitNikola = "TOTLEnergyUnit";

    private static final String DEVICE_TDLEnergyNameNikola = "TDLEnergyName";
    private static final String DEVICE_TDLEnergyValueNikola = "TDLEnergyValue";
    private static final String DEVICE_TDLEnergyUnitNikola = "TDLEnergyUnit";

    private static final String DEVICE_TOTBRHNameNikola = "TOTBRHName";
    private static final String DEVICE_TOTBRHValueNikola = "TOTBRHValue";
    private static final String DEVICE_TOTBRHUnitNikola = "TOTBRHUnit";

    private static final String DEVICE_TDBRHNameNikola = "TDBRHName";
    private static final String DEVICE_TDBRHValueNikola = "TDBRHValue";
    private static final String DEVICE_TDBRHUnitNikola = "TDBRHUnit";

    private static final String DEVICE_Nikola_DEVICE_TYPE = "DeviceType";


    private static final String DEVICE_Nikola_TEST1 = "TestOne";
    private static final String DEVICE_Nikola_TEST2 = "TestTwo";
    private static final String DEVICE_Nikola_TEST3 = "TestThree";

    private static final String DEVICE_Nikola_NAME = "devicenikolable";////////////////table name

    private static final String CREATE_TABLE_Nikola = "CREATE TABLE "
            + DEVICE_Nikola_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_TotalFaultNikola + " VARCHAR, " +
            DEVICE_FaultNikola + " VARCHAR, " +
            DEVICE_Nikola_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_TOTGRHNameNikola + " VARCHAR, " +
            DEVICE_TOTGRHValueNikola + " VARCHAR, " +
            DEVICE_TOTGRHUnitNikola + " VARCHAR, " +
            DEVICE_TDGRHNameNikola + " VARCHAR, " +
            DEVICE_TDGRHValueNikola + " VARCHAR, " +
            DEVICE_TDGRHUnitNikola + " VARCHAR, " +
            DEVICE_TOTGEnergyNameNikola + " VARCHAR, " +
            DEVICE_TOTGEnergyValueNikola + " VARCHAR, " +
            DEVICE_TOTGEnergyUnitNikola + " VARCHAR, " +
            DEVICE_TDGEnergyNameNikola + " VARCHAR, " +
            DEVICE_TDGEnergyValueNikola + " VARCHAR, " +
            DEVICE_TDGEnergyUnitNikola + " VARCHAR, " +
            DEVICE_TOTLEnergyNameNikola + " VARCHAR, " +//////////////////change karna hai
            DEVICE_TOTLEnergyValueNikola + " VARCHAR, " +
            DEVICE_TOTLEnergyUnitNikola + " VARCHAR, " +
            DEVICE_TDLEnergyNameNikola + " VARCHAR, " +
            DEVICE_TDLEnergyValueNikola + " VARCHAR, " +
            DEVICE_TDLEnergyUnitNikola + " VARCHAR, " +
            DEVICE_TOTBRHNameNikola + " VARCHAR, " +
            DEVICE_TOTBRHValueNikola + " VARCHAR, " +
            DEVICE_TOTBRHUnitNikola + " VARCHAR, " +
            DEVICE_TDBRHNameNikola + " VARCHAR, " +
            DEVICE_TDBRHValueNikola + " VARCHAR, " +
            DEVICE_TDBRHUnitNikola + " VARCHAR, " +
            DEVICE_Nikola_TEST1 + " VARCHAR, " +
            DEVICE_Nikola_TEST2 + " VARCHAR, " +
            DEVICE_Nikola_TEST3 + " VARCHAR " +
            "); ";


    ////////////////////////////////insert Device status data



    ///////////////////////////////shima device local data

    private static final String DEVICE_TotalFaultSHIMA = "TotalFault";
    private static final String DEVICE_FaultSHIMA = "Fault";
    private static final String DEVICE_TDMFlowNameSHIMA = "TDMFlowName";
    private static final String DEVICE_TDMFlowUnitSHIMA = "TDMFlowUnit";
static final String DEVICE_TDMFlowValueSHIMA = "TDMFlowValue";

    private static final String DEVICE_TDMRHrNameSHIMA = "TDMRHrName";
    private static final String DEVICE_TDMRHrUnitSHIMA = "TDMRHrUnit";
    private static final String DEVICE_TDMRHrValueSHIMA = "TDMRHrValue";

    private static final String DEVICE_TDPVEnergyNameSHIMA = "TDPVEnergyName";
    private static final String DEVICE_TDPVEnergyUnitSHIMA = "TDPVEnergyUnit";
    private static final String DEVICE_TDPVEnergyValueSHIMA = "TDPVEnergyValue";

    private static final String DEVICE_TOTMFlowNameSHIMA = "TOTMFlowName";
    private static final String DEVICE_TOTMFlowUnitSHIMA = "TOTMFlowUnit";
    private static final String DEVICE_TOTMFlowValueSHIMA = "TOTMFlowValue";


    private static final String DEVICE_TOTMRHrNameSHIMA = "TOTMRHrName";
    private static final String DEVICE_TOTMRHrUnitSHIMA = "TOTMRHrUnit";
    private static final String DEVICE_TOTMRHrValueSHIMA = "TOTMRHrValue";

    private static final String DEVICE_TOTPVEnergyNameSHIMA = "TOTPVEnergyName";
    private static final String DEVICE_TOTPVEnergyUnitSHIMA = "TOTPVEnergyUnit";
    private static final String DEVICE_TOTPVEnergyValueSHIMA = "TOTPVEnergyValue";

    private static final String DEVICE_SHIMA_DEVICE_TYPE = "DeviceType";

    private static final String DEVICE_SHIMA_TEST1 = "TestOne";
    private static final String DEVICE_SHIMA_TEST2 = "TestTwo";
    private static final String DEVICE_SHIMA_TEST3 = "TestThree";

    private static final String DEVICE_SHIMA_NAME = "deviceshima";////////////////table name

    private static final String CREATE_TABLE_SHIMA = "CREATE TABLE "
            + DEVICE_SHIMA_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_TotalFaultSHIMA + " VARCHAR, " +
            DEVICE_FaultSHIMA + " VARCHAR, " +
            DEVICE_SHIMA_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_TDMFlowNameSHIMA + " VARCHAR, " +
            DEVICE_TDMFlowUnitSHIMA + " VARCHAR, " +
            DEVICE_TDMFlowValueSHIMA + " VARCHAR, " +
            DEVICE_TDMRHrNameSHIMA + " VARCHAR, " +
            DEVICE_TDMRHrUnitSHIMA + " VARCHAR, " +
            DEVICE_TDMRHrValueSHIMA + " VARCHAR, " +
            DEVICE_TDPVEnergyNameSHIMA + " VARCHAR, " +
            DEVICE_TDPVEnergyUnitSHIMA + " VARCHAR, " +
            DEVICE_TDPVEnergyValueSHIMA + " VARCHAR, " +
            DEVICE_TOTMFlowNameSHIMA + " VARCHAR, " +
            DEVICE_TOTMFlowUnitSHIMA + " VARCHAR, " +
            DEVICE_TOTMFlowValueSHIMA + " VARCHAR, " +
            DEVICE_TOTMRHrNameSHIMA + " VARCHAR, " +
            DEVICE_TOTMRHrUnitSHIMA + " VARCHAR, " +
            DEVICE_TOTMRHrValueSHIMA + " VARCHAR, " +
            DEVICE_TOTPVEnergyNameSHIMA + " VARCHAR, " +
            DEVICE_TOTPVEnergyUnitSHIMA + " VARCHAR, " +
            DEVICE_TOTPVEnergyValueSHIMA + " VARCHAR, " +
            DEVICE_SHIMA_TEST1 + " VARCHAR, " +
            DEVICE_SHIMA_TEST2 + " VARCHAR, " +
            DEVICE_SHIMA_TEST3 + " VARCHAR " +
            "); ";




    /////////////////////Vikas KUSPC //////////////////////////////////////////////

    private static final String DEVICE_TotalFaultUSPC = "TotalFault";
    private static final String DEVICE_FaultUSPC = "Fault";

    private static final String DEVICE_TDUSPCHrUnitUSPC = "TDUSPCHrUnit";
    private static final String DEVICE_TDPEnergyNameUSPC = "TDPEnergyName";

    private static final String DEVICE_TDPEnergyUnitUSPC = "TDPEnergyUnit";
    private static final String DEVICE_TDPEnergyValueUSPC = "TDPEnergyValue";
    private static final String DEVICE_TDPRHRNameUSPC = "TDPRHRName";

    private static final String DEVICE_TDPRHRUnitUSPC = "TDPRHRUnit";
    private static final String DEVICE_TDPRHRValueUSPC = "TDPRHRValue";

    private static final String DEVICE_TDUSPCEnergyNameUSPC = "TDUSPCEnergyName";
    private static final String DEVICE_TDUSPCEnergyUnitUSPC = "TDUSPCEnergyUnit";

    private static final String DEVICE_TDUSPCEnergyValueUSPC = "TDUSPCEnergyValue";
    private static final String DEVICE_TDUSPCHrNameUSPC = "TDUSPCHrName";
    private static final String DEVICE_TDUSPCHrValueUSPC = "TDUSPCHrValue";

    private static final String DEVICE_TOTFlowNameUSPC = "TOTFlowName";
    private static final String DEVICE_TOTFlowUnitUSPC = "TOTFlowUnit";
    private static final String DEVICE_TOTFlowValueUSPC = "TOTFlowValue";

    private static final String DEVICE_TDFlowNameUSPC = "TDFlowName";
    private static final String DEVICE_TDFlowValueUSPC = "TDFlowValue";
    private static final String DEVICE_TDFlowUnitUSPC = "TDFlowUnit";

    private static final String DEVICE_TOTPEnergyNameUSPC = "TOTPEnergyName";
    private static final String DEVICE_TOTPEnergyUnitUSPC = "TOTPEnergyUnit";
    private static final String DEVICE_TOTPEnergyValueUSPC = "TOTPEnergyValue";

    private static final String DEVICE_TOTPRHRNameUSPC = "TOTPRHRName";
    private static final String DEVICE_TOTPRHRUnitUSPC = "TOTPRHRUnit";
    private static final String DEVICE_TOTPRHRValueUSPC = "TOTPRHRValue";

    private static final String DEVICE_TOTUSPCEnergyNameUSPC = "TOTUSPCEnergyName";
    private static final String DEVICE_TOTUSPCEnergyUnitUSPC = "TOTUSPCEnergyUnit";
    private static final String DEVICE_TOTUSPCEnergyValueUSPC = "TOTUSPCEnergyValue";

    private static final String DEVICE_TOTUSPCHrNameUSPC = "TOTUSPCHrName";
    private static final String DEVICE_TOTUSPCHrUnitUSPC = "TOTUSPCHrUnit";
    private static final String DEVICE_TOTUSPCHrValueUSPC = "TOTUSPCHrValue";

    private static final String DEVICE_USPC_APP_IDUSPC = "USPC_APP_ID";
    private static final String USPC_APP_IDImageUSPC = "USPC_APP_IDImage";
    private static final String USPC_APP_IDValueUSPC = "USPC_APP_IDValue";

    private static final String DEVICE_USPC_DEVICE_TYPE = "DeviceType";
    private static final String DEVICE_USPC_TEST1 = "TestOne";
    private static final String DEVICE_USPC_TEST2 = "TestTwo";
    private static final String DEVICE_USPC_TEST3 = "TestThree";

    private static final String DEVICE_USPC_NAME = "deviceUSPC";////////////////table name

    private static final String CREATE_TABLE_USPC = "CREATE TABLE "
            + DEVICE_USPC_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_TotalFaultUSPC + " VARCHAR, " +
            DEVICE_FaultUSPC + " VARCHAR, " +
            DEVICE_USPC_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_TDUSPCHrUnitUSPC + " VARCHAR, " +
            DEVICE_TDPEnergyNameUSPC + " VARCHAR, " +
            DEVICE_TDPEnergyUnitUSPC + " VARCHAR, " +
            DEVICE_TDPEnergyValueUSPC + " VARCHAR, " +
            DEVICE_TDPRHRNameUSPC + " VARCHAR, " +
            DEVICE_TDPRHRUnitUSPC + " VARCHAR, " +
            DEVICE_TDPRHRValueUSPC + " VARCHAR, " +
            DEVICE_TDUSPCEnergyNameUSPC + " VARCHAR, " +
            DEVICE_TDUSPCEnergyUnitUSPC + " VARCHAR, " +
            DEVICE_TDUSPCEnergyValueUSPC + " VARCHAR, " +
            DEVICE_TDUSPCHrNameUSPC + " VARCHAR, " +
            DEVICE_TDUSPCHrValueUSPC + " VARCHAR, " +
            DEVICE_TOTFlowNameUSPC + " VARCHAR, " +
            DEVICE_TOTFlowUnitUSPC + " VARCHAR, " +
            DEVICE_TOTFlowValueUSPC + " VARCHAR, " +
            DEVICE_TDFlowNameUSPC + " VARCHAR, " +
            DEVICE_TDFlowValueUSPC + " VARCHAR, " +
            DEVICE_TDFlowUnitUSPC + " VARCHAR, " +
            DEVICE_TOTPEnergyNameUSPC + " VARCHAR, " +
            DEVICE_TOTPEnergyUnitUSPC + " VARCHAR, " +
            DEVICE_TOTPEnergyValueUSPC + " VARCHAR, " +
            DEVICE_TOTPRHRNameUSPC + " VARCHAR, " +
            DEVICE_TOTPRHRUnitUSPC + " VARCHAR, " +
            DEVICE_TOTPRHRValueUSPC + " VARCHAR, " +
            DEVICE_TOTUSPCEnergyNameUSPC + " VARCHAR, " +
            DEVICE_TOTUSPCEnergyUnitUSPC + " VARCHAR, " +
            DEVICE_TOTUSPCEnergyValueUSPC + " VARCHAR, " +
            DEVICE_TOTUSPCHrNameUSPC + " VARCHAR, " +
            DEVICE_TOTUSPCHrUnitUSPC + " VARCHAR, " +
            DEVICE_TOTUSPCHrValueUSPC + " VARCHAR, " +
            DEVICE_USPC_APP_IDUSPC + " VARCHAR, " +
            USPC_APP_IDImageUSPC + " VARCHAR, " +
            USPC_APP_IDValueUSPC + " VARCHAR, " +
            DEVICE_USPC_TEST1 + " VARCHAR, " +
            DEVICE_USPC_TEST2 + " VARCHAR, " +
            DEVICE_USPC_TEST3 + " VARCHAR " +
            "); ";



    ///////////////////////////////AUTHModelData logger device local data

    private static final String DEVICE_TotalFaultDATALOger = "TotalFault";
    private static final String DEVICE_FaultDATALOger = "Fault";
    private static final String DEVICE_TDMFlowNameDATALOger = "TDMFlowName";
    private static final String DEVICE_TDMFlowUnitDATALOger = "TDMFlowUnit";
    private static final String DEVICE_TDMFlowValueDATALOger = "TDMFlowValue";

    private static final String DEVICE_TDMRHrNameDATALOger = "TDMRHrName";
    private static final String DEVICE_TDMRHrUnitDATALOger = "TDMRHrUnit";
    private static final String DEVICE_TDMRHrValueDATALOger = "TDMRHrValue";

    private static final String DEVICE_TDPVEnergyNameDATALOger = "TDPVEnergyName";
    private static final String DEVICE_TDPVEnergyUnitDATALOger = "TDPVEnergyUnit";
    private static final String DEVICE_TDPVEnergyValueDATALOger = "TDPVEnergyValue";

    private static final String DEVICE_TOTMFlowNameDATALOger = "TOTMFlowName";
    private static final String DEVICE_TOTMFlowUnitDATALOger = "TOTMFlowUnit";
    private static final String DEVICE_TOTMFlowValueDATALOger = "TOTMFlowValue";

    private static final String DEVICE_TOTMRHrNameDATALOger = "TOTMRHrName";
    private static final String DEVICE_TOTMRHrUnitDATALOger = "TOTMRHrUnit";
    private static final String DEVICE_TOTMRHrValueDATALOger = "TOTMRHrValue";

    private static final String DEVICE_TOTPVEnergyNameDATALOger = "TOTPVEnergyName";
    private static final String DEVICE_TOTPVEnergyUnitDATALOger = "TOTPVEnergyUnit";
    private static final String DEVICE_TOTPVEnergyValueDATALOger = "TOTPVEnergyValue";

    private static final String DEVICE_DATALOger_DEVICE_TYPE = "DeviceType";

    private static final String DEVICE_DATALOger_TEST1 = "TestOne";
    private static final String DEVICE_DATALOger_TEST2 = "TestTwo";
    private static final String DEVICE_DATALOger_TEST3 = "TestThree";

    private static final String DEVICE_DATALOger_NAME = "devicedatalogger";////////////////table name

    private static final String CREATE_TABLE_DATALOger = "CREATE TABLE "
            + DEVICE_DATALOger_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_TotalFaultDATALOger + " VARCHAR, " +
            DEVICE_FaultDATALOger + " VARCHAR, " +
            DEVICE_DATALOger_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_TDMFlowNameDATALOger + " VARCHAR, " +
            DEVICE_TDMFlowUnitDATALOger + " VARCHAR, " +
            DEVICE_TDMFlowValueDATALOger + " VARCHAR, " +
            DEVICE_TDMRHrNameDATALOger + " VARCHAR, " +
            DEVICE_TDMRHrUnitDATALOger + " VARCHAR, " +
            DEVICE_TDMRHrValueDATALOger + " VARCHAR, " +
            DEVICE_TDPVEnergyNameDATALOger + " VARCHAR, " +
            DEVICE_TDPVEnergyUnitDATALOger + " VARCHAR, " +
            DEVICE_TDPVEnergyValueDATALOger + " VARCHAR, " +
            DEVICE_TOTMFlowNameDATALOger + " VARCHAR, " +
            DEVICE_TOTMFlowUnitDATALOger + " VARCHAR, " +
            DEVICE_TOTMFlowValueDATALOger + " VARCHAR, " +
            DEVICE_TOTMRHrNameDATALOger + " VARCHAR, " +
            DEVICE_TOTMRHrUnitDATALOger + " VARCHAR, " +
            DEVICE_TOTMRHrValueDATALOger + " VARCHAR, " +
            DEVICE_TOTPVEnergyNameDATALOger + " VARCHAR, " +
            DEVICE_TOTPVEnergyUnitDATALOger + " VARCHAR, " +
            DEVICE_TOTPVEnergyValueDATALOger + " VARCHAR, " +
            DEVICE_DATALOger_TEST1 + " VARCHAR, " +
            DEVICE_DATALOger_TEST2 + " VARCHAR, " +
            DEVICE_DATALOger_TEST3 + " VARCHAR " +
            "); ";



    ///////////////////////////////AUTHModelData logger device local data

    private static final String DEVICE_TotalFaultAoneSs = "TotalFault";
    private static final String DEVICE_FaultAoneSs = "Fault";

    private static final String DEVICE_TDMFlowNameAoneSs = "TDMFlowName";
    private static final String DEVICE_TDMFlowUnitAoneSs = "TDMFlowUnit";
    private static final String DEVICE_TDMFlowValueAoneSs = "TDMFlowValue";


    private static final String DEVICE_TDMEnergyNameAoneSs = "TDPVEnergyName";
    private static final String DEVICE_TDMEnergyUnitAoneSs = "TDPVEnergyUnit";
    private static final String DEVICE_TDMEnergyValueAoneSs = "TDPVEnergyValue";

    private static final String DEVICE_TOTMFlowNameAoneSs = "TOTMFlowName";
    private static final String DEVICE_TOTMFlowUnitAoneSs = "TOTMFlowUnit";
    private static final String DEVICE_TOTMFlowValueAoneSs = "TOTMFlowValue";

    private static final String DEVICE_TOTMEnergyNameAoneSs = "TOTPVEnergyName";
    private static final String DEVICE_TOTMEnergyUnitAoneSs = "TOTPVEnergyUnit";
    private static final String DEVICE_TOTMEnergyValueAoneSs = "TOTPVEnergyValue";

    private static final String DEVICE_AoneSs_DEVICE_TYPE = "DeviceType";

    private static final String DEVICE_AoneSs_TEST1 = "TestOne";
    private static final String DEVICE_AoneSs_TEST2 = "TestTwo";
    private static final String DEVICE_AoneSs_TEST3 = "TestThree";

    private static final String DEVICE_AONESS_NAME = "deviceaoness";////////////////table name

    private static final String CREATE_TABLE_AONESS = "CREATE TABLE "
            + DEVICE_AONESS_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_TotalFaultAoneSs + " VARCHAR, " +
            DEVICE_FaultAoneSs + " VARCHAR, " +
            DEVICE_AoneSs_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_TDMFlowNameAoneSs + " VARCHAR, " +
            DEVICE_TDMFlowUnitAoneSs + " VARCHAR, " +
            DEVICE_TDMFlowValueAoneSs + " VARCHAR, " +
            DEVICE_TDMEnergyNameAoneSs + " VARCHAR, " +
            DEVICE_TDMEnergyUnitAoneSs + " VARCHAR, " +
            DEVICE_TDMEnergyValueAoneSs + " VARCHAR, " +
            DEVICE_TOTMFlowNameAoneSs + " VARCHAR, " +
            DEVICE_TOTMFlowUnitAoneSs + " VARCHAR, " +
            DEVICE_TOTMFlowValueAoneSs + " VARCHAR, " +
            DEVICE_TOTMEnergyNameAoneSs + " VARCHAR, " +
            DEVICE_TOTMEnergyUnitAoneSs + " VARCHAR, " +
            DEVICE_TOTMEnergyValueAoneSs + " VARCHAR, " +
            DEVICE_AoneSs_TEST1 + " VARCHAR, " +
            DEVICE_AoneSs_TEST2 + " VARCHAR, " +
            DEVICE_AoneSs_TEST3 + " VARCHAR " +
            "); ";





    ///////////////////////////////start stop command  data

    private static final String DEVICE_sno = "sno";
    private static final String DEVICE_buttonText = "buttonText";
    private static final String DEVICE_address = "address";
    private static final String DEVICE_BT_address = "btaddress";
    private static final String DEVICE_offset = "offset";
   // private static final String DEVICE_deviceType = "deviceType";
    private static final String DEVICE_deviceType = "DeviceType";
    private static final String DEVICE_data = "data";
    private static final String DEVICE_bColor = "bColor";
    private static final String DEVICE_cmdMsg = "cmdMsg";
    private static final String DEVICE_oldData = "oldData";
    private static final String DEVICE_START_STOP_TEST1 = "TestOne";
    private static final String DEVICE_START_STOP_TEST2 = "TestTwo";
    private static final String DEVICE_START_STOP_TEST3 = "TestThree";

    private static final String DEVICE_START_STOP_NAME = "devicestartstop";////////////////table name

    private static final String CREATE_TABLE_START_STOP = "CREATE TABLE "
            + DEVICE_START_STOP_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_sno + " VARCHAR, " +
            DEVICE_buttonText + " VARCHAR, " +
            DEVICE_deviceType + " TEXT NOT NULL, " +
            DEVICE_offset + " VARCHAR, " +
            DEVICE_address + " VARCHAR, " +
            DEVICE_BT_address + " VARCHAR, " +
            DEVICE_data + " VARCHAR, " +
            DEVICE_bColor + " VARCHAR, " +
            DEVICE_cmdMsg + " VARCHAR, " +
            DEVICE_oldData + " VARCHAR, " +
            DEVICE_START_STOP_TEST1 + " VARCHAR, " +
            DEVICE_START_STOP_TEST2 + " VARCHAR, " +
            DEVICE_START_STOP_TEST3 + " VARCHAR " +
            "); ";


    public void deleteStart_StopDATA(String vDeviceNo) {
        int hh;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("delete  from " + DEVICE_START_STOP_NAME + " where " + DEVICE_deviceType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void AllDeviceDdelete(String mTableName, String mDeviceType) {
        int hh;
        SQLiteDatabase db = this.getWritableDatabase();
        try {

                db.execSQL("delete  from " + mTableName + " where " + DEVICE_deviceType + "=" + mDeviceType);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /////////////////////////////device parameter
    private static final String DEVICE_REAL_PARA_MPINDEX = "MPIndexReal";
    private static final String DEVICE_REAL_PARA_MPNAME = "MPNameReal";

    private static final String DEVICE_REAL_PARA_UNIT = "UnitReal";
    private static final String DEVICE_REAL_PARA_VALUE = "ValueReal";
    private static final String DEVICE_REAL_PARA_DEVICE_TYPE = "MPDeviceTypeReal";

    private static final String DEVICE_REAL_PARA_DEVICE_TEST1 = "RealTest1";
    private static final String DEVICE_REAL_PARA_DEVICE_TEST2 = "RealTest2";
    private static final String DEVICE_REAL_PARA_DEVICE_TEST3 = "RealTest3";
    private static final String DEVICE_REAL_PARA_DEVICE_TEST4 = "RealTest4";

    private static final String DEVICE_REAL_PARAMETER_TABLE_NAME = "deviceparameterRealTimelist";////////////////table name

    private static final String CREATE_TABLE_DEVICE_REAL_PARAMETER = "CREATE TABLE "
            + DEVICE_REAL_PARAMETER_TABLE_NAME + "(" + LOGIN_KEY_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            DEVICE_REAL_PARA_MPINDEX + " TEXT NOT NULL, " +
            DEVICE_REAL_PARA_MPNAME + " TEXT NOT NULL, " +
            DEVICE_REAL_PARA_UNIT + " TEXT NOT NULL, " +
            DEVICE_REAL_PARA_VALUE + " VARCHAR, " +
            DEVICE_REAL_PARA_DEVICE_TYPE + " TEXT NOT NULL, " +
            DEVICE_REAL_PARA_DEVICE_TEST1 + " VARCHAR, " +
            DEVICE_REAL_PARA_DEVICE_TEST2 + " VARCHAR, " +
            DEVICE_REAL_PARA_DEVICE_TEST3 + " VARCHAR, " +
            DEVICE_REAL_PARA_DEVICE_TEST4 + " VARCHAR " +
            "); ";


    public DatabaseHelperTeacher(Context context) {
        super(context, DATABASE_NAME_RMS, null, DATABASE_VERSION);
        Log.d("table", CREATE_TABLE_TEACHERS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TEACHERS);

        db.execSQL(CREATE_TABLE_DEVICE_PARAMETER);
        db.execSQL(CREATE_TABLE_DEVICE_REAL_PARAMETER);

        db.execSQL(CREATE_TABLE_KLP);
        db.execSQL(CREATE_TABLE_SHIMA);
        db.execSQL(CREATE_TABLE_USPC);
        db.execSQL(CREATE_TABLE_DATALOger);
        db.execSQL(CREATE_TABLE_START_STOP);
        db.execSQL(CREATE_TABLE_AONESS);
        db.execSQL(CREATE_TABLE_Nikola);
        db.execSQL(CREATE_TABLE_OldKLP);
        db.execSQL(CREATE_TABLE_KLPGRID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_TEACHERS + "'");

        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_DEVICE_PARAMETER + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_DEVICE_REAL_PARAMETER + "'");

        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_KLP + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_SHIMA + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_USPC + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_DATALOger + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_START_STOP + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_AONESS + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_Nikola + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_OldKLP + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + CREATE_TABLE_KLPGRID + "'");
        //onCreate(db);
    }

    public long insertLoginData(String userid, String parentid, String username, String password, String phone, String clientid, String islogin, String status, String active) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Creating content values
        ContentValues values = new ContentValues();
        values.put(LOGIN_USER_ID, userid);
        values.put(LOGIN_PARENT_ID, parentid);
        values.put(LOGIN_USER_NAME, username);
        values.put(LOGIN_USER_PASSWORD, password);
        values.put(LOGIN_MOBILE_NO, phone);
        values.put(LOGIN_CLIENT_ID, clientid);
        values.put(IS_LOGIN, islogin);///7
        values.put(LOGIN_STATUS, status);
        values.put(LOGIN_ACTIVE, active);
      /*  values.put(LOGIN_TEST1, active);
        values.put(LOGIN_TEST2, active);
        values.put(LOGIN_TEST3, active);
        values.put(LOGIN_TEST4, active);*/
        //insert row in table
        long insert = db.insert(TABLE_USER_LOGIN, null, values);
        return insert;
    }

    public long updateLoginData(String userid, String parentid, String username, String password, String phone, String clientid, String islogin, String status, String active, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Creating content values
        ContentValues values = new ContentValues();
        values.put(LOGIN_USER_ID, userid);
        values.put(LOGIN_PARENT_ID, parentid);
        values.put(LOGIN_USER_NAME, username);
        values.put(LOGIN_USER_PASSWORD, password);
        values.put(LOGIN_MOBILE_NO, phone);
        values.put(LOGIN_CLIENT_ID, clientid);
        values.put(IS_LOGIN, islogin);///7
        values.put(LOGIN_STATUS, status);
        values.put(LOGIN_ACTIVE, active);
       /* values.put(LOGIN_TEST1, active);
        values.put(LOGIN_TEST2, active);
        values.put(LOGIN_TEST3, active);
        values.put(LOGIN_TEST4, active);*/
        //insert row in table
        // long insert = db.insert(TABLE_USER_LOGIN, null, values);
        return db.update(TABLE_USER_LOGIN, values, KEY_ID + " = ?", new String[]{id});

        // return insert;
    }

    public String checkUserLoginData(String username, String password) {
        String mAllUserInfo = null;
        // int id=-1; //0
        String id = ""; //0
        String mUserID = ""; //1
        String mParentID = "";//2
        String mUserName = "";//3
        String mUserPhone = "";//5
        String mClientID = "";///6
        String mISLogin = "";///7
        String mLoginStatus = "";///8
        String mLoginActive = "";///9

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userlogin WHERE " + LOGIN_USER_NAME + "=? AND " + LOGIN_USER_PASSWORD + "=?", new String[]{username, password});
        int ccccc = cursor.getCount();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            // id=cursor.getInt(0);
            id = cursor.getString(0);
            mUserID = cursor.getString(1);
            mParentID = cursor.getString(2);
            mUserName = cursor.getString(3);
            mUserPhone = cursor.getString(5);
            mClientID = cursor.getString(6);
            mISLogin = cursor.getString(7);
            mLoginStatus = cursor.getString(8);
            mLoginActive = cursor.getString(9);
            cursor.close();
        }

        mAllUserInfo = id + "SAK000IVS" + mUserID + "SAK000IVS" + mParentID + "SAK000IVS" + mUserName + "SAK000IVS" + mUserPhone + "SAK000IVS" + mClientID + "SAK000IVS" + mISLogin + "SAK000IVS" + mLoginStatus + "SAK000IVS" + mLoginActive;

        return mAllUserInfo;
    }

    public String checkUserAlreadyExist(String username, String password) {
        String mAllUserInfo = null;

        String id = ""; //0

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM userlogin WHERE " + LOGIN_USER_NAME + "=? AND " + LOGIN_USER_PASSWORD + "=?", new String[]{username, password});
        int ccccc = cursor.getCount();
        if (cursor.getCount() > 0) {

            cursor.moveToFirst();
            // id=cursor.getInt(0);
            id = cursor.getString(0);

            cursor.close();
            mAllUserInfo = id + "DHARSHANSIR000DHARSHANSIRUser already exist";
        } else {
            mAllUserInfo = id + "DHARSHANSIR111DHARSHANSIRUser not exist";
        }


        return mAllUserInfo;
    }

    ////////////////////////////////insert Device status data

    public long insertDeviceStatusListData(String stMode, String stName, String stColor, String stDeviceType, String testOne, String testTwo, String testThree, boolean mCheckFirst) {

        SQLiteDatabase db = this.getWritableDatabase();

        if (mCheckFirst) {//db.execSQL("TRUNCATE table" + DEVICE_LIST_TABLE_NAME);
            try {
                db.execSQL("delete  from " + DEVICE_STATUS_TABLE_NAME + " where " + DEVICE_STATUS_DEVICE_TYPE + "='" + stDeviceType+"'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Creating content values
        ContentValues values = new ContentValues();
        values.put(DEVICE_STATUS_MODE, stMode);
        values.put(DEVICE_STATUS_NAME, stName);
        values.put(DEVICE_STATUS_COLOR, stColor);
        values.put(DEVICE_STATUS_DEVICE_TYPE, stDeviceType);
        values.put(DEVICE_STATUS_TEST1, testOne);
        values.put(DEVICE_STATUS_TEST2, testTwo);
        values.put(DEVICE_STATUS_TEST3, testThree);

        /*private static final String DEVICE_STATUS_MODE = "SMode";
    private static final String DEVICE_STATUS_NAME = "SName";
    private static final String DEVICE_STATUS_COLOR = "SColor";
    private static final String DEVICE_STATUS_DEVICE_TYPE = "DeviceType";
    private static final String DEVICE_STATUS_TEST1 = "TestOne";
    private static final String DEVICE_STATUS_TEST2 = "TestTwo";
    private static final String DEVICE_STATUS_TEST3 = "TestThree";*/

        //insert row in table
        long insert = db.insert(DEVICE_STATUS_TABLE_NAME, null, values);
        return insert;
    }

///////////////para meter name list

 /*   public long insertDeviceParameterListData(String Address, String Divisible, String MDeviceNo, String MPId, String MPIndex, String MPName, String Status, String Unit, String PMin, String PMax, String MODAddress, String DeviceTyape, String Offset, boolean mCheckFirst) {

        SQLiteDatabase db = this.getWritableDatabase();

        if (mCheckFirst) {//db.execSQL("TRUNCATE table" + DEVICE_LIST_TABLE_NAME);
            try {
                db.execSQL("delete  from " + DEVICE_PARAMETER_TABLE_NAME + " where " + DEVICE_PARA_DEVICE_TYPE + "=" + DeviceTyape);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Creating content values
        ContentValues values = new ContentValues();
        values.put(DEVICE_PARA_ADDRESS, Address);
        values.put(DEVICE_PARA_DIVISIBLE, Divisible);
        values.put(DEVICE_PARA_DEVICENO, MDeviceNo);
        values.put(DEVICE_PARA_MPID, MPId);
        values.put(DEVICE_PARA_MPINDEX, MPIndex);
        values.put(DEVICE_PARA_MPNAME, MPName);
        values.put(DEVICE_PARA_STATUS, Status);
        values.put(DEVICE_PARA_UNIT, Unit);
        values.put(DEVICE_PARA_PMIN, PMin);
        values.put(DEVICE_PARA_PMAX, PMax);
        values.put(DEVICE_PARA_MODADDRESS, MODAddress);
        values.put(DEVICE_PARA_DEVICE_TYPE, DeviceTyape);
        values.put(DEVICE_PARA_DEVICE_TEST1, Offset);
        System.out.println("Offset_insert="+Offset);
        //values.put(DEVICE_PARA_DEVICE_TEST1, DeviceTyape);
        values.put(DEVICE_PARA_DEVICE_TEST2, DeviceTyape);
        values.put(DEVICE_PARA_DEVICE_TEST3, DeviceTyape);
        values.put(DEVICE_PARA_DEVICE_TEST4, DeviceTyape);

        //insert row in table
        long insert = db.insert(DEVICE_PARAMETER_TABLE_NAME, null, values);
        return insert;
    }

    public ArrayList<SettingModelResponse> getDevicePARAMeterListData(String DeviceType) {
        mSettingModelResponseList = new ArrayList<>();
        //customer_gps = new Customer_GPS_Search();
        String mAllUserInfo = null;

        // int id=-1; //0
        String id = ""; //0

        String Address, Divisible, MDeviceNo, MPId, MPIndex, MPName, Status, Unit, PMin, PMax, MODAddress, Offset, deviceType;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // Cursor cursor = db.rawQuery("SELECT * FROM " + DEVICE_PARAMETER_TABLE_NAME +" where "+DEVICE_PARA_DEVICE_TYPE+"=?", new String[]{ DeviceType });
            Cursor cursor = db.rawQuery("SELECT * FROM " + DEVICE_PARAMETER_TABLE_NAME + " where " + DEVICE_PARA_DEVICE_TYPE + "='" + DeviceType+"'", null);
            int ccccc = cursor.getCount();
            int PU = 0;
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    //  for(PU = 0 ; PU < ccccc ; PU++)
                    while (!cursor.isAfterLast()) {
                        //cursor.moveToFirst();
                        // id=cursor.getInt(0);
                        mSettingModelResponse = new SettingModelResponse();

                        id = cursor.getString(cursor.getColumnIndex(LOGIN_KEY_ID));
                        Address = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_ADDRESS));
                        //  Customer_GPS_SearchList.set(PU,mCustomer_name);
                        Divisible = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_DIVISIBLE));
                        //  Customer_GPS_SearchList.set()
                        MDeviceNo = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_DEVICENO));
                        MPId = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_MPID));
                        MPIndex = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_MPINDEX));
                        MPName = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_MPNAME));
                        Status = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_STATUS));
                        Unit = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_UNIT));
                        PMin = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_PMIN));
                        PMax = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_PMAX));
                        MODAddress = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_MODADDRESS));
                        deviceType = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_DEVICE_TYPE));
                        Offset = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_DEVICE_TEST1));

                        System.out.println("Offset_get=="+Offset);
                        mSettingModelResponse.setAddress(Address);
                        mSettingModelResponse.setDivisible(Long.parseLong(Divisible));
                        mSettingModelResponse.setMDeviceNo(Long.parseLong(MDeviceNo));
                        mSettingModelResponse.setMPId(MPId);
                        mSettingModelResponse.setMPIndex(Long.parseLong(MPIndex));
                        mSettingModelResponse.setMPName(MPName);
                        mSettingModelResponse.setStatus(Status);
                        mSettingModelResponse.setUnit(Unit);
                        mSettingModelResponse.setPMin(Integer.parseInt(PMin));
                        mSettingModelResponse.setPMax(Integer.parseInt(PMax));
                        mSettingModelResponse.setMPName(MPName);
                        mSettingModelResponse.setMobBTAddress(MODAddress);

                        try {
                            mSettingModelResponse.setOffset(Integer.parseInt(Offset));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        mSettingModelResponseList.add(mSettingModelResponse);
                        cursor.moveToNext();
                    }
                }

            }////////////addd chaech
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null && db.inTransaction()) {
                db.close();
            }
            // Close database
        }
        //mAllUserInfo = id+"SAK000IVS"+mUserID+"SAK000IVS"+mParentID+"SAK000IVS"+mUserName+"SAK000IVS"+mUserPhone+"SAK000IVS"+mClientID+"SAK000IVS"+mISLogin+"SAK000IVS"+mLoginStatus+"SAK000IVS"+mLoginActive;
        return mSettingModelResponseList;
    }///////////////para meter name list

    */

  /*  public long insertDeviceParameterListDataNew(String Address, String Divisible, String MDeviceNo, String MPId, String MPIndex, String MPName, String Status, String Unit, String PMin, String PMax, String MODAddress, String DeviceTyape, String Offset, boolean mCheckFirst) {

        SQLiteDatabase db = this.getWritableDatabase();

        if (mCheckFirst) {//db.execSQL("TRUNCATE table" + DEVICE_LIST_TABLE_NAME);
            try {
                db.execSQL("delete  from " + DEVICE_PARAMETER_TABLE_NAME + " where " + DEVICE_PARA_DEVICE_TYPE + "=" + DeviceTyape);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // Creating content values
        ContentValues values = new ContentValues();
        values.put(DEVICE_PARA_ADDRESS, Address);
        values.put(DEVICE_PARA_DIVISIBLE, Divisible);
        values.put(DEVICE_PARA_DEVICENO, MDeviceNo);
        values.put(DEVICE_PARA_MPID, MPId);
        values.put(DEVICE_PARA_MPINDEX, MPIndex);
        values.put(DEVICE_PARA_MPNAME, MPName);
        values.put(DEVICE_PARA_STATUS, Status);
        values.put(DEVICE_PARA_UNIT, Unit);
        values.put(DEVICE_PARA_PMIN, PMin);
        values.put(DEVICE_PARA_PMAX, PMax);
        values.put(DEVICE_PARA_MODADDRESS, MODAddress);
        values.put(DEVICE_PARA_DEVICE_TYPE, DeviceTyape);
        values.put(DEVICE_PARA_DEVICE_TEST1, Offset);
        System.out.println("Offset_insert="+Offset);
        //values.put(DEVICE_PARA_DEVICE_TEST1, DeviceTyape);
        values.put(DEVICE_PARA_DEVICE_TEST2, DeviceTyape);
        values.put(DEVICE_PARA_DEVICE_TEST3, DeviceTyape);
        values.put(DEVICE_PARA_DEVICE_TEST4, DeviceTyape);

        //insert row in table
        long insert = db.insert(DEVICE_PARAMETER_TABLE_NAME, null, values);
        return insert;
    }
*/
    /*public ArrayList<SettingModelResponse> getDevicePARAMeterListDataNew(String DeviceType) {
        mSettingModelResponseList = new ArrayList<>();
        //customer_gps = new Customer_GPS_Search();
        String mAllUserInfo = null;

        // int id=-1; //0
        String id = ""; //0

        String Address, Divisible, MDeviceNo, MPId, MPIndex, MPName, Status, Unit, PMin, PMax, MODAddress, Offset, deviceType;

        SQLiteDatabase db = this.getReadableDatabase();
        try {
            // Cursor cursor = db.rawQuery("SELECT * FROM " + DEVICE_PARAMETER_TABLE_NAME +" where "+DEVICE_PARA_DEVICE_TYPE+"=?", new String[]{ DeviceType });
            Cursor cursor = db.rawQuery("SELECT * FROM " + DEVICE_PARAMETER_TABLE_NAME + " where " + DEVICE_PARA_DEVICE_TYPE + "='" + DeviceType+"'", null);
            int ccccc = cursor.getCount();
            int PU = 0;
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    //  for(PU = 0 ; PU < ccccc ; PU++)
                    while (!cursor.isAfterLast()) {
                        //cursor.moveToFirst();
                        // id=cursor.getInt(0);
                        mSettingModelResponse = new SettingModelResponse();

                        id = cursor.getString(cursor.getColumnIndex(LOGIN_KEY_ID));
                        Address = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_ADDRESS));
                        //  Customer_GPS_SearchList.set(PU,mCustomer_name);
                        Divisible = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_DIVISIBLE));
                        //  Customer_GPS_SearchList.set()
                        MDeviceNo = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_DEVICENO));
                        MPId = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_MPID));
                        MPIndex = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_MPINDEX));
                        MPName = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_MPNAME));
                        Status = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_STATUS));
                        Unit = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_UNIT));
                        PMin = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_PMIN));
                        PMax = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_PMAX));
                        MODAddress = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_MODADDRESS));
                        deviceType = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_DEVICE_TYPE));
                        Offset = cursor.getString(cursor.getColumnIndex(DEVICE_PARA_DEVICE_TEST1));

                        System.out.println("Offset_get=="+Offset);
                        mSettingModelResponse.setAddress(Address);
                        mSettingModelResponse.setDivisible(Long.parseLong(Divisible));
                        mSettingModelResponse.setMDeviceNo(Long.parseLong(MDeviceNo));
                        mSettingModelResponse.setMPId(MPId);
                        mSettingModelResponse.setMPIndex(Long.parseLong(MPIndex));
                        mSettingModelResponse.setMPName(MPName);
                        mSettingModelResponse.setStatus(Status);
                        mSettingModelResponse.setUnit(Unit);
                        mSettingModelResponse.setPMin(Integer.parseInt(PMin));
                        mSettingModelResponse.setPMax(Integer.parseInt(PMax));
                        mSettingModelResponse.setMPName(MPName);
                        mSettingModelResponse.setMobBTAddress(MODAddress);

                        try {
                            mSettingModelResponse.setOffset(Integer.parseInt(Offset));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }

                        mSettingModelResponseList.add(mSettingModelResponse);
                        cursor.moveToNext();
                    }
                }

            }////////////addd chaech
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if (db != null && db.inTransaction()) {
                db.close();
            }
            // Close database
        }
        //mAllUserInfo = id+"SAK000IVS"+mUserID+"SAK000IVS"+mParentID+"SAK000IVS"+mUserName+"SAK000IVS"+mUserPhone+"SAK000IVS"+mClientID+"SAK000IVS"+mISLogin+"SAK000IVS"+mLoginStatus+"SAK000IVS"+mLoginActive;
        return mSettingModelResponseList;
    }*/




}
