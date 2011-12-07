package org.conan.mahout.demo2;

import java.io.File;
import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MainRun {

    public static void main(String[] args) throws Exception {
        Similarity1(getSimple());
        System.out.println("\n\n");
        Similarity2(getSimple());
        System.out.println("\n\n");
        Similarity3(getSimple());
        System.out.println("\n\n");
        Similarity4(getSimple());
        System.out.println("\n\n");
        Similarity5(getSimple());
        System.out.println("\n\n");
       
    }
    
    public static void Similarity1(DataModel m) throws TasteException {
        UserSimilarity s = new PearsonCorrelationSimilarity(m);
        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n.getUserNeighborhood(1));
        Recommender recommender = new GenericUserBasedRecommender(m, n, s);
        recommender.recommend(1, 2);
    }
    
    public static void Similarity2(DataModel m) throws TasteException {
        UserSimilarity s = new EuclideanDistanceSimilarity(m);
        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n.getUserNeighborhood(1));
        Recommender recommender = new GenericUserBasedRecommender(m, n, s);
        recommender.recommend(1, 2);
    }
    
    public static void Similarity3(DataModel m) throws TasteException {
        UserSimilarity s = new UncenteredCosineSimilarity(m);
        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n.getUserNeighborhood(1));
        Recommender recommender = new GenericUserBasedRecommender(m, n, s);
        recommender.recommend(1, 2);
    }
    
    public static void Similarity4(DataModel m) throws TasteException {
        UserSimilarity s = new LogLikelihoodSimilarity(m);
        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n.getUserNeighborhood(1));
        Recommender recommender = new GenericUserBasedRecommender(m, n, s);
        recommender.recommend(1, 2);
    }
    
    public static void Similarity5(DataModel m) throws TasteException {
        UserSimilarity s = new TanimotoCoefficientSimilarity(m);
        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n.getUserNeighborhood(1));
        Recommender recommender = new GenericUserBasedRecommender(m, n, s);
        recommender.recommend(1, 2);
    }
    
    

    
    //===============================================
    public static void NeighborhoodTest() throws TasteException, IOException{
        Neighborhood1(getSimple());
        System.out.println("\n\n");
        Neighborhood2(getSimple());
        System.out.println("\n\n");
    }
    
    public static void Neighborhood1(DataModel m) throws TasteException {
        UserSimilarity s = new PearsonCorrelationSimilarity(m);
        UserNeighborhood n = new NearestNUserNeighborhood(2, s, m);
        displayNeighborhood(n.getUserNeighborhood(1));
        Recommender recommender = new GenericUserBasedRecommender(m, n, s);
        recommender.recommend(1, 2);
    }

    public static void Neighborhood2(DataModel m) throws TasteException {
        UserSimilarity s = new PearsonCorrelationSimilarity(m);
        UserNeighborhood n = new ThresholdUserNeighborhood(0.9, s, m);
        displayNeighborhood(n.getUserNeighborhood(1));
        Recommender recommender = new GenericUserBasedRecommender(m, n, s);
        recommender.recommend(1, 2);
    }

    // ==================================================
    public static DataModel getSimple() throws IOException {
        DataModel model = new FileDataModel(new File("metadata/data/ch02.csv"));
        return model;
    }

    public static DataModel getRatings() throws IOException {
        DataModel model = new FileDataModel(new File("metadata/data/libimseti/ratings_u50000.dat"));
        return model;
    }

    // ==================================================
    public static void displayNeighborhood(long[] userids) {
        System.out.print("Show Neighborhoods:");
        for (long u : userids) {
            System.out.print(u + ",");
        }
        System.out.println();

    }

}
