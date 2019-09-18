package lingaraj.hourglass.in.letswalk.di.modules;

import com.patloew.rxlocation.RxLocation;
import dagger.Module;
import dagger.Provides;
import lingaraj.hourglass.in.letswalk.BaseApplication;

@Module
public class ApplicationModule {

  private  final BaseApplication baseApplication;
  private final RxLocation rxLocation;

  public ApplicationModule(BaseApplication baseApplication) {
    this.baseApplication = baseApplication;
    this.rxLocation = new RxLocation(baseApplication);
  }

  @Provides
  BaseApplication providesApplicationContext(){
    return baseApplication;
  }

  @Provides
  RxLocation providesRxLocation(){ return rxLocation;}
}
