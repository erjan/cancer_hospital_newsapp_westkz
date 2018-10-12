package kz.onko_zko.hospital;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.SingleNewsArticle> {
    private ArrayList<NewsItem> mDataset;
    NewsInterface newsListener;
    String url_base = "https://onkozko.herokuapp.com/";

    public static class SingleNewsArticle extends RecyclerView.ViewHolder {
        public TextView titleNews;
        ImageView newsImage;
        TextView contentNews;
        public SingleNewsArticle(View v) {
            super(v);
            titleNews = v.findViewById(R.id.title_news);
            newsImage = v.findViewById(R.id.image_news);
            contentNews = v.findViewById(R.id.content_news);
        }
    }

    public NewsAdapter(ArrayList<NewsItem> dataset, NewsInterface newsListener) {
        mDataset = dataset;
        this.newsListener = newsListener;
    }

    @NonNull
    @Override
    public NewsAdapter.SingleNewsArticle onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item, parent, false);

        SingleNewsArticle vh = new SingleNewsArticle(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.SingleNewsArticle holder, final int position) {
        holder.titleNews.setText(mDataset.get(position).title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsListener.clickedNews(mDataset.get(position));
            }
        });
        if(!mDataset.get(position).imageUrl.equals("")) {
            GlideApp.with(holder.itemView)
                    .load(mDataset.get(position).imageUrl)
                    .fitCenter()
                    .into(holder.newsImage);
            holder.contentNews.setVisibility(View.GONE);
        } else {
            String content = mDataset.get(position).content;
            if(content.length()>250) {
                content = content.substring(0, Math.min(content.length(), 250-3)) + "...";
            }
            holder.contentNews.setText(content);
            holder.newsImage.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public static class NewsItem {
        public String title;
        public String content;
        public String imageUrl;
        public NewsItem(String _title, String _content, String _imageUrl) {
            title = _title;
            content = _content;
            imageUrl = _imageUrl;
        }
    }
    public interface NewsInterface {
        void clickedNews(NewsItem item);
    }
}
