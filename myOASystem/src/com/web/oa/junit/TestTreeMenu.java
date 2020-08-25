package com.web.oa.junit;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.oa.mapper.SysPermissionCustomMapper;
import com.web.oa.pojo.SysPermission;
import com.web.oa.pojo.TreeMenu;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext.xml","classpath:spring/springmvc.xml"})
public class TestTreeMenu {

	@Autowired
	private SysPermissionCustomMapper mapper;
	
	@Test
	public void testMenu() {
		List<TreeMenu> list = mapper.getTreeMenu();
		for (TreeMenu treeMenu : list) {
			System.out.println(treeMenu.getId()+"."+treeMenu.getName());
			List<SysPermission> subMenus = treeMenu.getSubMenu();
			for (SysPermission subMenu : subMenus) {
				System.out.println("\t" + subMenu.getName()+","+subMenu.getUrl()+","+subMenu.getPercode());
			}
		}
	}
	
}
