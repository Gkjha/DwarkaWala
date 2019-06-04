package Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edward.dwarkawala.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Models.Banners;

import static com.example.edward.dwarkawala.FullFeed.TAG;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    static List<Banners> bannersList = new ArrayList<>();
    //private Integer[] images = {R.drawable.one, R.drawable.two, R.drawable.three};


    public ViewPagerAdapter(Context context,List<Banners> bannersList) {
        this.context = context;
        layoutInflater = layoutInflater.from(context);
        this.bannersList = bannersList;

    }

    @Override
    public int getCount() {
        return bannersList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.banner_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.bannerImage);


       // Picasso.get().load(bannersList.get(position).getImage()).into(imageView);
        Glide.with(context).load(bannersList.get(position).getImage())
                .apply(new RequestOptions()
                .centerCrop())
                .into(imageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://"+bannersList.get(position).getLink()));
                context.startActivity(intent);
            }
        });

        ViewPager vp = (ViewPager) container;
        vp.addView(view,0);
        return view;



    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
