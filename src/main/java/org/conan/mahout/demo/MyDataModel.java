package org.conan.mahout.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericDataModel;
import org.apache.mahout.cf.taste.impl.model.GenericPreference;
import org.apache.mahout.cf.taste.impl.model.GenericUserPreferenceArray;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class MyDataModel {
    
    public static DataModel getPrefDataByFile() throws Exception {
        System.out.println("数据源: PrefDataByFile");
        String data = "metadata/data/data1.csv";
        return new FileDataModel(new File(data));
    }

    public static DataModel getPrefDataByFileFast() throws Exception {
        System.out.println("数据源: PrefDataByFileFast");
        String data = "metadata/data/data1.csv";
        FastByIDMap<PreferenceArray> preferences = new FastByIDMap<PreferenceArray>();
        BufferedReader br = new BufferedReader(new FileReader(data));

        List<Preference> list = new ArrayList<Preference>();
        String line = br.readLine();
        long uid_init = Long.parseLong(line.split(",")[0]);
        while (line != null) {
            String[] arr = line.split(",");
            long uid = Long.parseLong(arr[0]);
            if (uid_init == uid) {
                list.add(new GenericPreference(uid, Long.parseLong(arr[1]), Float.parseFloat(arr[2])));
            } else {
                preferences.put(uid_init, new GenericUserPreferenceArray(list));
                list = new ArrayList<Preference>();
                list.add(new GenericPreference(uid, Long.parseLong(arr[1]), Float.parseFloat(arr[2])));
                uid_init = uid;
            }
            line = br.readLine();

            if (line == null) {
                preferences.put(uid_init, new GenericUserPreferenceArray(list));
            }
        }
        return new GenericDataModel(preferences);
    }

    public static DataModel getNoPrefDataByFile() throws Exception {
        System.out.println("数据源: NoPrefDataByFile");
        String data = "metadata/data/data2.csv";
        return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(new FileDataModel(new File(data))));
    }

    public static DataModel getDataByMysql() throws Exception {
        System.out.println("数据源: PrefDataByMySQL");
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("mahout");
        dataSource.setPassword("mahout");
        dataSource.setDatabaseName("mahout");
        JDBCDataModel dataModel = new MySQLJDBCDataModel(dataSource, "t_data1", "user_id", "item_id", "preference", null);
        return dataModel;
    }

    public static DataModelBuilder createNoPrefDataModelBuilder() throws TasteException {
        return new DataModelBuilder() {
            @Override
            public DataModel buildDataModel(FastByIDMap<PreferenceArray> trainingData) {
                return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(trainingData));
            }
        };
    }

    public static DataModelBuilder createPrefDataModelBuilder() throws TasteException {
        return new DataModelBuilder() {
            @Override
            public DataModel buildDataModel(FastByIDMap<PreferenceArray> trainingData) {
                return new GenericDataModel(trainingData);
            }
        };
    }

}
