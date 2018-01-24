package com.stepcounter.charting.interfaces.dataprovider;

import com.stepcounter.charting.components.YAxis.AxisDependency;
import com.stepcounter.charting.data.BarLineScatterCandleBubbleData;
import com.stepcounter.charting.utils.Transformer;

public interface BarLineScatterCandleBubbleDataProvider extends ChartInterface {

    Transformer getTransformer(AxisDependency axis);
    boolean isInverted(AxisDependency axis);
    
    float getLowestVisibleX();
    float getHighestVisibleX();

    BarLineScatterCandleBubbleData getData();
}
