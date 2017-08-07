package org.ccccoder.rapbattle.Adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import org.ccccoder.rapbattle.Model.Bit;
import org.ccccoder.rapbattle.SwipeLayout.SwipeRevealLayout;
import org.ccccoder.rapbattle.Model.Record;
import org.ccccoder.rapbattle.Model.Title;
import org.ccccoder.rapbattle.R;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Sangdeok on 2017-08-05.
 */
public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //private List<ItemModel> mitemModels;
    private MediaPlayer player = new MediaPlayer();
    private int playbackPosition = 0;
    private RealmResults<Title> mtitleModels;
    private RealmResults<Bit> mbitModels;
    private RealmResults<Record> mrecordModels;
    private final LayoutInflater mInflater;
    private static Context mContext;
    //private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();//Swipe 기능
    private Realm realm;

    /*애니메이션*/
    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_button";
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);
    Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimationsMap = new HashMap<>();
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();
    private int lastAddAnimatedItem = -2;

    /*멀티 타입*/
    public static int TITLE_VIEW = 1010;
    public static int BIT_VIEW = 1020;
    public static int RECORD_VIEW = 1030;

    public ItemAdapter(Context context, RealmResults<Title> titlelist, RealmResults<Record> recordlist, RealmResults<Bit> bitlist) {
        this.mtitleModels = titlelist;
        this.mrecordModels = recordlist;
        this.mbitModels = bitlist;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        realm = Realm.getDefaultInstance();
        if (viewType == TITLE_VIEW){
            View v = mInflater.inflate(R.layout.recycleview_title_item, viewGroup, false);
            return new TitleViewHolder(v);
        } else if(viewType == BIT_VIEW){
            View v = mInflater.inflate(R.layout.recycleview_bit_item, viewGroup, false);
            return new BitViewHolder(v);
        }else{
            View v = mInflater.inflate(R.layout.recycleview_record_item, viewGroup, false);
            return new RecordViewHolder(v);
            //throw new RuntimeException("there is no type (" + viewType + "). make sure your using types correctly");

        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mtitleModels != null)
            return TITLE_VIEW;
        else if(mrecordModels != null)
            return RECORD_VIEW;
        else if(mbitModels != null)
            return BIT_VIEW;
        return TITLE_VIEW;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, final int i) {
        if(viewHolder instanceof TitleViewHolder){
            ((TitleViewHolder)viewHolder).titletext.setText(mtitleModels.get(i).getName());
        }
        else if(viewHolder instanceof RecordViewHolder){
            ((RecordViewHolder) viewHolder).ivFeedCenter.setText(mrecordModels.get(i).getUser_lyrics());
            ((RecordViewHolder) viewHolder).ivUser.setText(mrecordModels.get(i).getUser_nickname());
            ((RecordViewHolder) viewHolder).btnLike.setImageResource(mrecordModels.get(i).getIsLikes() ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);
            ((RecordViewHolder) viewHolder).tsLikesCounter.setCurrentText(((RecordViewHolder) viewHolder).vImageRoot.getResources().getQuantityString(
                    R.plurals.likes_count, (int)mrecordModels.get(i).getCount_likes(), (int)mrecordModels.get(i).getCount_likes()));
            ((RecordViewHolder)viewHolder).btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateHeartButton((RecordViewHolder)viewHolder);
                }
            });
            ((RecordViewHolder)viewHolder).ivFeedCenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animatePhotoLike((RecordViewHolder)viewHolder);
                }
            });
        }else if(viewHolder instanceof BitViewHolder){
            ((BitViewHolder)viewHolder).title.setText(mbitModels.get(i).getTitleName());//중요
            ((BitViewHolder)viewHolder).btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateHeartButton((BitViewHolder)viewHolder);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if(mtitleModels != null) return mtitleModels.size();
        if(mrecordModels != null)return  mrecordModels.size();
        if(mbitModels != null)return mbitModels.size();
        return 0;
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView titletext;
        public TitleViewHolder(View itemView) {
            super(itemView);
            titletext = (TextView)itemView.findViewById(R.id.titlename);
            Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "font.ttf");  //asset > fonts 폴더 내 폰트파일 적용
            titletext.setTypeface(typeFace);
        }
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public TextView ivFeedCenter;//Image->text
        public TextView ivUser;
        public ImageButton btnLike;
        public ImageButton btnMore;
        public View vBgLike;
        public ImageView ivLike;
        public TextSwitcher tsLikesCounter;
        public TextView ivUserProfile;//image->text
        public FrameLayout vImageRoot;
        public TextView vScroll;
        public Button play_btn;


        public RecordViewHolder(final View itemView) {
            super(itemView);
            ivUser = (TextView)itemView.findViewById(R.id.ivUserId);
            ivFeedCenter = (TextView)itemView.findViewById(R.id.ivFeedCenter);//Image->text
            btnLike = (ImageButton)itemView.findViewById(R.id.btnLike);
            vBgLike = (View)itemView.findViewById(R.id.vBgLike);
            ivLike = (ImageView)itemView.findViewById(R.id.ivLike);
            tsLikesCounter = (TextSwitcher)itemView.findViewById(R.id.tsLikesCounter);
            ivUserProfile= (TextView)itemView.findViewById(R.id.ivUserProfile);//image->text
            vImageRoot = (FrameLayout)itemView.findViewById(R.id.vImageRoot);
            play_btn = (Button)itemView.findViewById(R.id.play);
            play_btn.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int idx = getAdapterPosition();
            if(player.isPlaying()){
                playstop_call();
            }else {
                play_call(mrecordModels.get(idx).getTitleName());
            }
        }
        @Override
        public boolean onLongClick(View v){
            //swipeLayout.open(true);
            Timer timer = new Timer();
            TimerTask timertask = new TimerTask()
            {
                @Override
                public void run()
                {
                    //swipeLayout.close(true);
                }
            };
            timer.schedule(timertask, 5000);
            return true;
        }
}
    public class BitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public TextView title;//Image->text
        public ImageButton btnLike;
        public Button play;

        public BitViewHolder(final View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.titlename);//Image->text
            btnLike = (ImageButton)itemView.findViewById(R.id.btnLike);
            play = (Button)itemView.findViewById(R.id.play);
            play.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int idx = getAdapterPosition();
            if(player.isPlaying()){
                playstop_call();
            }else {
                play_call(mbitModels.get(idx).getTitleName());
            }
        }
        @Override
        public boolean onLongClick(View v){
            //swipeLayout.open(true);
            Timer timer = new Timer();
            TimerTask timertask = new TimerTask()
            {
                @Override
                public void run()
                {
                    //swipeLayout.close(true);
                }
            };
            timer.schedule(timertask, 5000);
            return true;
        }
    }

    private int getFulldate(DateTime date){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyyMMdd");
        return Integer.parseInt(date.toString(fmt));
    }

    public void animateHeartButton(final RecordViewHolder holder) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.btnLike, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.btnLike, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.btnLike, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                holder.btnLike.setImageResource(R.drawable.ic_heart_red);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                heartAnimationsMap.remove(holder);
                dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

        heartAnimationsMap.put(holder, animatorSet);
    }

    public void animateHeartButton(final BitViewHolder holder) {
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.btnLike, "rotation", 0f, 360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.btnLike, "scaleX", 0.2f, 1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.btnLike, "scaleY", 0.2f, 1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                holder.btnLike.setImageResource(R.drawable.ic_heart_red);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                heartAnimationsMap.remove(holder);
                dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();

        heartAnimationsMap.put(holder, animatorSet);
    }

    private void dispatchChangeFinishedIfAllAnimationsEnded(ItemAdapter.RecordViewHolder holder) {
        if (likeAnimationsMap.containsKey(holder) || heartAnimationsMap.containsKey(holder)) {
            return;
        }
        //dispatchAnimationFinished(holder);
    }
    private void dispatchChangeFinishedIfAllAnimationsEnded(BitViewHolder holder) {
        if (likeAnimationsMap.containsKey(holder) || heartAnimationsMap.containsKey(holder)) {
            return;
        }
        //dispatchAnimationFinished(holder);
    }

    private void animatePhotoLike(final ItemAdapter.RecordViewHolder holder) {
        holder.vBgLike.setVisibility(View.VISIBLE);
        holder.ivLike.setVisibility(View.VISIBLE);

        holder.vBgLike.setScaleY(0.1f);
        holder.vBgLike.setScaleX(0.1f);
        holder.vBgLike.setAlpha(1f);
        holder.ivLike.setScaleY(0.1f);
        holder.ivLike.setScaleX(0.1f);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator bgScaleYAnim = ObjectAnimator.ofFloat(holder.vBgLike, "scaleY", 0.1f, 1f);
        bgScaleYAnim.setDuration(200);
        bgScaleYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator bgScaleXAnim = ObjectAnimator.ofFloat(holder.vBgLike, "scaleX", 0.1f, 1f);
        bgScaleXAnim.setDuration(200);
        bgScaleXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator bgAlphaAnim = ObjectAnimator.ofFloat(holder.vBgLike, "alpha", 1f, 0f);
        bgAlphaAnim.setDuration(200);
        bgAlphaAnim.setStartDelay(150);
        bgAlphaAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

        ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleY", 0.1f, 1f);
        imgScaleUpYAnim.setDuration(300);
        imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleX", 0.1f, 1f);
        imgScaleUpXAnim.setDuration(300);
        imgScaleUpXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

        ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleY", 1f, 0f);
        imgScaleDownYAnim.setDuration(300);
        imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
        ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleX", 1f, 0f);
        imgScaleDownXAnim.setDuration(300);
        imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        animatorSet.playTogether(bgScaleYAnim, bgScaleXAnim, bgAlphaAnim, imgScaleUpYAnim, imgScaleUpXAnim);
        animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(imgScaleUpYAnim);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                likeAnimationsMap.remove(holder);
                resetLikeAnimationState(holder);
                dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });
        animatorSet.start();

        likeAnimationsMap.put(holder, animatorSet);
    }
    private void resetLikeAnimationState(ItemAdapter.RecordViewHolder holder) {
        holder.vBgLike.setVisibility(View.INVISIBLE);
        holder.ivLike.setVisibility(View.INVISIBLE);
    }
    public void play_call(String musicname)
    {
        try{
            switch (musicname)
            {
                case "angry":
                    player = MediaPlayer.create(mContext, R.raw.angry);
                    player.start();
                    break;
                case "chacha":
                    player =  MediaPlayer.create(mContext, R.raw.chacha);
                    player.start();
                    break;
                case "funky":
                    player = MediaPlayer.create(mContext, R.raw.funky);
                    player.start();
                    break;
                case "happy":
                    player = MediaPlayer.create(mContext, R.raw.happy);
                    player.start();
                    break;
                case "lonely":
                    player = MediaPlayer.create(mContext, R.raw.lonely);
                    player.start();
                    break;
                case "lovely":
                    player = MediaPlayer.create(mContext, R.raw.lovely);
                    player.start();
                    break;
                case "thinking":
                    player = MediaPlayer.create(mContext, R.raw.thinking);
                    player.start();
                    break;
                default:
                    player = MediaPlayer.create(mContext, R.raw.thinking);
                    player.start();
                    break;


            }

        } catch(Exception e){
            e.printStackTrace();
        }

    }
    private void playAudio(String url) throws Exception{
        killMediaPlayer();
        player = new MediaPlayer();
        player.setDataSource(url);
        player.prepare();
        player.start();
    }
    private void killMediaPlayer() {
        if(player != null){
            try {
                player.release();
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }
    public void playstop_call()
    {
        if(player != null){
            playbackPosition = player.getCurrentPosition();
            player.pause();
              }
    }



}