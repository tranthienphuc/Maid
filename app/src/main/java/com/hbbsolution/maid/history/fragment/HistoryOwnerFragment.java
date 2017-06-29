package com.hbbsolution.maid.history.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hbbsolution.maid.R;
import com.hbbsolution.maid.history.adapter.HistoryOwnerAdapter;
import com.hbbsolution.maid.history.inteface.OwnerHistoryView;
import com.hbbsolution.maid.history.model.owner.OwnerHistory;
import com.hbbsolution.maid.history.presenter.OwnerHistoryPresenter;
import com.hbbsolution.maid.utils.ShowAlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 15/05/2017.
 */

public class HistoryOwnerFragment extends Fragment implements OwnerHistoryView {
    private View v;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private HistoryOwnerAdapter mHistoryOwnerAdapter;
    private TextView tvStartDate, tvEndDate;
    private Calendar cal;
    private Date startDate, endDate;
    private String strStartDate, strEndDate;
    private OwnerHistoryPresenter mOwnerHistoryPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private ProgressBar progressBar;
    private LinearLayout lnNoData;
    private String tempStartDate, tempEndDate;

    public static HistoryOwnerFragment newInstance() {
        HistoryOwnerFragment fragment = new HistoryOwnerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_history_owner, container, false);
        //Gán adapter các thứ
        progressBar = (ProgressBar) v.findViewById(R.id.progressPost);
        progressBar.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        lnNoData = (LinearLayout) v.findViewById(R.id.lnNoData);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycleview_history_owner);
        layoutManager = new LinearLayoutManager(getActivity());
        mOwnerHistoryPresenter = new OwnerHistoryPresenter(this);
        recyclerView.setLayoutManager(layoutManager);

        cal = Calendar.getInstance();

        tvStartDate = (TextView) v.findViewById(R.id.tvStartDate);
        tvStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog1();
            }
        });
        tvEndDate = (TextView) v.findViewById(R.id.tvEndDate);
        tvEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2();
            }
        });

        getTime();

        mOwnerHistoryPresenter.getInfoOwnerHistory(simpleDateFormat.format(endDate));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(startDate!=null)
                        {
                            mOwnerHistoryPresenter.getInfoOwnerHistoryTime(simpleDateFormat.format(startDate),simpleDateFormat.format(endDate));
                        }
                        else {
                            mOwnerHistoryPresenter.getInfoOwnerHistory(simpleDateFormat.format(endDate));
                        }
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
        return v;
    }

    public void showDatePickerDialog1() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date
                String day = String.valueOf(dayOfMonth), month = String.valueOf(monthOfYear);
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                }
                if (monthOfYear + 1 < 10) {
                    month = "0" + (monthOfYear + 1);
                }
                tempStartDate = tvStartDate.getText().toString();
                tvStartDate.setText(
                        day + "/" + month + "/" + year);

                //Lưu vết lại biến ngày hoàn thành
                cal.set(year, monthOfYear, dayOfMonth);
                startDate = cal.getTime();
                if (endDate.getTime() - startDate.getTime() >= 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    mOwnerHistoryPresenter.getInfoOwnerHistoryTime(simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                } else {
                    ShowAlertDialog.showAlert(getResources().getString(R.string.rangetime), getActivity());
                    tvStartDate.setText(tempStartDate);
                }
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s = tvStartDate.getText().toString();
        if (tvStartDate.getText().toString().equals("- - / - - / - - - -")) {
            s = strStartDate;
        }
        String strArrtmp[] = s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(getActivity(), callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày bắt đầu");
        pic.show();
    }

    public void showDatePickerDialog2() {
        DatePickerDialog.OnDateSetListener callback = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear,
                                  int dayOfMonth) {
                //Mỗi lần thay đổi ngày tháng năm thì cập nhật lại TextView Date
                String day = String.valueOf(dayOfMonth), month = String.valueOf(monthOfYear);
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                }
                if (monthOfYear + 1 < 10) {
                    month = "0" + (monthOfYear + 1);
                }
                tempEndDate = tvEndDate.getText().toString();
                tvEndDate.setText(
                        day + "/" + month + "/" + year);

                //Lưu vết lại biến ngày hoàn thành
                cal.set(year, monthOfYear, dayOfMonth);
                endDate = cal.getTime();
                if (startDate != null) {
                    if (endDate.getTime() - startDate.getTime() >= 0) {
                        progressBar.setVisibility(View.VISIBLE);
                        mOwnerHistoryPresenter.getInfoOwnerHistoryTime(simpleDateFormat.format(startDate), simpleDateFormat.format(endDate));
                    } else {
                        ShowAlertDialog.showAlert(getResources().getString(R.string.rangetime), getActivity());
                        tvEndDate.setText(tempEndDate);
                    }
                } else {
                    view.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    mOwnerHistoryPresenter.getInfoOwnerHistory(simpleDateFormat.format(endDate));
                }
            }
        };
        //các lệnh dưới này xử lý ngày giờ trong DatePickerDialog
        //sẽ giống với trên TextView khi mở nó lên
        String s = tvEndDate.getText().toString();
        String strArrtmp[] = s.split("/");
        int ngay = Integer.parseInt(strArrtmp[0]);
        int thang = Integer.parseInt(strArrtmp[1]) - 1;
        int nam = Integer.parseInt(strArrtmp[2]);
        DatePickerDialog pic = new DatePickerDialog(getActivity(), callback, nam, thang, ngay);
        pic.setTitle("Chọn ngày kết thúc");
        pic.show();
    }

    public void getTime() {
        endDate = new Date();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        Date myDate = new Date();
        strEndDate = date.format(myDate);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(myDate);
//        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date newDate = new Date();
        strStartDate = date.format(newDate);
        tvStartDate.setText("- - / - - / - - - -");
        tvEndDate.setText(strEndDate);
    }

    @Override
    public void getInfoOwnerHistory(List<OwnerHistory> datumList) {
        mHistoryOwnerAdapter = new HistoryOwnerAdapter(getActivity(), datumList);
        mHistoryOwnerAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mHistoryOwnerAdapter);
        if(datumList.size()>0) {
            lnNoData.setVisibility(View.GONE);
        }
        else
        {
            lnNoData.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void getInfoOwnerHistoryFail() {
        lnNoData.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
