package com.web.oa.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.oa.mapper.LeavebillMapper;
import com.web.oa.pojo.Leavebill;
import com.web.oa.service.LeaveBillService;

@Service
public class LeaveBillServiceImpl implements LeaveBillService {

	@Autowired
	private LeavebillMapper leaveBillMapper;

	// 保存请假单
	@Override
	public void saveLeaveBill(Leavebill leaveBill) {
		this.leaveBillMapper.insert(leaveBill);

	}

	// 根据id删除请假单信息
	@Override
	public void deleteLeavebill(Long id) {
		this.leaveBillMapper.deleteByPrimaryKey(id);
	}

	// 根据id查询请假单信息
	@Override
	public Leavebill findLeaveBillById(long id) {
		return this.leaveBillMapper.selectByPrimaryKey(id);
	}

}
