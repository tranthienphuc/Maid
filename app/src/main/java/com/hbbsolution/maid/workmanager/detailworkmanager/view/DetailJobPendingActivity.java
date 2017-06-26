package com.hbbsolution.maid.workmanager.detailworkmanager.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbbsolution.maid.R;
import com.hbbsolution.maid.home.owner_profile.view.OwnerProfileActivity;
import com.hbbsolution.maid.utils.ShowAlertDialog;
import com.hbbsolution.maid.utils.WorkTimeValidate;
import com.hbbsolution.maid.workmanager.detailworkmanager.presenter.DetailJobPendingPresenter;
import com.hbbsolution.maid.workmanager.listworkmanager.model.workmanager.Datum;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by tantr on 6/6/2017.
 */

public class DetailJobPendingActivity extends AppCompatActivity implements DetailJobPostView, View.OnClickListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.lo_infoOwner)
    RelativeLayout lo_infoOwner;
    @BindView(R.id.img_avatarQwner)
    ImageView img_avatarQwner;
    @BindView(R.id.txtNameOwner)
    TextView txtNameOwner;
    @BindView(R.id.txtAddressOwner)
    TextView txtAddressOwner;
    @BindView(R.id.txtTitle_job_detail_post)
    TextView txtTitle_job_detail_post;
    @BindView(R.id.txtType_job_detail_post)
    TextView txtType_job_detail_post;
    @BindView(R.id.txtContent_job_detail_psot)
    TextView txtContent_job_detail_psot;
    @BindView(R.id.txtPrice_job_detail_post)
    TextView txtPrice_job_detail_post;
    @BindView(R.id.txtDate_job_detail_post)
    TextView txtDate_job_detail_post;
    @BindView(R.id.txtTime_work_doing_detail_post)
    TextView txtTime_work_doing_detail_post;
    @BindView(R.id.txtAddress_detail_post)
    TextView txtAddress_detail_post;
    @BindView(R.id.imgType_job_detail_post)
    ImageView imgType_job_detail_post;
    @BindView(R.id.lo_clear_job_pending)
    LinearLayout lo_clear_job;
    @BindView(R.id.progressDetailJobPending)
    ProgressBar progressBar;
    @BindView(R.id.txtIsTools)
    TextView txtIsTools;
    @BindView(R.id.txtExpired_request_detail_post)
    TextView txtExpired_request_detail_post;
    private DetailJobPendingPresenter mDetailJobPostPresenter;

    private Datum mDatum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_job_pending);
//        mDetailJobPostActivity = this;
        ButterKnife.bind(this);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lo_infoOwner.setOnClickListener(this);
        lo_clear_job.setOnClickListener(this);

        mDetailJobPostPresenter = new DetailJobPendingPresenter(this);

        final Intent intent = getIntent();
        mDatum = (Datum) intent.getSerializableExtra("mDatum");
        if (!WorkTimeValidate.compareDays(mDatum.getInfo().getTime().getEndAt())) {
            txtExpired_request_detail_post.setVisibility(View.VISIBLE);
        } else {
            txtExpired_request_detail_post.setVisibility(View.GONE);

        }

        if (mDatum.getInfo().getTools()) {
            txtIsTools.setVisibility(View.VISIBLE);
        } else {
            txtIsTools.setVisibility(View.GONE);
        }

        txtNameOwner.setText(mDatum.getStakeholders().getOwner().getInfo().getUsername());
        txtAddressOwner.setText((mDatum.getStakeholders().getOwner().getInfo().getAddress().getName()));
        txtTitle_job_detail_post.setText(mDatum.getInfo().getTitle());
        txtType_job_detail_post.setText(mDatum.getInfo().getWork().getName());
        txtContent_job_detail_psot.setText(mDatum.getInfo().getDescription());
        txtPrice_job_detail_post.setText(formatPrice(mDatum.getInfo().getPrice()));
        txtAddress_detail_post.setText(mDatum.getInfo().getAddress().getName());
        txtDate_job_detail_post.setText(WorkTimeValidate.getDatePostHistory(mDatum.getHistory().getUpdateAt()));
        txtTime_work_doing_detail_post.setText(WorkTimeValidate.getTimeWork(mDatum.getInfo().getTime().getStartAt()) + " - " + WorkTimeValidate.getTimeWork(mDatum.getInfo().getTime().getEndAt()));
        Picasso.with(this).load(mDatum.getInfo().getWork().getImage())
                .error(R.drawable.no_image)
                .placeholder(R.drawable.no_image)
                .into(imgType_job_detail_post);
        Picasso.with(this).load(mDatum.getStakeholders().getOwner().getInfo().getImage())
                .error(R.drawable.avatar)
                .placeholder(R.drawable.avatar)
                .into(img_avatarQwner);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            EventBus.getDefault().postSticky(false);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lo_clear_job_pending:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setCancelable(false);
                alertDialog.setTitle(getResources().getString(R.string.notification));
                alertDialog.setMessage(getResources().getString(R.string.notification_del_job_post));
                alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        progressBar.setVisibility(View.VISIBLE);
                        mDetailJobPostPresenter.deleteJob(mDatum.getId(), mDatum.getStakeholders().getOwner().getId());
                    }
                });
                alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
                break;
            case R.id.lo_infoOwner:
                Intent itInfoUser = new Intent(DetailJobPendingActivity.this, OwnerProfileActivity.class);
                itInfoUser.putExtra("InfoOwner",mDatum.getStakeholders().getOwner());
                startActivity(itInfoUser);
                break;
        }

    }

    private String formatPrice(Integer _Price) {
        String mOutputPrice = null;
        if (_Price != null && _Price != 0) {
            DecimalFormat myFormatter = new DecimalFormat("#,###,##0");
            mOutputPrice = myFormatter.format(_Price);
        } else if (_Price == 0) {
            mOutputPrice = "Tính tiền theo thời gian";
        }
        return mOutputPrice;
    }


    @Override
    public void displayNotifyJobPost(boolean isJobPost) {
        progressBar.setVisibility(View.GONE);
        if (isJobPost) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle(getResources().getString(R.string.notification));
            alertDialog.setMessage(getResources().getString(R.string.notification__pass_del_job_post));
            alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    EventBus.getDefault().postSticky(true);
                    finish();
                }
            });

            alertDialog.show();
        } else {
            ShowAlertDialog.showAlert("Thất bại", DetailJobPendingActivity.this);
        }
    }

    @Override
    public void displayError(String error) {

    }

    @Override
    public void displayNotifyAccceptJobRequested(boolean isJobPost) {

    }

    @Override
    public void displayErrorAccceptJobRequested(String error) {

    }

    @Override
    public void displayNotifyRefuseJobRequested(boolean isJobPost) {

    }

    @Override
    public void displayErrorRefuseJobRequested(String error) {

    }

}
