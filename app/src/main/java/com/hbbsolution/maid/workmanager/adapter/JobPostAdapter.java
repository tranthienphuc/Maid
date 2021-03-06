package com.hbbsolution.maid.workmanager.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.daimajia.swipe.SwipeLayout;
import com.hbbsolution.maid.R;
import com.hbbsolution.maid.utils.SessionManagerUser;
import com.hbbsolution.maid.utils.WorkTimeValidate;
import com.hbbsolution.maid.workmanager.listworkmanager.model.workmanager.Datum;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tantr on 6/6/2017.
 */

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.JobPostViewHolder> {

    private Context context;
    private List<Datum> datumList;
    private Callback callback;
    private int tabJob;
    private SessionManagerUser sessionManagerUser;
    private HashMap<String, String> userData;
    private String idMaid;


    public JobPostAdapter(Context context, List<Datum> datumList, int tabJob) {
        this.context = context;
        this.datumList = datumList;
        this.tabJob = tabJob;
        sessionManagerUser = new SessionManagerUser(context);
        userData = sessionManagerUser.getUserDetails();
        idMaid = userData.get(SessionManagerUser.KEY_ID);
//         idMaid = "5923c12f7d7da13b240e7321";
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public JobPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = View.inflate(context, R.layout.item_job_post, null);
        return new JobPostAdapter.JobPostViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(JobPostViewHolder holder, int position) {

        switch (tabJob) {
            case 1:
                holder.txtType.setText(context.getResources().getString(R.string.awaiting_assignment));
                break;
            case 2:
                holder.txtType.setText(context.getResources().getString(R.string.pending_confirmation));
                break;
        }
        final Datum mDatum = datumList.get(position);

//        String idMaid = "5923c12f7d7da13b240e7321";


        try {
            int requestSize = 0;
            if (mDatum.getStakeholders() != null && mDatum.getStakeholders().getRequest() != null) {
                requestSize = mDatum.getStakeholders().getRequest().size();
            }
            for (int i = 0; i < requestSize; i++) {
                String _idMaid = mDatum.getStakeholders().getRequest().get(i).getMaid();
                if (_idMaid.equals(idMaid)) {
                    String mTimeOfregister = mDatum.getStakeholders().getRequest().get(i).getTime();
                    WorkTimeValidate.setWorkTimeRegister(context, holder.txtTimePostHistory, mTimeOfregister);
//                setWorkTimeRegister(holder.txtTimePostHistory,mTimeOfregister );
                    break;
                }
            }

            holder.txtTitleJobPost.setText(mDatum.getInfo().getTitle());
            if (mDatum != null && mDatum.getInfo() != null && mDatum.getInfo().getWork() != null && mDatum.getInfo().getWork().getImage() != null) {
                Glide.with(context).load(mDatum.getInfo().getWork().getImage())
                        .placeholder(R.drawable.no_image)
                        .thumbnail(0.5f)
                        .dontAnimate()
                        .error(R.drawable.no_image)
                        .into(holder.imgTypeJobPost);
            }

            String mDatePostHistory = WorkTimeValidate.getDatePostHistory(mDatum.getInfo().getTime().getEndAt());
            holder.txtDatePostHistory.setText(mDatePostHistory);

            String mStartTime = WorkTimeValidate.getTimeWorkLanguage(context, mDatum.getInfo().getTime().getStartAt());
            String mEndTime = WorkTimeValidate.getTimeWorkLanguage(context, mDatum.getInfo().getTime().getEndAt());
            holder.txtTimeDoingPost.setText(mStartTime + " - " + mEndTime);

            if (!WorkTimeValidate.compareDays(mDatum.getInfo().getTime().getEndAt())) {
                holder.txtExpired.setVisibility(View.VISIBLE);
                holder.lo_background.setVisibility(View.VISIBLE);
                holder.txtRequestDirect.setVisibility(View.GONE);
            } else {
                holder.txtExpired.setVisibility(View.GONE);
                holder.lo_background.setVisibility(View.GONE);
                if (mDatum.getProcess().getId().equals("000000000000000000000006")) {
                    holder.txtRequestDirect.setVisibility(View.VISIBLE);
                } else {
                    holder.txtRequestDirect.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {

        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (callback != null) {
//                    callback.onItemClickDetail(mDatum);
//                }
//            }
//        });
//
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if (callback != null) {
//                    callback.onItemLongClick(mDatum);
//                }
//                return true;
//            }
//        });

        holder.linearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onItemClickDetail(mDatum);
                }
            }
        });

        holder.linearDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    callback.onItemClickDelete(mDatum);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return datumList.size();
    }

    public class JobPostViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTimePostHistory, txtDatePostHistory, txtTimeDoingPost,
                txtTitleJobPost, txtExpired, txtType, txtRequestDirect;
        private ImageView imgTypeJobPost;
        private LinearLayout lo_background ;

        private SwipeLayout swipeLayout;
        private LinearLayout linearData, linearDelete;

        public JobPostViewHolder(View itemView) {
            super(itemView);
            txtTitleJobPost = (TextView) itemView.findViewById(R.id.txtTitleJobPost);
            txtTimePostHistory = (TextView) itemView.findViewById(R.id.txtTimePostHistory);
            txtDatePostHistory = (TextView) itemView.findViewById(R.id.txtDatePostHistory);
            txtTimeDoingPost = (TextView) itemView.findViewById(R.id.txtTimeDoingPost);
            imgTypeJobPost = (ImageView) itemView.findViewById(R.id.imgTypeJobPost);
            txtExpired = (TextView) itemView.findViewById(R.id.txtExpired_request_detail_post);
            lo_background = (LinearLayout) itemView.findViewById(R.id.lo_background);
            txtType = (TextView) itemView.findViewById(R.id.txtType);
            txtRequestDirect = (TextView) itemView.findViewById(R.id.txtExpired_request_direct_detail);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe_post);
            linearData = (LinearLayout) itemView.findViewById(R.id.linear_data);
            linearDelete = (LinearLayout) itemView.findViewById(R.id.bottom_delete);

            //config swipe
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            swipeLayout.addDrag(SwipeLayout.DragEdge.Left, itemView.findViewById(R.id.bottom_delete));
            swipeLayout.setRightSwipeEnabled(true);
            swipeLayout.setLeftSwipeEnabled(false);
        }
    }

    private void setWorkTimeRegister(TextView txtTimePostHistory, String _timePostHistory) {
        String[] mWorkTimeHistory = WorkTimeValidate.workTimeValidate(_timePostHistory);
        if (!mWorkTimeHistory[2].equals("0")) {
            txtTimePostHistory.setText(mWorkTimeHistory[2] + " " + context.getResources().getString(R.string.before, context.getResources().getQuantityString(R.plurals.day, Integer.parseInt(mWorkTimeHistory[2]))));
        } else if (!mWorkTimeHistory[1].equals("0")) {
            txtTimePostHistory.setText(mWorkTimeHistory[1] + " " + context.getResources().getString(R.string.before, context.getResources().getQuantityString(R.plurals.hour, Integer.parseInt(mWorkTimeHistory[1]))));
        } else if (!mWorkTimeHistory[0].equals("0")) {
            txtTimePostHistory.setText(mWorkTimeHistory[0] + " " + context.getResources().getString(R.string.before, context.getResources().getQuantityString(R.plurals.minute, Integer.parseInt(mWorkTimeHistory[0]))));
        }
    }

    public interface Callback {
        void onItemClickDetail(Datum mDatum);

        void onItemClickDelete(Datum mDatum);

        void onItemLongClick(Datum mDatum);
    }
}
