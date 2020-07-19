package com.springboot.environment.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by sts on 2019/1/12.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class WarningServiceTest {

    @Autowired
    private WarningService warningService;

    @Test
    public void getRealWarning () throws Exception {
        System.out.println(warningService.getRealWarning());
    }
}