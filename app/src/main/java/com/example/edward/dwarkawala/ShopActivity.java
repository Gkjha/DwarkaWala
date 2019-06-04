package com.example.edward.dwarkawala;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Models.MerchantData;

public class ShopActivity extends AppCompatActivity {

    private static final String TAG = "ShopActivity";

    ImageView shopImage;
    Button PinButton, CallButton, LocationButton;
    TextView shopName, shopIntro, shopAddress, showShopAddress;
    private ImageButton backButton;
    TextView shopTag;

    DatabaseReference recyclerDatabaseReference,likeDatabase;
    FirebaseDatabase database;
    FirebaseAuth mAuth;





    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shopImage = findViewById(R.id.shopImage);
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        backButton = findViewById(R.id.backButtonID);
        showShopAddress = findViewById(R.id.shopFullAddress);
        CallButton =findViewById(R.id.CallButton);
        PinButton = findViewById(R.id.pinButton);
        LocationButton = findViewById(R.id.locationButton);
        shopTag = findViewById(R.id.shopTag);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //if (shouldChangeStatusBarTintToDark) {
//                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            } else {
//                // We want to change tint color to white again.
//                // You can also record the flags in advance so that you can turn UI back completely if
//                // you have set other flags before, such as translucent or full screen.
//                decor.setSystemUiVisibility(0);
//            }
        }

        getIncomingIntent();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*Uri mapUri = Uri.parse("geo:0,0?q=lat,lng(label)");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);*/


        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerDatabaseReference = FirebaseDatabase.getInstance().getReference("Bookmarks");


        LocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strUri = "http://maps.google.com/maps?q=loc:" + getIntent().getStringExtra("shopLat") + "," +
                        getIntent().getStringExtra("shopLong")+ " (" + getIntent().getStringExtra("shop_name") + ")";
                Intent locationIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));
                /*Intent data = locationIntent.setData(Uri.parse("geo:"+getIntent().getStringExtra("shopLat")+","+getIntent()
                .getStringExtra("shopLong")+ " (" + "My Shop" + ")"));*/
                //Log.d("lattitude", getIntent().getStringExtra("shopLat"));
                startActivity(locationIntent);
            }
        });

        Log.d("lattitude", getIntent().getStringExtra("shopLat"));

        CallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+getIntent().getStringExtra("shopMobileNumber")));
                startActivity(callIntent);
            }
        });


        PinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makeBookmark();

            }
        });


    }


    public void makeBookmark(){


        String shopId = getIntent().getStringExtra("id");
        Log.d(TAG, "makeBookmark: "+shopId);
        recyclerDatabaseReference.child(mAuth.getCurrentUser().getUid()).child(shopId).setValue(shopId);
        Toast.makeText(ShopActivity.this, "Shop Bookmarked", Toast.LENGTH_SHORT).show();


    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getIncomingIntent(){

        if(getIntent().hasExtra("shop_image") && getIntent().hasExtra("shop_name")){

            String shopImage = getIntent().getStringExtra("shop_image");
            String shopName = getIntent().getStringExtra("shop_name");
            String shopAddress = getIntent().getStringExtra("shop_address");
            String shopPhoneNumber = getIntent().getStringExtra("shopMobileNumber");
            String shopTag = getIntent().getStringExtra("shopTag");
            setdata(shopImage, shopName, shopAddress, shopTag);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setdata(String shopImage, String shopName, String shopAddress, String ShopTag){
        this.shopName.setText(shopName);

        this.showShopAddress.setText(shopAddress);
        this.shopTag.setText(ShopTag);
        Log.d(TAG, "setdata: "+shopTag);
        Glide.with(this)
                .asBitmap()
                .load(shopImage)
                .into(this.shopImage);

        Log.d(TAG, "shop Image is: "+getIntent().getStringExtra("shop_image"));
    }
}
