package com.asramaum.siarum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.asramaum.siarum.DetailActivity;
import com.asramaum.siarum.R;
import com.asramaum.siarum.SettingsActivity;
import com.asramaum.siarum.model.MyFarmViewPagerModel;

import java.util.List;

/**
 * Created by austi on 12/9/2018.
 */

public class MyFarmViewPagerAdapter extends PagerAdapter{

    private List<MyFarmViewPagerModel> models;
    private LayoutInflater layoutInflater;
    private Context context;

    public MyFarmViewPagerAdapter(List<MyFarmViewPagerModel> models, Context context) {
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
        View view = layoutInflater.inflate(R.layout.my_farm_viewpager_item, container,false);

        ImageView imageView, iconFarmStatus;
        View cardviewOverlay;
        TextView title, desc;

        iconFarmStatus = view.findViewById(R.id.icon_farm_status);
        cardviewOverlay = view.findViewById(R.id.cardview_overlay);
        imageView = view.findViewById(R.id.imageMainMenuViewPagerItem);
        title = view.findViewById(R.id.titleMainMenuViewPagerItem);
        desc = view.findViewById(R.id.descMainMenuViewPagerItem);

        imageView.setImageResource(models.get(position).getMenuImage());
        title.setText(models.get(position).getMenuName());
        desc.setText(models.get(position).getMenuDesc());

        /*if (position == 1){
            iconFarmStatus.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_warning_outline_white));
            cardviewOverlay.setBackground(view.getResources().getDrawable(R.drawable.warning_gradient_item));
        } else if (position == 2){
            iconFarmStatus.setImageDrawable(view.getResources().getDrawable(R.drawable.ic_info_outline_white_48dp));
            cardviewOverlay.setBackground(view.getResources().getDrawable(R.drawable.danger_gradient_item));
        }*/



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 0){
                    Intent farmDetail = new Intent(view.getContext(), DetailActivity.class);
                    farmDetail.putExtra("1", "Kajian Studi Ilmiah");
                    farmDetail.putExtra("2", "");
                    farmDetail.putExtra("3", "");
                    farmDetail.putExtra("sheetLink", "");
                    farmDetail.putExtra("sheetLinkChart", "");
                    view.getContext().startActivity(farmDetail);
                } else if (position == 1){
                    Intent farmDetail = new Intent (view.getContext(), SettingsActivity.class);
                    view.getContext().startActivity(farmDetail);
                } else if (position == 2){
                    Intent farmDetail = new Intent (view.getContext(), SettingsActivity.class);
                    view.getContext().startActivity(farmDetail);
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
