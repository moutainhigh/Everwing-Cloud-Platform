<!DOCTYPE html>
<html>
	<head>
		<!--if lt IE 9]>
        <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->
		<meta charset="utf-8" />
		<title></title>
		<style type="text/css"> 
			body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,input,button,textarea,p,blockquote,th,td { margin:0; padding:0; }
			body { background:#f2f2f2; color:#555; font-size:12px; font-family: SimSun, "微软雅黑"; width: 100%; }
			td,th,caption { font-size:12px; }
			h1, h2, h3, h4, h5, h6 { font-weight:normal; font-size:100%; }
			h6{ font-size: 12px;}
			address, caption, cite, code, dfn, em, strong, th, var,nav { font-style:normal; font-weight:normal;}
			a { color:#008ed8; text-decoration:none; }
			a:hover { text-decoration:none; cursor: pointer; }
			img { border:none; }
			ol,ul,li { list-style:none; }
			input, textarea, select, button { font:12px SimSun; }
			table { border-collapse:collapse; }
			html {overflow-y: scroll;} 
			.w{ width: 700px; margin:0 auto; display: block; }
			.contentP{ clear:both; display:block;background: #ffffff;box-shadow: 0px 1px 3px 3px #e1e1e1;}
			.PT20{padding-top:20px ;}
			.red{ color: #ff0000;}
			.fontW{ font-weight: bold;}
			.Ptopinfo{ margin:0px auto ; width: 100%;}
			.Ptopinfo ul li{ display: inline-block; height: 280px; float: left; }
			.Ptopinfo .Pinfo{ height:186px; width: 58%;}
			.Ptopinfo .Pinfo dl{ width: 100% ; height:24px; line-height: 24px; display: block; clear: both; }
			.Ptopinfo .Pinfo dt{ text-align: right; float: left; color: #7d7d7d;  padding-left: 12px;}
			.Ptopinfo .Pinfo dd{ text-align: left; float: left; color: #aaaaaa;width:300px ; }
			 h1{ font-size:32px; font-weight: bold; color:#018dd8 ; display:block; text-align:center; margin-bottom:8px}
			 h4{ font-size:24px; font-weight: bold; color:#333 ; display:block; text-align:center; margin-bottom:6px;
			 width: 650px; margin-left: auto; margin-right: auto;}
			.Ptopinfo .PinfoR{ height:186px;  float: right;}
			.Ptopinfo .PinfoR dl{ width: 100% ; height:24px; line-height: 24px; display: block;  }
			.Ptopinfo .PinfoR dt{  text-align: right; float: left; color: #7d7d7d; }
			.Ptopinfo .PinfoR dd{ width: 150px; text-align: left; float: left; color: #aaaaaa; }
			.Ptopinfo .PinfoR .red{ color: #ff0000; font-weight: bold;}
			.Ptitle{ clear: both; width:100%; border-bottom: 2px solid #e5e5e5; background: #f1f1f1; height:24px; line-height: 24px; margin-top: 10px;}
			.Ptitle ul li{ float:left}
			.Ptitle ul li.zjx{ float:right; padding-right:18px; font-weight:bold}
			.titlec{padding: 0 16px; background: #48b9f3; color: #ffffff; width:14% ; text-align: center; font-weight: bold; font-size:13px ; cursor: pointer;}
			.Ptitle01{ clear: both; width:96%; border-bottom: 2px solid #e5e5e5;  height: 24px; line-height: 24px; margin: 0 auto; }
			.titlec01{padding: 0 8px;border-bottom: 2px solid #48b9f3; width:16% ; text-align: center; font-weight: bold; font-size:14px ; cursor: pointer; margin-top:6px}
			.titInfo{ height:40px; padding:6px ;}
			.titInfo dl{  height:24px; line-height: 24px; display: block; float: left; padding-left:4% }
			.titInfo dt{  text-align: right; float: left; color: #7d7d7d; }
			.titInfo dd{ text-align: left; float: left; color: #aaaaaa; margin-right:32px; }
			/* 打印table css */
			.Pzebra{ width:94% ; margin:16px auto ; border: 2px solid #eeeeee;}
			.Pzebra td { padding:4px;  line-height:24px; height: 24px;border: 2px solid #eeeeee; text-align:center}
			.Pzebra th {text-align: center; height: 24px;padding:4px; background-color: #f1f1f1;border: 1px solid #e4e3e3;}
			/*****打印info2******/
			.titInfo1{ height: auto; padding:6px;}
			.titInfo1 dl{  height:24px; line-height: 24px; display: block; float: left; width:210px; padding-left:4%}
			.titInfo1 dt{  text-align: right; float: left; color: #7d7d7d; }
			.titInfo1 dd{ text-align: left; float: left; color: #aaaaaa; margin-right:32px; }
			.zongji{ height:32px; margin:126px auto 10px auto; border-top: 2px solid #d3d3d3; width:96%; display: block;  font-size:16px ; font-weight:bold ; display:block; padding-left:10px; padding-top:6px }
			.zongji .reda{color: #f10000;}
			.Pbeizhu{ height: auto; width: 90%; margin:0px  auto 20px  auto ;}
			.Pbeizhu h5{ font-size: 15px; color: #7c7c7c;}
			.Pbeizhu p{ line-height: 24px; color: #bdbdbd; }
			.erwm{ width: 60%; margin:60px auto 10px  auto; height: auto; clear: both; text-align: center;}
			.erwm p{line-height: 20px; color: #bdbdbd; }
            .zhangdanW{width:794px;height:1123px;}
            .zhangdanW01{width:794px;height:374px; }
            .zhangdanW01-Left{width:50%;height:350px; float:left; text-align:center; padding-top:24px}
            .zhangdanW01-Left input{ width:50px; height:50px; line-height:50px; font-size:50px; font-weight:bold; text-align:center }
            .zhangdanW01-Left .timg{ width:340px; height:30px }
            .zhangdanW01-Left h1{ text-align:left; margin-top:40px; padding:0; text-align:center}
            .zhangdanW01-Left .xxz{ width:310px; height:20px; line-height:20px; margin-top:-10px; text-align:center; margin-left:40px}
            .zhangdanW01-Right{width:44%;height:234px; padding-top:140px; float:right; text-align:right; padding-right:20px;}
		</style>
	</head>
	<body>
        <div class="w">
		    <div class="right" >
                <div class="contentP PT20 ">
                    <h1>账单</h1>
                    <h4>${all.fullName}</h4>
                    <div class="Ptopinfo">
                        <ul>
                            <li class="Pinfo">
                                <dl>
                                    <dt>客户姓名：</dt>
                                    <dd>${all.custName}</dd>
                                </dl>
                                <dl>
                                    <dt>客户编号 ：</dt>
                                    <dd>${all.custCode}</dd>
                                </dl>
                                <dl>
                                    <dt>帐单编号 ：</dt>
                                    <dd>${all.billCode}</dd>
                                </dl>
                                <dl>
                                    <dt>帐单打印日期：</dt>
                                    <dd>${all.billGenTime}</dd>
                                </dl>
                                <dl>
                                    <dt>房屋面积：</dt>
                                    <dd>${all.area}</dd>
                                </dl>
                                <dl>
                                    <dt>银行扣款帐号：</dt>
                                    <dd>${all.bankAccount}</dd>
                                </dl>
                            </li>
                            <li class="PinfoR">
                                <dl>
                                    <dt>上期欠费金额（合计）：</dt>
                                    <dd class="fontW">${all.lastTotalBill}元</dd>
                                </dl>
                                <dl>
                                    <dt>本期新产生费用（合计）：</dt>
                                    <dd class="fontW">${all.currTotalBill} 元</dd>
                                </dl>
                                <dl>
                                    <dt>违约金（合计）：</dt>
                                    <dd class="fontW">${all.totalLateFee} 元  </dd>
                                </dl>
                                <dl>
                                    <dt>本期应付（合计）：</dt>
                                    <dd class="red  fontW">${all.currBilling} 元 </dd>
                                </dl>
                            </li>
                        </ul>
                    </div>
				<#if (wy??)>
                    <div class="Ptitle">
                        <ul>
                            <li class="titlec">物业管理费</li>
                            <li class="zjx">欠费总额：<span class="red">${wy.total}元 </span></li>
                        </ul>
                    </div>
                    <div class="titInfo">
                        <dl>
                            <dt>本期费用：</dt>
                            <dd class="fontW">${wy.currFee}元</dd>
                        </dl>
                        <dl>
                            <dt>上期账单金额：</dt>
                            <dd class="fontW">${wy.lastBillFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>违约金：</dt>
                            <dd class="fontW">${wy.lateFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>分摊金额：</dt>
                            <dd class="fontW">${wy.shareFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>上期已付：</dt>
                            <dd class="fontW">${wy.lastPayed}元 </dd>
                        </dl>
                        <dl>
                            <dt>账户抵扣/交费：</dt>
                            <dd class="fontW">${wy.currPayed}元</dd>
                        </dl>
                    </div>
				</#if>
				<#if (bt??)>
                    <div class="Ptitle">
                        <ul>
                            <li class="titlec">本体基金</li>
                            <li class="zjx">欠费总额：<span class="red">${bt.total}元 </span></li>
                        </ul>
                    </div>
                    <div class="titInfo">
                        <dl>
                            <dt>本期费用：</dt>
                            <dd class="fontW">${bt.currFee}元</dd>
                        </dl>
                        <dl>
                            <dt>上期账单金额：</dt>
                            <dd class="fontW">${bt.lastBillFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>违约金：</dt>
                            <dd class="fontW">${bt.lateFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>分摊金额：</dt>
                            <dd class="fontW">${bt.shareFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>上期已付：</dt>
                            <dd class="fontW">${bt.lastPayed}元 </dd>
                        </dl>
                        <dl>
                            <dt>账户抵扣/交费：</dt>
                            <dd class="fontW">${bt.currPayed}元</dd>
                        </dl>
                    </div>
				</#if>
				<#if (water??)>
                    <div class="Ptitle">
                        <ul>
                            <li class="titlec">水费</li>
                            <li class="zjx">欠费总额：<span class="red">${water.total}元 </span></li>
                        </ul>
                    </div>
                    <div class="titInfo1">
                        <dl>
                            <dt>上次水表读数：</dt>
                            <dd class="fontW">${water.lastReading}</dd>
                        </dl>
                        <dl>
                            <dt>本次水表读数：</dt>
                            <dd class="fontW">${water.totalReading}</dd>
                        </dl>
                        <dl>
                            <dt>水表倍数：</dt>
                            <dd class="fontW">${water.rate}</dd>
                        </dl>
                        <dl>
                            <dt>本次实际用量：</dt>
                            <dd class="fontW">${water.useCount}</dd>
                        </dl>
                        <dl>
                            <dt>本期费用：</dt>
                            <dd class="fontW">${water.currFee}元</dd>
                        </dl>
                        <dl>
                            <dt>上期账单金额：</dt>
                            <dd class="fontW">${water.lastBillFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>违约金：</dt>
                            <dd class="fontW">${water.lateFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>分摊金额：</dt>
                            <dd class="fontW">${water.shareFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>上期已付：</dt>
                            <dd class="fontW">${water.lastPayed}元 </dd>
                        </dl>
                        <dl>
                            <dt>账户抵扣/交费：</dt>
                            <dd class="fontW">${water.currPayed}元</dd>
                        </dl>
                    </div>
				</#if>
				<#if (elect??)>
                    <div class="Ptitle">
                        <ul>
                            <li class="titlec">电费</li>
                            <li class="zjx">欠费总额：<span class="red">${elect.total}元</span></li>
                        </ul>
                    </div>
                    <div class="titInfo1">
                        <dl>
                            <dt>上次电读数（总）：</dt>
                            <dd class="fontW">${elect.lastReading}</dd>
                        </dl>
                        <dl>
                            <dt>本次电读数（总）：</dt>
                            <dd class="fontW">${elect.totalReading}</dd>
                        </dl>
                        <dl>
                            <dt>电表倍数：</dt>
                            <dd class="fontW">${elect.rate}</dd>
                        </dl>
                        <dl>
                            <dt>本次实际用量：</dt>
                            <dd class="fontW">${elect.useCount}</dd>
                        </dl>
                        <dl>
                            <dt>本期费用：</dt>
                            <dd class="fontW">${elect.currFee}元</dd>
                        </dl>
                        <dl>
                            <dt>上期账单金额：</dt>
                            <dd class="fontW">${elect.lastBillFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>违约金：</dt>
                            <dd class="fontW">${elect.lateFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>分摊金额：</dt>
                            <dd class="fontW">${elect.shareFee}元 </dd>
                        </dl>
                        <dl>
                            <dt>上期已付：</dt>
                            <dd class="fontW">${elect.lastPayed}元 </dd>
                        </dl>
                        <dl>
                            <dt>账户抵扣/交费：</dt>
                            <dd class="fontW">${elect.currPayed}元</dd>
                        </dl>
                    </div>
				</#if>
                    <div class="zongji">
                        本期应付合计：<span class="reda">${all.currBilling}元</span>
                    </div>
                    <div class="Pbeizhu">
                        <h5>备注：</h5>
                        <p>1.本帐单仅为用户管理费及相关费用的通知单，收到本帐单后请核对应交费的明细，如有疑问请拔下管理处电话或在邻音APP上查看；</p>
                        <p>2.收到本帐单后，请及时确认您所提供的代扣代缴帐户金额是否足够，以确保缴费成功，我处交分别于每月10号、25号进行银行托收。每月抄表时间为每月下旬</p>
                        <p>3.违约金的计算方式：如每月30号前未交费，系统将自动计算，物业管理费和本体基金违约金公式=总欠费*0.05%*实际天数，水费和电费
                            违约金=总欠费*0.06%*实际天数.</p>
                    </div>
                </div>
            </div>
		</div>
        <div style="page-break-after: always;"></div>
        <div class="zhangdanW">
            <!--1/3-->
            <div class="zhangdanW01">
                <div class="zhangdanW01-Left">
                    <form action="" method="get">
                        <#if postCode??>
                            <input name="" type="text" value="${postCode?substring(0,1)}" />
                            <input name="" type="text" value="${postCode?substring(1,2)}"/>
                            <input name="" type="text" value="${postCode?substring(2,3)}"/>
                            <input name="" type="text" value="${postCode?substring(3,4)}"/>
                            <input name="" type="text" value="${postCode?substring(4,5)}"/>
                            <input name="" type="text" value="${postCode?substring(5,6)}"/>
                        <#else>
                            <input name="" type="text" />
                            <input name="" type="text" />
                            <input name="" type="text" />
                            <input name="" type="text" />
                            <input name="" type="text" />
                            <input name="" type="text" />
                        </#if>
                    </form>
                    <p>${all.fullName}</p>
                    <h1>${all.custName}</h1>
                    <p class="xxz">${houseCode}</p>

                </div>
                <div class="zhangdanW01-Right">
                    <p><img src="/ftl/img/erweim.png" width="100" height="100" /></p>
                    <p><img src="/ftl/img/logo.jpg" width="100" height="34" /></p>
                    <p>邮政编码：${postCode?if_exists}</p>
                </div>
            </div>
            <!--1/3-->
        </div>
	</body>
</html>
