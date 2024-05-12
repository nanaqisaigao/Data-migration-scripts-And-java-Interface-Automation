package com.hellobike.pmo.cockpit.model.basic.devteamapproval;

import lombok.Data;

import java.util.Date;

/**
 * @author renmengxiwb304
 * @date 2024/3/5
 */
@Data
public class DevTeamMappingDOTest {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 开发团队id
     */
    private Long devTeamId;
    /**
     * 开发团队名称
     */
    private String devTeamName;
    /**
     * 分析部门id
     */
    private Long analysisDeptId;
    /**
     * 分析部门名称
     */
    private String analysisDeptName;
    /**
     * bu id
     */
    private Long buId;
    /**
     * bu 名称
     */
    private String buName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private String createUser;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人
     */
    private String updateUser;
    /**
     * 是否删除
     */
    private Integer isDelete;
}
