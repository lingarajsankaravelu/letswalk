package lingaraj.hourglass.in.letswalk.di.modules;
import dagger.Module;
import dagger.Provides;
import lingaraj.hourglass.in.letswalk.di.DisposableManager;
import lingaraj.hourglass.in.letswalk.di.ForScreen;
import lingaraj.hourglass.in.letswalk.di.FragmentScope;

@Module
public abstract class ScreenModule {

    @Provides
    @FragmentScope
    @ForScreen
    static DisposableManager provideDisposableManager() {
        return new DisposableManager();
    }

}
