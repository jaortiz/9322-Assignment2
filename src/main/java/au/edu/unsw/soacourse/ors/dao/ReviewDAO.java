package au.edu.unsw.soacourse.ors.dao;

import java.util.List;

import au.edu.unsw.soacourse.ors.model.*;

public interface ReviewDAO {

	/**
	 * Creates a review and return a URI to the new resource
	 * 
	 * @return
	 */
	int createReview(Review review);
	
	
	/**
	 * Gets a review by its rID
	 * 
	 * @param rID
	 * @return
	 */
	Review getReviewByID(int rID);
	
	
	/**
	 * Gets a list of all reviews
	 * 
	 * @return
	 */
	List<Review> getAllReviews();


	Review getReviewByAppID(int appID);
	
	
}
