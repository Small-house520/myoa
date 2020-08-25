package com.web.oa.mapper;

import java.util.List;

import com.web.oa.pojo.SysPermission;
import com.web.oa.pojo.TreeMenu;

public interface SysPermissionCustomMapper {

	
	public List<TreeMenu> getTreeMenu();
	
	public List<SysPermission> getSubMenu(int id);
}
