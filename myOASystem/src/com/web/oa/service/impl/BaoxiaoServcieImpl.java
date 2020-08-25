package com.web.oa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.oa.mapper.BaoxiaoBillMapper;
import com.web.oa.pojo.BaoxiaoBill;
import com.web.oa.pojo.BaoxiaoBillExample;
import com.web.oa.service.BaoxiaoService;

@Service
public class BaoxiaoServcieImpl implements BaoxiaoService {

	@Autowired
	private BaoxiaoBillMapper baoxiaoBillMapper;

	@Override
	public List<BaoxiaoBill> findBaoxiaoBillListByUser(Long userid) {
		BaoxiaoBillExample example = new BaoxiaoBillExample();
		BaoxiaoBillExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userid);
		return baoxiaoBillMapper.selectByExample(example);
	}

	@Override
	public void saveBaoxiao(BaoxiaoBill baoxiaoBill) {
		// 获取请假单ID
		Long id = baoxiaoBill.getId();
		/** 新增保存 */
		if (id == null) {
			// 1：从Session中获取当前用户对象，将LeaveBill对象中user与Session中获取的用户对象进行关联
			// leaveBill.setUser(SessionContext.get());//建立管理关系
			// 2：保存请假单表，添加一条数据
			baoxiaoBillMapper.insert(baoxiaoBill);
		}
		/** 更新保存 */
		else {
			// 1：执行update的操作，完成更新
			baoxiaoBillMapper.updateByPrimaryKey(baoxiaoBill);
		}

	}

	@Override
	public BaoxiaoBill findBaoxiaoBillById(Long id) {
		BaoxiaoBill bill = baoxiaoBillMapper.selectByPrimaryKey(id);
		return bill;
	}

	@Override
	public void deleteBaoxiaoBillById(Long id) {
		baoxiaoBillMapper.deleteByPrimaryKey(id);
	}

	@Override
	public List<BaoxiaoBill> findLeaveBillListByUser(Long id) {
		BaoxiaoBillExample example = new BaoxiaoBillExample();
		BaoxiaoBillExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(id);
		return baoxiaoBillMapper.selectByExample(example);
	}

	// 根据id删除请假单信息
	@Override
	public void deleteBaoxiaobill(Long id) {
		this.baoxiaoBillMapper.deleteByPrimaryKey(id);
	}

}
