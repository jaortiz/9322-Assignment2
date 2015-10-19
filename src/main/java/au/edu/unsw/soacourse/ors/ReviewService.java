package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import au.edu.unsw.soacourse.ors.dao.support.*;
import au.edu.unsw.soacourse.ors.model.*;

@Path("/reviews")
public class ReviewService {

	// Allows to insert contextual objects into the class, 
		// e.g. ServletContext, Request, Response, UriInfo
		@Context
		UriInfo uriInfo;
		@Context
		Request request;
		
		@GET
		@Path("setUpDatabase")
		public void setUpDatabase() {
			ReviewsDAOImpl reviewDAO = new ReviewsDAOImpl();
			reviewDAO.setUpDatabase();
		}
		
		@POST
		@Path("createReview")
	    @Consumes("application/json")
		public String newJob(@HeaderParam("ShortKey") String shortkey, Review review) throws IOException {
			RegisteredUsersDAOImpl userDAO = new RegisteredUsersDAOImpl();
			RegisteredUser user = userDAO.getUsersbyShortKey(shortkey);
			if (user != null && user.getRole().equals("reviewer")) {
				ReviewsDAOImpl reviewDAO = new ReviewsDAOImpl();
				int reviewId = reviewDAO.createReview(review);
				return "The created review is available at: " 
				+ uriInfo.getBaseUri().toASCIIString()
				+ "reviews/" + reviewId;
			} else {
				return "Must be a Reviewer to create a review";
			}			
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public List<Review> getAllReviews() throws IOException {
			ReviewsDAOImpl reviewDAO = new ReviewsDAOImpl();
			
			List<Review> reviewList = reviewDAO.getAllReviews();
			return reviewList;
		}
		
		@GET
		@Path("{reviewID}")
		@Produces(MediaType.APPLICATION_JSON)
		public Review getReviewById(@PathParam("reviewID") String idString) throws IOException {
			int reviewID = Integer.parseInt(idString);
			
			ReviewsDAOImpl reviewDAO = new ReviewsDAOImpl();
			Review review = reviewDAO.getReviewByID(reviewID);
			
			return review;
		}
}
