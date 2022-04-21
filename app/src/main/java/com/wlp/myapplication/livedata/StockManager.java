package com.wlp.myapplication.livedata;

import java.math.BigDecimal;

/**
 * Created by wlp on 2022/4/21
 * Description:
 */
public class StockManager {
    public StockManager(String symbol){

    }

    public void requestPriceUpdates(SimplePriceListener listener){
        //listener.onPriceChanged();
    }

    public void removeUpdates(SimplePriceListener listener){

    }

    public interface SimplePriceListener{
        void onPriceChanged(BigDecimal price);
    }
}
