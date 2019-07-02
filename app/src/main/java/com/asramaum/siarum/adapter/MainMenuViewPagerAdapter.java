package com.asramaum.siarum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asramaum.siarum.R;
import com.asramaum.siarum.SettingsActivity;
import com.asramaum.siarum.model.MainMenuViewPagerModel;

import java.util.List;


/**
 * Created by austi on 12/9/2018.
 */

public class MainMenuViewPagerAdapter extends PagerAdapter{

    private List<MainMenuViewPagerModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public MainMenuViewPagerAdapter(List<MainMenuViewPagerModel> models, Context context) {
        this.models = models;
        this.context = context;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.main_menu_viewpager_item, container,false);

        ImageView imageView;
        TextView title, desc;

        imageView = view.findViewById(R.id.imageMainMenuViewPagerItem);
        title = view.findViewById(R.id.titleMainMenuViewPagerItem);
        desc = view.findViewById(R.id.descMainMenuViewPagerItem);

        imageView.setImageResource(models.get(position).getImageMainMenuViewPagerItem());
        title.setText(models.get(position).getTitleMainMenuViewPagerItem());
        desc.setText(models.get(position).getDescMainMenuViewPagerItem());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 0){
                    Intent readMore = new Intent(view.getContext(), SettingsActivity.class);
                    view.getContext().startActivity(readMore);
                } else if(position == 1){
                    Intent readMore = new Intent(view.getContext(), SettingsActivity.class);
                    view.getContext().startActivity(readMore);
                } else if(position == 2){
                    Intent readMore = new Intent(view.getContext(), SettingsActivity.class);
                    view.getContext().startActivity(readMore);
                } else if(position == 3){
                    Intent readMore = new Intent(view.getContext(), SettingsActivity.class);
                    view.getContext().startActivity(readMore);
                }
            }
        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }
}
