package com.hellobike.pmo.cockpit;



import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hellobike.pmo.cockpit.util.DateUtil;
import com.hellobike.pmo.cockpit.util.HttpUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;


import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiefangrong15227 2021年12月7日
 */
public class SyncTable extends BaseTest {

    private static final Logger LOG = LoggerFactory.getLogger(SyncTable.class);


    private final static int MAX_NUM = 100;
    /**
     * 一些特别的，不是以Time结尾的Date类型
     */
    private final static Set<String> SP_TIME = new HashSet<>();

    private final static String token = "bearer_6d449eb9-0f5d-48f9-b4e0-7aa2c8d80022";

    static {
        SP_TIME.add("review_time_finish");
    }

    /**
     * 同步大数据表的时候一定要注意，确认好要同步多少条数据，尽量不要同步超过1w条数据
     */
    // @Test
    public void test() {
        Arrays.asList(
        // "t_analysis_department",
        //                 "t_sub_campaign",
        //                 "t_campaign",
        //                 "t_bu",
        //                 "t_target_s_bu_m",
        //                 "t_target_s_bu_q",
        //                 "t_target_s_bu_y",
        //                 "t_target_s_dept_m",
        //                 "t_target_s_dept_q",
        //                 "t_target_s_dept_y",
        //                 "t_target_s_campaign_m",
        //                 "t_target_s_campaign_q",
        //                 "t_target_s_campaign_y",
        //                 "t_target_bu_m",
        //                 "t_target_bu_q",
        //                 "t_target_bu_y",
        //                 "t_target_dept_m",
        //                 "t_target_dept_q",
        //                 "t_target_dept_y",
        //                 "t_target_campaign_m",
        //                 "t_target_campaign_q",
        //                 "t_target_campaign_y",
        //                 "t_target_sub_campaign_m",
        //                 "t_target_sub_campaign_q",
        //                 "t_target_sub_campaign_y",
        //                 "t_target_objective_m",
        //                 "t_target_objective_q",
        //                 "t_target_objective_y",
        //                 "t_target_key_result_m",
        //                 "t_target_key_result_q",
        //                 "t_target_key_result_y",
        //                 "t_target_bu_own_objective_m",
        //                 "t_target_bu_own_objective_q",
        //                 "t_target_bu_own_objective_y",
        //                 "t_target_bu_own_kr_m",
        //                 "t_target_bu_own_kr_q",
        //                 "t_target_bu_own_kr_y",
        //                 "t_target_dept_own_objective_m",
        //                 "t_target_dept_own_objective_q",
        //                 "t_target_dept_own_objective_y",
        //                 "t_target_dept_own_kr_m",
        //                 "t_target_dept_own_kr_q",
        //                 "t_target_dept_own_kr_y",
        //                 "t_target_bu_no_okr_m",
        //                 "t_target_bu_no_okr_q",
        //                 "t_target_bu_no_okr_y",
        //                 "t_target_dept_no_okr_m",
        //                 "t_target_dept_no_okr_q",
        //                 "t_target_dept_no_okr_y",
        //                 "t_target_dept_other_okr_m",
        //                 "t_target_dept_other_okr_q",
        //                 "t_target_dept_other_okr_y",
        //                 "t_target_bu_other_okr_detail_m",
        //                 "t_target_bu_other_okr_detail_q",
        //                 "t_target_bu_other_okr_detail_y",
        //                 "t_target_dept_other_okr_detail_m",
        //                 "t_target_dept_other_okr_detail_q",
        //                 "t_target_dept_other_okr_detail_y",
        //                 "t_object",
        //                 "t_key_result",
        //                 "t_project_okr_rel",
        //         "t_target_pro_bu_no_okr_d",
        //         "t_target_pro_bu_okr_d",
        //         "t_target_pro_bu_other_okr_d",
        //         "t_target_pro_bu_own_okr_d",
        //         "t_target_pro_campaign_d",
        //         "t_target_pro_dept_no_okr_d",
        //         "t_target_pro_dept_okr_d",
        //         "t_target_pro_dept_other_okr_d",
        //         "t_target_pro_dept_own_okr_d",
        //         "t_target_pro_sub_campaign_d",
        //         "t_target_bu_objective_m",
        //         "t_target_bu_objective_q",
        //         "t_target_bu_objective_y",
        //         "t_target_bu_own_objective_m",
        //         "t_target_bu_own_objective_q",
        //         "t_target_bu_own_objective_y",
        //         "t_target_dept_objective_m",
        //         "t_target_dept_objective_q",
        //         "t_target_dept_objective_y",
        //         "t_target_dept_own_objective_m",
        //         "t_target_dept_own_objective_q",
        //         "t_target_dept_own_objective_y",
        //         "t_target_objective_m",
        //         "t_target_objective_q",
        //         "t_target_objective_y",
        //         "t_target_bu_key_result_m",
        //         "t_target_bu_key_result_q",
        //         "t_target_bu_key_result_y",
        //         "t_target_dept_key_result_m",
        //         "t_target_dept_key_result_q",
        //         "t_target_dept_key_result_y",
        //         "t_target_key_result_m",
        //         "t_target_key_result_q",
        //         "t_target_key_result_y",
                "t_okr_target_configuration"
                        )
                .forEach(tableName -> {
                    if (tableName.contains(",")) {
                        final String[] tables = tableName.split(",");
                        syncTable(tables[0], tables[1]);
                    } else {
                        syncTable(tableName, "0");
                    }
                });
    }

    // 同步数据
    private void syncTable(final String tableName, final String startId) {
        final long startTime = System.currentTimeMillis();
        LOG.info("开始同步：{}", tableName);
        // 获取表格的字段信息,从字段信息中提取字段名
        final List<String> fieldList = queryFileds(MessageFormat.format("DESC {0}", tableName));

        // 删除表中id大于等于startId的数据
        deleteData(MessageFormat.format("DELETE FROM {0} WHERE id >= {1}", tableName, startId));

        // 构造插入语句的前半部分
        final String insertSqlHeader = MessageFormat.format(
                "INSERT INTO {0} ({1}) VALUES ",
                tableName,
                "`" + String.join("`,`", fieldList) + "`");
        int start = 0;
        // 使用while循环进行批量插入，防止一次插入过多数据导致数据库连接超时
        while (true) {
            // 构造查询语句，每次查询MAX_NUM条数据
            final String selectSql = MessageFormat.format(
                    "SELECT * FROM {0} WHERE id >= {3} ORDER BY id LIMIT {1,number,#},{2,number,#}",
                    tableName,
                    start,
                    MAX_NUM,
                    startId);
            // 执行查询语句，获取数据库记录
            final List<JSONObject> dataList = selectData(selectSql);
            // 如果查询结果为空，则退出循环
            if (dataList.size() <= 0) {
                break;
            }
            // 构造插入语句的后半部分
            final StringBuilder insertSql = new StringBuilder(insertSqlHeader);
            boolean first = true;
            for (int i = 0; i < dataList.size(); i++) {
                final JSONObject data = dataList.get(i);
                // 每个insert之间需要,分割
                if (!first) {
                    insertSql.append(",");
                } else {
                    first = false;
                }
                insertSql.append("(");
                for (int j = 0; j < fieldList.size(); j++) {
                    // 获取字段名
                    final String field = fieldList.get(j);
                    // 获取值
                    String value = data.getString(field);
                    // 判断字段是否为时间类型
                    final boolean isTime = field.endsWith("time") ||field.endsWith("date") || SP_TIME.contains(field);
                    // 如果是时间类型并且值不为空，进行时间格式转换
                    if (isTime && value != null && value.contains("T")) {
                        value = DateUtil.parseDate2String(String.valueOf(data.getDate(field)));
                    }
                    // 字段之间需要,分割
                    if (j != 0) {
                        insertSql.append(",");
                    }
                    // 如果值为空，插入null，否则插入值
                    if (value == null) {
                        insertSql.append("null");
                    } else {
                        insertSql.append("'");
                        insertSql.append(value.replace("\\", "\\\\").replace("'", "\\'"));
                        insertSql.append("'");
                    }
                }
                insertSql.append(")");
            }
            // 在所有记录都添加到insertSql后，添加一个分号，表示SQL语句的结束
            insertSql.append("; ");
            // 执行插入语句
            insertData(insertSql.toString());
            // 更新start，以便在下一次循环中获取下一批数据
            start += MAX_NUM;
        }
        LOG.info("完成同步：{}，用时：{}秒", tableName, (System.currentTimeMillis() - startTime) / 1000);
    }

    // 查询数据和获取表结构
    private List<JSONObject> selectData(final String sql) {
        LOG.info("查询语句：{}", sql);
        String params = new JSONObject().fluentPut("sql", sql).toString();
        Map<String,String> headers = new HashMap<>();
        headers.put("token",token);
        final JSONObject selectResp = JSONObject.parseObject(HttpUtil.doPost("https://pre-toms-app.hellobike.cn/api/components/common/data/queryDataBySql",
                headers, params));

        if (selectResp.getIntValue("code") != 10000) {
            LOG.info("查询请求失败：{}", selectResp);
            // throw new PassException("查询请求失败");
        }
        JSONArray jsonArray = selectResp.getJSONArray("data");
        List<JSONObject> jsonObjectList = new ArrayList<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObjectList.add(jsonObject);
        }
        return jsonObjectList;
    }

    // 插入数据
    private Boolean insertData(final String sql) {
        LOG.info("插入语句：{}", sql);
        String params = new JSONObject().fluentPut("sql", sql).toString();
        Map<String,String> headers = new HashMap<>();
        headers.put("token",token);
        // 需要重新获取
        final JSONObject insertResp = JSONObject.parseObject(HttpUtil.doPost("https://fat-toms-app.hellobike.cn/api/components/common/data/insertDataBySql",
                headers, params));
        if (insertResp.getIntValue("code") != 10000) {
            LOG.info("更新请求失败：{}", insertResp);
            // throw new PassException("更新请求失败");
        }
        Boolean isSuccess = insertResp.getBoolean("data");

        return isSuccess;
    }

    private Boolean deleteData(final String sql) {
        LOG.info("删除语句：{}", sql);
        String params = new JSONObject().fluentPut("sql", sql).toString();
        Map<String,String> headers = new HashMap<>();
        headers.put("token",token);

        // 需要重新获取
        final JSONObject deleteResp = JSONObject.parseObject(HttpUtil.doPost("https://fat-toms-app.hellobike.cn/api/components/common/data/deleteDataBySql",
                headers, params));
        if (deleteResp.getIntValue("code") != 10000) {
            LOG.info("更新请求失败：{}", deleteResp);
            // throw new PassException("更新请求失败");
        }
        Boolean isSuccess = deleteResp.getBoolean("data");

        return isSuccess;
    }

    private List<String> queryFileds(final String sql) {
        LOG.info("查询字段信息：{}", sql);
        String params = new JSONObject().fluentPut("sql", sql).toString();
        Map<String,String> headers = new HashMap<>();
        headers.put("token",token);
        // 需要重新获取
        final JSONObject filedResp = JSONObject.parseObject(HttpUtil.doPost("https://fat-toms-app.hellobike.cn/api/components/common/data/queryFiledsBySql",
                headers, params));
        if (filedResp.getIntValue("code") != 10000) {
            LOG.info("更新请求失败：{}", filedResp);
            // throw new PassException("更新请求失败");
        }
        List<String> jsonObjectList = filedResp.getJSONArray("data").stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        return jsonObjectList;
    }



}

