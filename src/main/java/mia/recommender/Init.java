package mia.recommender;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.example.grouplens.GroupLensDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.model.DataModel;

public class Init {

    public static DataModel getSimple() throws IOException {
        DataModel model = new FileDataModel(new File("metadata/data/ch02.csv"));
        return model;
    }

    public static DataModel getUaBase() throws IOException {
        DataModel model = new FileDataModel(new File("metadata/data/ua.base"));
        return model;
    }
    
    public static DataModel getGroupLensRatings() throws IOException {
        DataModel model = new GroupLensDataModel(new File("metadata/data/libimseti/ratings.dat"));
        return model;
    }
    
    public static DataModel getRatings() throws IOException {
//        DataModel model = new FileDataModel(new File("metadata/data/libimseti/ratings.dat"));
        DataModel model = new FileDataModel(new File("metadata/data/libimseti/ratings_u50000.dat"));
        return model;
    }
    
    public static DataModel getGenders() throws IOException {
//        DataModel model = new FileDataModel(new File("metadata/data/libimseti/gender.dat"));
        DataModel model = new FileDataModel(new File("metadata/data/libimseti/gender_u50000.dat"));
        return model;
    }
    
    
    
    
}
