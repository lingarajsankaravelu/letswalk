package lingaraj.hourglass.in.letswalk;

import android.app.Application;
import android.content.Context;
import javax.inject.Inject;
import lingaraj.hourglass.in.letswalk.di.components.ApplicationComponent;
import lingaraj.hourglass.in.letswalk.di.components.DaggerApplicationComponent;
import lingaraj.hourglass.in.letswalk.di.injectors.ActivityInjector;
import lingaraj.hourglass.in.letswalk.di.modules.ApplicationModule;

public class BaseApplication extends Application {

  private ApplicationComponent component;
  @Inject ActivityInjector activityInjector;

  @Override
  public void onCreate() {
    super.onCreate();
    component = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    component.inject(this);

  }

  public ApplicationComponent getComponent() {
    return component;
  }

  public static BaseApplication get(Context context) {
    return (BaseApplication) context.getApplicationContext();
  }

  public ActivityInjector getActivityInjector() {
    return activityInjector;
  }


}
