package org.seckill.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}", list);
    }

    @Test
    public void getById() {
        Seckill seckill = seckillService.getById(1000);
        logger.info("seckill={}", seckill);
    }

    @Test
    public void seckillLogic() {
        long id = 1001;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}", exposer);
        if (exposer.isExposed()) {
            long userPhone = 18650738527L;
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(id, userPhone, exposer.getMd5());
                logger.info("seckillExecution={}", seckillExecution);
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            }
        }
    }

}