package com.example.mqttapplication.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.mqttapplication.roomdatabase.DeviceDao;
import com.example.mqttapplication.roomdatabase.DeviceDatabase;
import com.example.mqttapplication.roomdatabase.DeviceEntity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DeviceRepository {
    private DeviceDao deviceDao;
    //Fire Database
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mGetReference;

    public DeviceRepository(Application application){
        DeviceDatabase database = DeviceDatabase.getInstance(application);
        deviceDao = database.deviceDao();
    }

    public void insert(DeviceEntity deviceEntity){
        new InsertAsyncTask(deviceDao).execute(deviceEntity);
    }

    public void deleteAll(int deviceID){
        new DeleteAsyncTask(deviceDao).execute(deviceID);
        mGetReference = mDatabase.getReference("End_device/deviceID_" + deviceID);
        mGetReference.removeValue();
    }

    public void deleteDatabase(){new DeleteAllAsyncTask(deviceDao).execute();}

    public LiveData<DeviceEntity> getLatestData(int deviceID) {
        return deviceDao.getLatestData(deviceID);
    }

    public LiveData<List<DeviceEntity>> getAllData(int deviceID){
        return deviceDao.getAllData(deviceID);
    }
    /**
     ***************************************************************
     ****************************************************************
     * Create async task classes to interact to database via dao and
     * do in background to avoid delay in thread main UI
     ****************************************************************
     *****************************************************************
     */

//    /**
//     * Class get latest data from database
//     */
//    private static class GetLatestAsyncTask extends AsyncTask<Integer, Void, LiveData<DeviceEntity>> {
//
//        private DeviceDao asyncTaskDao;
//
//        GetLatestAsyncTask(DeviceDao dao){
//            asyncTaskDao = dao;
//        }
//
//        @Override
//        protected LiveData<DeviceEntity> doInBackground(Integer... ints) {
//            return asyncTaskDao.getLatestData(ints[0]);
//        }
//    }

    /**
     * Class insert data into database
     */
    private static class InsertAsyncTask extends AsyncTask<DeviceEntity, Void, Void>{

        private DeviceDao asyncTaskDao;

        InsertAsyncTask(DeviceDao dao){
            asyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(DeviceEntity... device_Entity) {
            asyncTaskDao.insert(device_Entity[0]);
            return null;
        }
    }

    /**
     * Class delete all data with device ID
     */
    private static class DeleteAsyncTask extends AsyncTask<Integer, Void, Void>{

        private DeviceDao asyncTaskDao;

        DeleteAsyncTask(DeviceDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            asyncTaskDao.deleteAll(integers[0]);
            return null;
        }
    }

    /**
     * Class delete all data in database
     */
    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void>{

        private DeviceDao asyncTaskDao;

        DeleteAllAsyncTask(DeviceDao dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteDatabase();
            return null;
        }
    }
}
