package Adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edward.dwarkawala.FullFeed;
import com.example.edward.dwarkawala.ShopActivity;
import com.example.edward.dwarkawala.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Models.AccountData;
import Models.MerchantData;

public class DealsAdapter extends RecyclerView.Adapter <DealsAdapter.MyViewHolder> {

    private static final String TAG = DealsAdapter.class.getSimpleName();
    public Context mContext;
    public static List<MerchantData> mMerchants;
    public static List<AccountData> User;
    FirebaseAuth mAuth;




    public DealsAdapter(Context mContext) {
        this.mContext = mContext;
        mMerchants = new ArrayList<>();
        User = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deals_list,viewGroup,false);
        return new MyViewHolder(view);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView shopName, ShopTag;
        CircularImageView shopImage;
        TextView pointAddress;
        RelativeLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            shopImage  = (CircularImageView) itemView.findViewById(R.id.shopImageID);
            shopName  =itemView.findViewById(R.id.shopNameID);
            ShopTag = itemView.findViewById(R.id.offersID);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            pointAddress = itemView.findViewById(R.id.shopPointAddress);
    }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ShopActivity.class);
            intent.putExtra("id", mMerchants.get(getAdapterPosition()).getId());
            intent.putExtra("shop_image", mMerchants.get(getAdapterPosition()).getShopPic());
            intent.putExtra("shop_name", mMerchants.get(getAdapterPosition()).getShopName());
            intent.putExtra("shop_address", mMerchants.get(getAdapterPosition()).getShopAddress());
            intent.putExtra("shopMobileNumber", mMerchants.get(getAdapterPosition()).phoneNumber);
            intent.putExtra("shopLat",mMerchants.get(getAdapterPosition()).latitude);
            intent.putExtra("shopLong",mMerchants.get(getAdapterPosition()).longitude);
            intent.putExtra("shopTag", mMerchants.get(getAdapterPosition()).tag);
            mContext.startActivity(intent);
        }
    }
@Override
    public void onBindViewHolder(@NonNull final DealsAdapter.MyViewHolder myViewHolder, final int i) {


        myViewHolder.shopName.setText(mMerchants.get(i).getShopName());
        myViewHolder.ShopTag.setText(mMerchants.get(i).tag);
        Glide.with(mContext)
                .load(mMerchants.get(i).getShopPic())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(myViewHolder.shopImage);

        myViewHolder.pointAddress.setText(mMerchants.get(i).getLocation());


//    Location startPoint=new Location("locationA");
//    startPoint.setLatitude(28.6354747);
//    startPoint.setLongitude(77.0184355);
//
//    Location endPoint=new Location("locationA");
//    endPoint.setLatitude(Float.valueOf(mMerchants.get(i).latitude));
//    endPoint.setLongitude(Float.valueOf(mMerchants.get(i).longitude));
//
//    double distance=startPoint.distanceTo(endPoint);
//    double distanceinKm = distance/1000;
//
//    Log.d(TAG, "onBindViewHolder: "+ distanceinKm);
 }


    @Override
    public int getItemCount() {
        return mMerchants.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void addAll(List<MerchantData> newWalls) {
        int initialSize = mMerchants.size();
        mMerchants.addAll(newWalls);
        notifyItemRangeInserted(initialSize, newWalls.size());
    }

    public List<MerchantData> getItemList(){
        return mMerchants;
    }

}