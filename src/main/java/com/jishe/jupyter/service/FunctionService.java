package com.jishe.jupyter.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jishe.jupyter.component.RequestUtil;
import com.jishe.jupyter.global;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.exp;

/**
 * @program: jupyter
 * @description: 实现系统查询功能的服务层
 * @author: kfzjw008(Junwei Zhang)
 * @create: 2020-01-13 10:58
 **/
@Service
@Transactional
public class FunctionService {


    /**
     * @return :实时观测适宜度的Map值
     * @name: 实时数据模块
     * @description: 实时数据模块，用于观测适宜度。
     * @input: WechatUser对象，一般只包括code
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-10 22:35
     **/
    public Map GetSAO(Double LON, Double LAT) {
        global glo = new global();
        RequestUtil request = new RequestUtil();
        String DataResultString = request.CreateRequestUtil("http://www.7timer.info/bin/api.pl?lon=" + LON + "&lat=" + LAT + "&product=astro&output=json");
        String CityResultrSting = request.CreateRequestUtil("http://api.map.baidu.com/reverse_geocoding/v3/?ak=YlBkV0nnbjcFFGl9TfgUMdsL2TEV59tW&output=json&coordtype=wgs84ll&location=" + LAT + "," + LON);
        JSONObject CityjsonObject = (JSONObject) JSONObject.parse(CityResultrSting);
        JSONObject RresultjsonData = CityjsonObject.getJSONObject("result");
        JSONObject RresultCjsonData = RresultjsonData.getJSONObject("addressComponent");
        String City = RresultCjsonData.get("city") + "";
        String AQIyResultString = request.CreateRequestUtil("https://free-api.heweather.net/s6/air/now?location=" + City + "&key=1de5ba99c8c74892b88a21ee5100e6cf");
        JSONObject AQIjsonObject = (JSONObject) JSONObject.parse(AQIyResultString);
        JSONArray AQIData = AQIjsonObject.getJSONArray("HeWeather6");
        JSONObject AQIData2 = AQIData.getJSONObject(0);
        JSONObject AQIData3 = AQIData2.getJSONObject("air_now_city");
        String AQI = AQIData3.get("aqi") + "";

        JSONObject jsonObject = (JSONObject) JSONObject.parse(DataResultString);
        JSONArray JsonDataSeries = jsonObject.getJSONArray("dataseries");
        List<String> seeings = new ArrayList<>();
        List<String> rh2ms = new ArrayList<>();
        List<String> prec_types = new ArrayList<>();
        List<String> transparencys = new ArrayList<>();
        List<String> wind10m_speeds = new ArrayList<>();
        List<String> wind10m_directions = new ArrayList<>();
        List<String> temp2ms = new ArrayList<>();
        List<String> lifted_indexs = new ArrayList<>();
        List<String> timepoints = new ArrayList<>();
        List<String> cloudcovers = new ArrayList<>();
        List<String> SAOs = new ArrayList<>();
        List<String> scloudcovers = new ArrayList<>();
        List<String> sseeings = new ArrayList<>();
        List<String> stranss = new ArrayList<>();
        List<String> slifteds = new ArrayList<>();
        for (int i = 0; i < JsonDataSeries.size(); i++) {
            JSONObject partDaily = JsonDataSeries.getJSONObject(i);
            String seeing = partDaily.getString("seeing");
            String rh2m = partDaily.getString("rh2m");
            rh2ms.add(rh2m);
            String prec_type = partDaily.getString("prec_type");
            prec_types.add(prec_type);
            if (prec_type.equals("none")) {
                prec_type = "1";
            } else {
                prec_type = "0";
            }
            String transparency = partDaily.getString("transparency");
            String lifted_index = partDaily.getString("lifted_index");
            String timepoint = partDaily.getString("timepoint");
            timepoints.add(timepoint);
            String cloudcover = partDaily.getString("cloudcover");
            String temp2m = partDaily.getString("temp2m");
            temp2ms.add(temp2m);
            JSONArray wind10mData = jsonObject.getJSONArray("wind10m");
            String speed = "";
            String direction = "";
            for (int j = 0; j < JsonDataSeries.size(); j++) {
                JSONObject partDailywind = JsonDataSeries.getJSONObject(i).getJSONObject("wind10m");
                speed = partDailywind.getString("speed");
                wind10m_speeds.add(speed);
                direction = partDailywind.getString("direction");
                wind10m_directions.add(direction);
            }
            //以下为观测适宜度计算
            int SAO = 0;
            SAO = SaoAlgorithm(Double.valueOf(cloudcover), Double.valueOf(seeing), Double.valueOf(transparency), Double.valueOf(lifted_index), Double.valueOf(rh2m), Double.valueOf(speed), Double.valueOf(temp2m), Double.valueOf(prec_type), Double.valueOf(AQI));
            SAOs.add(String.valueOf(SAO));
            String scloudcover = "";
            String sseeing = "";
            String strans = "";
            String slifted = "";
            if (cloudcover.equals("1")) {
                cloudcover = "3%";
                scloudcover = "万里无云";
            }
            if (cloudcover.equals("2")) {
                cloudcover = "13%";
                scloudcover = "少量云朵，可忽略";
            }
            if (cloudcover.equals("3")) {
                cloudcover = "25%";
                scloudcover = "云层稀薄";
            }
            if (cloudcover.equals("4")) {
                cloudcover = "37%";
                scloudcover = "云层较薄";
            }
            if (cloudcover.equals("5")) {
                cloudcover = "50%";
                scloudcover = "云层明显";
            }
            if (cloudcover.equals("6")) {
                cloudcover = "62%";
                scloudcover = "云层稍厚";
            }
            if (cloudcover.equals("7")) {
                cloudcover = "75%";
                scloudcover = "云层较厚";
            }
            if (cloudcover.equals("8")) {
                cloudcover = "87%";
                scloudcover = "云层很厚";
            }
            if (cloudcover.equals("9")) {
                cloudcover = "97%";
                scloudcover = "云层很厚，天空不可见";
            }
            cloudcovers.add(cloudcover);
            scloudcovers.add(scloudcover);
            if (seeing.equals("1")) {
                seeing = "<0.5";
                sseeing = "视宁度很差，大气湍流干扰很大";
            }
            if (seeing.equals("2")) {
                seeing = "0.75";
                sseeing = "视宁度差，大气湍流干扰明显";
            }
            if (seeing.equals("3")) {
                seeing = "1";
                sseeing = "视宁度较差，天体闪烁较明显";
            }
            if (seeing.equals("4")) {
                seeing = "1.25";
                sseeing = "视宁度稍差，可见天体闪烁";
            }
            if (seeing.equals("5")) {
                seeing = "1.5";
                sseeing = "视宁度适中";
            }
            if (seeing.equals("6")) {
                seeing = "2";
                sseeing = "视宁度较好，天体无明显闪烁";
            }
            if (seeing.equals("7")) {
                seeing = "2.5";
                sseeing = "视宁度好，大气湍流干扰可忽略";
            }
            if (seeing.equals("8")) {
                seeing = ">2.5";
                sseeing = "视宁度非常好，无干扰";
            }
            seeings.add(seeing);
            sseeings.add(sseeing);
            if (transparency.equals("1")) {
                transparency = "0.3";
                strans = "透明度非常差，天空浑浊";
            }
            if (transparency.equals("2")) {
                transparency = "0.4";
                strans = "透明度差，天空较浑浊";
            }
            if (transparency.equals("3")) {
                transparency = "0.5";
                strans = "透明度较差，天空通透性差";
            }
            if (transparency.equals("4")) {
                transparency = "0.6";
                strans = "透明度一般，天空通透性不好";
            }
            if (transparency.equals("5")) {
                transparency = "0.7";
                strans = "透明度适中";
            }
            if (transparency.equals("6")) {
                transparency = "0.85";
                strans = "透明度较好，天空较通透";
            }
            if (transparency.equals("7")) {
                transparency = "1";
                strans = "透明度好，天空清澈";
            }
            transparencys.add(transparency);
            stranss.add(strans);
            if (lifted_index.equals("-10")) {
                lifted_index = "<-7";
                slifted = "非常不稳定，短期内观测条件很可能有较大改变";
            }
            if (lifted_index.equals("-6")) {
                lifted_index = "-6";
                slifted = "不稳定，短期内观测条件很可能改变";
            }
            if (lifted_index.equals("-4")) {
                lifted_index = "-4";
                slifted = "不太稳定，短期内观测条件可能改变";
            }
            if (lifted_index.equals("-1")) {
                lifted_index = "-1.5";
                slifted = "一般，短期内观测条件可能改变";
            }
            if (lifted_index.equals("2")) {
                lifted_index = "2";
                slifted = "适中，短期内观测条件可能改变不大";
            }
            if (lifted_index.equals("6")) {
                lifted_index = "6";
                slifted = "较好，短期内观测条件可能较稳定";
            }
            if (lifted_index.equals("10")) {
                lifted_index = "9";
                slifted = "好，短期内观测条件改变的可能较小";
            }
            if (lifted_index.equals("15")) {
                lifted_index = ">11";
                slifted = "非常好，短期内观测条件很可能不改变";
            }
            lifted_indexs.add(lifted_index);
            slifteds.add(slifted);
        }
        Map<Object, Object> AllDataMap = new HashMap<Object, Object>();
        AllDataMap.put("City", City);
        AllDataMap.put("AQI", AQI);
        for (int i = 0; i < 24; i++) {
            Map<Object, Object> BasicDataMap = new HashMap<Object, Object>();
            BasicDataMap.put("TimePoint", timepoints.get(i));
            BasicDataMap.put("SAO", SAOs.get(i));
            BasicDataMap.put("Humidity", rh2ms.get(i));
            BasicDataMap.put("Temperature", temp2ms.get(i));
            BasicDataMap.put("Prec_Type", prec_types.get(i));
            Map<Object, Object> WindDataMap = new HashMap<Object, Object>();
            WindDataMap.put("Wind_Speed", wind10m_speeds.get(i));
            WindDataMap.put("Wind_Direction", wind10m_directions.get(i));
            BasicDataMap.put("Wind", WindDataMap);
            Map<Object, Object> SeeDataMap = new HashMap<Object, Object>();
            SeeDataMap.put("Seeing_Value", seeings.get(i));
            SeeDataMap.put("Explain", sseeings.get(i));
            BasicDataMap.put("Seeing", SeeDataMap);
            Map<Object, Object> LeftDataMap = new HashMap<Object, Object>();
            LeftDataMap.put("Lifted_Index_Value", lifted_indexs.get(i));
            LeftDataMap.put("Explain", slifteds.get(i));
            BasicDataMap.put("Lifted_Index", (LeftDataMap));
            Map<Object, Object> CloudcoverDataMap = new HashMap<Object, Object>();
            CloudcoverDataMap.put("CloudCover_Value", cloudcovers.get(i));
            CloudcoverDataMap.put("Explain", scloudcovers.get(i));
            BasicDataMap.put("CloudCover", (CloudcoverDataMap));
            Map<Object, Object> TransparencyDataMap = new HashMap<Object, Object>();
            TransparencyDataMap.put("Transparency_Value", transparencys.get(i));
            TransparencyDataMap.put("Explain", stranss.get(i));
            BasicDataMap.put("Transparency", (TransparencyDataMap));
            AllDataMap.put(i, (BasicDataMap));
        }
        return Map.of("SAO_Result", AllDataMap);
    }
    /**
     * @return :实时观测适宜度的Map值
     * @name: 观测适宜度评价算法
     * @description: 观测适宜度的计算。
     * @input: 各种观测适宜度评价指标。
     * @author: kfzjw008(Junwei Zhang)
     * @create: 2020-01-13 12:35
     **/
    private int SaoAlgorithm(double cloudcoverapi, double seeingapi, double transparencyapi, double lifted_indexapi, double rh2m, double wind10m, double temp2m, double prec_type, double aqiapi) {
        double hi, mi, lo;
        double all;
        double cloudcover, seeing, transparency;
        double lifted_index, aqi;
        double tgtemp = 0.0, tgtempapi, T, V, RH, TT, e;
        double tgmin = -20;
        double tgmax = 40;
        double tggood = 25;
//高空部分
        cloudcover = 100 - (cloudcoverapi - 1) * 0.125 * 100;
        seeing = 100 - (seeingapi - 1) * 0.147258 * 100;
        transparency = 100 - (transparencyapi - 1) * 0.147258 * 100;
//低空部分
        prec_type = prec_type * 100;
        lifted_index = (lifted_indexapi + 10) * 4;
        aqi = (250 - aqiapi) * 0.4;
//地表部分
        T = temp2m;
        V = wind10m;
        RH = rh2m;
        TT = (17.27 * T) / (237.7 + T);
        e = 0.01 * RH * 0.6105 * exp(TT);
        tgtempapi = 1.07 * T + 0.2 * e - 0.65 * V - 2.7;
        if (tgtempapi < tgmin) tgtemp = 0;
        else if (tgtempapi > tgmax) tgtemp = 0;
        else {
            if (tgtempapi >= tggood) tgtemp = 100 - (tgtempapi - tggood) / (tgmax - tggood) * 100;
            if (tgtempapi < tggood) tgtemp = 100 - (tggood - tgtempapi) / (tggood - tgmin) * 100;
        }
//高地空部分合并计算
        hi = 0.443 * cloudcover + 0.2766 * seeing + 0.2804 * transparency;
        mi = prec_type * 0.5248 + lifted_index * 0.2238 + aqi * 0.2514;
        lo = tgtemp;
//求和
        all = 0.6289 * hi + 0.1285 * mi + 0.2426 * lo;
        return (int) all;
    }



}
