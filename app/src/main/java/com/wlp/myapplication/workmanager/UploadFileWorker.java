package com.wlp.myapplication.workmanager;

import static java.util.jar.Pack200.Unpacker.PROGRESS;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class UploadFileWorker extends Worker {
    public UploadFileWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        setProgressAsync(new Data.Builder().putInt(PROGRESS, 0).build());
    }

    @NonNull
    @Override
    public Result doWork() {
        //设置任务进度
//        setProgressAsync(new Data.Builder().putInt("progress", 100)
//                .build());
//        return Result.success();

        //接收从外面传递进来的数据
        Data inputData = getInputData();
        String filePath = inputData.getString("file");
        String fileUrl = "";//FileUploadManager.upload(filePath);
        //任务完成后返回数据
        if (TextUtils.isEmpty(fileUrl)) {
            return Result.failure();
        } else {
            Data outputData = new Data.Builder().putString("fileUrl", fileUrl)
                    .build();
            return Result.success(outputData);
        }
    }
}

