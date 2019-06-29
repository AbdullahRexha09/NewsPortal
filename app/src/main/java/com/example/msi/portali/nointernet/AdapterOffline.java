package com.example.msi.portali.nointernet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.msi.portali.Adapter;
import com.example.msi.portali.R;
import com.example.msi.portali.Utils;

import java.util.ArrayList;

public class AdapterOffline extends RecyclerView.Adapter<AdapterOffline.ViewHolder> {

private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mAuthors = new ArrayList<>();
    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<String> mDescriptions = new ArrayList<>();
    private ArrayList<String> mSources = new ArrayList<>();
    private ArrayList<String> mPublishedAts = new ArrayList<>();
    private ArrayList<String> mTimes = new ArrayList<>();
    private Context context;
    private OnItemClickListener onItemClickListener;


    public AdapterOffline(Context context,ArrayList<String> mImages, ArrayList<String> mAuthor, ArrayList<String> mTitle, ArrayList<String> mDescription, ArrayList<String> mSource, ArrayList<String> mPublishedAt, ArrayList<String> mTime) {
        this.mImages = mImages;
        this.mAuthors = mAuthor;
        this.mTitles = mTitle;
        this.mDescriptions = mDescription;
        this.mSources = mSource;
        this.mPublishedAts = mPublishedAt;
        this.mTimes = mTime;
        this.context = context;
    }

    private ArrayList<String> mTime = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_offline,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {

        Glide.with(context).asBitmap().load(mImages.get(i)).into(holder.mImage);
        holder.mTitle.setText(mTitles.get(i));
        holder.mDescription.setText(mDescriptions.get(i));
        holder.mSource.setText(mSources.get(i));
        holder.mAuthor.setText(mAuthors.get(i));
        holder.mPublishedAt.setText("\u2022"+ Utils.DateToTimeFormat(mPublishedAts.get(i)));
        holder.mTime.setText("\u2022"+mTimes.get(i));



    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }
    public void setOnItemClickListener(Adapter.OnItemClickListener onItemClickListener){
        this.onItemClickListener= (OnItemClickListener) onItemClickListener;
    }
    public interface OnItemClickListener{
        void onItemClickListener(View view,int position);


    }

    public class ViewHolder extends RecyclerView.ViewHolder{
    ImageView mImage;
    TextView mAuthor,mTitle,mDescription,mSource,mPublishedAt,mTime;
    RelativeLayout mParent;

    public ViewHolder(@NonNull final View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.img);
        mAuthor = itemView.findViewById(R.id.author);
        mTitle = itemView.findViewById(R.id.title);
        mDescription = itemView.findViewById(R.id.desc);
        mSource = itemView.findViewById(R.id.source);
        mPublishedAt = itemView.findViewById(R.id.publishedAt);
        mTime = itemView.findViewById(R.id.time);
        mParent = itemView.findViewById(R.id.parental);

        mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        onItemClickListener.onItemClickListener(itemView,position);
                    }

                }
            }
        });









    }

}


}
