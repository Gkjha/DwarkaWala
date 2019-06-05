package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edward.dwarkawala.R;
import com.example.edward.dwarkawala.ShopActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import Models.AccountData;
import Models.MerchantData;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.MyViewHolder> {

    private static final String TAG = BookmarksAdapter.class.getSimpleName();
    public Context mContext;
    public static List<MerchantData> mBookmarks = new ArrayList<>();
    public static List<AccountData> User;
    FirebaseAuth mAuth;




    public BookmarksAdapter(Context mContext,List<MerchantData> mBookmarks) {
        this.mContext = mContext;
        this.mBookmarks = mBookmarks;
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
        ImageView shopImage;
        TextView pointAddress;
        RelativeLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            shopImage  = (ImageView) itemView.findViewById(R.id.shopImageID);
            shopName  =itemView.findViewById(R.id.shopNameID);
            ShopTag = itemView.findViewById(R.id.offersID);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            pointAddress = itemView.findViewById(R.id.shopPointAddress);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ShopActivity.class);
            intent.putExtra("id", mBookmarks.get(getAdapterPosition()).getId());
            intent.putExtra("shop_image", mBookmarks.get(getAdapterPosition()).getShopPic());
            intent.putExtra("shop_name", mBookmarks.get(getAdapterPosition()).getShopName());
            intent.putExtra("shop_address", mBookmarks.get(getAdapterPosition()).getShopAddress());
            intent.putExtra("shopMobileNumber", mBookmarks.get(getAdapterPosition()).phoneNumber);
            intent.putExtra("shopLat", mBookmarks.get(getAdapterPosition()).latitude);
            intent.putExtra("shopLong", mBookmarks.get(getAdapterPosition()).longitude);
            intent.putExtra("shopTag", mBookmarks.get(getAdapterPosition()).tag);
            mContext.startActivity(intent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarksAdapter.MyViewHolder myViewHolder, final int i) {


        myViewHolder.shopName.setText(mBookmarks.get(i).getShopName());
        myViewHolder.ShopTag.setText(mBookmarks.get(i).tag);
        Glide.with(mContext)
                .load(mBookmarks.get(i).getShopPic())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(myViewHolder.shopImage);

        myViewHolder.pointAddress.setText(mBookmarks.get(i).getLocation());




//    Location startPoint=new Location("locationA");
//    startPoint.setLatitude(28.6354747);
//    startPoint.setLongitude(77.0184355);
//
//    Location endPoint=new Location("locationA");
//    endPoint.setLatitude(Float.valueOf(mBookmarks.get(i).latitude));
//    endPoint.setLongitude(Float.valueOf(mBookmarks.get(i).longitude));
//
//    double distance=startPoint.distanceTo(endPoint);
//    double distanceinKm = distance/1000;
//
//    Log.d(TAG, "onBindViewHolder: "+ distanceinKm);
    }


    @Override
    public int getItemCount() {
        return mBookmarks.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void clear() {
        final int size = mBookmarks.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mBookmarks.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    public List<MerchantData> getItemList(){
        return mBookmarks;
    }

}
