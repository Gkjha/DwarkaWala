package com.example.edward.dwarkawala;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import Models.AccountData;

public class header_information extends AppCompatPreferenceActivity {

    Context mContext;
    public static List<AccountData> User;
    ImageView userImage;
    TextView userName;
    TextView userEmail;
    final  int i;



    public header_information(Context mContext, int i) {
        this.mContext = mContext;
        this.i = i;
        User = new ArrayList<>();
    }



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);

        userImage = findViewById(R.id.userImage);
        userEmail = findViewById(R.id.userEmail);
        userName = findViewById(R.id.userName);

        Glide.with(mContext)
                .load(User.get(i).getProfilePic())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(userImage);
    }

}
