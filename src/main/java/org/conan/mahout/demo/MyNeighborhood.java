package org.conan.mahout.demo;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MyNeighborhood {

    public static UserNeighborhood getNearestN(DataModel m, UserSimilarity s, int num) throws TasteException {
        System.out.println("相邻算法: NearestNUser");
        return new NearestNUserNeighborhood(num, s, m);
    }

    public static UserNeighborhood getThreshold(DataModel m, UserSimilarity s, int num) throws TasteException {
        System.out.println("相邻算法: Threshold");
        return new ThresholdUserNeighborhood(num, s, m);
    }

}
