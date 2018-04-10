package com.cracky_axe.pxohlqo.xihudewater.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cracky_axe.pxohlqo.xihudewater.R;
import com.cracky_axe.pxohlqo.xihudewater.model.net.GlideApp;
import com.cracky_axe.pxohlqo.xihudewater.model.objectBox.GankAndroidResultBeanObjectBox;

import java.util.List;

import io.objectbox.Box;


class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder>{

    private static final String TAG = "HomeAdapter";

    private Box<GankAndroidResultBeanObjectBox> mData;

    private final Context mContext;

    final private HomeAdapterOnItemClickHandler mClickHandler;

    private static final int VIEW_TYPE_HAVE_IMAGE = 100;
    private static final int VIEW_TYPE_HAVE_NO_IMAGE = 101;


    public interface HomeAdapterOnItemClickHandler {
        void onItemClick(String id);
    }

    /**
     * Called when RecyclerView needs a new {@link HomeViewHolder} of the given type to represent
     * an item.
     * <p>
     * This new HomeViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p>
     * The new HomeViewHolder will be used to display items of the adapter using
     * . Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new HomeViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(HomeViewHolder, int)
     */
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;

        switch (viewType) {
            case VIEW_TYPE_HAVE_IMAGE:
                layoutId = R.layout.item_view_with_image;
                break;
            case VIEW_TYPE_HAVE_NO_IMAGE:
                layoutId = R.layout.item_view_without_image;
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new HomeViewHolder(v);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link HomeViewHolder#itemView} to reflect the item at the given
     * position.
     * <p>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link HomeViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p>
     * Override  instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The HomeViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        List<GankAndroidResultBeanObjectBox> listData = mData.getAll();

        holder.descriptionView.setText(listData.get(position).getDesc());
        holder.linkView.setText(listData.get(position).getUrl());
//        holder.publishedAtView.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
//                .format(mData.getAll().get(position).getPublishedAt()));
        holder.publishedAtView.setText(listData.get(position).getPublishedAt().substring(0, 10));

        int viewType = getItemViewType(position);
        //Log.d(TAG, "onBindViewHolder: viewType: " + viewType);

        switch (viewType) {
            case VIEW_TYPE_HAVE_IMAGE:
                //Log.d(TAG, "onBindViewHolder: load image");
                GlideApp.with(mContext)
                        .load(listData.get(position).getImages().get(0).getImageUrl())
                        .placeholder(new ColorDrawable(Color.GRAY))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(holder.imageView);
                break;

            case VIEW_TYPE_HAVE_NO_IMAGE:
                break;

            default:
                throw new IllegalArgumentException("Invalid view type, value of " + viewType);
        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {

        return (int) mData.count();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView descriptionView;
        final TextView linkView;
        final TextView publishedAtView;
        final CardView itemCard;
        final ConstraintLayout constraintLayout;
        ImageView imageView;

        public HomeViewHolder(View v) {
            super(v);
            itemCard = v.findViewById(R.id.item_card);
            constraintLayout = v.findViewById(R.id.item_constraintLayout);

            descriptionView = v.findViewById(R.id.item_description);
            linkView = v.findViewById(R.id.item_url);
            publishedAtView = v.findViewById(R.id.item_publishat);

            imageView = v.findViewById(R.id.item_imageView);

            int viewType = HomeAdapter.this.getItemViewType(getAdapterPosition());
            switch (viewType) {
                case VIEW_TYPE_HAVE_IMAGE:
                    //imageView = v.findViewById(R.id.item_imageView);
                    //Log.d(TAG, "HomeViewHolder: get imageView");
                    break;
                case VIEW_TYPE_HAVE_NO_IMAGE:

                    break;

                default:
                    throw new IllegalArgumentException("Invalid view type, value of " + viewType);
            }


            constraintLayout.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String id = mData.getAll().get(adapterPosition).get_id();
            mClickHandler.onItemClick(id);
        }
    }


    public HomeAdapter(Box<GankAndroidResultBeanObjectBox> box, HomeAdapterOnItemClickHandler clickHandler, Context context) {
        mData = box;
        mClickHandler = clickHandler;
        mContext = context;
    }

    public void swapData(Box<GankAndroidResultBeanObjectBox> newData) {
        mData = newData;
        notifyDataSetChanged();
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        boolean haveImages = false;
        //Log.d(TAG, "getItemViewType: position: " + position);

        if (position == -1) {
            haveImages = false;
        } else {
            if (mData.getAll().get(position).getImages().size() == 0) {
                haveImages = false;
            } else {
                haveImages = true;
            }
        }

        if (haveImages) {
            //Log.d(TAG, "getItemViewType: haveImages " + haveImages);
            return VIEW_TYPE_HAVE_IMAGE;
        } else {
            //Log.d(TAG, "getItemViewType: haveImages " + haveImages);
            return VIEW_TYPE_HAVE_NO_IMAGE;
        }
    }
}
