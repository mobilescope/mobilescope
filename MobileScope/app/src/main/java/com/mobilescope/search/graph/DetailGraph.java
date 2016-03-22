package com.mobilescope.search.graph;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.mobilescope.database.auth.PlayerDetail;

/**
 * Created by IntelliJ IDEA.
 * User: Jude.Fernando
 * Date: 11/28/11
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class DetailGraph extends AbstractDrawGraph {


//    public Intent execute(Context context) {
//      String[] titles = new String[] { "New tickets", "Fixed tickets" };
//      List<Date[]> dates = new ArrayList<Date[]>();
//      List<double[]> values = new ArrayList<double[]>();
//      int length = titles.length;
//      for (int i = 0; i < length; i++) {
//        dates.add(new Date[12]);
//        dates.get(i)[0] = new Date(108, 9, 1);
//        dates.get(i)[1] = new Date(108, 9, 8);
//        dates.get(i)[2] = new Date(108, 9, 15);
//        dates.get(i)[3] = new Date(108, 9, 22);
//        dates.get(i)[4] = new Date(108, 9, 29);
//        dates.get(i)[5] = new Date(108, 10, 5);
//        dates.get(i)[6] = new Date(108, 10, 12);
//        dates.get(i)[7] = new Date(108, 10, 19);
//        dates.get(i)[8] = new Date(108, 10, 26);
//        dates.get(i)[9] = new Date(108, 11, 3);
//        dates.get(i)[10] = new Date(108, 11, 10);
//        dates.get(i)[11] = new Date(108, 11, 17);
//      }
//      values.add(new double[] { 142, 123, 142, 152, 149, 122, 110, 120, 125, 155, 146, 150 });
//      values.add(new double[] { 102, 90, 112, 105, 125, 112, 125, 112, 105, 115, 116, 135 });
//      length = values.get(0).length;
//      int[] colors = new int[] { Color.BLUE, Color.GREEN };
//      PointStyle[] styles = new PointStyle[] { PointStyle.POINT, PointStyle.POINT };
//      XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
//      setChartSettings(renderer, "Project work status", "Date", "Tickets", dates.get(0)[0].getTime(),
//          dates.get(0)[11].getTime(), 50, 190, Color.GRAY, Color.LTGRAY);
//      renderer.setXLabels(5);
//      renderer.setYLabels(10);
//      length = renderer.getSeriesRendererCount();
//      for (int i = 0; i < length; i++) {
//        SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
//        seriesRenderer.setDisplayChartValues(true);
//      }
//      return ChartFactory.getTimeChartIntent(context, buildDateDataset(titles, dates, values),
//          renderer, "MM/dd/yyyy");
//    }

    public Intent execute(Context context, Number[] _lCumulative) {
//        String[] titles = new String[] { "Profit Graph" };
        String[] titles = new String[] { "" };
        List<double[]> _x = new ArrayList<double[]>();
        List<double[]> values = new ArrayList<double[]>();
        int length = titles.length;
      
       
        values.add(ConvertNumber(_lCumulative));
        _x.add(xValue(_lCumulative.length));
        
        System.out.println("Size of x axis:"+_x.size());
        System.out.println("Size of y axis:"+values.size());
        
//        values.add(new double[] { 142, 123, 142, 152, 149, 122, 110, 120, 125, 155, 146, 150 });
//        values.add(new double[] { 102, 90, 112, 105, 125, 112, 125, 112, 105, 115, 116, 135 });
        length = values.get(0).length;
        
        int[] colors = new int[] { Color.GREEN };
        PointStyle[] styles = new PointStyle[] {PointStyle.POINT };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        Arrays.sort(_lCumulative);
        
        
        System.out.println("Size of y min:"+_lCumulative[0]);
        System.out.println("Size of y max:"+_lCumulative[_lCumulative.length-1]);
        
        System.out.println("Size of x min:"+_x.get(0)[0]);
        System.out.println("Size of x max:"+_x.get(_x.size()-1)[_x.size()-1]);
        
        
        setChartSettings(renderer, "Player Profit", "No of Games", "Amount", 0,
            (double)_lCumulative.length-1, _lCumulative[0].doubleValue(), _lCumulative[_lCumulative.length-1].doubleValue(), Color.GRAY, Color.LTGRAY);
        renderer.setXLabels(5);
        
        renderer.setAxisTitleTextSize(8);
        renderer.setLegendTextSize(5);
        
        System.out.println(
        		_lCumulative.length
        		);
        
//        renderer.clearYTextLabels();
        
        for(int i=1;i<_lCumulative.length;i++){
        	String renderTextLable= null;
        	String text;
        	text = (Double)(_lCumulative[i])<1000 ? String.valueOf(_lCumulative[i]): String.valueOf(Math.round((Double) _lCumulative[i])/1000)+"k";
        	System.out.println(
            i+"_lCumulative"+_lCumulative[i]+"::"+text
            		);
            
//        	renderer.addYTextLabel(_lCumulative[i].doubleValue(), text);
//        	renderer.getYTextLabelLocations();
//        	
//            Double[] yDoubleArray = renderer.getYTextLabelLocations();
//            for(Double yDouble : yDoubleArray){
//            	System.out.println("Double Y Text"+yDouble);
//            }
        	
//        	renderTextLable= renderer.getYTextLabel((double) i).toString();
//        	System.out.println(renderTextLable);
//        	String value = Integer.valueOf(renderTextLable) < 1000?String.valueOf(renderTextLable): String.valueOf(Math.round(Integer.valueOf(renderTextLable)/1000))+"k";
//        	renderer.addYTextLabel(i+1, value,i);
//        	
        }
        renderer.setYLabels(10);
        renderer.setLabelsTextSize(10);
        renderer.setShowLegend(false);
        
       
        length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
        	XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
            r.setLineWidth(1);
            r.setFillPoints(true);
            
        }
       
        String[] types = new String[] { LineChart.TYPE};

        return ChartFactory.getCombinedXYChartIntent(context,buildDataset(titles,_x, values),  renderer, types, "Profit History");
      } 
    
    public double[] ConvertNumber(Number[] numberArray){

        double[] _lCumulativedouble = new double[numberArray.length];
        for(int i=0;i<numberArray.length;i++){
        	_lCumulativedouble[i] =  numberArray[i].doubleValue();
        	System.out.println("Value of number array"+ numberArray[i].doubleValue());
        	System.out.println("Value of double array"+ _lCumulativedouble[i]);
        }
        return _lCumulativedouble;
       
    }
    
    public double[] xValue(int size){
    	double[] _xValue = new double[size];
    	for(int i=0;i<size;i++){
    		_xValue[i]= (double)i;
    	}
    	return _xValue;
    }

	@Override
	public Intent execute(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
    
}
