package usecase.recommand;

import presenter.center.dashboard.AttendeeDashboardPresent;
import usecase.event.EventManage;
import usecase.friends.manager.FriendsManage;
import usecase.people.PeopleManage;
import usecase.recommand.facade.CollaborativeFiltering;
import usecase.score.ScoreManage;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class consists exclusively of methods that operate on or return recommended
 * events or people based on users' satisfaction level.
 *
 * It contains polymorphic algorithms that operate on recommend.
 * Collaborative filtering, which is a family of algorithms,
 * in which there are multiple ways to find similar users or items,
 * and multiple methods to calculate ratings based on similar users' ratings.
 *
 * Based on the choices you make, you will eventually get a collaborative filtering method.
 *
 * It shows a simulator that allows users to evaluate
 * the algorithm of the recommendation system.
 *
 * The simulator is composed of agents,
 * events, recommenders, controllers and organizers, it can locate agents and distribute
 * events according to the small world network.
 *
 * The agents plays the role of the user in the recommendation system,
 * and the recommender also plays the role in the system.
 *
 * The controller processes the simulation process, in which:
 *
 * (1) the recommender recommends events and people to the agents according
 * to the recommendation algorithm;
 *
 * (2) each user evaluates the events and people according to the scoring algorithm of the agent and
 * using the attributes of each event and agent; and
 *
 * (3) the organizers obtain ratings and evaluation measurements related to
 * recommendations, which are related to information such as accuracy and recall.
 *
 *
 * @see CollaborativeFiltering
 * @see RecommendManage
 * @see EventManage
 * @see PeopleManage
 *
 *
 */

public interface RecommendManage extends Serializable {
	
	void initialize(EventManage em, PeopleManage pm, ScoreManage sm, FriendsManage fm);
	
	/**
	 * Return a list of person that has the most similar preference as person.
	 *
	 * Postcondition, the friends recommend to p:
	 * - p itself must be excluded
	 * - all person returned must not already be friend with p.
	 *
	 * @param p the person to recommend friend with.
	 */
	void updateRecommendFriends(UUID p, AttendeeDashboardPresent presenter);

	/**
	 * Return a list of events that has the most similar preference as person.
	 *
	 * Postcondition, the event recommend to p:
	 * - all events returned must not be the event that p signed up.
	 *
	 * @param p the person to recommend event with.
	 */
	void updateRecommendEvents(UUID p, AttendeeDashboardPresent presenter);
}
