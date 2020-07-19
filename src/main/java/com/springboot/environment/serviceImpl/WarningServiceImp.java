package com.springboot.environment.serviceImpl;

/**
 * Created by sts on 2018/11/26.
 */

//import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.springboot.environment.service.WarningService;
import com.springboot.environment.bean.*;
import com.springboot.environment.dao.WarningDao;
import com.springboot.environment.dao.StationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class WarningServiceImp implements WarningService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Autowired
    private WarningDao warningDao;

    @Autowired
    private StationDao stationDao;

    @Override
    public String queryWarningByDomainAndTimeAndDistrictAndStation(String warning_district,int warning_domain, String start_time, String end_time,String station_id) {

            JSONArray warningArray = new JSONArray();
            JSONObject warningData = new JSONObject();
            JSONObject dataJson = new JSONObject();

            List<Warning> warnings = warningDao.queryWarningByDomainAndTimeAndDistrictAndStation(warning_district,warning_domain,start_time,end_time, station_id);

            System.out.println(warnings.size());
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH");

            if (warnings.size() > 0) {

                for (Warning warning : warnings) {
                    JSONObject warningJSON = new JSONObject();
                    warningJSON.put("station_name",warning.getStation_name());
                    warningJSON.put("norm_name",warning.getNorm_name());
                    warningJSON.put("threshold",warning.getLimit_value());
                    warningJSON.put("warning_start_time",sdf.format(warning.getWarning_start_time()));
                    warningJSON.put("warning_end_time",sdf.format(warning.getWarning_end_time()));
                    warningJSON.put("leq",warning.getReal_value());
                    warningJSON.put("lmx",warning.getLmx());
                    warningJSON.put("sd",warning.getSd());
                    warningJSON.put("cal",warning.getCal());
                    warningJSON.put("vdr",warning.getVdr());

                    warningArray.add(warningJSON);

                }
                warningData.put("count",warnings.size());
                warningData.put("data",warningArray);
                dataJson.put("warningData",warningData);
                return dataJson.toJSONString();

            }
            else {
                warningData.put("count",warnings.size());
                warningData.put("data",warningArray);
                dataJson.put("warningData",warningData);
                return dataJson.toJSONString();
            }
        }

    @Override
    public List<Warning> queryNewWarning(int lastNum ) {
        return warningDao.queryNewWarning(lastNum);
    }

    @Override
    public int getCount() {
        return warningDao.getCount();
    }

//    @Override
//    public Map getRealWarning() {
//        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
//        String hourEnd = sdf.format(new Date());
//        long time = 60*60*1000;//1小时
//        String hourBegin = sdf.format(new Date(System.currentTimeMillis() - time));
//        List<Warning> realWarnings = warningDao.queryRealWarning(hourBegin,hourEnd);
//
//        JSONArray realWarningArray = new JSONArray();
//        JSONObject realWarningData = new JSONObject();
//        JSONObject dataJson = new JSONObject();
//
//
//        if (realWarnings.size() > 0) {
//            for (Warning realWarning : realWarnings) {
//                JSONObject realWarningJSON = new JSONObject();
//                realWarningJSON.put("station_id",realWarning.getStation_id());
//                realWarningJSON.put("norm_code",realWarning.getNorm_code());
//                realWarningJSON.put("norm_name",realWarning.getNorm_name());
//                realWarningJSON.put("leq",realWarning.getReal_value());
//
//                realWarningArray.add(realWarningJSON);
//            }
//        }
////        else {
////            realWarningData.put("data", realWarningArray);
////            dataJson.put("realWarningData", realWarningData);
////            return dataJson.toJSONString();
////        }
//        realWarningData.put("count",realWarningArray.size());
//        realWarningData.put("data",realWarningArray);
//        resultMap.put("realWarningData",realWarningData);
//        return resultMap;
//    }

    @Override
    public Map getRealWarning() {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:00:00");
        String hourEnd = sdf.format(new Date());
        long time = 60*60*1000;//1小时
        String hourBegin = sdf.format(new Date(System.currentTimeMillis() - time));
        List<Warning> realWarnings = warningDao.queryRealWarning(hourBegin,hourEnd);

        JSONArray realWarningArray = new JSONArray();
        JSONObject realWarningData = new JSONObject();

        if (realWarnings.size() > 0) {
            for (Warning realWarning : realWarnings) {
                JSONObject realWarningJSON = new JSONObject();
                Station findStation = stationDao.findByStationId(realWarning.getStation_id());
//                String station_name = stationDao.findStationNameByStationId();
//                String station_position = stationDao.findPositionByStationId(realWarning.getStation_id());

                String[] stringPosition = findStation.getPosition().split(",");
                Double[] position = new Double[stringPosition.length];
                DecimalFormat df = new DecimalFormat("0.000000");
                for (int i = 0; i < stringPosition.length; i++) {
                    double doublePosition = Double.parseDouble(stringPosition[i]);
                    String tempPosition= df.format(doublePosition);
                    position[i] = Double.parseDouble(tempPosition);
                }
                double temp = position[0];
                position[0] = position[1];
                position[1] = temp;
                realWarningJSON.put("stationName",findStation.getStationName());
                realWarningJSON.put("leqa",realWarning.getReal_value());
                realWarningJSON.put("realWarningTime",realWarning.getWarning_start_time().toString());
                realWarningJSON.put("limitValue",realWarning.getLimit_value());
                realWarningJSON.put("normCode",realWarning.getNorm_code());
                realWarningJSON.put("position",position);

                realWarningArray.add(realWarningJSON);
            }
        }
        realWarningData.put("count",realWarningArray.size());
        realWarningData.put("data",realWarningArray);
        resultMap.put("realWarningData",realWarningData);
        return resultMap;
    }



}
