package lingaraj.hourglass.in.letswalk.letswalkscreen;

import android.location.Address;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.google.android.gms.maps.model.Marker;
import io.reactivex.Single;
import java.util.HashMap;
import java.util.Map;
import lingaraj.hourglass.in.letswalk.letswalkscreen.parser.PolyLineParser;
import lingaraj.hourglass.in.letswalk.rest.DirectionAPIEndpoints;
import lingaraj.hourglass.in.letswalk.rest.response.DirectionResults;
import lingaraj.hourglass.in.letswalk.rest.response.distanceresponse.DistanceResult;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LetsWalkViewModelTest {

  private LetsWalkViewModel view_model;
  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Captor
  private ArgumentCaptor<RepoCallbacks> repo_callback;



  String origin= "1.0,2.0";
   String destination = "10.0,11.9";
   String way_points="20.13,30.12";
   String mode = "walk";
   double latitude = 20.9999;
   double longitude = 35.0875;
   @Mock DirectionAPIEndpoints endpoints;
   @Mock Repository repository;
   @Mock PolyLineParser polyLineParser;
   @Mock private Map<Integer, Marker> marker_map = new HashMap<>();
   @Mock Address address;
   private String api_key = "api_key";
  @Mock DistanceResult distance_result_response;
  @Mock DirectionResults direction_result_response;


  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    view_model = new LetsWalkViewModel(repository);
    distance_result_response = new DistanceResult();
  }


  @Test
  public void testRetrofit(){
    when(endpoints.getDistance(origin,destination,mode,api_key)).thenReturn(Single.just(distance_result_response));
    when(endpoints.getJson(origin,destination,way_points,mode,api_key)).thenReturn(Single.just(direction_result_response));
  }


  @Test
  public void testDrawRoute(){
    view_model.drawRoute(marker_map,api_key);
    setUpDirectionsCallback();
    repo_callback.getValue().onSuccess(polyLineParser.getPolyLines());
    assertThat(view_model.getPoly_lines().getValue(),is(polyLineParser.getPolyLines()));
    assertThat(view_model.getNetwork_loader().getValue(),is(false));
  }

  private void setUpDirectionsCallback(){
    verify(repository).parseRouteInformation(eq(marker_map),eq(api_key),repo_callback.capture());
  }



}