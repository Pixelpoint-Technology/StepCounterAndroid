package com.stepcounter.charting.interfaces.dataprovider;

import com.stepcounter.charting.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleBubbleDataProvider {

    BubbleData getBubbleData();
}
