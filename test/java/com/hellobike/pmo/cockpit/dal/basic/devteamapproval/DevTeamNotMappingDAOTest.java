package com.hellobike.pmo.cockpit.dal.basic.devteamapproval;

import com.hellobike.pmo.cockpit.model.basic.devteamapproval.DevTeamNotMappingDOTest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author renmengxiwb304
 * @date 2024/3/6
 */
@Mapper
public interface DevTeamNotMappingDAOTest {
    /**
     * 根据开发团队名称模糊查询研发团队未关联分析部门记录
     * @param devTeamName 开发团队名称
     * @return List<DevTeamNotMappingDO>
     */
    List<DevTeamNotMappingDOTest> queryListTest(@Param("devTeamName") String devTeamName);

    /**
     * 根据id更新t_analysis_dev_team_not_mapping记录
     * @param id 主键id
     * @param mappingState 是否绑定映射
     * @param updateUser 更新人
     * @return 是否更新成功
     */
    Boolean updateTest( @Param("id") Long id,
                    @Param("mappingState") Integer mappingState,
                    @Param("updateUser") String updateUser);
    Boolean addTest(@Param("devTeamId") Long devTeamId,
                @Param("devTeamName") String devTeamName,
                @Param("mappingState") Integer mappingState,
                @Param("createUser") String createUser,
                @Param("updateUser") String updateUser);

    /**
     * 批量更新not_mapping
     * @param updateIds
     * @param mappingState
     * @return
     */
    Boolean batchUpdateTest(@Param("updateIds")List<Long> updateIds, @Param("mappingState")Integer mappingState);
}
