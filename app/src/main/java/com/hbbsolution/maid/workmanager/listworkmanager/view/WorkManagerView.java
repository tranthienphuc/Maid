package com.hbbsolution.maid.workmanager.listworkmanager.view;

import com.hbbsolution.maid.base.ConnectionInterface;
import com.hbbsolution.maid.workmanager.detailworkmanager.model.JobPendingResponse;
import com.hbbsolution.maid.workmanager.listworkmanager.model.workmanager.WorkManagerResponse;

/**
 * Created by tantr on 5/10/2017.
 */

public interface WorkManagerView extends ConnectionInterface{
    void getInfoJob(WorkManagerResponse mExample);
//    void getInfoJobPending(JobPendingResponse mJobPendingResponse);
    void displayNotifyJobPost(JobPendingResponse isJobPost);
//    void authenticationFailed();
    void getError();
}
