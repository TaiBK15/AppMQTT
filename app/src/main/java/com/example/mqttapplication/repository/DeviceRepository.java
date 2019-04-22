package com.example.mqttapplication.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.mqttapplication.roomdatabase.DaoDevice_1;
import com.example.mqttapplication.roomdatabase.DatabaseDevice_1;
import com.example.mqttapplication.roomdatabase.EntityDevice_1;

import java.util.List;

public class DeviceRepository {
    private LiveData<List<EntityDevice_1>> sensorData ;
    private DaoDevice_1 daoDevice1;

    public DeviceRepository(Application application){
        DatabaseDevice_1 database = DatabaseDevice_1.getInstance(application);
        daoDevice1 = database.daoDevice1();
    }

    public void insert(EntityDevice_1 device_1){
        new InsertAsyncTask(daoDevice1).execute(device_1);
    }

    public void deleteAll(){
        new DeleteAsyncTask(daoDevice1).execute();
    }

    public LiveData<List<EntityDevice_1>> getLatestData() {
        return daoDevice1.getLatestData();
    }

    public LiveData<List<EntityDevice_1>> getAllData(){
        return daoDevice1.getAllData();
    }
    /**
     ***************************************************************
     ****************************************************************
     * Create async task classes to interact to database via dao and
     * do in background to avoid delay in thread main UI
     ****************************************************************
     *****************************************************************
     */

    /**
     * Class get latest data from database
     */
    private static class GetLatestAsyncTask extends AsyncTask<Void, Void, LiveData<List<EntityDevice_1>>> {

        private DaoDevice_1 asyncTaskDao;

        GetLatestAsyncTask(DaoDevice_1 dao){
            asyncTaskDao = dao;
        }


        @Override
        protected LiveData<List<EntityDevice_1>> doInBackground(Void... voids) {
            return asyncTaskDao.getLatestData();
        }
    }

    /**
     * Class insert data into database
     */
    private static class InsertAsyncTask extends AsyncTask<EntityDevice_1, Void, Void>{

        private  DaoDevice_1 asyncTaskDao;

        InsertAsyncTask(DaoDevice_1 dao){
            asyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(EntityDevice_1... entityDevice_1) {
            asyncTaskDao.insert(entityDevice_1[0]);
            return null;
        }
    }

    /**
     * Class delete all data in database
     */
    private static class DeleteAsyncTask extends AsyncTask<Void, Void, Void>{

        private  DaoDevice_1 asyncTaskDao;

        DeleteAsyncTask(DaoDevice_1 dao){
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }
}
