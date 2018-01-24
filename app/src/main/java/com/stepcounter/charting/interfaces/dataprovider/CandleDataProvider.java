package com.stepcounter.charting.interfaces.dataprovider;

import com.stepcounter.charting.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    CandleData getCandleData();
}
