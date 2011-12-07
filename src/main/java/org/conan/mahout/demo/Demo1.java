package org.conan.mahout.demo;

import mia.recommender.Init;

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
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class Demo1 {
    
    public static void run() throws Exception{
//        DataModel m1 = MyDataModel.getPrefDataByFile();
        DataModel m1 = Init.getRatings();
        demo1(m1);
        demo2(m1);
        prefEvaluate(m1);

        // DataModel m2 = MyDataModel.getNoPrefDataByFile();
        // demo3(m2);
        // noPrefEvaluate(m2);
        //
        // DataModel m3 = MyDataModel.getPrefDataByFileFast();
        // demo4(m3);
        //
        // DataModel m4 = MyDataModel.getDataByMysql();
        // demo1(m4);
        // demo2(m4);
        // prefEvaluate(m4);
    }
    
    /**
     * 基本算法
     */
    public static void demo1(DataModel m) throws Exception {
        System.out.println("==============用户,打分,平均差值(0)==============");

        // 推荐结果
        UserSimilarity s = MySimilarity.getPearsonCorrelation(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        MyRecommender.showItems(rb.buildRecommender(m).recommend(26, 1));

        RecommenderEvaluator e = new AverageAbsoluteDifferenceRecommenderEvaluator();
        double score = e.evaluate(rb, null, m, 0.9, 1.0);// testing data, all
                                                         // data
        System.out.println("算法得分: " + score);
        System.out.println();
    }

    public static void demo2(DataModel m) throws Exception {
        System.out.println("==============用户,打分,均方根(0)==============");

        // 推荐结果
        UserSimilarity s = MySimilarity.getPearsonCorrelation(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        MyRecommender.showItems(rb.buildRecommender(m).recommend(26, 1));

        // RandomUtils.useTestSeed();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        double score = e.evaluate(rb, null, m, 0.7,0.3);// testing data, all
                                                         // data
        System.out.println("算法得分: " + score);

        System.out.println();
    }

    public static void prefEvaluate(DataModel m) throws Exception {
        System.out.println("==============用户,打分,评估(1)==============");

        // 推荐结果
        UserSimilarity s = MySimilarity.getPearsonCorrelation(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);

        // 算法评估
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        RecommenderIRStatsEvaluator e = new GenericRecommenderIRStatsEvaluator();
        IRStatistics stats = e.evaluate(rb, null, m, null, 1, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1);
        System.out.println("查准率 : " + stats.getPrecision());
        System.out.println("全查率 : " + stats.getRecall());

        System.out.println();
    }

    public static void demo3(DataModel m) throws Exception {
        System.out.println("==============用户,无打分,平均差值(0)==============");

        // 推荐结果
        UserSimilarity s = new LogLikelihoodSimilarity(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        MyRecommender.showItems(rb.buildRecommender(m).recommend(1, 1));

        // 算法得分
        // RandomUtils.useTestSeed();
        RecommenderEvaluator e = new AverageAbsoluteDifferenceRecommenderEvaluator();
        DataModelBuilder db = MyDataModel.createNoPrefDataModelBuilder();
        double score = e.evaluate(rb, db, m, 0.7, 1.0);// testing data, all data
        System.out.println("算法得分: " + score);

        System.out.println();
    }

    public static void noPrefEvaluate(DataModel m) throws Exception {
        System.out.println("==============用户,无打分,评估(1)==============");

        // 推荐结果
        UserSimilarity s = new LogLikelihoodSimilarity(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);

        // 算法评估
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        RecommenderIRStatsEvaluator e = new GenericRecommenderIRStatsEvaluator();
        DataModelBuilder db = MyDataModel.createNoPrefDataModelBuilder();
        IRStatistics stats = e.evaluate(rb, db, m, null, 1, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
        System.out.println("查准率 : " + stats.getPrecision());
        System.out.println("查全率 : " + stats.getRecall());

        System.out.println();
    }

    public static void demo4(DataModel m) throws Exception {
        System.out.println("==============用户,打分,fast,平均差值(0)==============");

        // 推荐结果
        UserSimilarity s = MySimilarity.getPearsonCorrelation(m);
        UserNeighborhood n = MyNeighborhood.getNearestN(m, s, 2);
        RecommenderBuilder rb = MyRecommender.userBuilder(s, n);
        MyRecommender.showItems(rb.buildRecommender(m).recommend(1, 1));

        // 算法得分
        // RandomUtils.useTestSeed();
        RecommenderEvaluator e = new AverageAbsoluteDifferenceRecommenderEvaluator();
        DataModelBuilder db = MyDataModel.createPrefDataModelBuilder();
        double score = e.evaluate(rb, db, m, 0.7, 1.0);// testing data, all data
        System.out.println("算法得分: " + score);

        System.out.println();
    }
}
