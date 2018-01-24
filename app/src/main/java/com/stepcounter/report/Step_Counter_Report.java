package com.stepcounter.report;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.stepcounter.R;
import com.stepcounter.charting.DemoBase;
import com.stepcounter.charting.animation.Easing;
import com.stepcounter.charting.charts.PieChart;
import com.stepcounter.charting.components.Legend;
import com.stepcounter.charting.components.LegendEntry;
import com.stepcounter.charting.data.Entry;
import com.stepcounter.charting.data.PieData;
import com.stepcounter.charting.data.PieDataSet;
import com.stepcounter.charting.data.PieEntry;
import com.stepcounter.charting.formatter.PercentFormatter;
import com.stepcounter.charting.highlight.Highlight;
import com.stepcounter.charting.listener.OnChartValueSelectedListener;
import com.stepcounter.charting.utils.ColorTemplate;
import com.stepcounter.data.DatabaseHandler;
import com.stepcounter.data.ReportModel;


import java.util.ArrayList;

public class Step_Counter_Report extends DemoBase implements OnSeekBarChangeListener, OnChartValueSelectedListener {

    Context context;
    private PieChart mChart;
    private SeekBar mSeekBarX, mSeekBarY;
    private TextView tvX, tvY;
    TextView tv_dates,tv_heading;
    ImageView iv_start,iv_end,iv_back;
    DatabaseHandler db;
    ReportModel reportModel;
    ArrayList<ReportModel> mlist;
    ArrayList<String> steps_cc=new ArrayList<>();
    ArrayList<String> dates=new ArrayList<>();
    ArrayList<String> max_stps=new ArrayList<>();
    boolean display;
    int total_stp;
    boolean pre_clickable=true,for_clickable=false;
    int list_from=0,list_to=7,temp_to,temp_from;
    ArrayList<Integer> colors = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step__counter__report);

        context      = this;
        tvX          = (TextView) findViewById(R.id.tvXMax);
        tvY          = (TextView) findViewById(R.id.tvYMax);
        tv_dates     = (TextView) findViewById(R.id.tv_date);
        tv_heading   = (TextView) findViewById(R.id.tv_heading);
        iv_start     = (ImageView) findViewById(R.id.iv_start);
        iv_end       = (ImageView) findViewById(R.id.iv_end);
        iv_back      = (ImageView) findViewById(R.id.im_backbutton);

        mSeekBarX     = (SeekBar) findViewById(R.id.seekBar1);
        mSeekBarY     = (SeekBar) findViewById(R.id.seekBar2);

        db            = new DatabaseHandler(context);
        reportModel   = new ReportModel();
        mlist         = db.getSteps("0");

        setDefaultData();

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlist.size()>7 && pre_clickable){
                    for_clickable=true;
                    if (list_from>=7) {
                        steps_cc = new ArrayList<>();
                        dates = new ArrayList<>();
                        reportModel = new ReportModel();
                        total_stp=0;
                        for (int i = list_from-7; i < list_from; i++) {
                            reportModel = mlist.get(i);
                            steps_cc.add(String.valueOf(reportModel.getTotal_steps()));
                            dates.add(reportModel.getDate());
                            max_stps.add(String.valueOf(reportModel.getMax_steps()));
                            total_stp = total_stp + reportModel.getTotal_steps();

                            if (i == list_from-7) {
                                tv_dates.setText("");
                                tv_dates.setText(reportModel.getDate());
                                temp_from=i;
                            }
                            if (i == list_from-1) {
                                tv_dates.setText(tv_dates.getText().toString() + " - " + reportModel.getDate());
                                list_to=i;
                            }
                        }
                        list_from=temp_from;
                    }
                    else {
                        pre_clickable=false;
                        reportModel = new ReportModel();
                        steps_cc    = new ArrayList<>();
                        dates       = new ArrayList<>();
                        total_stp=0;
                        for (int i = 0; i < 7; i++) {
                            reportModel = mlist.get(i);
                            steps_cc.add(String.valueOf(reportModel.getTotal_steps()));
                            dates.add(reportModel.getDate());
                            max_stps.add(String.valueOf(reportModel.getMax_steps()));
                            total_stp = total_stp + reportModel.getTotal_steps();
                            if (i == 0) {
                                tv_dates.setText("");
                                tv_dates.setText(reportModel.getDate());
                                list_from=i;
                            }
                            if (i == 6) {
                                tv_dates.setText(tv_dates.getText().toString() + " - " + reportModel.getDate());
                                list_to=i;
                            }
                        }
                    }

                    setData(7,total_stp);
                    mChart.setCenterText(generateCenterSpannableText(String.valueOf(total_stp)));
                    if (list_from==0)
                        pre_clickable=false;

                }
            }
        });

        iv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mlist.size()>7 && for_clickable){
                    pre_clickable=true;
                    if (list_to+7 < mlist.size()) {
                        steps_cc = new ArrayList<>();
                        dates = new ArrayList<>();
                        reportModel = new ReportModel();
                        total_stp=0;
                        for (int i = list_to+1; i < list_to+8; i++) {
                            reportModel = mlist.get(i);
                            steps_cc.add(String.valueOf(reportModel.getTotal_steps()));
                            dates.add(reportModel.getDate());
                            max_stps.add(String.valueOf(reportModel.getMax_steps()));
                            total_stp = total_stp + reportModel.getTotal_steps();
                            if (i == list_to+1) {
                                tv_dates.setText("");
                                tv_dates.setText(reportModel.getDate());
                                list_from=i;
                            }
                            if (i == list_to+7) {
                                tv_dates.setText(tv_dates.getText().toString() + " - " + reportModel.getDate());
                                temp_to=i;
                            }
                        }
                        list_to=temp_to;
                    }
                    else {
                        steps_cc    = new ArrayList<>();
                        dates       = new ArrayList<>();
                        reportModel = new ReportModel();
                        total_stp=0;
                        for_clickable=false;
                        // int sets=mlist.size()%7;
                        for (int i=mlist.size()-7;i<mlist.size();i++){
                            reportModel = mlist.get(i);
                            steps_cc.add(String.valueOf(reportModel.getTotal_steps()));
                            dates.add(reportModel.getDate());
                            max_stps.add(String.valueOf(reportModel.getMax_steps()));
                            total_stp = total_stp + reportModel.getTotal_steps();
                            if (i == mlist.size()-7) {
                                tv_dates.setText("");
                                tv_dates.setText(reportModel.getDate());
                                list_from=i;
                            }
                            if (i == mlist.size()-1) {
                                tv_dates.setText(tv_dates.getText().toString() + " - " + reportModel.getDate());
                                list_to=i;
                            }
                        }
                    }
                    setData(7,total_stp);
                    mChart.setCenterText(generateCenterSpannableText(String.valueOf(total_stp)));
                    if (list_to==mlist.size()-1)
                        for_clickable=false;

                }
            }
        });
    }

    public void setDefaultData(){
        if (mlist.size()<7) {
            mSeekBarX.setProgress(mlist.size());
            mSeekBarX.setMax(mlist.size());
            tvX.setText(String.valueOf(mlist.size()));
        }
        else
            mSeekBarX.setProgress(7);
        mSeekBarY.setProgress(10);

        mChart = (PieChart) findViewById(R.id.chart1);
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setCenterTextTypeface(mTfLight);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        //setData(4, 100);

        if (mlist.size()<=7) {
            reportModel = new ReportModel();
            steps_cc    = new ArrayList<>();
            dates       = new ArrayList<>();
            for (int i = 0; i < mlist.size(); i++) {
                reportModel = mlist.get(i);
                Log.e("Total steps", String.valueOf(reportModel.getTotal_steps()));
                Log.e("Max steps", String.valueOf(reportModel.getMax_steps()));
                Log.e("Total time", String.valueOf(reportModel.getTotal_time()));
                Log.e("Date", reportModel.getDate());
                steps_cc.add(String.valueOf(reportModel.getTotal_steps()));
                dates.add(reportModel.getDate());
                max_stps.add(String.valueOf(reportModel.getMax_steps()));
                total_stp = total_stp + reportModel.getTotal_steps();
                if (i == 0) {
                    tv_dates.setText("");
                    tv_dates.setText(reportModel.getDate());
                    list_from=i;
                }
                if (i == mlist.size() - 1) {
                    tv_dates.setText(tv_dates.getText().toString() + " - " + reportModel.getDate());
                    list_to=mlist.size();
                }
            }

            setData(mlist.size(), total_stp);
        }
        else {
            steps_cc    = new ArrayList<>();
            dates       = new ArrayList<>();
            reportModel = new ReportModel();
            for (int i=mlist.size()-7;i<mlist.size();i++){
                reportModel = mlist.get(i);
                steps_cc.add(String.valueOf(reportModel.getTotal_steps()));
                dates.add(reportModel.getDate());
                max_stps.add(String.valueOf(reportModel.getMax_steps()));
                total_stp = total_stp + reportModel.getTotal_steps();
                if (i == mlist.size()-7) {
                    tv_dates.setText("");
                    tv_dates.setText(reportModel.getDate());
                    list_from=i;
                }
                if (i == mlist.size()-1) {
                    tv_dates.setText(tv_dates.getText().toString() + " - " + reportModel.getDate());
                    list_to=i;
                }
            }
            setData(7, total_stp);
        }

        mChart.setCenterText(generateCenterSpannableText(String.valueOf(total_stp)));
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);
        mSeekBarX.setOnSeekBarChangeListener(this);
        mSeekBarY.setOnSeekBarChangeListener(this);

        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvX.setText("" + (mSeekBarX.getProgress()));
        tvY.setText("" + (mSeekBarY.getProgress()));

        setData(mSeekBarX.getProgress(), mSeekBarY.getProgress());
        total_stp=0;
        for (int i=0; i<mSeekBarX.getProgress(); i++){
            total_stp=total_stp+Integer.parseInt(steps_cc.get(i));
        }
        mChart.setCenterText(generateCenterSpannableText(String.valueOf(total_stp)));
    }

    private void setData(int count, float range) {
        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries.add(new PieEntry((float) (Integer.parseInt(steps_cc.get(i % steps_cc.size()))/mult)*100, getResources().getString(R.string.Step)+"\n"+steps_cc.get(i % dates.size())));
        }

        PieDataSet dataSet = new PieDataSet(entries, getResources().getString(R.string.Date));
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        ArrayList<LegendEntry> entries_steps = new ArrayList<LegendEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < count ; i++) {
            entries_steps.add(new LegendEntry(dates.get(i % dates.size()), Legend.LegendForm.SQUARE,10f,15f, null,colors.get(i)));
        }

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // set custom labels and colors
       // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "Set1", "Set2", "Set3", "Set4", "Set5", "Set5", "Set5" });
        l.setCustom(entries_steps);

        // set custom labels and colors

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }

    private SpannableString generateCenterSpannableText(String total_steps) {
        SpannableString s = new SpannableString(getResources().getString(R.string.Total_steps)+"\n"+total_steps);
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 11, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 11, s.length(), 0);
        //s.setSpan(new ForegroundColorSpan(Color.GRAY), 11, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.5f), 11, s.length(), 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), 0, 11, 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), 11, s.length(), 0);
        return s;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
        Log.e("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());

       // new String(Character.toChars(unicode))
        int per=(Integer.parseInt(steps_cc.get((int) h.getX()))*100)/Integer.parseInt(max_stps.get((int) h.getX()));
        steps_popup(colors.get((int) h.getX()),getResources().getString(R.string.Date_small)+": "+dates.get((int) h.getX())+"\n"+getResources().getString(R.string.Step)+": "+String.valueOf(steps_cc.get((int) h.getX()))+"\n"+getResources().getString(R.string.Max_Step)+": "+String.valueOf(max_stps.get((int) h.getX())),per);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // TODO Auto-generated method stub
    }

    public void steps_popup(int clr, String stps_txt,int performance){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.cloud_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        //final TextView tv_next   = (TextView) dialog.findViewById(R.id.tv_next);
        LinearLayout ll_cloud      = (LinearLayout) dialog.findViewById(R.id.ll_cloud_popup);
        ImageView iv_smiles        = (ImageView) dialog.findViewById(R.id.iv_smile);
        TextView tv_steps          = (TextView) dialog.findViewById(R.id.tv_steps);
        //iv_cloud.getDrawable().setColorFilter(clr, PorterDuff.Mode.SRC_IN);
        ll_cloud.getBackground().setColorFilter(clr, PorterDuff.Mode.SRC_IN);
        tv_steps.setText(stps_txt);
        if (performance<=25)
            iv_smiles.setImageResource(R.drawable.smile_bad);
        else if (performance>25 && performance<=50)
            iv_smiles.setImageResource(R.drawable.smile_avg);
        else if (performance>50 && performance<=75)
            iv_smiles.setImageResource(R.drawable.smile_good);
        else if (performance>75)
            iv_smiles.setImageResource(R.drawable.smile_vgood);

        dialog.show();


        ll_cloud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
