package org.conan.mahout.book;

import java.io.File;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.common.FastByIDMap;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.LoadEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

/**
 * Demo1
 */
public class UserBase {

    public static void demo1(DataModel model) throws Exception {
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        List<RecommendedItem> recommendations = recommender.recommend(1, 2);
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }
    }

    public static void demo2(DataModel model) throws Exception {
        RandomUtils.useTestSeed();
        RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        RecommenderBuilder builder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };
        double score = evaluator.evaluate(builder, null, model, 0.7, 1.0);
        System.out.println(score);
    }

    public static void demo3(DataModel model) throws Exception {
        RandomUtils.useTestSeed();
        RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };
        IRStatistics stats = evaluator.evaluate(recommenderBuilder, null, model, null, 2, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
        System.out.println(stats.getPrecision());
        System.out.println(stats.getRecall());
    }

    public static void demo4(DataModel model) throws Exception {
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                return new SlopeOneRecommender(model);
            }
        };

    }

    public static void demo5(DataModel model) throws Exception {
        RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            public Recommender buildRecommender(DataModel model) throws TasteException {
                UserSimilarity similarity = new LogLikelihoodSimilarity(model);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(10, similarity, model);
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };
        DataModelBuilder modelBuilder = new DataModelBuilder() {
            public DataModel buildDataModel(FastByIDMap<PreferenceArray> trainingData) {
                return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(trainingData));
            }
        };
        double score = evaluator.evaluate(recommenderBuilder, modelBuilder, model, 0.9, 1.0);
        System.out.println(score);
    }

    public static void demo6(DataModel model) throws Exception {
        RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) {
                UserSimilarity similarity = new LogLikelihoodSimilarity(model);
                UserNeighborhood neighborhood = null;
                try {
                    neighborhood = new NearestNUserNeighborhood(10, similarity, model);
                } catch (TasteException e) {
                    e.printStackTrace();
                }
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };
        DataModelBuilder modelBuilder = new DataModelBuilder() {
            @Override
            public DataModel buildDataModel(FastByIDMap<PreferenceArray> trainingData) {
                return new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(trainingData));
            }
        };
        IRStatistics stats = evaluator.evaluate(recommenderBuilder, modelBuilder, model, null, 10, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);

        System.out.println(stats.getPrecision());
        System.out.println(stats.getRecall());

    }

    public static void demo7(DataModel model) throws Exception {
        // DataModel model = new GroupLensDataModel(new File("ratings.dat"));
        UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
        UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, model);
        Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        LoadEvaluator.runLoad(recommender);
    }

    public static void demo8(DataModel model) throws Exception {
        // DataModel model = new GroupLensDataModel(new File("ratings.dat"));
        RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(100, similarity, model);
                return new GenericUserBasedRecommender(model, neighborhood, similarity);
            }
        };
        double score = evaluator.evaluate(recommenderBuilder, null, model, 0.95, 0.05);
        System.out.println(score);
    }

    public static void main(String[] args) throws Exception {
         String data1 = "metadata/data/data1.csv";
         DataModel model = new FileDataModel(new File(data1));
         demo1(model);

//        String data2 = "metadata/data/u1.base";
//        DataModel model2 = new FileDataModel(new File(data2));
//        demo8(model2);

        // String data3 = "metadata/data/ua.base";
        // DataModel model3 = new
        // GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(new
        // FileDataModel(new File(data3))));
        // demo6(model3);
    }

}
