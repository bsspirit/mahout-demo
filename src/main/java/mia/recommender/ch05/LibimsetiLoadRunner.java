package mia.recommender.ch05;

import mia.recommender.Init;

import org.apache.mahout.cf.taste.impl.eval.LoadEvaluator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;


class LibimsetiLoadRunner {

    private LibimsetiLoadRunner() {
    }

    public static void main(String[] args) throws Exception {
        DataModel model = Init.getRatings();
        Recommender rec = new LibimsetiRecommender(model);
        LoadEvaluator.runLoad(rec);
        
//        new LibimsetiRecommender();
        
    }

}