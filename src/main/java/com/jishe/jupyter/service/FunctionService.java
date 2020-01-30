package com.jishe.jupyter.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jishe.jupyter.component.RequestUtil;
import com.jishe.jupyter.entity.Calendar;
import com.jishe.jupyter.global;
import com.jishe.jupyter.repository.CalendarRepoistory;
import com.jishe.jupyter.repository.StarssRepoistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
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
    @Autowired
    private CalendarRepoistory calendarRepoistory;


    @Autowired
    private ElasticsearchTemplate template;

    private StarssRepoistory StarssRepoistory;


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
            AllDataMap.put("P" + i, (BasicDataMap));
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


    public Map Search(String string, int page) throws Exception {
        return global.StarssRepoistory.testQueryStringQuery(string, page);
    }


    //八大行星数值返回
    public Map GetEightPlanets() {
        Map<Object, Object> AllDataMap = new HashMap<Object, Object>();
        Map<Object, Object> SunMap = new HashMap<Object, Object>();
        Map<Object, Object> MercuryMap = new HashMap<Object, Object>();
        Map<Object, Object> VenusMap = new HashMap<Object, Object>();
        Map<Object, Object> EarthMap = new HashMap<Object, Object>();
        Map<Object, Object> MarsMap = new HashMap<Object, Object>();
        Map<Object, Object> JupiterMap = new HashMap<Object, Object>();
        Map<Object, Object> SaturnMap = new HashMap<Object, Object>();
        Map<Object, Object> UranusMap = new HashMap<Object, Object>();
        Map<Object, Object> NeptuneMap = new HashMap<Object, Object>();

        SunMap.put("Equatorial_radius", 696000);
        SunMap.put("Bias_rate", 0);
        SunMap.put("Equatorial_gravity", 28.01);
        SunMap.put("Volume", 1E+06);
        SunMap.put("Quality", 333400);
        SunMap.put("Proportion", 1.4);
        SunMap.put("Orbital_Radius", null);
        SunMap.put("Orbital_inclination", null);
        SunMap.put("Equatorial_inclination", 7.25);
        SunMap.put("Transit_cycle", "约两亿六千天天");
        SunMap.put("Rotation_cycle", "25.38天");
        SunMap.put("Number_of_satellites", null);

        MercuryMap.put("Equatorial_radius", 2440);
        MercuryMap.put("Bias_rate", 0);
        MercuryMap.put("Equatorial_gravity", 0.38);
        MercuryMap.put("Volume", 0.056);
        MercuryMap.put("Quality", 0.055);
        MercuryMap.put("Proportion", 5.4);
        MercuryMap.put("Orbital_Radius", 0.387);
        MercuryMap.put("Orbital_inclination", 7);
        MercuryMap.put("Equatorial_inclination", 0);
        MercuryMap.put("Transit_cycle", "88天");
        MercuryMap.put("Rotation_cycle", "59天");
        MercuryMap.put("Number_of_satellites", 0);

        VenusMap.put("Equatorial_radius", 6052);
        VenusMap.put("Bias_rate", 0);
        VenusMap.put("Equatorial_gravity", 0.91);
        VenusMap.put("Volume", 0.857);
        VenusMap.put("Quality", 0.815);
        VenusMap.put("Proportion", 5.2);
        VenusMap.put("Orbital_Radius", 0.723);
        VenusMap.put("Orbital_inclination", 3.4);
        VenusMap.put("Equatorial_inclination", 177);
        VenusMap.put("Transit_cycle", "225天");
        VenusMap.put("Rotation_cycle", "243天");
        VenusMap.put("Number_of_satellites", 0);

        EarthMap.put("Equatorial_radius", 6378);
        EarthMap.put("Bias_rate", 0.003);
        EarthMap.put("Equatorial_gravity", 1);
        EarthMap.put("Volume", 1);
        EarthMap.put("Quality", 1);
        EarthMap.put("Proportion", 5.5);
        EarthMap.put("Orbital_Radius", 1);
        EarthMap.put("Orbital_inclination", 0);
        EarthMap.put("Equatorial_inclination", 23.4);
        EarthMap.put("Transit_cycle", "365天");
        EarthMap.put("Rotation_cycle", "24小时");
        EarthMap.put("Number_of_satellites", 1);

        MarsMap.put("Equatorial_radius", 3397);
        MarsMap.put("Bias_rate", 0.005);
        MarsMap.put("Equatorial_gravity", 0.38);
        MarsMap.put("Volume", 0.151);
        MarsMap.put("Quality", 0.107);
        MarsMap.put("Proportion", 3.9);
        MarsMap.put("Orbital_Radius", 1.524);
        MarsMap.put("Orbital_inclination", 1.9);
        MarsMap.put("Equatorial_inclination", 25.2);
        MarsMap.put("Transit_cycle", "687天");
        MarsMap.put("Rotation_cycle", "24小时37分钟");
        MarsMap.put("Number_of_satellites", 2);

        JupiterMap.put("Equatorial_radius", 71492);
        JupiterMap.put("Bias_rate", 0.065);
        JupiterMap.put("Equatorial_gravity", 2.48);
        JupiterMap.put("Volume", 1321);
        JupiterMap.put("Quality", 317.832);
        JupiterMap.put("Proportion", 1.3);
        JupiterMap.put("Orbital_Radius", 5.203);
        JupiterMap.put("Orbital_inclination", 1.3);
        JupiterMap.put("Equatorial_inclination", 3.08);
        JupiterMap.put("Transit_cycle", "11.86年");
        JupiterMap.put("Rotation_cycle", "9小时50分钟");
        JupiterMap.put("Number_of_satellites", 79);

        SaturnMap.put("Equatorial_radius", 60268);
        SaturnMap.put("Bias_rate", 0.108);
        SaturnMap.put("Equatorial_gravity", 0.94);
        SaturnMap.put("Volume", 755);
        SaturnMap.put("Quality", 95.16);
        SaturnMap.put("Proportion", 0.7);
        SaturnMap.put("Orbital_Radius", 9.555);
        SaturnMap.put("Orbital_inclination", 2.5);
        SaturnMap.put("Equatorial_inclination", 26.7);
        SaturnMap.put("Transit_cycle", "29.46年");
        SaturnMap.put("Rotation_cycle", "10小时14分钟");
        SaturnMap.put("Number_of_satellites", 82);

        UranusMap.put("Equatorial_radius", 25559);
        UranusMap.put("Bias_rate", 0.023);
        UranusMap.put("Equatorial_gravity", 0.890);
        UranusMap.put("Volume", 63);
        UranusMap.put("Quality", 14.54);
        UranusMap.put("Proportion", 1.3);
        UranusMap.put("Orbital_Radius", 19.22);
        UranusMap.put("Orbital_inclination", 0.8);
        UranusMap.put("Equatorial_inclination", 97.9);
        UranusMap.put("Transit_cycle", "84.01年");
        UranusMap.put("Rotation_cycle", "24小时");
        UranusMap.put("Number_of_satellites", 23);

        NeptuneMap.put("Equatorial_radius", 24764);
        NeptuneMap.put("Bias_rate", 0.017);
        NeptuneMap.put("Equatorial_gravity", 1.11);
        NeptuneMap.put("Volume", 58);
        NeptuneMap.put("Quality", 17.15);
        NeptuneMap.put("Proportion", 1.6);
        NeptuneMap.put("Orbital_Radius", 30.11);
        NeptuneMap.put("Orbital_inclination", 1.8);
        NeptuneMap.put("Equatorial_inclination", 27.8);
        NeptuneMap.put("Transit_cycle", "164.82年");
        NeptuneMap.put("Rotation_cycle", "16小时06分");
        NeptuneMap.put("Number_of_satellites", 14);

        AllDataMap.put("SunMap", SunMap);
        AllDataMap.put("MercuryMap", MercuryMap);
        AllDataMap.put("VenusMap", VenusMap);
        AllDataMap.put("EarthMap", EarthMap);
        AllDataMap.put("MarsMap", MarsMap);
        AllDataMap.put("JupiterMap", JupiterMap);
        AllDataMap.put("SaturnMap", SaturnMap);
        AllDataMap.put("UranusMap", UranusMap);
        AllDataMap.put("NeptuneMap", NeptuneMap);

        return AllDataMap;
    }

    //日出日落计算
    public Map GetSunriseset(int year, int month, int day, int zone, double lon, double lat) {

        Map obj = Cal(mjd(day, month, year, 0), zone, lon, lat);
        var ret = "";
        if (obj.get("rise") == null && obj.get("set") == null) {
            obj.put("message", "极昼//极夜");
        }
        if (obj.get("rise") == null) {
            obj.put("rise", "太阳不升");
        } else {
            obj.putIfAbsent("set", "太阳不落");
        }

        return obj;
    }

    //http://api.help.bj.cn/apis/nongli/?id=101010100&now=2019-11-06   农历和月相接口
    public Map GetMoonPhase(int year, int month, int day) {
        Map<Object, Object> AllDataMap = new HashMap<>();
        RequestUtil request = new RequestUtil();
        String DataResultString = request.CreateRequestUtil("http://api.help.bj.cn/apis/nongli/?id=101010100&now=" + year + "-" + month + "-" + day);
        JSONObject jsonObject = (JSONObject) JSONObject.parse(DataResultString);
        JSONArray AQIData = jsonObject.getJSONArray("data");
        JSONObject years = AQIData.getJSONObject(13);//16 21
        String yearss = years.get("val") + "";
        JSONObject months = AQIData.getJSONObject(16);
        String monthss = months.get("val") + "";
        JSONObject days = AQIData.getJSONObject(21);
        String dayss = days.get("val") + "";
        System.out.println(dayss);
        JSONObject yx = AQIData.getJSONObject(40);
        String yuexiangs = yx.get("val") + "";
        JSONObject yt = AQIData.getJSONObject(41);
        String yuexiangspng = yt.get("val") + "";
        AllDataMap.put("year", yearss);
        AllDataMap.put("month", monthss);
        AllDataMap.put("day", dayss);
        AllDataMap.put("phase", yuexiangs);
        AllDataMap.put("phasePNG", "http:"+yuexiangspng);
        return AllDataMap;
    }
//返回当日天象
public List<Calendar> GetCalendar(int year,int  month,int day){
   return  calendarRepoistory.findByYearAndMonthAndDay(year, month, day);
}
    //以下为日出日落系列算法
    private double mjd(int day, int month, int year, int hour) {
        double a, b;
        if (month <= 2) {
            month = month + 12;
            year = year - 1;
        }
        a = 10000.0 * year + 100.0 * month + day;
        if (a <= 15821004.1) {
            b = -2 * Math.floor((year + 4716) / 4.0) - 1179;
        } else {
            b = Math.floor(year / 400.0) - Math.floor(year / 100.0) + Math.floor(year / 4.0);
        }
        a = 365.0 * year - 679004.0;
        return (a + b + Math.floor(30.6001 * (month + 1)) + day + hour / 24.0);
    }

    private double frac(double x) {
        double a;
        a = x - Math.floor(x);
        if (a < 0) a += 1;
        return a;
    }

    private double ipart(double x) {
        double a;
        if (x > 0) {
            a = Math.floor(x);
        } else {
            a = Math.ceil(x);
        }
        return a;
    }

    private double range(double x) {
        double b = x / 360;
        double a = 360 * (b - ipart(b));
        if (a < 0) {
            a = a + 360;
        }
        return a;
    }

    private double lmst(double mjday, double glong) {
        double d = mjday - 51544.5;
        double t = d / 36525.0;
        double lst = range(280.46061837 + 360.98564736629 * d + 0.000387933 * t * t - t * t * t / 38710000);
        return (lst / 15.0 + glong / 15);
    }

    private Map<Object, Double> minisun(double t) {
        double p2 = 6.283185307, coseps = 0.91748, sineps = 0.39778;
        double ra;
        Map<Object, Double> suneq = new HashMap<>();
        double M = p2 * frac(0.993133 + 99.997361 * t);
        double DL = 6893.0 * Math.sin(M) + 72.0 * Math.sin(2 * M);
        double L = p2 * frac(0.7859453 + M / p2 + (6191.2 * t + DL) / 1296000);
        double SL = Math.sin(L);
        double X = Math.cos(L);
        double Y = coseps * SL;
        double Z = sineps * SL;
        double RHO = Math.sqrt(1 - Z * Z);
        double dec = (360.0 / p2) * Math.atan(Z / RHO);
        ra = (48.0 / p2) * Math.atan(Y / (X + RHO));
        if (ra < 0) ra += 24;
        suneq.put("1", dec);
        suneq.put("2", ra);
        return suneq;
    }

    private double sin_alt(int iobj, double mjd0, double hour, double glong, double cglat, double sglat) {
        double mjday = mjd0 + hour / 24.0;
        double t = (mjday - 51544.5) / 36525.0;
        double rads = 0.0174532925;
        Map<Object, Double> objpos = minisun(t);
        double ra = objpos.get("2");
        double dec = objpos.get("1");
        double tau = 15.0 * (lmst(mjday, glong) - ra);
        double salt = sglat * Math.sin(rads * dec) + cglat * Math.cos(rads * dec) * Math.cos(rads * tau);
        return salt;
    }

    private Map<Object, Double> quad(double ym, double yz, double yp) {
        Map<Object, Double> quadout = new HashMap<>();
        double nz = 0;
        double a = 0.5 * (ym + yp) - yz;
        double b = 0.5 * (yp - ym);
        double c = yz;
        double xe = -b / (2 * a);
        double ye = (a * xe + b) * xe + c;
        double dis = b * b - 4.0 * a * c;
        double z1 = 0, z2 = 0;
        if (dis > 0) {
            double dx = 0.5 * Math.sqrt(dis) / Math.abs(a);
            z1 = xe - dx;
            z2 = xe + dx;
            if (Math.abs(z1) <= 1.0) nz += 1;
            if (Math.abs(z2) <= 1.0) nz += 1;
            if (z1 < -1.0) z1 = z2;
        }
        quadout.put(0, nz);
        quadout.put(1, z1);
        quadout.put(2, z2);
        quadout.put(3, xe);
        quadout.put(4, ye);
        return quadout;
    }

    private String hrsmin(double hours) {
        int hrs;
        String hh, mm;
        double hp;
        double m;
        String dum;
        m = Math.floor(60 * ((Math.floor(hours * 60 + 0.5) / 60.0) - (Math.floor((Math.floor(hours * 60 + 0.5) / 60.0)))) + 0.5);
        System.out.println("m=" + m);
        hrs = (int) (Math.floor(hours * 60 + 0.5) / 60.0);
        int h = (int) (Math.floor(hrs));
        int mmm = (int) m;
        if (h < 10) {
            hh = String.valueOf("0" + h);
        } else {
            hh = String.valueOf(h);
        }
        if (mmm < 10) {
            mm = String.valueOf("0" + mmm);

        } else {
            mm = String.valueOf(mmm);
        }
        dum = hh + ":" + mm;
/*
        if (dum < 1000) dum = "0" + dum;
        if (dum <100) dum = "0" + dum;
        if (dum < 10) dum = "0" + dum;*/
        return dum;
    }

    private double getzttime(double mjday, double tz, double glong) {

        double yz;
        double utrise = 0, utset = 0, j;
        double yp;
        double nz = 0, z1 = 0, z2 = 0;
        double rads = 0.0174532925;
        double xe = 0, ye = 0;
        double sinho = Math.sin(rads * -0.833);
        double date = mjday - tz / 24;
        double hour = 1.0;
        double ym = sin_alt(2, date, hour - 1.0, glong, 1, 0) - sinho;

        while (hour < 25) {
            yz = sin_alt(2, date, hour, glong, 1, 0) - sinho;
            yp = sin_alt(2, date, hour + 1.0, glong, 1, 0) - sinho;
            Map<Object, Double> quadout = quad(ym, yz, yp);
            quadout.put(0, nz);
            quadout.put(1, z1);
            quadout.put(2, z2);
            quadout.put(3, xe);
            quadout.put(4, ye);

            if (nz == 1) {
                if (ym < 0.0)
                    utrise = hour + z1;
                else
                    utset = hour + z1;

            }
            if (nz == 2) {
                if (ye < 0.0) {
                    utrise = hour + z2;
                    utset = hour + z1;
                } else {
                    utrise = hour + z1;
                    utset = hour + z2;
                }
            }
            ym = yp;
            hour += 2.0;
        }
        var zt = (utrise + utset) / 2;
        if (zt < utrise)
            zt = (zt + 12) % 24;
        return zt;
    }

    //核心算法
    private Map<Object, Object> Cal(double mjday, int tz, double glong, double glat) {
        double rads = 0.0174532925;
        double
                utrise = 0, utset = 0, j;
        double nz, z1, z2, xe, ye;

        String always_up = "日不落";
        String always_down = "日不出";
        Map<Object, Object> resobj = new HashMap<Object, Object>();

        double sinho = Math.sin(rads * -0.833);
        double sglat = Math.sin(rads * glat);
        double cglat = Math.cos(rads * glat);
        double date = mjday - tz / 24.0;

        boolean rise = false;
        boolean sett = false;
        boolean above = false;
        double hour = 1.0;
        double ym = sin_alt(2, date, hour - 1.0, glong, cglat, sglat) - sinho;
        if (ym > 0.0) above = true;
        while (hour < 25 && (!sett || !rise)) {
            double yz = sin_alt(2, date, hour, glong, cglat, sglat) - sinho;
            double yp = sin_alt(2, date, hour + 1.0, glong, cglat, sglat) - sinho;
            Map<Object, Double> quadout = quad(ym, yz, yp);

            nz = quadout.get(0);
            z1 = quadout.get(1);
            z2 = quadout.get(2);
            xe = quadout.get(3);
            ye = quadout.get(4);

            if (nz == 1) {
                if (ym < 0.0) {
                    utrise = hour + z1;
                    rise = true;
                } else {
                    utset = hour + z1;
                    sett = true;
                }
            }

            if (nz == 2) {
                if (ye < 0.0) {
                    utrise = hour + z2;
                    utset = hour + z1;
                } else {
                    utrise = hour + z1;
                    utset = hour + z2;
                }
            }

            ym = yp;
            hour += 2.0;

        }

        if (rise == true || sett == true) {
            if (rise == true) {
                resobj.put("rise", hrsmin(utrise));//日出
            } else {
                resobj.put("message2", "日不出或日不落");
            }
            double zt = getzttime(mjday, tz, glong);
            // resobj.put("center",hrsmin(zt));
            if (sett == true) {
                resobj.put("set", hrsmin(utset));
            } else {

                resobj.put("message2", "日不出或日不落");
            }
        } else {
            if (above == true) {

                var zt = getzttime(mjday, tz, glong);
                resobj.put("center", hrsmin(zt));
                resobj.put("message2", "极昼");
            } else {
                resobj.put("message2", "极夜");
            }
        }
        return resobj;

    }

}
