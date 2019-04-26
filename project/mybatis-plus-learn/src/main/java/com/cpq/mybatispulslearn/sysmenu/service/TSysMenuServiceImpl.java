package com.cpq.mybatispulslearn.sysmenu.service;

import com.cpq.mybatispulslearn.sysmenu.entity.TSysMenu;
import com.cpq.mybatispulslearn.sysmenu.mapper.TSysMenuMapper;
import com.cpq.mybatispulslearn.sysmenu.service.TSysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * SASS功能权限清单表，管理员管理此菜单表 服务实现类
 * </p>
 *
 * @author cpq
 * @since 2019-04-18
 */
@Service
public class TSysMenuServiceImpl extends ServiceImpl<TSysMenuMapper, TSysMenu> implements TSysMenuService {

}
