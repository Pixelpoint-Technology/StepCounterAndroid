package com.stepcounter.charting.interfaces.dataprovider;

import com.stepcounter.charting.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleBubbleDataProvider {

    ScatterData getScatterData();
}
