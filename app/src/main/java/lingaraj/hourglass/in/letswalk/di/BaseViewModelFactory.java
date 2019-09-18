package lingaraj.hourglass.in.letswalk.di;


import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class BaseViewModelFactory implements ViewModelProvider.Factory {

  private final String TAG = "BaseViewModelFact";
  private final Map<Class<? extends ViewModel>, Provider<ViewModel>> mProviderMap;


  @Inject
  BaseViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> providerMap) {
    mProviderMap = providerMap;
  }

  @SuppressWarnings("unchecked")
  @NonNull
  @Override
  public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
    Provider<? extends ViewModel> creator = mProviderMap.get(modelClass);
    if (creator == null) {
      Log.d(TAG,"No previous model found in map:"+modelClass.getSimpleName());
      for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : mProviderMap.entrySet()) {
        if (modelClass.isAssignableFrom(entry.getKey())) {
          creator = entry.getValue();
          Log.d(TAG,"Created key for model in map:"+modelClass.getSimpleName()+"->"+entry.getKey());
          break;
        }
      }
    }
    if (creator == null) {
      Log.d(TAG,"Unkown model class:"+modelClass.getSimpleName());
      throw new IllegalArgumentException("unknown model class " + modelClass);
    }
    try {
      return (T) creator.get();
    } catch (Exception e) {
      Log.e(TAG,e.toString());
      throw new RuntimeException(e);
    }
  }
}