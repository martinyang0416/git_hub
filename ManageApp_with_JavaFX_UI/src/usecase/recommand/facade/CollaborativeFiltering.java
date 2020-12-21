package usecase.recommand.facade;

import usecase.event.EventManage;
import usecase.event.facade.RecurringEventCheck;
import usecase.people.PeopleManage;
import usecase.score.ScoreManage;

import java.io.Serializable;
import java.util.*;

/**
 * People usually get the best advice from people with similar tastes.
 *
 * Collaborative filtering includes technologies that match people with
 * similar interests and make suggestions on this basis.
 *
 * This class provides a benefit for attended information and wakens the
 * processing of unattended information.
 *
 * The CollaborativeFilter is "leaky". Instead of letting single event and person
 * pass through the filter and eventually to the recommendation system, it
 * will adjust the signal so that the events and people that best suit users'
 * preferences will have higher signal.
 *
 * In other words, dictionary units are added to the events and people so
 * that information can have different threshold levels.
 *
 * The dictionary units identify the signal with the highest signal.
 *
 * If the incoming activation of the event or person is high enough
 * to pass the threshold in the dictionary unit, the event or person
 * will be processed and eventually be recommended to the user.
 *
 * The more relevant information has a lower threshold level
 * in the dictionary unit and the less relevant
 * information has a higher threshold level in the dictionary unit.
 *
 * Those that have been liked or commented can have a lower threshold level
 * to make them easier to pass the filter.
 *
 * Although less attended information will gradually have lower signal and
 * activation and higher threshold level in the dictionary unit, there are still
 * chances left for them to be recommended.
 *
 * Even a weak incoming signal (e.g. one we didn't pay attention to or didn't
 * stimulate our interest at first glance) could still be processed
 * by the dictionary unit because only a small signal is needed for
 * the CollaborativeFilter.
 *
 * @see EventRecommendConvert
 * @see PeopleRecommendConvert
 * @see SimilarityCalculate
 * @see usecase.recommand.RecommendManage
 * @see usecase.recommand.RecommendManager
 *
 *
 */
public class CollaborativeFiltering implements Serializable{
    private final EventManage em;
    private final PeopleManage pm;
    private final ScoreManage sm;
    private final SimilarityCalculate sc;
    private final RecurringEventCheck rec;
    public  final double BENCHMARK = 3;

    public CollaborativeFiltering(EventManage em, PeopleManage pm, ScoreManage sm) {
        this.em = em;
        this.pm = pm;
        this.sm = sm;
        this.sc = new SimilarityCalculate(em, pm, sm);
        this.rec = new RecurringEventCheck(em);
    }

    /**
     * The method is used to normalize scores by subtracting the average of each list
     *
     * @param scores represents the scores of each event for every user
     * @return the normalized scores for each user
     */
    public Map<UUID, ArrayList<Double>> normalizeScore(Map<UUID, ArrayList<Double>> scores){
        Map<UUID, ArrayList<Double>> new_scores = new HashMap<>();

        ArrayList<Double> averages = new ArrayList<>();

        for(UUID person : scores.keySet()){
            double sum = 0;
            int count = 0;
            for(double score : scores.get(person)){
                if(score != 0){
                    count += 1;
                    sum += score;
                }
            }
            averages.add(sum / count);
        }

        int index = 0;
        for(UUID person : scores.keySet()){
            ArrayList<Double> new_score = new ArrayList<>();
            for(double score : scores.get(person)){
                if(score != 0){
                    new_score.add(score - averages.get(index));
                }
                else{
                    new_score.add(0d);
                }
            }
            new_scores.put(person, new_score);
            index ++;
        }

        return new_scores;
    }

    /**
     * The method is used to get the similarity for the person between other users
     *
     * @param scores represents the scores of each event for every user
     * @param person represents the person used to calculate the similarity with others.
     * @return the similarity with other users
     */
    public Map<UUID, Double> getSimilarity(Map<UUID, ArrayList<Double>> scores, UUID person){
        ArrayList<Double> scoreA = scores.get(person);
        Map<UUID, Double> result = new HashMap<>();

        for(UUID p : scores.keySet()){
            if(p != person) {
                ArrayList<Double> scoreB = scores.get(p);
                result.put(p, sc.calSimilarity(scoreA, scoreB));
            }
        }

        return result;
    }

    /**
     * The method is used to get the users that we would use their similarity to predict the scores.
     *
     * @param index represents the index of the event required to predict the rate
     * @param scores represents the scores of each event for every user
     * @param similarities represents the similarity with other users
     * @return the list of users
     */
    public List<UUID> getRates(int index, Map<UUID, ArrayList<Double>> scores, Map<UUID, Double> similarities){
        List<UUID> result = new ArrayList<>();
        Map<UUID, Double> copy = new HashMap<>();

        for(UUID p : similarities.keySet()){
            copy.put(p, similarities.get(p));
        }

        List<Map.Entry<UUID, Double>> list = new ArrayList<>(copy.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        for(Map.Entry<UUID, Double> m : list){
            UUID r = m.getKey();
            if(scores.get(r).get(index) != 0){
                result.add(m.getKey());
            }
        }

        if(result.size() > 2){
            return result.subList(0, 2);
        }

        return result;
    }

    /**
     * The method is used to predict rates for the events that the person did not participate before.
     *
     * @param scores the original score of each event for every user
     * @param person the person required to predict the rates
     * @param similarities the similarities between the person and other users
     * @return the predict rates of not attended events
     */
    public Map<UUID, Double> predictRates(Map<UUID, ArrayList<Double>> scores, UUID person,
                                          Map<UUID, Double> similarities){
        List<UUID> events = em.getAll();
        ArrayList<Double> p_score = scores.get(person);
        Map<UUID, Double> results = new HashMap<>();

        int index = p_score.indexOf(0.d);

        while(index != -1){
            List<UUID> rates = getRates(index, scores, similarities);
            double predict = 0;
            if(rates.size() >= 2){
                UUID p1 = rates.get(0);
                UUID p2 = rates.get(1);
                double r1 = similarities.get(p1);
                double r2 = similarities.get(p2);
                if(r1 + r2 > 1){
                    double sum = r1 + r2;
                    r1 = r1/sum;
                    r2 = r2/sum;
                }
                predict = scores.get(p1).get(index) * r1 + scores.get(p2).get(index) * r2;
            }
            else if(rates.size() == 1){
                UUID p = rates.get(0);
                double r = similarities.get(p);
                predict = scores.get(p).get(index) * r;
            }
            if(predict < 0){
                results.put(events.get(index), 0.d);
            }else if(predict > 5){
                results.put(events.get(index), 5.0);
            }else{
                results.put(events.get(index), predict);
            }
            p_score.set(index, -1.0);
            index = p_score.indexOf(0d);
        }
        return results;
    }

    /**
     * The method is used to check if the event in the recurring event list is not finished yet.
     *
     * @param recurringEvents the list of recurring events with the same title
     * @return the list of recurring events which is not start
     */
    public List<UUID> getAvailableEvents(List<UUID> recurringEvents){
        List<UUID> results = new ArrayList<>();

        for(UUID e : recurringEvents){
            if(!em.isFinished(e)){
                results.add(e);
            }
        }

        return results;
    }

    /**
     * The method is used to get the list of UUID for events which would be recommended for the person.
     *
     * @param person represents the person required recommendation of events
     * @return the list of UUID of events
     */
    public LinkedHashMap<UUID, Double> getEvent_prompt(UUID person){
        LinkedHashMap<UUID, Double> event_prompt = new LinkedHashMap<>();

        List<UUID> events = em.getAll();
        List<UUID> users = pm.getAllAttendee();

        Map<UUID, ArrayList<Double>> raw_scores = new HashMap<>();

        for(UUID p : users){
            ArrayList<Double> scores = new ArrayList<>();

            for(UUID e : events){
                double score = 0d;

                if(sm.getPreference(p).containsKey(e)){
                    score = sm.getPreference(p).get(e);
                }

                scores.add(score);
            }

            raw_scores.put(p, scores);
        }

        Map<UUID, ArrayList<Double>> nor_score = normalizeScore(raw_scores);
        Map<UUID, Double> similarities = getSimilarity(nor_score, person);

        Map<UUID, Double> predicted = predictRates(raw_scores, person, similarities);

        List<Map.Entry<UUID, Double>> list = new ArrayList<>(predicted.entrySet());
        list.sort(Collections.reverseOrder(Map.Entry.comparingByValue()));

        for(Map.Entry<UUID, Double> m : list){
            UUID e = m.getKey();
            List<UUID> availableEvents = getAvailableEvents(rec.RecurringEvents(e));
            if(m.getValue() > BENCHMARK && !em.isSignedUp(person, e) && !availableEvents.isEmpty()){
                event_prompt.put(m.getKey(), m.getValue());
            }else if(m.getValue() <= BENCHMARK){
                return event_prompt;
            }
        }

        return event_prompt;
    }
}
