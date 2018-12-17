package example.boostcamptest.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.net.URL;
import java.util.ArrayList;

import example.boostcamptest.R;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<item> mItem;
    private String url;

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView image;
        protected TextView title;
        protected TextView year;
        protected TextView director;
        protected TextView actor;
        protected RatingBar ratingBar;
        protected URL link;
        public final View mView;

        public ViewHolder(View view){
            super(view);
            mView = view;
            this.image = (ImageView) view.findViewById(R.id.imageView);
            this.title = (TextView) view.findViewById(R.id.title);
            this.year = (TextView) view.findViewById(R.id.year);
            this.director = (TextView) view.findViewById(R.id.director);
            this.actor = (TextView) view.findViewById(R.id.actors);
            this.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        }
    }
    public Adapter(ArrayList<item> item){
        this.mItem = item;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position){
        url = mItem.get(position).getLink();
        viewHolder.title.setText(Html.fromHtml(mItem.get(position).getTitle()));
        Glide.with(viewHolder.image.getContext())
                .load(mItem.get(position).getImage())
                .into(viewHolder.image);
        viewHolder.actor.setText(Html.fromHtml(mItem.get(position).getActor()));
        viewHolder.director.setText(Html.fromHtml(mItem.get(position).getDirector()));
        viewHolder.year.setText(Html.fromHtml(mItem.get(position).getYear()));
        viewHolder.ratingBar.setRating(Float.parseFloat( mItem.get(position).getRatingBar())/2);
        viewHolder.mView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Context context = v.getContext();
                Uri url = Uri.parse(mItem.get(position).getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW,url);
                PackageManager packageManager = v.getContext().getPackageManager();
                if(intent.resolveActivity(packageManager) !=null){
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public  int getItemCount(){
        return (null != mItem ? mItem.size():0);
    }
}
