package com.web.oa.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.web.oa.pojo.Leavebill;
import com.web.oa.pojo.LeavebillExample;

public interface LeavebillMapper {
	int countByExample(LeavebillExample example);

	int deleteByExample(LeavebillExample example);

	int deleteByPrimaryKey(Long id);

	int insert(Leavebill record);

	int insertSelective(Leavebill record);

	List<Leavebill> selectByExample(LeavebillExample example);

	Leavebill selectByPrimaryKey(Long id);

	int updateByExampleSelective(@Param("record") Leavebill record, @Param("example") LeavebillExample example);

	int updateByExample(@Param("record") Leavebill record, @Param("example") LeavebillExample example);

	int updateByPrimaryKeySelective(Leavebill record);

	int updateByPrimaryKey(Leavebill record);
}