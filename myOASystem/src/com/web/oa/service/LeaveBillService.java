package com.web.oa.service;

import com.web.oa.pojo.Leavebill;

public interface LeaveBillService {
	void saveLeaveBill(Leavebill leaveBill);

	void deleteLeavebill(Long id);

	Leavebill findLeaveBillById(long id);
}
