package mia.recommender.ch05;

import java.io.IOException;

import mia.recommender.Init;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;

class LibimsetiEvalRunner {

    //all: 0.8855421686746989
    //50000: 1.7777777777777777

    private LibimsetiEvalRunner() {
    }

    public static void main(String[] args) throws Exception {
        DataModel model = Init.getRatings();
        RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
        RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    return new LibimsetiRecommender(model);
                } catch (IOException ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        Recommender r = recommenderBuilder.buildRecommender(model);
        r.recommend(26, 1);
        
        double score = evaluator.evaluate(recommenderBuilder, null, model, 0.95, 0.1);
        System.out.println(score);
    }

}
