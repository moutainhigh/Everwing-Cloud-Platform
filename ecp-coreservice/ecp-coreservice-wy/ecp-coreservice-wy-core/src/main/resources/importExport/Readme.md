export文件夹：存放导出配置
import文件夹：存放导入配置


********************************************************************导入配置示例开始*************************************************
<!--  excel元素，一个xml配置只能有一个  -->
<!--  id属性，必填，且属性值必须在整个系统保持唯一，命名格式最好是：大模块名_小模块名  -->
<excel id="basedata_building">
    <!--  sheet元素，一个excel元素里面根据业务需求可以有多个sheet节点 -->
    <!--  index属性，必填，且值从0开始表示第一个工作页，以此类推index=1表示第二个工作页...index=n表示第(n+1)个工作页 -->
    <sheet index="1">
        <!--  list元素，一个sheet元素下面只能有一个list节点  -->
        <!--  startRow属性，必填，表示数据行的开始行数，其值和sheet里面的行数一一对应  -->
        <!--  class属性，必填，值对应实体类  -->
        <list startRow="1" class="com.everwing.coreservice.common.wy.entity.property.building.TcBuildingImportList">
            <!--  field元素，一个list元素下面可以有多个field节点，根据class配置的实体类的字段一一对应 -->
            <!--  index属性，必填，对应excel表格列的位置，从0开始表示第一列，以此类推 -->
            <!--  property属性，必填，对应class导入实体类里面的属性名称 -->
            <!--  required属性，必填，表示该字段是必填项 -->
            <!--  type属性，非必填，字段值类型，其值可选范围[String,Double,Float,Long,Integer,Date,Map,LookupItem]，默认为String类型 -->
            <!--  regex属性和regexErrMsg属性，使用正则regex验证字段值并提供自定义错误消息regexErrorMsg 一一对应 -->
            <!--  pattern属性，当type属性=Date的时候必须要有pattern属性，表示日期格式 -->
            <!--  format属性， 当type属性=Map的时候，需要增加format属性，format表示自定义键值对格式的值，如format=1:是;2:否 -->
            <!--  lookupCode属性，当type属性=LookupItem的时候，需要增加lookupCode属性，lookupCode属性的值来自t_sys_lookup.xml里面的code值 -->
            <field index="0" property="fieldName4String" required="true"></field>
            <field index="1" property="fieldName4Double" type="Double" required="true" regex="^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$" regexErrMsg="只能输入整数或两位小数点以内的数字"></field>
            <field index="2" property="fieldName4Integer" type="Integer" required="true" regex="[0-9]" regexErrMsg="只能输入0到9的数字"></field>
            <field index="3" property="fieldName4Date" type="Date" required="true" pattern="yyyy-MM-dd"></field>
            <field index="4" property="propertyRightType" type="LookupItem" lookupCode="propertyRightType"></field>
            <field index="5" property="fieldName4Lookup" type="Map" required="true" lookupItem="houseType"></field>
            <field index="6" property="fieldName4Format" type="Map" required="true" format="Yes:是;No:否"></field>
        </list>
    </sheet>
</excel>
********************************************************************导入配置示例结束*************************************************




********************************************************************导出配置示例开始*************************************************
<!--  excel元素，一个xml配置只能有一个  -->
<!--  id属性，必填，且属性值必须在整个系统保持唯一，命名格式最好是：大模块名_小模块名  -->
<excel id="basedata_building">
    <!--  sheet元素，根据实际需求可以有多个  -->
    <!--  label属性，必填，表示sheet名称  -->
    <sheet label="住宅信息">
        <!--  list元素，只能有一个，表示数据列表  -->
        <list>
            <!--  sql元素，只能有一个，查询数据的SQL  -->
            <!--  id属性，必填，工具生成UUID  -->
            <sql id="20f49248-4193-48cf-b8b9-d4d2a1b1c8e7">
                <![CDATA[
                    SELECT
                            a,
                            b,
                            c,
                            d
                    FROM t
                    WHERE 1 = 1
                    <#if projectId??>
                        AND project_id = '${projectId}'
                    </#if>
                ]]>
            </sql>

            <!--  field元素，可以有多个，对应excel的列数据  -->
            <!--  column属性，必填，属性值对应sql的查询列名  -->
            <!--  label属性，必填，导出的excel列名  -->
            <!--  type属性，必填，导出的数据类型，可选值[String,Double,Integer,Date,Map,lookupItem]  -->
            <!--  format属性，当type=Double或type=Date时，可以对数据进行格式化  -->
            <!--  lookupCode属性，当type属性=LookupItem的时候，需要增加lookupCode属性，lookupCode的值来自t_sys_lookup.xml里面的code值 -->
            <field column="a" label="字符串值"></field>
            <field column="b" label="Double值" type="Double" format="#.00"></field>
            <field column="c" label="日期值" type="Date" format="yyyy-MM-dd"></field>
            <field column="d" label="产权类型" type="LookupItem" lookupCode="propertyRightType"></field>

            当lookupCode需要从查询的记录中获取的时候，可以使用变量${column_name}来代替，具体可以参考basedata_building.xml
        </list>
    </sheet>
</excel>
********************************************************************导出配置示例结束*************************************************
