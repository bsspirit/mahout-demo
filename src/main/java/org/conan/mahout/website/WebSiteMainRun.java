package org.conan.mahout.website;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.impl.eval.RMSRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.MemoryDiffStorage;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.slopeone.DiffStorage;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;
import org.apache.mahout.common.iterator.FileLineIterable;

public class WebSiteMainRun {
    public static void main(String[] args) throws Exception {
        initUser();
        initWebsite();

        int count = 1;
        for (long i = 1; i <= count; i++) {
            System.out.println("User " + i + "(" + user.get(new Long(i)) + ") Recommendation");
            System.out.println("\n==============User Similarity==============");
            userSimilarity1(i);
            userSimilarity2(i);
            userSimilarity3(i);
            userSimilarity4(i);
            userSimilarity5(i);
            userSimilarity6(i);
            userSimilarity7(i);

            System.out.println("\n==============Item Similarity==============");
            itemSimilarity1(i);
            itemSimilarity2(i);
            itemSimilarity3(i);
            itemSimilarity4(i);
            itemSimilarity5(i);
            itemSimilarity6(i);
            itemSimilarity7(i);

            System.out.println("\n==============Recommender Slope One==============");
            itemSlope(i);
            slopeOneEvaluate();

            System.out.println("\n==============Evaluate GenericUserBasedRecommender ==============(Lower is better.)");
            userEvaluate1();
            userEvaluate2();
            userEvaluate3();
            userEvaluate4();
            userEvaluate5();
            userEvaluate6();
            userEvaluate7();

            System.out.println("\n==============Evaluate GenericItemBasedRecommender ==============(Score: Lower is better.)");
            itemEvaluate1();
            itemEvaluate2();
            itemEvaluate3();
            itemEvaluate4();
            itemEvaluate5();
            itemEvaluate6();
            itemEvaluate7();

            System.out.println("\n\n");
        }
    }

    public static void userSimilarity1(long userid) throws Exception {
        System.out.println("EuclideanDistanceSimilarity");
        DataModel m = getData();
        UserSimilarity s = new EuclideanDistanceSimilarity(m);
        displayUserSimilarity(s, userid);

        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n, userid);

        recommenderUser(m, n, s, userid);
        System.out.println("\n\n");
    }

    public static void userSimilarity2(long userid) throws Exception {
        System.out.println("PearsonCorrelationSimilarity");
        DataModel m = getData();
        UserSimilarity s = new PearsonCorrelationSimilarity(m);
        displayUserSimilarity(s, userid);

        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n, userid);

        recommenderUser(m, n, s, userid);
        System.out.println("\n\n");
    }

    public static void userSimilarity3(long userid) throws Exception {
        System.out.println("UncenteredCosineSimilarity");
        DataModel m = getData();
        UserSimilarity s = new UncenteredCosineSimilarity(m);
        displayUserSimilarity(s, userid);

        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n, userid);

        recommenderUser(m, n, s, userid);
        System.out.println("\n\n");
    }

    public static void userSimilarity4(long userid) throws Exception {
        System.out.println("LogLikelihoodSimilarity");
        DataModel m = getData();
        UserSimilarity s = new LogLikelihoodSimilarity(m);
        displayUserSimilarity(s, userid);

        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n, userid);

        recommenderUser(m, n, s, userid);
        System.out.println("\n\n");
    }

    public static void userSimilarity5(long userid) throws Exception {
        System.out.println("TanimotoCoefficientSimilarity");
        DataModel m = getData();
        UserSimilarity s = new TanimotoCoefficientSimilarity(m);
        displayUserSimilarity(s, userid);

        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n, userid);

        recommenderUser(m, n, s, userid);
        System.out.println("\n\n");
    }

    public static void userSimilarity6(long userid) throws Exception {
        System.out.println("CityBlockSimilarity");
        DataModel m = getData();
        UserSimilarity s = new CityBlockSimilarity(m);
        displayUserSimilarity(s, userid);

        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n, userid);

        recommenderUser(m, n, s, userid);
        System.out.println("\n\n");
    }

    public static void userSimilarity7(long userid) throws Exception {
        // System.out.println("HybridSimilarity");
        // DataModel m = getData();
        // UserSimilarity s = new HybridSimilarity(m);
        // displayUserSimilarity(s, userid);
        //
        // UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        // displayNeighborhood(n, userid);
        //
        // recommenderUser(m, n, s, userid);
        // System.out.println("\n\n");
    }

    public static void itemSimilarity1(long userid) throws Exception {
        System.out.println("EuclideanDistanceSimilarity");
        DataModel m = getData();
        ItemSimilarity s = new EuclideanDistanceSimilarity(m);
        displayItemSimilarity(s);

        recommenderItem(m, s, userid);
        System.out.println("\n\n");
    }

    public static void itemSimilarity2(long userid) throws Exception {
        System.out.println("PearsonCorrelationSimilarity");
        DataModel m = getData();
        ItemSimilarity s = new PearsonCorrelationSimilarity(m);
        displayItemSimilarity(s);

        recommenderItem(m, s, userid);
        System.out.println("\n\n");
    }

    public static void itemSimilarity3(long userid) throws Exception {
        System.out.println("UncenteredCosineSimilarity");
        DataModel m = getData();
        ItemSimilarity s = new UncenteredCosineSimilarity(m);
        displayItemSimilarity(s);

        recommenderItem(m, s, userid);
        System.out.println("\n\n");
    }

    public static void itemSimilarity4(long userid) throws Exception {
        System.out.println("LogLikelihoodSimilarity");
        DataModel m = getData();
        ItemSimilarity s = new LogLikelihoodSimilarity(m);
        displayItemSimilarity(s);

        recommenderItem(m, s, userid);
        System.out.println("\n\n");
    }

    public static void itemSimilarity5(long userid) throws Exception {
        System.out.println("TanimotoCoefficientSimilarity");
        DataModel m = getData();
        ItemSimilarity s = new TanimotoCoefficientSimilarity(m);
        displayItemSimilarity(s);

        recommenderItem(m, s, userid);
        System.out.println("\n\n");
    }

    public static void itemSimilarity6(long userid) throws Exception {
        System.out.println("CityBlockSimilarity");
        DataModel m = getData();
        ItemSimilarity s = new CityBlockSimilarity(m);
        displayItemSimilarity(s);

        recommenderItem(m, s, userid);
        System.out.println("\n\n");
    }

    public static void itemSimilarity7(long userid) throws Exception {
        // System.out.println("HybridSimilarity");
        // DataModel m = getData();
        // ItemSimilarity s = new HybridSimilarity(m);
        // displayItemSimilarity(s);
        //
        // recommenderItem(m, s, userid);
        // System.out.println("\n\n");
    }

    public static void itemSlope(long userid) throws Exception {
        System.out.println("recommenderSlopeOne");
        DataModel m = getData();
        recommenderSlopeOne(m, userid);
        System.out.println("\n\n");

    }

    public static void recommenderUser(DataModel m, UserNeighborhood n, UserSimilarity s, long userid) throws Exception {
        Recommender recommender = new GenericUserBasedRecommender(m, n, s);
        List<RecommendedItem> list = recommender.recommend(userid, 2);
        displayItems(list);
    }

    public static void recommenderItem(DataModel m, ItemSimilarity s, long userid) throws Exception {
        Recommender recommender = new GenericItemBasedRecommender(m, s);
        List<RecommendedItem> list = recommender.recommend(userid, 2);
        displayItems(list);
    }

    public static void recommenderSlopeOne(DataModel m, long userid) throws Exception {
        System.out.println("Not Weight");
        Recommender recommender = new SlopeOneRecommender(m);
        List<RecommendedItem> list = recommender.recommend(userid, 2);
        displayItems(list);

        System.out.println("\nWeight: 10000000L");
        DiffStorage diffStorage = new MemoryDiffStorage(m, Weighting.WEIGHTED, 10000000L);
        Recommender recommender1 = new SlopeOneRecommender(m, Weighting.WEIGHTED, Weighting.WEIGHTED, diffStorage);
        List<RecommendedItem> list1 = recommender1.recommend(userid, 2);
        displayItems(list1);
    }

    // =================================================
    public static void userEvaluate1() throws Exception {
        System.out.println("EuclideanDistanceSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    UserSimilarity s = new EuclideanDistanceSimilarity(model);
                    UserNeighborhood n = new NearestNUserNeighborhood(2, s, model);
                    return new GenericUserBasedRecommender(model, n, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);

    }

    public static void userEvaluate2() throws Exception {
        System.out.println("PearsonCorrelationSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    UserSimilarity s = new PearsonCorrelationSimilarity(model);
                    UserNeighborhood n = new NearestNUserNeighborhood(2, s, model);
                    return new GenericUserBasedRecommender(model, n, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void userEvaluate3() throws Exception {
        System.out.println("UncenteredCosineSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    UserSimilarity s = new UncenteredCosineSimilarity(model);
                    UserNeighborhood n = new NearestNUserNeighborhood(2, s, model);
                    return new GenericUserBasedRecommender(model, n, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void userEvaluate4() throws Exception {
        System.out.println("LogLikelihoodSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    UserSimilarity s = new LogLikelihoodSimilarity(model);
                    UserNeighborhood n = new NearestNUserNeighborhood(2, s, model);
                    return new GenericUserBasedRecommender(model, n, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void userEvaluate5() throws Exception {
        System.out.println("TanimotoCoefficientSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    UserSimilarity s = new TanimotoCoefficientSimilarity(model);
                    UserNeighborhood n = new NearestNUserNeighborhood(2, s, model);
                    return new GenericUserBasedRecommender(model, n, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void userEvaluate6() throws Exception {
        System.out.println("CityBlockSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    UserSimilarity s = new CityBlockSimilarity(model);
                    UserNeighborhood n = new NearestNUserNeighborhood(2, s, model);
                    return new GenericUserBasedRecommender(model, n, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void userEvaluate7() throws Exception {
        // System.out.println("HybridSimilarity");
        // RandomUtils.useTestSeed();
        // DataModel m = getData();
        // RecommenderEvaluator e = new RMSRecommenderEvaluator();
        // RecommenderBuilder rb = new RecommenderBuilder() {
        // @Override
        // public Recommender buildRecommender(DataModel model) throws
        // TasteException {
        // try {
        // UserSimilarity s = new HybridSimilarity(model);
        // UserNeighborhood n = new NearestNUserNeighborhood(2, s, model);
        // return new GenericUserBasedRecommender(model, n, s);
        // } catch (Exception ioe) {
        // throw new TasteException(ioe);
        // }
        // }
        // };
        // displayEvaluate(e, rb, m);
        // displayEvaluateStat(rb, m);
    }

    public static void itemEvaluate1() throws Exception {
        System.out.println("EuclideanDistanceSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    ItemSimilarity s = new EuclideanDistanceSimilarity(model);
                    return new GenericItemBasedRecommender(model, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void itemEvaluate2() throws Exception {
        System.out.println("PearsonCorrelationSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    ItemSimilarity s = new PearsonCorrelationSimilarity(model);
                    return new GenericItemBasedRecommender(model, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void itemEvaluate3() throws Exception {
        System.out.println("UncenteredCosineSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    ItemSimilarity s = new UncenteredCosineSimilarity(model);
                    return new GenericItemBasedRecommender(model, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void itemEvaluate4() throws Exception {
        System.out.println("LogLikelihoodSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    ItemSimilarity s = new LogLikelihoodSimilarity(model);
                    return new GenericItemBasedRecommender(model, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void itemEvaluate5() throws Exception {
        System.out.println("TanimotoCoefficientSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    ItemSimilarity s = new TanimotoCoefficientSimilarity(model);
                    return new GenericItemBasedRecommender(model, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void itemEvaluate6() throws Exception {
        System.out.println("CityBlockSimilarity");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    ItemSimilarity s = new CityBlockSimilarity(model);
                    return new GenericItemBasedRecommender(model, s);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    public static void itemEvaluate7() throws Exception {
        // System.out.println("HybridSimilarity");
        // RandomUtils.useTestSeed();
        // DataModel m = getData();
        // RecommenderEvaluator e = new RMSRecommenderEvaluator();
        // RecommenderBuilder rb = new RecommenderBuilder() {
        // @Override
        // public Recommender buildRecommender(DataModel model) throws
        // TasteException {
        // try {
        // ItemSimilarity s = new HybridSimilarity(model);
        // return new GenericItemBasedRecommender(model, s);
        // } catch (Exception ioe) {
        // throw new TasteException(ioe);
        // }
        // }
        // };
        // displayEvaluate(e, rb, m);
        // displayEvaluateStat(rb, m);
    }

    public static void slopeOneEvaluate() throws Exception {
        System.out.println("SlopeOneRecommender");
        RandomUtils.useTestSeed();
        DataModel m = getData();
        RecommenderEvaluator e = new RMSRecommenderEvaluator();
        RecommenderBuilder rb = new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                try {
                    return new SlopeOneRecommender(model);
                } catch (Exception ioe) {
                    throw new TasteException(ioe);
                }
            }
        };
        displayEvaluate(e, rb, m);
        displayEvaluateStat(rb, m);
    }

    // ========================================================================
    public static DataModel getData() throws IOException {
        DataModel model = new FileDataModel(new File("metadata/data/website/data.dat"));
        return model;
    }

    // ==================================================================
    static Map<Long, String> user = new HashMap<Long, String>();

    public static void initUser() throws IOException {
        if (user.isEmpty()) {
            File f = new File("metadata/data/website/user.csv");
            for (String line : new FileLineIterable(f)) {
                String[] l = line.split(",");
                user.put(Long.parseLong(l[0]), l[1]);
            }
        }
    }

    static Map<Long, String> website = new HashMap<Long, String>();

    public static void initWebsite() throws IOException {
        if (website.isEmpty()) {
            File f = new File("metadata/data/website/website.csv");
            for (String line : new FileLineIterable(f)) {
                String[] l = line.split(",");
                website.put(Long.parseLong(l[0]), l[1]);
            }
        }
    }

    // ============================
    public static void displayUserSimilarity(UserSimilarity s, long userid) throws TasteException {
        System.out.println("Show user　" + userid + " Similarity:");
        long len = 9;
        for (long i = 1; i <= len; i++) {
            try {
                double d = s.userSimilarity(userid, i);
                System.out.println(userid + "," + i + ": " + d);
            } catch (Exception ex) {
                System.out.println(i + ": e");
            }
        }
        System.out.println();
    }

    public static void displayItemSimilarity(ItemSimilarity s) throws TasteException {
        System.out.println("Show item Similarity :");
        long len = 15;
        for (int i = 1; i <= len; i++) {
            for (int j = i; j <= len; j++) {
                try {
                    double d = s.itemSimilarity(i, j);
                    System.out.println(i + "," + j + ": " + d);
                } catch (Exception ex) {
                    System.out.println(i + "," + j + ": e");
                }
            }
        }
        System.out.println();
    }

    public static void displaySimilarity(UserSimilarity s) throws TasteException {
        System.out.println("Show Similarity:");
        long len = 9;
        for (long i = 1; i <= len; i++) {
            for (long j = i; j <= len; j++) {
                double d = s.userSimilarity(i, j);
                System.out.println(i + "," + j + ": " + d);
            }
        }
        System.out.println();

    }

    public static void displayNeighborhood(UserNeighborhood n, long userid) throws TasteException {
        System.out.print("Show user " + userid + " Neighborhoods(TOP 2):");
        long[] userids = n.getUserNeighborhood(userid);
        for (long u : userids) {
            System.out.print(u + ",");
        }
        System.out.println();
    }

    public static void displayItems(List<RecommendedItem> recommendations) throws TasteException, IOException {
        int i = 0;
        for (RecommendedItem recommendation : recommendations) {
            long itemid = recommendation.getItemID();
            float val = recommendation.getValue();
            System.out.println("Result: (" + (++i) + ") " + itemid + ":" + website.get(new Long(itemid)) + "," + val);
        }
        if (i == 0) {
            System.out.println("No Recommendation Result.");
        }
    }

    public static void displayEvaluate(RecommenderEvaluator e, RecommenderBuilder rb, DataModel m) throws TasteException {
        double score = e.evaluate(rb, null, m, 0.5, 1);
        System.out.println("score:" + score);
    }

    public static void displayEvaluateStat(RecommenderBuilder rb, DataModel m) throws TasteException {
        RecommenderIRStatsEvaluator se = new GenericRecommenderIRStatsEvaluator();
        IRStatistics stats = se.evaluate(rb, null, m, null, 2, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1);
        System.out.println(stats + "\n");
    }

}
