package org.conan.mahout.website;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.iterator.FileLineIterable;

public class WebSiteMainRun {
    public static void main(String[] args) throws Exception {
        initUser();
        initWebsite();

        for (long i = 1; i <= 9; i++) {
            System.out.println("User " + i + "(" + user.get(new Long(i)) + ") Recommendation");
            similarity1(i);
            // similarity2(i);
            // similarity3(i);
            System.out.println("\n\n");
        }
    }

    public static void similarity1(long userid) throws Exception {
        System.out.println("EuclideanDistanceSimilarity");
        DataModel m = getData();
        UserSimilarity s = new EuclideanDistanceSimilarity(m);
        displaySimilarity(s, userid);

        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n, userid);

        recommender(m, n, s, userid);
        System.out.println("\n\n");
    }

    public static void similarity2(long userid) throws Exception {
        System.out.println("PearsonCorrelationSimilarity");
        DataModel m = getData();
        UserSimilarity s = new PearsonCorrelationSimilarity(m);
        displaySimilarity(s, userid);

        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n, userid);

        recommender(m, n, s, userid);
        System.out.println("\n\n");
    }

    public static void similarity3(long userid) throws Exception {
        System.out.println("UncenteredCosineSimilarity");
        DataModel m = getData();
        UserSimilarity s = new UncenteredCosineSimilarity(m);
        displaySimilarity(s, userid);

        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n, userid);

        recommender(m, n, s, userid);
        System.out.println("\n\n");
    }

    public static void recommender(DataModel m, UserNeighborhood n, UserSimilarity s, long userid) throws Exception {
        Recommender recommender = new GenericUserBasedRecommender(m, n, s);
        List<RecommendedItem> list = recommender.recommend(userid, 2);
        displayItems(list);

    }

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
    public static void displaySimilarity(UserSimilarity s, long userid) throws TasteException {
        System.out.println("Show user " + userid + " Similarity:");
        long len = 9;
        for (long i = 1; i <= len; i++) {
            double d = (s.userSimilarity(userid, i) + 1) / 2;
            System.out.println(userid + "," + i + ": " + d);
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
            System.out.println("Result: (" + (++i) + ") " + itemid + ":" + website.get(new Long(itemid)));
        }
        if (i == 0) {
            System.out.println("No Recommendation Result.");
        }
    }

}
