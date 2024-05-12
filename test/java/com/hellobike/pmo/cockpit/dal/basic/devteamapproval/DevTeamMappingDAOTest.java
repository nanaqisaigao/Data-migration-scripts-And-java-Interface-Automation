package com.hellobike.pmo.cockpit.dal.basic.devteamapproval;

import com.hellobike.pmo.cockpit.dal.basic.devteamapproval.mapping.DevTeamMappingDO;
import com.hellobike.pmo.cockpit.model.basic.devteamapproval.DevTeamMappingDOTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author renmengxiwb304
 * @date 2024/3/6
 */
@Mapper
public interface DevTeamMappingDAOTest {
    /**
     * 根据开发团队名称等模糊查询获得AnalysisDeptDevTeamNotMappingDO
     * @param devTeamName 开发团队名称
     * @return List<DevTeamMappingDO>
     */
    List<DevTeamMappingDOTest> queryListTest(@Param("devTeamName") String devTeamName);

    /**
     * 根据id更新t_analysis_dev_team_not_mapping表
     * @param id 主键id
     * @param analysisDeptId 分析部门id
     * @param analysisDeptName 开发团队名称
     * @param updateUser: 更新人邮箱
     * @return 是否更新成功
     */
    Boolean updateTest(@Param("id") Long id,
                   @Param("analysisDeptId") Long analysisDeptId,
                   @Param("analysisDeptName") String analysisDeptName,
                   @Param("updateUser") String updateUser);



    /**
     * 向t_analysis_dev_team_not_mapping表插入记录
     * @param devTeamId 开发团队id
     * @param devTeamName 开发团队名称
     * @param analysisDeptId 分析团队id
     * @param analysisDeptName 分析团队名称
     * @param buId bu
     * @param buName bu名称
     * @param createUser 创建人
     * @param updateUser 更新人
     * @return 是否更新成功
     */
    Boolean addTest(@Param("devTeamId") Long devTeamId,
                @Param("devTeamName") String devTeamName,
                @Param("analysisDeptId") Long analysisDeptId,
                @Param("analysisDeptName") String analysisDeptName,
                @Param("buId") Long buId,
                @Param("buName") String buName,
                @Param("createUser") String createUser,
                @Param("updateUser") String updateUser);
    Boolean deleteById(@Param("id") Long id,
                       @Param("updateUser") String updateUser);

    /**
     * 向t_analysis_dev_team_not_mapping表插入记录
     * @param list
     * @return 是否添加成功
     */
    Boolean batchSaveTest(List<DevTeamMappingDO> list);
}
