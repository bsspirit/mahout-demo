package org.conan.mahout.demo;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MySimilarity {

    public static UserSimilarity getPearsonCorrelation(DataModel m) throws TasteException {
        System.out.println("相似算法: PearsonCorrelation");
        return new PearsonCorrelationSimilarity(m);
    }

    public static UserSimilarity getEuclideanDistance(DataModel m) throws TasteException {
        System.out.println("相似算法: EuclideanDistance");
        return new EuclideanDistanceSimilarity(m);
    }

    public static UserSimilarity getUncenteredCosine(DataModel m) throws TasteException {
        System.out.println("相似算法: UncenteredCosine");
        return new UncenteredCosineSimilarity(m);
    }

}
