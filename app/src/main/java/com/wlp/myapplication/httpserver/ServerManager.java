package com.wlp.myapplication.httpserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 *  mServerManager = new ServerManager(this, this);
 *  registerScreenBroadcastReceiver();
 *  mServerManager.register();
 *  mServerManager.startServer();//启动服务
 *  mServerManager.stopServer();
 *
 *  CoreService extend Service
 *  Mgr 内部使用广播分发数据
 */
public class ServerManager extends BroadcastReceiver {

    private static final String ACTION = "com.yanzhenjie.andserver.receiver";

    private static final String CMD_KEY = "CMD_KEY";
    private static final String MESSAGE_KEY = "MESSAGE_KEY";

    private static final int CMD_VALUE_START = 1;
    private static final int CMD_VALUE_ERROR = 2;
    private static final int CMD_VALUE_STOP = 4;

    private OnServerListener mOnServerListener;

    /**
     * Notify serverStart.
     *
     * @param context context.
     */
    public static void onServerStart(Context context, String hostAddress) {
        sendBroadcast(context, CMD_VALUE_START, hostAddress);
    }

    /**
     * Notify serverStop.
     *
     * @param context context.
     */
    public static void onServerError(Context context, String error) {
        sendBroadcast(context, CMD_VALUE_ERROR, error);
    }

    /**
     * Notify serverStop.
     *
     * @param context context.
     */
    public static void onServerStop(Context context) {
        sendBroadcast(context, CMD_VALUE_STOP);
    }

    private static void sendBroadcast(Context context, int cmd) {
        sendBroadcast(context, cmd, null);
    }

    private static void sendBroadcast(Context context, int cmd, String message) {
        Intent broadcast = new Intent(ACTION);
        broadcast.putExtra(CMD_KEY, cmd);
        broadcast.putExtra(MESSAGE_KEY, message);
        context.sendBroadcast(broadcast);
    }

    private Context mContext;

    private Intent mService;

    public ServerManager(Context context, OnServerListener serverListener) {
        mOnServerListener = serverListener;
        mContext = context;
        mService = new Intent(context, CoreService.class);
    }

    /**
     * Register broadcast.
     */
    public void register() {
        IntentFilter filter = new IntentFilter(ACTION);
        mContext.registerReceiver(this, filter);
    }

    /**
     * UnRegister broadcast.
     */
    public void unRegister() {
        mContext.unregisterReceiver(this);
    }

    public void startServer() {
        mContext.startService(mService);
    }

    public void stopServer() {
        mContext.stopService(mService);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION.equals(action)) {
            int cmd = intent.getIntExtra(CMD_KEY, 0);
            switch (cmd) {
                case CMD_VALUE_START: {
                    String ip = intent.getStringExtra(MESSAGE_KEY);
                    if (null != mOnServerListener)
                        mOnServerListener.onServerStart(ip);
                    break;
                }
                case CMD_VALUE_ERROR: {
                    String error = intent.getStringExtra(MESSAGE_KEY);
                    if (null != mOnServerListener)
                        mOnServerListener.onServerError(error);
                    break;
                }
                case CMD_VALUE_STOP: {
                    if (null != mOnServerListener)
                        mOnServerListener.onServerStop();
                    break;
                }
            }
        }
    }

    public interface OnServerListener {

        void onServerStart(String ip);

        void onServerError(String error);

        void onServerStop();
    }
}