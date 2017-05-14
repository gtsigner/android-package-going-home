package com.onynet.a30home.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.onynet.a30home.R;

import java.util.Arrays;


/**
 * Created by Aislli on 2015/12/29.
 */
public class ARecycleView extends FrameLayout {
    private int mPadding;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private boolean mClipToPadding;
    private int mEmptyId;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ViewStub mEmpty;
    private View mEmptyView;
    private ARecyclerBaseAdapter mAdapter;
    private boolean isLoadingMore;
    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerView.LayoutManager mLayoutManager;
    /**
     * 数据总条数
     */
    private int mTotal;
    private int mMoreProgressId;
    private ViewStub mMoreProgress;
    private View mMoreProgressView;
    /**
     * 是否可以显示加载更多
     */
    private boolean canShowLoadmore;
    int itemCount = 0;

    public ARecycleView(Context context) {
        super(context);
        initViews();
    }

    public ARecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initViews();
    }

    public ARecycleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initViews();
    }

    protected void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.AislliRecyclerview);

        try {
            mPadding = (int) typedArray.getDimension(R.styleable.AislliRecyclerview_Padding, -1.1f);
            mPaddingTop = (int) typedArray.getDimension(R.styleable.AislliRecyclerview_PaddingTop, 0.0f);
            mPaddingBottom = (int) typedArray.getDimension(R.styleable.AislliRecyclerview_PaddingBottom, 0.0f);
            mPaddingLeft = (int) typedArray.getDimension(R.styleable.AislliRecyclerview_PaddingLeft, 0.0f);
            mPaddingRight = (int) typedArray.getDimension(R.styleable.AislliRecyclerview_PaddingRight, 0.0f);
            mClipToPadding = typedArray.getBoolean(R.styleable.AislliRecyclerview_ClipToPadding, false);
            mEmptyId = typedArray.getResourceId(R.styleable.AislliRecyclerview_EmptyView, 0);
            mMoreProgressId = typedArray.getResourceId(R.styleable.AislliRecyclerview_layout_moreProgress, R.layout.view_more_progress);
        } finally {
            typedArray.recycle();
        }
    }

    protected void initViews() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_recyclerview, this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources()
                        .getDisplayMetrics()));
        if (mRecyclerView != null) {
            mRecyclerView.setClipToPadding(mClipToPadding);
            if (mPadding > 0) {
                mRecyclerView.setPadding(mPadding, mPadding, mPadding, mPadding);
            } else {
                mRecyclerView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            }
        }

        setScrollListener();

        mMoreProgress = (ViewStub) view.findViewById(R.id.more_progress);
        mMoreProgress.setLayoutResource(mMoreProgressId);
        if (mMoreProgressId != 0)
            mMoreProgressView = mMoreProgress.inflate();
        mMoreProgress.setVisibility(View.GONE);

        mEmpty = (ViewStub) view.findViewById(R.id.emptyview);
        mEmpty.setLayoutResource(mEmptyId);

        if (mEmptyId != 0)
            mEmptyView = mEmpty.inflate();
        mEmpty.setVisibility(View.GONE);

    }

    private void setScrollListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (null == onLoadMoreListener || isLoadingMore) {
                    return;
                }
                int currentCount = 0;
                if (mLayoutManager instanceof LinearLayoutManager) {
                    currentCount = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition() + 1;
                } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                    int[] lastVisibleItemPositions = ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(null);
                    Arrays.sort(lastVisibleItemPositions);
                    currentCount = lastVisibleItemPositions[lastVisibleItemPositions.length - 1] + 1;
                }
                itemCount = mAdapter.getItemCount();
                if (canShowLoadmore && newState == RecyclerView.SCROLL_STATE_IDLE
                        && currentCount == itemCount) {
                    if (mTotal > 0 && currentCount < mTotal) {
                        isLoadingMore = true;
                        mMoreProgressView.setVisibility(VISIBLE);
                        onLoadMoreListener.loadMore();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mLayoutManager = manager;
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    public void setAdapter(ARecyclerBaseAdapter adapter) {
        mAdapter = adapter;
        mRecyclerView.setAdapter(mAdapter);
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        if (mAdapter != null)
            mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    super.onItemRangeChanged(positionStart, itemCount);
                    updateHelperDisplays();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    updateHelperDisplays();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    super.onItemRangeRemoved(positionStart, itemCount);
                    updateHelperDisplays();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                    updateHelperDisplays();
                }

                @Override
                public void onChanged() {
                    super.onChanged();
                    updateHelperDisplays();
                }
            });
        if ((adapter == null || mAdapter.getItemCount() == 0) && mEmptyId != 0) {
            mEmpty.setVisibility(View.VISIBLE);
        }
    }

    private void updateHelperDisplays() {
        isLoadingMore = false;
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        if (mAdapter == null)
            return;
        if (mAdapter.getItemCount() == 0) {
            mEmpty.setVisibility(mEmptyId != 0 ? View.VISIBLE : View.GONE);
        } else if (mEmptyId != 0) {
            mEmpty.setVisibility(View.GONE);
        }
    }

    /**
     * Sets the {@link RecyclerView.ItemAnimator} that will handle animations involving changes
     * to the items in this RecyclerView. By default, RecyclerView instantiates and
     * uses an instance of {@link android.support.v7.widget.DefaultItemAnimator}. Whether item animations are enabled for the RecyclerView depends on the ItemAnimator and whether
     * the LayoutManager {@link android.support.v7.widget.RecyclerView.LayoutManager#supportsPredictiveItemAnimations()
     * supports item animations}.
     *
     * @param animator The ItemAnimator being set. If null, no animations will occur
     *                 when changes occur to the items in this RecyclerView.
     */
    public void setItemAnimator(RecyclerView.ItemAnimator animator) {
        mRecyclerView.setItemAnimator(animator);
    }

    public void setHasFixedSize(boolean hasFixedSize) {
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }

    public void setRefreshing(boolean refreshing) {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(refreshing);
        mLayoutManager.scrollToPosition(0);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener listener) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setOnRefreshListener(listener);
    }

    /**
     * Set the load more listener of recyclerview
     *
     * @param onLoadMoreListener load listen
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setTotal(int mTotal) {
        this.mTotal = mTotal;
        canShowLoadmore = true;
    }

    public void showMoreProgress() {
        mMoreProgress.setVisibility(View.VISIBLE);

    }

    public void hideMoreProgress() {
        mMoreProgress.setVisibility(View.GONE);
        canShowLoadmore = true;
        mLayoutManager.scrollToPosition(itemCount);
    }

    public void hideLoading() {
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setRefreshing(false);
        mMoreProgress.setVisibility(View.GONE);
        canShowLoadmore = true;
    }

    private static boolean isParallaxHeader = false;

    public static class CustomRelativeWrapper extends RelativeLayout {

        private int mOffset;

        public CustomRelativeWrapper(Context context) {
            super(context);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            if (isParallaxHeader)
                canvas.clipRect(new Rect(getLeft(), getTop(), getRight(), getBottom() + mOffset));
            super.dispatchDraw(canvas);
        }

        public void setClipY(int offset) {
            mOffset = offset;
            invalidate();
        }
    }

    /**
     * Add an {@link RecyclerView.ItemDecoration} to this RecyclerView. Item decorations can affect both measurement and drawing of individual item views. Item decorations are ordered. Decorations placed earlier in the list will be run/queried/drawn first for their effects on item views. Padding added to views will be nested; a padding added by an earlier decoration will mean further item decorations in the list will be asked to draw/pad within the previous decoration's given area.
     *
     * @param itemDecoration Decoration to add
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }
}
