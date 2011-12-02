package org.conan.mahout.demo;

import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
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

    public static void showItems(List<RecommendedItem> recommendations) {
        int i = 0;
        for (RecommendedItem recommendation : recommendations) {
            System.out.println("推荐结果: (" + (++i) + ") " + recommendation);
        }
    }

}
