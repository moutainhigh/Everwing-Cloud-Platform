package com.everwing.coreservice.common.wy.initialization;/**
 * Created by wust on 2018/8/16.
 */

import com.everwing.cache.redis.SpringRedisTools;
import com.everwing.coreservice.common.utils.jdbc.DBParams;
import com.everwing.coreservice.common.utils.jdbc.DBUtil;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.system.menu.TSysMenu;
import com.everwing.coreservice.common.wy.entity.system.resource.TSysResource;
import com.everwing.coreservice.common.xml.XMLAbstractResolver;
import com.everwing.coreservice.common.xml.XMLDefinitionFactory;
import com.everwing.coreservice.common.xml.factory.XMLWyPermissionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * Function:初始化菜单资源
 * Reason:
 * Date:2018/8/16
 * @author wusongti@lii.com.cn
 */
@Component
public class InitializeMenusResources extends AbstractInitializeData {
    @Value("${platform.jdbc.url}")
    private String platformUrl;

    @Value("${platform.jdbc.username}")
    private String platformUsername;

    @Value("${platform.jdbc.password}")
    private String platformPassword;

    @Value("${platform.jdbc.driver}")
    private String platformDriver;

    @Autowired
    private SpringRedisTools springRedisTools;


    @Override
    public void init(String companyId) {
        XMLDefinitionFactory xmlDefinitionFactory = new XMLWyPermissionFactory();
        XMLAbstractResolver xmlAbstractResolver = xmlDefinitionFactory.createXMLResolver();

        Map<String,List> resultMap = xmlAbstractResolver.getResult();
        List<TSysMenu> parseMenuList = resultMap.get("parseMenuList");
        List<TSysResource> parseResourceList = resultMap.get("parseResourceList");

        Map<String, Object> map = getCompanyById(companyId);
        String url = map.get("jdbc_url") + "";
        String username = map.get("jdbc_username") + "";
        String password = map.get("jdbc_password") + "";
        Connection connection = DBUtil.getConn(getPlatformDriver(), url, username, password);


        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;
        PreparedStatement pst3 = null;
        PreparedStatement pst4 = null;
        PreparedStatement pst5 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs5 = null;
        try {
            connection.setAutoCommit(false);//关闭自动提交模式，此时必须通过显示调用commit()提交

            /**
             * 1.找出已经分配给角色的脏菜单并删除
             */
            DBParams params1 = new DBParams();
            params1.addParam(WyEnum.m.name());
            String selectSql1 = "SELECT * FROM t_sys_role_resource t WHERE 1=1 AND t.src_type = ?;";
            pst1 = connection.prepareStatement(selectSql1);
            params1.prepareStatement(pst1);
            rs1 = pst1.executeQuery();
            while(rs1.next()){
                String srcId = rs1.getString("src_id");
                boolean flag = false;
                for (TSysMenu tSysMenu : parseMenuList) {
                    if (tSysMenu.getMenuId().equals(srcId)) {
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    String delSql1 = "DELETE FROM t_sys_role_resource WHERE src_type =? AND src_id = ?;";

                    params1 = new DBParams();
                    params1.addParam(WyEnum.m.name());
                    params1.addParam(srcId);

                    pst1 = connection.prepareStatement(delSql1);
                    params1.prepareStatement(pst1);
                    pst1.executeUpdate();
                }
            }


            /**
             * 2.找出已经分配给角色的脏资源并删除
             */
            DBParams params2 = new DBParams();
            params2.addParam(WyEnum.r.name());
            String selectSql2 = "SELECT * FROM t_sys_role_resource t WHERE 1=1 AND t.src_type = ?;";
            pst2 = connection.prepareStatement(selectSql2);
            params2.prepareStatement(pst2);
            rs2 = pst2.executeQuery();
            while(rs2.next()){
                String srcId = rs2.getString("src_id");
                boolean flag = false;
                for (TSysResource tSysResource : parseResourceList) {
                    if (tSysResource.getSrcId().equals(srcId)) {
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    String delSql2 = "DELETE FROM t_sys_role_resource WHERE src_type =? AND src_id = ?;";

                    params2 = new DBParams();
                    params2.addParam(WyEnum.r.name());
                    params2.addParam(srcId);

                    pst2 = connection.prepareStatement(delSql2);
                    params2.prepareStatement(pst2);
                    pst2.executeUpdate();
                }
            }


            /**
             * 3.初始化菜单
             */
            if(!CollectionUtils.isEmpty(parseMenuList)){
                String delSql3 = "DELETE FROM t_sys_menu;";
                pst3 = connection.prepareStatement(delSql3);
                pst3.executeUpdate();

                String insertSql3 = "INSERT INTO t_sys_menu(menu_id ,menu_name,menu_desc,menu_url,menu_permission,menu_level,menu_order,menu_img,pid,create_id,create_time)VALUES(?,?,?,?,?,?,?,?,?,?,sysdate());";
                pst3 = connection.prepareStatement(insertSql3);
                for (TSysMenu tSysMenu : parseMenuList) {
                    pst3.setString(1,tSysMenu.getMenuId());
                    pst3.setString(2,tSysMenu.getMenuName());
                    pst3.setString(3,tSysMenu.getMenuDesc());
                    pst3.setString(4,tSysMenu.getMenuUrl());
                    pst3.setString(5,tSysMenu.getMenuPermission());
                    pst3.setInt(6,tSysMenu.getMenuLevel());
                    pst3.setInt(7,tSysMenu.getMenuOrder());
                    pst3.setString(8,tSysMenu.getMenuImg());
                    pst3.setString(9,tSysMenu.getpId());
                    pst3.setString(10,tSysMenu.getCreaterId());
                    pst3.addBatch();
                }

                pst3.executeBatch();
            }


            /**
             * 4.初始化资源
             */
            if(!CollectionUtils.isEmpty(parseMenuList)){
                String delSql4 = "DELETE FROM t_sys_resource;";
                pst4 = connection.prepareStatement(delSql4);
                pst4.executeUpdate();

                String insertSql4 = "INSERT INTO t_sys_resource(src_id ,menu_id,src_name,src_desc,src_permission,src_url,create_id,create_time)VALUES(?,?,?,?,?,?,?,sysdate());";
                pst4 = connection.prepareStatement(insertSql4);
                for (TSysResource tSysResource : parseResourceList) {
                    pst4.setString(1,tSysResource.getSrcId());
                    pst4.setString(2,tSysResource.getMenuId());
                    pst4.setString(3,tSysResource.getSrcName());
                    pst4.setString(4,tSysResource.getSrcDesc());
                    pst4.setString(5,tSysResource.getSrcPermission());
                    pst4.setString(6,tSysResource.getSrcUrl());
                    pst4.setString(7,tSysResource.getCreaterId());
                    pst4.addBatch();
                }

                pst4.executeBatch();
            }


            /**
             * 5.为已经分配了资源的角色重新设置白名单（因为程序员可能新增了白名单）
             */
            String selectSql5 = "SELECT rs.role_id,max(rs.src_id) AS src_id FROM t_sys_role_resource rs WHERE rs.src_type = 'm' GROUP BY rs.role_id;";
            pst5 = connection.prepareStatement(selectSql5);
            rs5 = pst5.executeQuery();
            while(rs5.next()){
                String roleId = rs5.getString("role_id");
                String menuId = rs5.getString("src_id");

                /**
                 * 重置该菜单下的白名单资源
                 */
                selectSql5 = "SELECT sr.src_id,sr.src_name,sr.src_desc,sr.src_permission,sr.src_url FROM t_sys_resource sr WHERE sr.src_permission = 'anon' AND sr.menu_id = ?;";
                DBParams params5 = new DBParams();
                params5.addParam(menuId);
                pst5 = connection.prepareStatement(selectSql5);
                params5.prepareStatement(pst5);
                rs5 = pst5.executeQuery();
                while(rs5.next()){
                    String srcId = rs5.getString("src_id");
                    String delSql5 = "DELETE FROM t_sys_role_resource where role_id = ? AND src_id = ?;";

                    params5 = new DBParams();
                    params5.addParam(roleId);
                    params5.addParam(srcId);

                    pst5 = connection.prepareStatement(delSql5);
                    params5.prepareStatement(pst5);
                    pst5.executeUpdate();

                    String insertSql5 = "INSERT INTO t_sys_role_resource(id,role_id,src_id,src_type) VALUES(?,?,?,?);";
                    pst5 = connection.prepareStatement(insertSql5);
                    pst5.setString(1,UUID.randomUUID().toString());
                    pst5.setString(2,roleId);
                    pst5.setString(3,srcId);
                    pst5.setString(4,WyEnum.r.name());
                    pst5.executeUpdate();
                }
            }

            connection.commit();//提交事务
        } catch (SQLException e) {
            logger.error(e);
            DBUtil.rollBack(connection);
        }finally {
            DBUtil.closeRs(rs1);
            DBUtil.closeRs(rs2);
            DBUtil.closeRs(rs5);
            DBUtil.closePst(pst1);
            DBUtil.closePst(pst2);
            DBUtil.closePst(pst3);
            DBUtil.closePst(pst4);
            DBUtil.closePst(pst5);
            DBUtil.closeCon(connection);
        }
    }

    @Override
    public void init() {
        XMLDefinitionFactory xmlDefinitionFactory = new XMLWyPermissionFactory();
        XMLAbstractResolver xmlAbstractResolver = xmlDefinitionFactory.createXMLResolver();

        Map<String,List> resultMap = xmlAbstractResolver.getResult();
        List<TSysMenu> parseMenuList = resultMap.get("parseMenuList");
        List<TSysResource> parseResourceList = resultMap.get("parseResourceList");

        List<Map<String, Object>> companyList = getCompanyList();
        for (Map<String, Object> map : companyList) {
            String url = map.get("jdbc_url") + "";
            String username = map.get("jdbc_username") + "";
            String password = map.get("jdbc_password") + "";
            Connection connection = DBUtil.getConn(getPlatformDriver(), url, username, password);


            PreparedStatement pst1 = null;
            PreparedStatement pst2 = null;
            PreparedStatement pst3 = null;
            PreparedStatement pst4 = null;
            PreparedStatement pst5 = null;
            ResultSet rs1 = null;
            ResultSet rs2 = null;
            ResultSet rs5 = null;
            try {
                connection.setAutoCommit(false);//关闭自动提交模式，此时必须通过显示调用commit()提交

                /**
                 * 1.找出已经分配给角色的脏菜单并删除
                 */
                DBParams params1 = new DBParams();
                params1.addParam(WyEnum.m.name());
                String selectSql1 = "SELECT * FROM t_sys_role_resource t WHERE 1=1 AND t.src_type = ?;";
                pst1 = connection.prepareStatement(selectSql1);
                params1.prepareStatement(pst1);
                rs1 = pst1.executeQuery();
                while(rs1.next()){
                    String srcId = rs1.getString("src_id");
                    boolean flag = false;
                    for (TSysMenu tSysMenu : parseMenuList) {
                        if (tSysMenu.getMenuId().equals(srcId)) {
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        String delSql1 = "DELETE FROM t_sys_role_resource WHERE src_type =? AND src_id = ?;";

                        params1 = new DBParams();
                        params1.addParam(WyEnum.m.name());
                        params1.addParam(srcId);

                        pst1 = connection.prepareStatement(delSql1);
                        params1.prepareStatement(pst1);
                        pst1.executeUpdate();
                    }
                }


                /**
                 * 2.找出已经分配给角色的脏资源并删除
                 */
                DBParams params2 = new DBParams();
                params2.addParam(WyEnum.r.name());
                String selectSql2 = "SELECT * FROM t_sys_role_resource t WHERE 1=1 AND t.src_type = ?;";
                pst2 = connection.prepareStatement(selectSql2);
                params2.prepareStatement(pst2);
                rs2 = pst2.executeQuery();
                while(rs2.next()){
                    String srcId = rs2.getString("src_id");
                    boolean flag = false;
                    for (TSysResource tSysResource : parseResourceList) {
                        if (tSysResource.getSrcId().equals(srcId)) {
                            flag = true;
                            break;
                        }
                    }
                    if(!flag){
                        String delSql2 = "DELETE FROM t_sys_role_resource WHERE src_type =? AND src_id = ?;";

                        params2 = new DBParams();
                        params2.addParam(WyEnum.r.name());
                        params2.addParam(srcId);

                        pst2 = connection.prepareStatement(delSql2);
                        params2.prepareStatement(pst2);
                        pst2.executeUpdate();
                    }
                }


                /**
                 * 3.初始化菜单
                 */
                if(!CollectionUtils.isEmpty(parseMenuList)){
                    String delSql3 = "DELETE FROM t_sys_menu;";
                    pst3 = connection.prepareStatement(delSql3);
                    pst3.executeUpdate();

                    String insertSql3 = "INSERT INTO t_sys_menu(menu_id ,menu_name,menu_desc,menu_url,menu_permission,menu_level,menu_order,menu_img,pid,create_id,create_time)VALUES(?,?,?,?,?,?,?,?,?,?,sysdate());";
                    pst3 = connection.prepareStatement(insertSql3);
                    for (TSysMenu tSysMenu : parseMenuList) {
                        pst3.setString(1,tSysMenu.getMenuId());
                        pst3.setString(2,tSysMenu.getMenuName());
                        pst3.setString(3,tSysMenu.getMenuDesc());
                        pst3.setString(4,tSysMenu.getMenuUrl());
                        pst3.setString(5,tSysMenu.getMenuPermission());
                        pst3.setInt(6,tSysMenu.getMenuLevel());
                        pst3.setInt(7,tSysMenu.getMenuOrder());
                        pst3.setString(8,tSysMenu.getMenuImg());
                        pst3.setString(9,tSysMenu.getpId());
                        pst3.setString(10,tSysMenu.getCreaterId());
                        pst3.addBatch();
                    }

                    pst3.executeBatch();
                }


                /**
                 * 4.初始化资源
                 */
                if(!CollectionUtils.isEmpty(parseMenuList)){
                    String delSql4 = "DELETE FROM t_sys_resource;";
                    pst4 = connection.prepareStatement(delSql4);
                    pst4.executeUpdate();

                    String insertSql4 = "INSERT INTO t_sys_resource(src_id ,menu_id,src_name,src_desc,src_permission,src_url,create_id,create_time)VALUES(?,?,?,?,?,?,?,sysdate());";
                    pst4 = connection.prepareStatement(insertSql4);
                    for (TSysResource tSysResource : parseResourceList) {
                        pst4.setString(1,tSysResource.getSrcId());
                        pst4.setString(2,tSysResource.getMenuId());
                        pst4.setString(3,tSysResource.getSrcName());
                        pst4.setString(4,tSysResource.getSrcDesc());
                        pst4.setString(5,tSysResource.getSrcPermission());
                        pst4.setString(6,tSysResource.getSrcUrl());
                        pst4.setString(7,tSysResource.getCreaterId());
                        pst4.addBatch();
                    }

                    pst4.executeBatch();
                }


                /**
                 * 5.为已经分配了资源的角色重新设置白名单（因为程序员可能新增了白名单）
                 */
                String selectSql5 = "SELECT rs.role_id,max(rs.src_id) AS src_id FROM t_sys_role_resource rs WHERE rs.src_type = 'm' GROUP BY rs.role_id;";
                pst5 = connection.prepareStatement(selectSql5);
                rs5 = pst5.executeQuery();
                while(rs5.next()){
                    String roleId = rs5.getString("role_id");
                    String menuId = rs5.getString("src_id");

                    /**
                     * 重置该菜单下的白名单资源
                     */
                    selectSql5 = "SELECT sr.src_id,sr.src_name,sr.src_desc,sr.src_permission,sr.src_url FROM t_sys_resource sr WHERE sr.src_permission = 'anon' AND sr.menu_id = ?;";
                    DBParams params5 = new DBParams();
                    params5.addParam(menuId);
                    pst5 = connection.prepareStatement(selectSql5);
                    params5.prepareStatement(pst5);
                    rs5 = pst5.executeQuery();
                    while(rs5.next()){
                        String srcId = rs5.getString("src_id");
                        String delSql5 = "DELETE FROM t_sys_role_resource where role_id = ? AND src_id = ?;";

                        params5 = new DBParams();
                        params5.addParam(roleId);
                        params5.addParam(srcId);

                        pst5 = connection.prepareStatement(delSql5);
                        params5.prepareStatement(pst5);
                        pst5.executeUpdate();

                        String insertSql5 = "INSERT INTO t_sys_role_resource(id,role_id,src_id,src_type) VALUES(?,?,?,?);";
                        pst5 = connection.prepareStatement(insertSql5);
                        pst5.setString(1,UUID.randomUUID().toString());
                        pst5.setString(2,roleId);
                        pst5.setString(3,srcId);
                        pst5.setString(4,WyEnum.r.name());
                        pst5.executeUpdate();
                    }
                }

                connection.commit();//提交事务
            } catch (SQLException e) {
                logger.error(e);
                DBUtil.rollBack(connection);
            }finally {
                DBUtil.closeRs(rs1);
                DBUtil.closeRs(rs2);
                DBUtil.closeRs(rs5);
                DBUtil.closePst(pst1);
                DBUtil.closePst(pst2);
                DBUtil.closePst(pst3);
                DBUtil.closePst(pst4);
                DBUtil.closePst(pst5);
                DBUtil.closeCon(connection);
            }
        }
    }

    @Override
    public String getPlatformUrl() {
        return this.platformUrl;
    }

    @Override
    public String getPlatformUsername() {
        return this.platformUsername;
    }

    @Override
    public String getPlatformPassword() {
        return this.platformPassword;
    }

    @Override
    public String getPlatformDriver() {
        return platformDriver;
    }
}
