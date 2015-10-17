package au.edu.unsw.soacourse.ors;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
		public void newJob(Review review) throws IOException {
			ReviewsDAOImpl reviewDAO = new ReviewsDAOImpl();
			reviewDAO.createReview(review);
		}
		
		@GET
		@Produces(MediaType.APPLICATION_JSON)
		public List<Review> getAllJobs() throws IOException {
			ReviewsDAOImpl reviewDAO = new ReviewsDAOImpl();
			
			List<Review> reviewList = reviewDAO.getAllReviews();
			return reviewList;
		}
}
