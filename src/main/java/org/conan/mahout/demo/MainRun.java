package org.conan.mahout.demo;

import java.util.List;

import org.apache.mahout.cf.taste.eval.DataModelBuilder;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class MainRun {

    /**
     * 基本算法
     */
    public void demo1(DataModel m) throws Exception {
        System.out.println("==============用户,打分,平均差值(0)==============");

        // 推荐结果
        UserSimilarity s = MySimilarity.getPearsonCorrelation(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        MyRecommender.showItems(rb.buildRecommender(m).recommend(1, 1));

        // 算法评估
        RecommenderEvaluator e = new AverageAbsoluteDifferenceRecommenderEvaluator();
        getScore(m, s, n, e, rb, null);

        System.out.println();
    }

    public void demo2(DataModel m) throws Exception {
        System.out.println("==============用户,打分,均方根(0)==============");

        // 推荐结果
        UserSimilarity s = MySimilarity.getPearsonCorrelation(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        MyRecommender.showItems(rb.buildRecommender(m).recommend(1, 1));

        // 算法评估
        // RandomUtils.useTestSeed();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        getScore(m, s, n, e, rb, null);

        System.out.println();
    }

    public void prefEvaluate(DataModel m) throws Exception {
        System.out.println("==============用户,打分,评估(1)==============");

        // 推荐结果
        UserSimilarity s = MySimilarity.getPearsonCorrelation(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);

        // 算法评估
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        RecommenderIRStatsEvaluator e = new GenericRecommenderIRStatsEvaluator();
        IRStatistics stats = e.evaluate(rb, null, m, null, 1, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
        System.out.println("Precision  : " + stats.getPrecision());
        System.out.println("Recall     : " + stats.getRecall());

        System.out.println();
    }

    public void demo3(DataModel m) throws Exception {
        System.out.println("==============用户,无打分,平均差值(0)==============");

        // 推荐结果
        UserSimilarity s = new LogLikelihoodSimilarity(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        MyRecommender.showItems(rb.buildRecommender(m).recommend(1, 1));

        // 算法评估
        // RandomUtils.useTestSeed();
        RecommenderEvaluator e = new AverageAbsoluteDifferenceRecommenderEvaluator();
        DataModelBuilder db = MyDataModel.createNoPrefDataModelBuilder();
        getScore(m, s, n, e, rb, db);

        System.out.println();
    }

    public void noPrefEvaluate(DataModel m) throws Exception {
        System.out.println("==============用户,无打分,评估(1)==============");

        // 推荐结果
        UserSimilarity s = new LogLikelihoodSimilarity(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);

        // 算法评估
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        RecommenderIRStatsEvaluator e = new GenericRecommenderIRStatsEvaluator();
        DataModelBuilder db = MyDataModel.createNoPrefDataModelBuilder();
        IRStatistics stats = e.evaluate(rb, db, m, null, 1, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
        System.out.println("Precision  : " + stats.getPrecision());
        System.out.println("Recall     : " + stats.getRecall());

        System.out.println();
    }

    public void demo4(DataModel m) throws Exception {
        System.out.println("==============用户,打分,fast,平均差值(0)==============");

        // 推荐结果
        UserSimilarity s = MySimilarity.getPearsonCorrelation(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        MyRecommender.showItems(rb.buildRecommender(m).recommend(1, 1));

        // 算法评估
        // RandomUtils.useTestSeed();
        RecommenderEvaluator e = new AverageAbsoluteDifferenceRecommenderEvaluator();
        DataModelBuilder db = MyDataModel.createPrefDataModelBuilder();
        getScore(m, s, n, e, rb, db);

        System.out.println();
    }

    public static void main(String[] args) throws Exception {
        MainRun main = new MainRun();

        DataModel m1 = MyDataModel.getPrefDataByFile();
        main.demo1(m1);
        main.demo2(m1);
        main.prefEvaluate(m1);

        // DataModel m2 = MyDataModel.getNoPrefDataByFile();
        // main.demo3(m2);
        // main.noPrefEvaluate(m2);
        //
        // DataModel m3 = MyDataModel.getPrefDataByFileFast();
        // main.demo4(m3);
        //
        // DataModel m4 = MyDataModel.getDataByMysql();
        // main.demo1(m4);
        // main.demo2(m4);
        // main.prefEvaluate(m4);
    }

    public static void getRecommendations(DataModel m, UserSimilarity s, UserNeighborhood n) throws Exception {
        Recommender r = MyRecommender.userBuilder(s, n).buildRecommender(m);
        List<RecommendedItem> recommendations = r.recommend(1, 1);
        for (RecommendedItem recommendation : recommendations) {
            System.out.println("result : " + recommendation);
        }
    }

    public void getScore(DataModel m, UserSimilarity s, UserNeighborhood n, RecommenderEvaluator e, RecommenderBuilder rb, DataModelBuilder db) throws Exception {
        RandomUtils.useTestSeed();
        double score = e.evaluate(rb, db, m, 0.7, 1.0);// testing data, all data
        System.out.println("score  : " + score);
    }

}
