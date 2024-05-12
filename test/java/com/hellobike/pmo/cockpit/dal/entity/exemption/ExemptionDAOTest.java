package com.hellobike.pmo.cockpit.dal.entity.exemption;

import com.google.common.collect.Lists;
import com.hellobike.pmo.cockpit.BaseTest;
import com.hellobike.pmo.cockpit.dal.entity.exemption.query.ApplyExemptionDO;
import com.hellobike.pmo.cockpit.dal.entity.exemption.query.job.ProjectExemptionForJobDO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ly
 * @since 1.0
 */
public class ExemptionDAOTest extends BaseTest {

    @Autowired
    ExceptionExemptionDAO exceptionExemptionDAO;

    @Test
    public void getApplyExemptionInfoTest(){


        ZonedDateTime zonedDateTime1 = LocalDateTime.of(2023, 6, 5, 0, 0, 0)
                .atZone(ZoneId.systemDefault());
        Date from = Date.from(zonedDateTime1.toInstant());
        ZonedDateTime zonedDateTime2 = LocalDateTime.of(2023, 6, 6, 0, 0, 0)
                .atZone(ZoneId.systemDefault());
        Date to = Date.from(zonedDateTime2.toInstant());

        ApplyExemptionDO applyExemptionInfo = exceptionExemptionDAO.getApplyExemptionInfo(2725L, from,to);

    }

    @Test
    public void initProjectExemptionConfigTest() throws ParseException {

        List<Long> l = new ArrayList<>();
        l.add(1569L);
        l.add(2725L);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = s.parse("2023-01-01");
        Date parse1 = s.parse("2023-10-01");
        List<ProjectExemptionForJobDO> latestByProjectIds = exceptionExemptionDAO.getLatestByProjectIds(l, parse, parse1);
    }
}
