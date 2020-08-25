package com.web.oa.junit;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.web.oa.mapper.SysPermissionMapperCustom;
import com.web.oa.pojo.MenuTree;
import com.web.oa.pojo.SysPermission;
import com.web.oa.service.SysService;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext.xml","classpath:spring/springmvc.xml"})
public class TestMenu {

	@Autowired
	private SysPermissionMapperCustom mapper;
	@Autowired
	private SysService sysService;
	
	@Test
	public void testMenu() {
		List<MenuTree> list = mapper.getMenuTree();
		for (MenuTree menuTree : list) {
			System.out.println(menuTree.getId()+"."+menuTree.getName());
			List<SysPermission> subMenu = menuTree.getChildren();
			for (SysPermission sysPermission : subMenu) {
				System.out.println("\t" + sysPermission.getName()+","+sysPermission.getPercode()+","+sysPermission.getUrl());
			}
		}
	}
	
	@Test
	public void testPermission() throws Exception {
		List<SysPermission> list = sysService.findPermissionListByUserId("mike");
		for (SysPermission sysPermission : list) {
			System.out.println(sysPermission.getName() + "," + sysPermission.getUrl()+","+sysPermission.getPercode());
		}
	}
	
	@Test
	public void testMenuAndPermission() throws Exception {
		List<SysPermission> list = sysService.findMenuAndPermissionByUserId("li");
		for (SysPermission sysPermission : list) {
			System.out.println(sysPermission.getType()+"\n"+sysPermission.getName() + "," + sysPermission.getUrl()+","+sysPermission.getPercode());
		}
	}
}
