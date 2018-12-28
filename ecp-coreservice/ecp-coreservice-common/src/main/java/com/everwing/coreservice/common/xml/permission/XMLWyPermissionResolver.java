package com.everwing.coreservice.common.xml.permission;/**
 * Created by wust on 2017/8/16.
 */

import com.everwing.coreservice.common.exception.ECPBusinessException;
import com.everwing.coreservice.common.utils.CommonUtils;
import com.everwing.coreservice.common.wy.common.enums.WyEnum;
import com.everwing.coreservice.common.wy.entity.system.menu.TSysMenu;
import com.everwing.coreservice.common.wy.entity.system.resource.TSysResource;
import com.everwing.coreservice.common.xml.XMLDefaultResolver;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.ResourceUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Function:
 * Reason:
 * Date:2017/8/16
 *
 * @author wusongti@lii.com.cn
 */
public class XMLWyPermissionResolver extends XMLDefaultResolver {
    static Logger logger = LogManager.getLogger(XMLWyPermissionResolver.class);

    /**
     * 元素名称
     */

    private static final String ELEMENT_SUBSYSTEM = "subsystem";
    private static final String ELEMENT_PATH = "path";
    private static final String ELEMENT_MODULEGROUP = "modulegroup";
    private static final String ELEMENT_MODULE = "module";
    private static final String ELEMENT_OPERATION = "operation";

    /**
     * 属性名称
     */
    private static final String ELEMENT_ATTRIBUTE_ID = "id";
    private static final String ELEMENT_ATTRIBUTE_NAME = "name";
    private static final String ELEMENT_ATTRIBUTE_DESC = "desc";
    private static final String ELEMENT_ATTRIBUTE_PERMISSION = "permission";
    private static final String ELEMENT_ATTRIBUTE_URL = "url";
    private static final String ELEMENT_ATTRIBUTE_ORDER = "order";
    private static final String ELEMENT_ATTRIBUTE_IMG = "img";
    private static final String ELEMENT_ATTRIBUTE_PATH = "path";
    private static final String ELEMENT_ATTRIBUTE_PAGE = "page";


    /**
     * 解析到的数据集合
     */
    private final Map<String, Object> permissionsMap = new HashMap<>();
    private final Map<String, Object> parseMenuMap = new HashMap<>();
    private final Map<String, Object> parseResourceMap = new HashMap<>();
    private final List<TSysMenu> parseMenuList = new ArrayList<TSysMenu>();
    private final List<TSysResource> parseResourceList = new ArrayList<TSysResource>();


    @Override
    public Map<String, List> getResult() {
        this.parseXML();
        Map<String, List> map = new HashMap<>(2);
        map.put("parseMenuList",parseMenuList);
        map.put("parseResourceList",parseResourceList);
        return map;
    }

    public void parseXML() throws ECPBusinessException {
        String mainXMLPath = "permissions" + File.separator + "xml" + File.separator + "main.xml";
        String mainXSDPath = "permissions" + File.separator + "xsd" + File.separator + "main.xsd";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        validateXML(mainXSDPath, mainXMLPath);

        try{
            DocumentBuilder db = dbf.newDocumentBuilder();
            org.w3c.dom.Document doc = db.parse(ResourceUtils.getFile("classpath:" + mainXMLPath));
            org.w3c.dom.Element element = doc.getDocumentElement();
            doParseXML(element, db);
        }catch (Exception e){
            throw new ECPBusinessException(e);
        }
    }


    private void doParseXML(org.w3c.dom.Element element, DocumentBuilder db) throws Exception {
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            short nodeType = node.getNodeType();
            if (nodeType == Node.ELEMENT_NODE) {
                if (ELEMENT_SUBSYSTEM.equals(node.getNodeName())) {   // 解析subsystem节点
                    String id = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_ID);
                    if (!parseMenuMap.containsKey(id)) {  // 忽略已经解析过的节点
                        if (!"*".equals(id) && StringUtils.isNotBlank(id)) {    // id属性值为星号或为空则可以忽略
                            String name = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_NAME);
                            String desc = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_DESC);
                            String permission = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_PERMISSION);
                            String url = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_URL);
                            String orderString = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_ORDER);
                            String order = StringUtils.isBlank(CommonUtils.null2String(orderString)) ? "-1" : orderString;


                            TSysMenu menuEntity = new TSysMenu();
                            menuEntity.setMenuId(id);
                            menuEntity.setMenuName(name);
                            menuEntity.setMenuDesc(desc);
                            menuEntity.setMenuPermission(permission);
                            menuEntity.setMenuUrl(url);
                            menuEntity.setMenuLevel(0);
                            menuEntity.setMenuOrder(Integer.parseInt(order));
                            menuEntity.setpId(null);

                            parseMenuList.add(menuEntity);
                            parseMenuMap.put(menuEntity.getMenuId(), menuEntity);
                        } else {
                            logger.info("属性id值为[" + id + "]，的节点忽略");
                        }
                    } else {
                        logger.info("已经解析过此节点：" + parseMenuMap.get(id));
                    }
                } else if (ELEMENT_MODULEGROUP.equals(node.getNodeName())) {   // 解析modulegroup节点
                    String id = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_ID);
                    if (!parseMenuMap.containsKey(id)) {  // 忽略已经解析过的节点
                        if (!"*".equals(id) && StringUtils.isNotBlank(id)) {    // id属性值为星号或为空则可以忽略
                            String name = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_NAME);
                            String desc = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_DESC);
                            String permission = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_PERMISSION);
                            String url = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_URL);
                            String pid = ((org.w3c.dom.Element) node.getParentNode()).getAttribute(ELEMENT_ATTRIBUTE_ID);
                            String orderString = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_ORDER);
                            String order = StringUtils.isBlank(CommonUtils.null2String(orderString)) ? "-1" : orderString;
                            String img = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_IMG);


                            TSysMenu menuEntity = new TSysMenu();
                            menuEntity.setMenuId(id);
                            menuEntity.setMenuName(name);
                            menuEntity.setMenuDesc(desc);
                            menuEntity.setMenuPermission(permission);
                            menuEntity.setMenuUrl(url);
                            TSysMenu tSysMenuParent = parseMenuMap.get(pid) == null ? null : (TSysMenu) parseMenuMap.get(pid);
                            menuEntity.setMenuLevel(tSysMenuParent == null ? 0 : (tSysMenuParent.getMenuLevel() + 1));
                            menuEntity.setMenuOrder(Integer.parseInt(order));
                            menuEntity.setMenuImg(img);
                            menuEntity.setpId(pid);

                            parseMenuList.add(menuEntity);
                            parseMenuMap.put(menuEntity.getMenuId(), menuEntity);
                        } else {
                            logger.info("属性id值为[" + id + "]，的节点忽略");
                        }
                    } else {
                        logger.info("已经解析过此节点：" + parseMenuMap.get(id));
                    }
                } else if (ELEMENT_MODULE.equals(node.getNodeName())) {
                    String id = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_ID);
                    if (!parseMenuMap.containsKey(id)) {  // 忽略已经解析过的节点
                        if (!"*".equals(id) && StringUtils.isNotBlank(id)) {    // id属性值为星号或为空则可以忽略
                            String name = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_NAME);
                            String desc = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_DESC);
                            String permission = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_PERMISSION);
                            String url = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_URL);
                            String pid = ((org.w3c.dom.Element) node.getParentNode()).getAttribute(ELEMENT_ATTRIBUTE_ID);
                            String orderString = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_ORDER);
                            String order = StringUtils.isBlank(CommonUtils.null2String(orderString)) ? "-1" : orderString;
                            String img = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_IMG);
                            String page = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_PAGE);
                            String parentPage = ((org.w3c.dom.Element) node.getParentNode()).getAttribute(ELEMENT_ATTRIBUTE_PAGE);


                            // 设置菜单权限标识，如果权限标识没有填写，则用page名称作为permission
                            String permissionNew = "";
                            if (StringUtils.isBlank(CommonUtils.null2String(permission))) {
                                if (StringUtils.isBlank(CommonUtils.null2String(parentPage))) {
                                    permissionNew = page;
                                } else {
                                    permissionNew = parentPage + "_" + page;
                                }
                            } else {
                                permissionNew = permission;
                            }

                            // 判断权限标识是否重复了
                            if (!WyEnum.anon.name().equalsIgnoreCase(permissionNew) && StringUtils.isNotBlank(permissionNew)) {
                                if (permissionsMap.containsKey(permissionNew)) {
                                    throw new Exception("模块[" + desc + "]，权限标识[" + permissionNew + "]和已有的权限标识重复，请换一个。");
                                } else {
                                    permissionsMap.put(permissionNew, null);
                                }
                            }


                            TSysMenu menuEntity = new TSysMenu();
                            menuEntity.setMenuId(id);
                            menuEntity.setMenuName(name);
                            menuEntity.setMenuDesc(desc);
                            menuEntity.setMenuUrl(url);
                            TSysMenu tSysMenuParent = parseMenuMap.get(pid) == null ? null : (TSysMenu) parseMenuMap.get(pid);
                            menuEntity.setMenuLevel(tSysMenuParent == null ? 0 : (tSysMenuParent.getMenuLevel() + 1));
                            menuEntity.setMenuOrder(Integer.parseInt(order));
                            menuEntity.setMenuImg(img);
                            menuEntity.setpId(pid);
                            menuEntity.setMenuPermission(permissionNew);

                            parseMenuList.add(menuEntity);
                            parseMenuMap.put(menuEntity.getMenuId(), menuEntity);
                        } else {
                            logger.info("属性id值为[" + id + "]，的节点忽略");
                        }
                    } else {
                        logger.info("已经解析过此节点：" + parseMenuMap.get(id));
                    }
                } else if (ELEMENT_OPERATION.equals(node.getNodeName())) {
                    String id = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_ID);
                    if (!parseResourceMap.containsKey(id)) {  // 忽略已经解析过的节点
                        if (!"*".equals(id) && StringUtils.isNotBlank(id)) {    // id属性值为星号或为空则可以忽略
                            String name = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_NAME);
                            String desc = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_DESC);
                            String permission = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_PERMISSION);
                            String url = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_URL);
                            String pid = ((org.w3c.dom.Element) node.getParentNode()).getAttribute(ELEMENT_ATTRIBUTE_ID);
                            String parentPermission = ((org.w3c.dom.Element) node.getParentNode()).getAttribute(ELEMENT_ATTRIBUTE_PERMISSION);
                            String parentPage = ((org.w3c.dom.Element) node.getParentNode()).getAttribute(ELEMENT_ATTRIBUTE_PAGE);


                            /*
                             * 权限标识=父菜单权限标识.操作按钮权限标识，
                             * 如果父菜单权限标识不填写，则使用page作为前缀，如page="user"，则权限标识为user.add
                             */
                            String permissionNew = "";
                            if (WyEnum.anon.name().equalsIgnoreCase(parentPermission) || WyEnum.anon.name().equalsIgnoreCase(permission)) {
                                // 父级菜单是白名单，则自己默认也是白名单
                                permissionNew = WyEnum.anon.name();
                            } else {
                                // 父级菜单没有写权限标识，则使用page作为permission
                                if (StringUtils.isBlank(CommonUtils.null2String(parentPermission))) {
                                    permissionNew = parentPage;
                                } else {
                                    permissionNew = parentPermission;
                                }

                                // 如果operation节点没有设置权限标识，则根据name属性值生成：Search --> search, Ab Cd-->abcd
                                if (StringUtils.isBlank(CommonUtils.null2String(permission))) {
                                    String oPermissionNew = name.replace(" ", "");
                                    oPermissionNew = oPermissionNew.replace(oPermissionNew.substring(0, 1), oPermissionNew.substring(0, 1).toLowerCase());
                                    permissionNew = permissionNew + "." + oPermissionNew;
                                } else {
                                    permissionNew = permissionNew + "." + permission;
                                }
                            }

                            TSysResource resourceEntity = new TSysResource();
                            resourceEntity.setSrcId(id);
                            resourceEntity.setSrcName(name);
                            resourceEntity.setSrcDesc(desc);
                            resourceEntity.setSrcUrl(url);
                            resourceEntity.setMenuId(pid);
                            resourceEntity.setSrcPermission(permissionNew);

                            parseResourceList.add(resourceEntity);
                            parseResourceMap.put(resourceEntity.getSrcId(), resourceEntity);

                            /**
                             * 把父节点修改为叶子节点
                             */
                            TSysMenu tSysMenu = (TSysMenu) parseMenuMap.get(pid);
                            parseMenuList.remove(tSysMenu);
                            tSysMenu.setMenuLevel(-1);
                            parseMenuList.add(tSysMenu);
                            parseMenuMap.put(pid, tSysMenu);

                        } else {
                            logger.info("属性id值为[" + id + "]，的节点忽略");
                        }
                    } else {
                        logger.info("已经解析过此节点：" + parseResourceMap.get(id));
                    }
                } else if (ELEMENT_PATH.equals(node.getNodeName())) {
                    String path = ((org.w3c.dom.Element) node).getAttribute(ELEMENT_ATTRIBUTE_PATH);
                    if (!StringUtils.isBlank(path) && !"*".equals(path)) {// 星号表示可以忽略的节点
                        String XMLPath = path;
                        String XSDPath = "permissions" + File.separator + "xsd" + File.separator + "module.xsd";
                        super.validateXML(XSDPath, XMLPath);
                        org.w3c.dom.Document doc = db.parse(ResourceUtils.getFile("classpath:" + XMLPath));
                        org.w3c.dom.Element subElement = doc.getDocumentElement();
                        doParseXML(subElement, db);
                    } else {
                        logger.info("属性path值为[" + path + "]，的节点忽略");
                    }
                } else {
                    logger.info("未知节点：" + node.getNodeName());
                }
                doParseXML((org.w3c.dom.Element) node, db);
            }
        }
    }

}
