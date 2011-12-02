package org.conan.mahout.book;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.ClusterSimilarity;
import org.apache.mahout.cf.taste.impl.recommender.FarthestNeighborClusterSimilarity;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.TreeClusteringRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.KnnItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.NonNegativeQuadraticOptimizer;
import org.apache.mahout.cf.taste.impl.recommender.knn.Optimizer;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.MemoryDiffStorage;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.slopeone.DiffStorage;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class ItemBase {

    public static void demo1(DataModel model) throws Exception {
        ItemSimilarity similarity = new PearsonCorrelationSimilarity(model);
        Recommender recommender = new GenericItemBasedRecommender(model, similarity);
        List<RecommendedItem> recommendations = recommender.recommend(1, 2);
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }
    }

    public static void demo2(DataModel model) throws Exception {
        DiffStorage diffStorage = new MemoryDiffStorage(model, Weighting.UNWEIGHTED, Long.MAX_VALUE);
        SlopeOneRecommender recommender = new SlopeOneRecommender(model, Weighting.UNWEIGHTED, Weighting.UNWEIGHTED, diffStorage);
    }

    public static void demo3(DataModel model) throws Exception {
        ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
        Optimizer optimizer = new NonNegativeQuadraticOptimizer();
        Recommender recommender = new KnnItemBasedRecommender(model, similarity, optimizer, 10);
    }

    public static void demo4(DataModel model) throws Exception {
        UserSimilarity similarity = new LogLikelihoodSimilarity(model);
        ClusterSimilarity clusterSimilarity = new FarthestNeighborClusterSimilarity(similarity);
        new TreeClusteringRecommender(model, clusterSimilarity, 10);
    }

    public static void main(String[] args) throws Exception {
        String data1 = "metadata/data/data1.csv";
        DataModel model = new FileDataModel(new File(data1));
        demo1(model);
    }

}
