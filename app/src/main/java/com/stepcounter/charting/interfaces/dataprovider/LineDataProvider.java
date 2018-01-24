package com.stepcounter.charting.interfaces.dataprovider;

import com.stepcounter.charting.components.YAxis;
import com.stepcounter.charting.data.LineData;

public interface LineDataProvider extends BarLineScatterCandleBubbleDataProvider {

    LineData getLineData();

    YAxis getAxis(YAxis.AxisDependency dependency);
}
