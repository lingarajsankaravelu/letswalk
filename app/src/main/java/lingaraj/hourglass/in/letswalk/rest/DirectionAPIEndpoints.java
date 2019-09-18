package lingaraj.hourglass.in.letswalk.rest;

import io.reactivex.Single;
import lingaraj.hourglass.in.letswalk.rest.response.DirectionResults;
import lingaraj.hourglass.in.letswalk.rest.response.distanceresponse.DistanceResult;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionAPIEndpoints {

  @GET("/maps/api/directions/json")
  Single<DirectionResults> getJson(@Query("origin") String origin,@Query("destination") String destination,@Query("waypoints") String waypoints,@Query("mode") String mode, @Query("key") String Key);

  @GET("/maps/api/distancematrix/json")
  Single<DistanceResult> getDistance(@Query("origins") String origin,@Query("destinations") String destination,@Query("mode") String mode, @Query("key") String Key);
}
