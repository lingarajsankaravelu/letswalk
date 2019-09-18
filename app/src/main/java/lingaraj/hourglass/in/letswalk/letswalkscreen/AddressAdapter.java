package lingaraj.hourglass.in.letswalk.letswalkscreen;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import android.view.View;
import java.util.ArrayList;
import java.util.List;
import lingaraj.hourglass.in.ridegmap.R;
import lingaraj.hourglass.in.ridegmap.databinding.CardAddressBinding;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

  private final Context mcontext;
  private final List<Address> addresses = new ArrayList<Address>();
  private final List<Integer> favourites = new ArrayList<>();
  private final FavouriteLocationsFragment.FavouriteClick mclick;

  public AddressAdapter(Context context, FavouriteLocationsFragment.FavouriteClick favouriteClick){
    this.mcontext = context;
    this.mclick = favouriteClick;
  }

  @NonNull @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(mcontext).inflate(R.layout.card_address,parent);
    view.setOnClickListener(this.mclick);
    return new ViewHolder(view);
  }

  @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    int image_resource = favourites.contains(position)?R.drawable.ic_star_favourites_24dp:R.drawable.ic_star_defualt_24dp;
    Address address = this.addresses.get(position);
    holder.binding.streetDetails.setText(address.getFeatureName());
    holder.binding.country.setText(address.getLocality()+" "+address.getCountryName());
    holder.binding.star.setImageResource(image_resource);
  }

  public class ViewHolder extends RecyclerView.ViewHolder{

    public CardAddressBinding binding;
    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      binding = DataBindingUtil.bind(itemView);
    }
  }

  @Override public int getItemCount() {
    return addresses.size();
  }

  public void notifySelection(Integer position){
    if (favourites.contains(position)){
        favourites.remove(position);
    }
    else {
      favourites.add(position);
    }
    notifyDataSetChanged();

  }

  public void addLocation(Address address){
    this.addresses.add(address);
    notifyDataSetChanged();
  }
}
