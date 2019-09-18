package lingaraj.hourglass.in.letswalk.di.components;

import dagger.android.AndroidInjector;
import lingaraj.hourglass.in.letswalk.di.DisposableManager;
import lingaraj.hourglass.in.letswalk.di.ForScreen;

public interface ScreenComponent<T> extends AndroidInjector<T> {

    @ForScreen
    DisposableManager disposableManager();
}
