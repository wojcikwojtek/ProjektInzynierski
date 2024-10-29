package com.inzynierka.RatingTouristAttractions.Services;

import com.inzynierka.RatingTouristAttractions.Entities.Attraction;
import com.inzynierka.RatingTouristAttractions.Entities.Review;
import com.inzynierka.RatingTouristAttractions.Entities.User;
import com.inzynierka.RatingTouristAttractions.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AttractionRecommendationService {

    private Map<User, HashMap<Attraction, Double>> data;
    private Map<User, HashMap<Attraction, Double>> outputData;
    private Map<Attraction, Map<Attraction, Double>> differencesMatrix;
    private Map<Attraction, Map<Attraction, Integer>> frequenciesMatrix;
    private List<Attraction> attractions;

    public AttractionRecommendationService() {
        this.data = new HashMap<>();
        this.outputData = new HashMap<>();
        this.differencesMatrix = new HashMap<>();
        this.frequenciesMatrix = new HashMap<>();
        this.attractions = new ArrayList<>();
    }

    private boolean didUserReviewAttraction(User user, Attraction attraction) {
        for(Review review : user.getReviews()) {
            if(review.getAttraction().equals(attraction)) {
                return true;
            }
        }
        return false;
    }

    private void initializeData(UserRepository userRepository) {
        if(!this.data.isEmpty()) {
            this.data.clear();
        }
        List<User> users = userRepository.findAll();
        for (User user : users) {
            HashMap<Attraction, Double> map = new HashMap<>();
            for(Review review : user.getReviews()) {
                Attraction attraction = review.getAttraction();
                if(!attractions.contains(attraction)) {
                    attractions.add(attraction);
                }
                map.put(attraction, review.getRating());
            }
            this.data.put(user, map);
        }
    }

    private void buildMatrices() {
        for(HashMap<Attraction, Double> user : data.values()) {
            for(Entry<Attraction, Double> entry : user.entrySet()) {
                if (!differencesMatrix.containsKey(entry.getKey())) {
                    differencesMatrix.put(entry.getKey(), new HashMap<Attraction, Double>());
                    frequenciesMatrix.put(entry.getKey(), new HashMap<Attraction, Integer>());
                }
                for (Entry<Attraction, Double> entry2 : user.entrySet()) {
                    int count = 0;
                    if (frequenciesMatrix.get(entry.getKey()).containsKey(entry2.getKey())) {
                        count = frequenciesMatrix.get(entry.getKey()).get(entry2.getKey());
                    }
                    double oldDiff = 0.0;
                    if (differencesMatrix.get(entry.getKey()).containsKey(entry2.getKey())) {
                        oldDiff = differencesMatrix.get(entry.getKey()).get(entry2.getKey());
                    }
                    double observedDiff = entry.getValue() - entry2.getValue();
                    frequenciesMatrix.get(entry.getKey()).put(entry2.getKey(), count + 1);
                    differencesMatrix.get(entry.getKey()).put(entry2.getKey(), oldDiff + observedDiff);
                }
            }
        }
        for(Attraction attraction : differencesMatrix.keySet()) {
            for(Attraction attraction2 : differencesMatrix.get(attraction).keySet()) {
                double oldValue = differencesMatrix.get(attraction).get(attraction2);
                int count = frequenciesMatrix.get(attraction).get(attraction2);
                differencesMatrix.get(attraction).put(attraction2, oldValue / count);
            }
        }
    }

    private void predict() {
        HashMap<Attraction, Double> uPred = new HashMap<>();
        HashMap<Attraction, Integer> uFreq = new HashMap<>();
        for (Attraction attraction : differencesMatrix.keySet()) {
            uFreq.put(attraction, 0);
            uPred.put(attraction, 0.0);
        }
        for(Entry<User, HashMap<Attraction, Double>> entry : data.entrySet()) {
            for(Attraction attraction : entry.getValue().keySet()) {
                for(Attraction attraction2 : differencesMatrix.keySet()) {
                    try {
                        double predictedValue =
                                differencesMatrix.get(attraction2).get(attraction) + entry.getValue().get(attraction);
                        double finalValue = predictedValue + frequenciesMatrix.get(attraction2).get(attraction);
                        uPred.put(attraction2, uPred.get(attraction2) + finalValue);
                        uFreq.put(attraction2, uFreq.get(attraction2) + frequenciesMatrix.get(attraction2).get(attraction));
                    } catch (NullPointerException e) {}
                }
            }
            HashMap<Attraction, Double> clean = new HashMap<>();
            for(Attraction attraction : uPred.keySet()) {
                if(uFreq.get(attraction) > 0) {
                    clean.put(attraction, uPred.get(attraction) / uFreq.get(attraction));
                }
            }
            for(Attraction attraction : attractions) {
                if(entry.getValue().containsKey(attraction)) {
                    clean.put(attraction, entry.getValue().get(attraction));
                } else if (!clean.containsKey(attraction)) {
                    clean.put(attraction, -1.0);
                }
            }
            outputData.put(entry.getKey(), clean);
        }
    }

    public HashMap<Attraction, Double> getPredictions(User user, UserRepository userRepository) {
        this.initializeData(userRepository);
        this.buildMatrices();
        this.predict();
        for(Entry<User, HashMap<Attraction, Double>> entry : outputData.entrySet()) {
            if(entry.getKey().getUser_id() == user.getUser_id()) {
                HashMap<Attraction, Double> predictions = new HashMap<>();
                for(Attraction attraction : entry.getValue().keySet()) {
                    if(didUserReviewAttraction(user, attraction)) continue;
                    predictions.put(attraction, entry.getValue().get(attraction));
                }
                return predictions;
            }
        }
        return null;
    }
}
