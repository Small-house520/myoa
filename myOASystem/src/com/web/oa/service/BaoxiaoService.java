package com.web.oa.service;

import java.util.List;

import com.web.oa.pojo.BaoxiaoBill;

public interface BaoxiaoService {

	public List<BaoxiaoBill> findBaoxiaoBillListByUser(Long userid);

	public void saveBaoxiao(BaoxiaoBill baoxiaoBill);

	public BaoxiaoBill findBaoxiaoBillById(Long id);

	public void deleteBaoxiaoBillById(Long id);

	public List<BaoxiaoBill> findLeaveBillListByUser(Long id);

	public void deleteBaoxiaobill(Long id);
}
