package com.springboot.environment.controller;

/**
 * Created by sts on 2018/12/6.
 */

import com.springboot.environment.dao.WarningDao;
import com.springboot.environment.service.WarningService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.util.*;


@RestController
@RequestMapping("/warning")
@Api("数据类api")
public class WarningController {

    @Autowired
    private WarningService warningService;

    @Autowired
    WarningDao warningDao;



    @ApiOperation(value = "查询指定行政区，功能区，时间段的报警信息", notes = "需要定义行政区，功能区，开始时间，结束时间")
    @ApiImplicitParam(name = "params", value = "包含行政区，功能区，开始时间，结束时间的json", dataType = "JSON")
    @RequestMapping(value = "/queryWarningByDomainAndTimeAndDistrictAndStation", method = RequestMethod.POST)
    public String getWarningByDomainAndTimeAndDistrictAndStation(@RequestBody Map<String, Object> params) throws ParseException {
        System.out.println(params.toString());

        String warning_district = params.get("district_name").toString();
        int warning_domain = (Integer) params.get("domain_id");
        String station_id = params.get("station_id").toString();
        String start_time = params.get("requestStartTime").toString();
        String end_time = params.get("requestEndTime").toString();

        return warningService.queryWarningByDomainAndTimeAndDistrictAndStation(warning_district, warning_domain, start_time, end_time, station_id);
    }

    @ApiOperation(value = "查询实时报警数据")
    @RequestMapping(value = "/queryRealWarning", method = RequestMethod.POST)
    public Map queryRealWarning() {
        return warningService.getRealWarning();
    }
}
