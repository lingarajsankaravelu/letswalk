package lingaraj.hourglass.in.letswalk.rest.di;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import lingaraj.hourglass.in.letswalk.rest.DirectionAPIEndpoints;
import retrofit2.Retrofit;

@Module
public abstract class RestAPISModule {


  @Provides @Singleton
  static DirectionAPIEndpoints providesDirectionsAPIEndpoints(Retrofit retrofit) {
    return retrofit.create(DirectionAPIEndpoints.class);
  }


}
