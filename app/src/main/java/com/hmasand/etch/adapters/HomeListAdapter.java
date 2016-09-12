package com.hmasand.etch.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hmasand.etch.R;
import com.hmasand.etch.models.RichLinkPreviewData;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by hmasand on 8/24/16.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    Context mContext;
    List<RichLinkPreviewData> mEntries;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvRichLinkTitle;
        public TextView tvRichLinkDesc;
        public ImageView ivRichLinkThumb;

        public ViewHolder(View v) {
            super(v);
            tvRichLinkTitle = (TextView) v.findViewById(R.id.tvRichLinkTitle);
            tvRichLinkDesc = (TextView) v.findViewById(R.id.tvRichLinkDescription);
            ivRichLinkThumb = (ImageView) v.findViewById(R.id.ivRichLinkThumbnail);
        }
    }

    public HomeListAdapter(Context context, List<RichLinkPreviewData> entries) {
        mContext = context;
        mEntries = entries;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_etch, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final RichLinkPreviewData entry = mEntries.get(position);

        holder.tvRichLinkTitle.setText(entry.title);
        holder.tvRichLinkDesc.setText(entry.description);
        Picasso.with(mContext).load(entry.imageUrl).resize(100, 100).centerCrop().into(holder.ivRichLinkThumb);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(entry.url));
                mContext.startActivity(i);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mEntries.size();
    }

}
