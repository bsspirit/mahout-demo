package org.conan.mahout.demo;

import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.model.jdbc.AbstractJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.ClusterSimilarity;
import org.apache.mahout.cf.taste.impl.recommender.FarthestNeighborClusterSimilarity;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.TreeClusteringRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.ConjugateGradientOptimizer;
import org.apache.mahout.cf.taste.impl.recommender.knn.KnnItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.knn.Optimizer;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.MemoryDiffStorage;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.jdbc.MySQLJDBCDiffStorage;
import org.apache.mahout.cf.taste.impl.recommender.svd.ALSWRFactorizer;
import org.apache.mahout.cf.taste.impl.recommender.svd.SVDRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.slopeone.DiffStorage;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class MyRecommender {

    public static RecommenderBuilder userBuilder(final UserSimilarity us, final UserNeighborhood un) throws TasteException {
        System.out.println("推荐模型: UserRecommender");
        return new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                return new GenericUserBasedRecommender(model, un, us);
            }
        };
    }

    public static RecommenderBuilder itemBuilder(final ItemSimilarity is) throws TasteException {
        System.out.println("推荐模型: ItemRecommender");
        return new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                return new GenericItemBasedRecommender(model, is);
            }
        };
    }

    public static RecommenderBuilder svdBuilder() throws TasteException {
        System.out.println("推荐模型: SVDRecommender");
        return new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                return new SVDRecommender(model, new ALSWRFactorizer(model, 10, 0.05, 10));
            }
        };
    }

    public static RecommenderBuilder knnItemBuilder() throws TasteException {
        System.out.println("推荐模型: KnnItemBasedRecommender");
        return new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                ItemSimilarity similarity = new LogLikelihoodSimilarity(model);
                Optimizer optimizer = new ConjugateGradientOptimizer();
                return new KnnItemBasedRecommender(model, similarity, optimizer, 10);
            }
        };
    }

    public static RecommenderBuilder treeClusteringBuilder() throws TasteException {
        System.out.println("推荐模型: TreeClusteringRecommender");
        return new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                UserSimilarity similarity = new LogLikelihoodSimilarity(model);
                ClusterSimilarity clusterSimilarity = new FarthestNeighborClusterSimilarity(similarity);
                return new TreeClusteringRecommender(model, clusterSimilarity, 10);
            }
        };
    }

    public static RecommenderBuilder SlopeOneJDBCBuilder() throws TasteException {
        System.out.println("推荐模型: SlopeOneJDBCRecommender");
        return new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel dataModel) throws TasteException {
                AbstractJDBCDataModel model = new MySQLJDBCDataModel();
                DiffStorage diffStorage = new MySQLJDBCDiffStorage(model);
                return new SlopeOneRecommender(model, Weighting.WEIGHTED, Weighting.WEIGHTED, diffStorage);
            }
        };
    }

    public static RecommenderBuilder SlopeOneNoWeightingBuilder() throws TasteException {
        System.out.println("推荐模型: SlopeOneNoWeightingRecommender");
        return new RecommenderBuilder() {
            @Override
            public Recommender buildRecommender(DataModel model) throws TasteException {
                DiffStorage diffStorage = new MemoryDiffStorage(model, Weighting.UNWEIGHTED, Long.MAX_VALUE);
                return new SlopeOneRecommender(model, Weighting.UNWEIGHTED, Weighting.UNWEIGHTED, diffStorage);
            }
        };
    }

    public static void showItems(List<RecommendedItem> recommendations) {
        int i = 0;
        for (RecommendedItem recommendation : recommendations) {
            System.out.println("推荐结果: (" + (++i) + ") " + recommendation);
        }
    }

}
