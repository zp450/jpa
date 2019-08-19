package com.zp.Jpa.service.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.zp.Jpa.entity.queryentity.sys.SysPermissionQuery;
import com.zp.Jpa.entity.search.PermisssionQuerySearch;
import com.zp.Jpa.entity.sys.Node;
import com.zp.Jpa.entity.sys.SysPermission;
import com.zp.Jpa.repository.PermissionRepository;
import com.zp.Jpa.service.PermissionQueryService;
import com.zp.Jpa.service.PermissionService;
import com.zp.Jpa.tools.StringUtils;

@Service
public class PermissionServiceImpl implements PermissionService{
	private Map<String, Object> map = new HashMap<String, Object>();
	@Autowired
	private PermissionQueryService permissionQueryService;
	@Autowired
	private PermissionRepository permissionRepository;

	@Override
	public List<SysPermission> query(SysPermissionQuery permission) {
		//return mapper.query(permission);
		return null;
	}

	@Override
	public List<String> queryAll() {
		return permissionRepository.findAllPermissionValue();
	}

	@Override
	public int batchInsert(List<SysPermission> pList) {
		return permissionRepository.save(pList).size();
	}
	
	@Override
	public List<Node> queryNode(){
		//return permissionRepository.queryNode();
		return null;
	}
	/**
	 * 根据角色Ids查询出角色设置的权限树
	 */
	@SuppressWarnings("null")
	@Override
	
	public Map<String, Object> queryPermissionTree(Integer roleId) {
		//要选中的ids
		List<SysPermission> permissions =permissionRepository.queryPermissionIdsByRoleIds(roleId);//查询出角色拥有的权限Ids
		//先查出来模块集合
	//	List<String> modules=permissionRepository.findModuleName();
		List<SysPermission> sysPermission = permissionRepository.findAll();//查询出所有的权限
		//String aString="";
		//最外层
//		List<PermissionNode> Treelist=new ArrayList<PermissionNode>();
//		for (int i = 0; i < modules.size(); i++) {
//			PermissionNode permissionNode=new PermissionNode(null,modules.get(i),null);
//			Treelist.add(permissionNode);
//				for (int j = 0; j < sysPermission.size(); j++) {
//					if(modules.get(i).equals(sysPermission.get(j).getPermissionModule())) {
//						SysPermission q=sysPermission.get(j);
//						aString="id:"+q.getPermissionId()+",lable:"+q.getPermissionName();
//				}
//					Treelist.get(i).setChildrens(aString);
//			}
//		}
		Integer perIds[]=null;
		int i=0;
		if(!StringUtils.isEmpty(permissions)) {
			for(SysPermission node:permissions) {
				perIds[i]=node.getPermissionId();
				i+=1;
			}
		}
		map.put("checked", perIds);
		map.put("data", sysPermission);
		return map;
	}
//	public void setChildrens(List<SysPermission> parentList) {
//		// 遍历父节点 对象
//		for (SysPermission sysPermission : parentList) {
//			List<SysPermission> childrenList =permissionRepository.findByPermissionModule(sysPermission.getPermissionModule());
//			// 是否有孩子
//			if (childrenList != null && !childrenList.isEmpty()) {
//				// 调用set方法把他孩子存进去
//				sysPermission.setChildrens(childrenList);
//				// 如果孩子还有孩子那就接着调用，也就是有孙子
//				//this.setChildrens(childrenList);
//			}
//		}
//	}
	
//PermisssionQuerySearch//PermissionQuery
	@Override
	//public List<SysPermission> queryPermission(PermissionQuery pquery) {
		public List<SysPermission> queryPermission(PermisssionQuerySearch pquery) {
		// TODO Auto-generated method stub
		return permissionQueryService.queryPermissionQuery(pquery).getContent();
	}

	@Override
	public Integer addPermission(SysPermission permission) {
		// TODO Auto-generated method stub
		SysPermission sysPermission=permissionRepository.save(permission);
		if(sysPermission!=null) {
			return 1;
		}
		return 0;
	}

	@Override
	public Integer deletePermission(Integer permissionId) {
		// TODO Auto-generated method stub
		SysPermission sysPermission=permissionRepository.findOne(permissionId);
		sysPermission.setIsDel(1);
		SysPermission sysPermission2=permissionRepository.save(sysPermission);
		if(sysPermission2!=null) {
			return 1;
		}
		return 0;
	}

	@Override
	public Integer totalPermission(PermisssionQuerySearch pquery) {
		// TODO Auto-generated method stub
		
		return (int) permissionQueryService.queryPermissionQuery(pquery).getTotalElements();
	}

	@Override
	public Page<SysPermission> queryPermissionPage(PermisssionQuerySearch pquery) {
		// TODO Auto-generated method stub
		
		return permissionQueryService.queryPermissionQuery(pquery);
	}



	

	

	
	
	
	
	
	
	
	
	

}
