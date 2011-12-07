package mia.recommender.ch05;

import mia.recommender.Init;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

class LibimsetiIREvalRunner {

    private LibimsetiIREvalRunner() {
    }

    public static void main(String[] args) throws Exception {
        DataModel model = Init.getRatings();
        model = new GenericBooleanPrefDataModel(GenericBooleanPrefDataModel.toDataMap(model));
        RecommenderIRStatsEvaluator evaluator = new GenericRecommenderIRStatsEvaluator();
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                UserSimilarity similarity = new EuclideanDistanceSimilarity(model);//new TanimotoCoefficientSimilarity(model);
                UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);
                return new GenericBooleanPrefUserBasedRecommender(model, neighborhood, similarity);
            }
        };
        IRStatistics stats = evaluator.evaluate(recommenderBuilder, null, model, null, 10, Double.NaN, 0.1);
        System.out.println(stats);
    }

}