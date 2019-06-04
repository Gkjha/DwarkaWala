package Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.edward.dwarkawala.R;

public class info extends Fragment {

    ImageButton facebookButton,instaButton, youtubeButton, snapchatButton;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_info,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        facebookButton = (ImageButton) view.findViewById(R.id.facebookButton);
        instaButton = (ImageButton) view.findViewById(R.id.instagramButton);
        youtubeButton = (ImageButton) view.findViewById(R.id.youtubeButton);
        snapchatButton = (ImageButton) view.findViewById(R.id.snapchatButton);

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.facebook.com/DwarkaWala/"));
                startActivity(intent);
               // getOpenFacebookIntent(context);
            }
        });

        instaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.instagram.com/dwarkawala/"));
                startActivity(intent);
            }
        });

        youtubeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/channel/UCfAXWT29HCIKqeE2yXOIhMg/"));
                startActivity(intent);
            }
        });

        snapchatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.snapchat.com/add/dwarkawala"));
                startActivity(intent);
            }
        });



    }


        public static Intent getOpenFacebookIntent(Context context) {


                return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/DwarkaWala/"));

        }
    }
