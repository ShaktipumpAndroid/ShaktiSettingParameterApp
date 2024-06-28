package Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import activity.BeanVk.MotorParamListModel;
import activity.utility.CustomUtility;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "SettingParameterApp";
    public static final String TABLE_NAME = "DeviceInformation";
    public static final String COLUMN_pmID = "PmId";
    public static final String COLUMN_ParametersName = "ParametersName";
    public static final String COLUMN_ModBusAddress = "ModBusAddress";
    public static final String COLUMN_MobBTAddress = "MobBTAddress";
    public static final String COLUMN_factor = "Factor";
    public static final String COLUMN_pValue = "PValue";
    public static final String COLUMN_MaterialCode = "MaterialCode";
    public static final String COLUMN_Unit = "Unit";
    public static final String COLUMN_offset = "Offsets";

    private SQLiteDatabase database;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String qury = " CREATE TABLE " + TABLE_NAME + " ( "
                + COLUMN_pmID + " TEXT ,"
                + COLUMN_ParametersName + " TEXT ,"
                + COLUMN_ModBusAddress + " TEXT ,"
                + COLUMN_MobBTAddress + " TEXT ,"
                + COLUMN_factor + " TEXT ,"
                + COLUMN_pValue + " TEXT ,"
                + COLUMN_MaterialCode + " TEXT ,"
                + COLUMN_Unit + " TEXT ,"
                + COLUMN_offset + " TEXT)";
        db.execSQL(qury);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertRecordAlternate( String pmID,String ParametersName,String ModBusAddress,String MobBTAddress,String factor, String PValue,String MaterialCode,
                                       String Unit,String offset) {
        database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_pmID, pmID);
        contentValues.put(COLUMN_ParametersName, ParametersName);
        contentValues.put(COLUMN_ModBusAddress, ModBusAddress);
        contentValues.put(COLUMN_MobBTAddress, MobBTAddress);
        contentValues.put(COLUMN_factor, factor);
        contentValues.put(COLUMN_pValue, PValue);
        contentValues.put(COLUMN_MaterialCode, MaterialCode);
        contentValues.put(COLUMN_Unit, Unit);
        contentValues.put(COLUMN_offset, offset);
        database.insert(TABLE_NAME, null, contentValues);
        database.close();
    }

    public void updateRecordAlternate(String pmID,String ParametersName, String PValue) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(COLUMN_pValue, PValue);
            values.put(COLUMN_ParametersName,ParametersName);
            String  where = COLUMN_ParametersName + "='" + ParametersName + "'" + " AND " +
                    COLUMN_pmID + "='" + pmID + "'" ;

            database.update(TABLE_NAME, values, where, null);

            // Insert into database successfully.
            database.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            database.close();


        }

    }

    public void updateRecordAlternate(MotorParamListModel.Response response) {

        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        ContentValues values;

        try {

            values = new ContentValues();
            values.put(COLUMN_pValue, response.getpValue());
            values.put(COLUMN_ParametersName,response.getParametersName());
            String  where = COLUMN_ParametersName + "='" + response.getParametersName() + "'" + " AND " +
                    COLUMN_pmID + "='" + response.getPmId() + "'" ;

            database.update(TABLE_NAME, values, where, null);

            // Insert into database successfully.
            database.setTransactionSuccessful();

        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            database.close();


        }

    }

    @SuppressLint("Range")
    public int getRecordCount(){
        database = this.getWritableDatabase();
        Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_NAME, null);

          return  mcursor.getCount();

    }

    @SuppressLint("Range")
    public ArrayList<MotorParamListModel.Response> getRecordDetails(){
        ArrayList<MotorParamListModel.Response> arrayList = new ArrayList<>();
        database = this.getWritableDatabase();
        Cursor mcursor = database.rawQuery(" SELECT * FROM " + TABLE_NAME, null);

        if(mcursor.getCount()>0){
            Log.e("Count====>", String.valueOf(mcursor.getCount()));
            while (mcursor.moveToNext()) {

                MotorParamListModel.Response motorPumpList = new MotorParamListModel.Response();
                motorPumpList.setPmId(Integer.parseInt(mcursor.getString(mcursor.getColumnIndex(COLUMN_pmID))));
                motorPumpList.setParametersName(mcursor.getString(mcursor.getColumnIndex(COLUMN_ParametersName)));
                motorPumpList.setModbusaddress(mcursor.getString(mcursor.getColumnIndex(COLUMN_ModBusAddress)));
                motorPumpList.setMobBTAddress(mcursor.getString(mcursor.getColumnIndex(COLUMN_MobBTAddress)));
                motorPumpList.setpValue(Math.toIntExact(Math.round(Double.parseDouble(mcursor.getString(mcursor.getColumnIndex(COLUMN_pValue))))));
                motorPumpList.setMaterialCode(mcursor.getString(mcursor.getColumnIndex(COLUMN_MaterialCode)));
                motorPumpList.setFactor(Integer.parseInt(mcursor.getString(mcursor.getColumnIndex(COLUMN_factor))));
                motorPumpList.setOffset(Integer.parseInt(mcursor.getString(mcursor.getColumnIndex(COLUMN_offset))));
                arrayList.add(motorPumpList);
            }

        }

        mcursor.close();
        database.close();


        return arrayList;
    }

    public void deleteDatabase() {
       database = this.getWritableDatabase();
        if(CustomUtility.doesTableExist(database,TABLE_NAME)) {
            database.delete(TABLE_NAME, null, null);
        }

    }
}
