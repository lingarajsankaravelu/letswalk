package lingaraj.hourglass.in.letswalk.di.modules;

import android.location.Geocoder;
import dagger.Module;
import dagger.Provides;
import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

  static final String BASE_URL = "https://maps.googleapis.com/";

  @Provides
  @Singleton
  static OkHttpClient providesOkhttpClient() {
    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    logging.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    httpClient.addInterceptor(logging);
    httpClient.readTimeout(1, TimeUnit.MINUTES);
    httpClient.connectTimeout(1, TimeUnit.MINUTES);
    return httpClient.build();
  }

  @Provides
  @Singleton
  static Retrofit providesRetrofit(){
    return new Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(providesOkhttpClient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(BASE_URL)
        .build();

  }






}
