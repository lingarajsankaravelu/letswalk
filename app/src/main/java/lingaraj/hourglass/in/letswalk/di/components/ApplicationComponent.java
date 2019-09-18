package lingaraj.hourglass.in.letswalk.di.components;

import dagger.Component;
import javax.inject.Singleton;
import lingaraj.hourglass.in.letswalk.BaseApplication;
import lingaraj.hourglass.in.letswalk.di.modules.ActivityBindingModule;
import lingaraj.hourglass.in.letswalk.di.modules.ApplicationModule;
import lingaraj.hourglass.in.letswalk.di.modules.NetworkModule;
import lingaraj.hourglass.in.letswalk.di.modules.ViewModelBindingModule;
import lingaraj.hourglass.in.letswalk.rest.di.RestAPISModule;

@Singleton
@Component(modules = {
    ApplicationModule.class,
    ViewModelBindingModule.class,
    NetworkModule.class,
    RestAPISModule.class,
    ActivityBindingModule.class
})
public interface ApplicationComponent {
  void inject(BaseApplication baseApplication);
}
