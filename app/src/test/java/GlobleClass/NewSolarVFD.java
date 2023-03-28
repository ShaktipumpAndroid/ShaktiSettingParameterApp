package GlobleClass;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shakti on 07-Apr-18.
 */
public class NewSolarVFD {
//*********************   development url ****************************************
   // public static final String HOST_NAME =  "http://111.118.249.180:1112/Home/";
   // public static final String HOST_NAME2 = "http://111.118.249.180:1112/Home/";

    public static  String COMPELETE_ACCESS_TOKEN_NAME =   "";   // https://www your domain .com/

    public static  String PAYMENT_PLAN_ID =   "";   // https://www your domain .com/
    public static  String DEVICE_NUMBER_PAYMENT =   "";   // https://www your domain .com/
    public static final String BASE_URL =   "https://test.payu.in";   // https://www your domain .com/
   // public static final String BASE_URL =   "http://192.168.2.242:1112/server/";   // https://www your domain .com/
    public static final String SERVER_main_folder = "payment/"; // use "foldername/"  -- if www.yourdomain.com/foldername/app
    public static final long API_CONNECTION_TIMEOUT = 1201;
    public static final long API_READ_TIMEOUT = 901;
    public static String IMEI_NUMBER =  "";
    public static String mNumberOfMonth =  "";
    public static String FCM_TOKEN =  "";
    public static int OEM_NORMAL_USER_CHECK =  0;
    public static int MOTOR_ON_OFF_CHECK =  0;
    public static int USPC_SELECTION_CHECK =  0;
    public static String USPC_SELECTION_IMAGE =  "";
    public static String USPC_SELECTION_NAME =  "";

    public static int CHECK_LAT_LONG_AND_MQTT = 0;

  /*  public static final String HOST_NAME =  "http://111.118.249.180:1112/ShaktiHome/";
    public static final String HOST_NAME2 = "http://111.118.249.180:1112/ShaktiHome/";
    public static final String HOST_NAME3 = "http://111.118.249.180:1112/ShaktiHome/";*/

    /*public static final String HOST_NAME3 = "https://solar10.shaktisolarrms.com/Home/";
  //************************  production url ****************************************
    public static final String HOST_NAME = "https://solar10.shaktisolarrms.com/RMSApp1/";
    public static final String HOST_NAME2 = "https://solar10.shaktisolarrms.com/RMSApp1/";*/

    /*public static final String HOST_NAME = "http://192.168.2.242:8086/RMSApp/";
    public static final String HOST_NAME2 = "http://192.168.2.242:8086/RMSApp/";*/
    //9993247636

    public static  String USERNAME_RG="";
    public static  String Password_RG="";
    public static  String APP_TYPE_CHECKVK="";

    public static final String HOST_NAME_IMEI = "https://pmkapi.hareda.gov.in/api/";
    public static String versionNameForAll;
  /*  public static final String HOST_NAME3 = "http://111.118.249.180:8888/api/";//http://111.118.249.180:8888/api/
   //************************  production url ****************************************
   public static final String HOST_NAME = "http://111.118.249.180:8888/api/";
    public static final String HOST_NAME2 = "http://111.118.249.180:8888/api/";*/


    public static final String HOST_NAME2 = "https://solar10.shaktisolarrms.com/RMSAppTest/";
    public static final String HOST_NAME = "https://solar10.shaktisolarrms.com/RMSAppTest/";
    public static final String HOST_NAME3 = "https://solar10.shaktisolarrms.com/RMSAppTest/";



    public static final String MOTOR_PERSMETER_LIST = "MoterParamList";
    public static final String LOGIN_USER_REGISTRATION = HOST_NAME + "UserLogin";
    public static final String UPDATE_USER_OTP = HOST_NAME + "UserAuth";
    public static final String UPDATE_DEVICE_OTP = HOST_NAME + "DeviceAuth";
    public static final String DELETE_DEVICE = HOST_NAME + "DeviceDelete";
    public static final String START_STOP_MOTOR = HOST_NAME3 + "DeviceSettingParam";
    public static final String GET_DEVICE = HOST_NAME + "MobileDevice";
    public static final String REAL_MONITORING =  HOST_NAME3 + "DataMonitor";/////
    public static final String DEVICE_DETAILS = HOST_NAME + "DeviceDetails";  // lat long
    public static final String FAULT_RECORD = HOST_NAME + "FaultMRecord";
    public static final String DATA_REPORT = HOST_NAME + "CumulativeReports";
    public static final String FORGOT_PASSWORD = HOST_NAME + "Dashboard/MForgetPasswordUsername.jsp";
    public static final String SMS_URL = "https://spprdsrvr1.shaktipumps.com:8423/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zrms_mobileapp/sms_service.htm";
    public static final String APP_VERSION = "https://spprdsrvr1.shaktipumps.com:8423/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zrms_mobileapp/app_version.htm";

    //*************** organisation login ********************* //
    public static final String ORG_GET_DEVICE = HOST_NAME + "ClientDeviceList";
    public static final String ORG_GET_DEVICE_SETTING = "DeviceSetting";
    public static final String ORG_GET_WELCOME_IMAGE = "MobBackImg";
    public static final String ORG_SEND_OTP_FORGOTPASS = "MFotgetPassword";
    public static final String ORG_RESET_FORGOTPASS = "ResetPassword";
    //public static final String ORG_DEVICE_DETAILS = "TotalEnergy2";
    public static final String ORG_DEVICE_DETAILS = "TotalEnergy";
    public static final String ORG_DEVICE_ENERGY_GRAPH = "DeviceEnergy";
    public static final String ORG_MOBILE_VERSION = "MobVersion";
    public static final String ORG_TOTAL_KLP_ENERGY = "TOTEKalP";
    public static final String ORG_TOTAL_KLP_HYBRID_ENERGY = "TOTEKalpaHybrid";
    public static final String ORG_TOTAL_KLP_HYBRID_GRAPH = "KalpaGraph";
    public static final String ORG_TOTAL_NIKOLA_ENERGY = "TOTENikola";
    public static final String ORG_TOTAL_SHIMA_ENERGY = "TOTESimha";
    public static final String ORG_TOTAL_SHIMA2_ENERGY = "TOTESimha2";
    public static final String ORG_SIMHA_GRAPH_API = "SimhaGraph";
    public static final String ORG_A_ONE_SS_GRAPH_API = "AOneGraph";
    public static final String ORG_DATALOGER_VEICHI_GRAPH_API = "DLoggerGraph";
    public static final String ORG_NIKOLA_GRAPH_API = "NikolaGraph";
    public static final String ORG_TOTAL_A1SS_ENERGY = "TOTEAONE";
    public static final String ORG_TOTAL_KLP_GRID_ENERGY = "TOTEKalpaGrid";
    public static final String ORG_TOTAL_KLP_GRID_GRAPH_ENERGY = "KalpaGridGraph";
    public static final String ORG_TOTAL_SAJ_ENERGY = "TOTESAJ";
    public static final String ORG_TOTAL_GRIDTIE_oldKLP_ENERGY = "TOTEGTie";////Andra project
    public static final String ORG_GRIDTIE_SEVENTEEN_ENERGY = "TOTESUNShaktiGrid";////saj
    public static final String ORG_GRIDTIE_SEVENTEEN_GRAPH_ENERGY = "SunShaktiGTGraph";////saj
    public static final String ORG_KLP_SIXTYNINE_GRAPH_ENERGY = "KalpaGraph";////KalpaGraph
    public static final String ORG_OLD_KLP_SEVEN_GRAPH_ENERGY = "GridTieGraph";////OLG_KalpaGraph
    public static final String ORG_TOTAL_DATA_LOGGER_ENERGY = "TOTEDLogger";
    public static final String ORG_PRODUCT_STATUS = "ProductStatus";
    public static final String ORG_PRODUCT_DYNAMIC_BTN = "MobileControlButton";
    public static final String CHANGE_PASSWORD_API = "PasswordChange";
    public static final String USPCLIST_API = "USPCList";
    public static final String PAYMENT_PLAN_API = "MobilePlan";
    public static final String INSERTUPDATE_FCM_API = "FCMTokken";
    public static final String INSERT_DEBUG_DATA_API = "DeviceDebug";
    public static final String SAVE_TRANSACTION = "SaveTransaction";
    public static final String SEND_ADD_DEVICE_OTP = "DeviceSendOTP";
    public static final String SEND_USER_ACCOUNT_OTP = "UserSendOTP";

    public static final String IMEI_GET_TOKEN = "Token/Login_new_3";
    public static final String IMEI_SEND_AUTH_TOKEN = "SSLCert/GetConnectionResult?";

    public static final String IMEI_SEND_AUTH_TOKEN_NEERAJ = "MNREAPICalling?";

    /*public static void popupDialodCustom(final Context mContext, final String mModelType) {

         final WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);;
        // Create custom dialog object
        final Dialog dialog = new Dialog(mContext);
        // Include dialog.xml file
        dialog.setContentView(R.layout.popupwindow);
        // Set dialog title
        //dialog.setTitle("");
       // dialog.co
        dialog.setCancelable(false);

        // set values for custom dialog components - text, image and button
        ImageView imgCLoseID = (ImageView) dialog.findViewById(R.id.imgCLoseID);
        RelativeLayout rlvInternetViewID = (RelativeLayout) dialog.findViewById(R.id.rlvInternetViewID);
        RelativeLayout rlvBluetothViewID = (RelativeLayout) dialog.findViewById(R.id.rlvBluetothViewID);
        RelativeLayout rlvWifihotspotViewID = (RelativeLayout) dialog.findViewById(R.id.rlvWifihotspotViewID);

        dialog.show();
        // if decline button is clicked, close the custom dialog
        imgCLoseID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
            }
        });

        // if decline button is clicked, close the custom dialog
        rlvInternetViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                dialog.dismiss();
                try {
                    Constant.BT_DEVICE_NAME = "";
                    Constant.BT_DEVICE_MAC_ADDRESS = "";
                    Constant.CHECK_FOR_WORK_WITH_BT_OR_IN = 0;
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // if decline button is clicked, close the custom dialog
        rlvBluetothViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                //dialog.dismiss();
                try {
                    Constant.CHECK_FOR_WORK_WITH_BT_OR_IN = 1;
                    if(mModelType.equalsIgnoreCase("8"))
                    {
                        // Intent ii = new Intent(mContext, Options_Activity.class);
                        //  mContext.startActivity(ii);
                        SharedPreferences sharedPref = mContext.getSharedPreferences("Pref", 0);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("user_comm_option", "B");
                        editor.apply();
                        Intent i = new Intent(mContext, Bluetooth_DeviceLists_Activity.class);
                        mContext.startActivity(i);

                    }
                    else
                    {
                        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                        if (mBluetoothAdapter.isEnabled()) {
                            if (AllPopupUtil.pairedDeviceListGloable(mContext)) {

                                if (Constant.BT_DEVICE_NAME.equalsIgnoreCase("") || Constant.BT_DEVICE_NAME.equalsIgnoreCase(null) || Constant.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase("") || Constant.BT_DEVICE_MAC_ADDRESS.equalsIgnoreCase(null)) {
                                    Intent intent = new Intent(mContext, PairedDeviceActivity.class);
                                    mContext.startActivity(intent);
                                }
                                ///////////////write the query
                                //   new BluetoothCommunicationForMotorStop().execute(":TURNON#", ":TURNON#", "START");
                            } else {
                                // AllPopupUtil.btPopupCreateShow(mContext);
                                mContext.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                            }
                        } else {
                            //  AllPopupUtil.btPopupCreateShow(mContext);
                            mContext.startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        // if decline button is clicked, close the custom dialog
        rlvWifihotspotViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                //dialog.dismiss();
                try {
                    Constant.BT_DEVICE_NAME = "";
                    Constant.BT_DEVICE_MAC_ADDRESS = "";
                    Constant.CHECK_FOR_WORK_WITH_BT_OR_IN = 0;
                    wifiManager.setWifiEnabled(true);
                    Intent mIntent = new Intent(mContext, Wifi_safe_Activity.class);
                    //  Intent mIntent = new Intent(mContext, Wifi_Parameters_GetSet_Activity.class);
                    // mIntent.putExtra("mDeviceDetail", (Serializable) customerSearchesList);
                    // mIntent.putExtra("mDeviceDetail", (Serializable) arraylist);
                    //  mIntent.putExtra("mPos", position);
                    mContext.startActivity(mIntent);

                    //mContext.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }*/

    public static boolean emailValidator(String email)
    {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /* public static final String ORG_GET_DEVICE = HOST_NAME + "/ClientDeviceList";
    public static final String ORG_GET_DEVICE_SETTING = HOST_NAME + "/DeviceSetting";*/
}
