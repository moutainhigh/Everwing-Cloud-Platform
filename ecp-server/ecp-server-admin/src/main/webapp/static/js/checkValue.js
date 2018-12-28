/**
  * common验证
  */
 function checkValue()
 {
	 return {
			 	/**
	             * 判断输入的值知否为空
	             * @param item
	             * @returns {{state: string, info: string}}
	             */
	            isNull : function(item){
	                if(typeof item=='number')
	                {
	                    item = item.toString();
	                }
	                var result = {state : '', info : ''};
	                if(item==undefined || item == '' || item == null){
	                    result.state = false;
	                    result.info = '输入不能为空！';
	                }else{
	                    result.state = true;
	                    result.info = '输入无误！';
	                }
	                return result;
	            },

	            /**
	             * 判断输入的值是否为正整数且不为空
	             * @param item
	             * @returns {{state: string, info: string}}
	             */
	            isOnlyNumberAndNotNull : function(item){
	                var result = {state : '', info : ''};
	                if(item==undefined || item == '' || item == null){
	                    result.state = false;
	                    result.info = '输入不能为空！';
	                }else{
	                    var reg = new RegExp("^\\d+$");
	                    if(!reg.test(item)){
	                        result.state = false;
	                        result.info = '只能输入不小于0的整数！';
	                    }else{
	                        result.state = true;
	                        result.info = '输入无误！';
	                    }
	                }
	                return result;
	            },

	            /**
	             * 判断输入的值不为空时是否为正整数
	             * @param item
	             * @returns {{state: string, info: string}}
	             */
	            isOnlyNumberOrNull : function(item){
	                var result = {state : '', info : ''};
	                if(item==undefined && item != '' && item != null){
	                    var reg = new RegExp("^\\d+$");
	                    if(!reg.test(item)){
	                        result.state = false;
	                        result.info = '只能输入不小于0的整数！';
	                    }else{
	                        result.state = true;
	                        result.info = '输入无误！';
	                    }
	                }else{
	                    result.state = true;
	                    result.info = '可以为空';
	                }
	                return result;
	            },

	            /**
	             * 判断输入的值是否为正浮点数且不为空
	             * @param item
	             * @returns {{state: string, info: string}}
	             */
	            isNumberAndNotNull : function(item){
	                var result = {state : '', info : ''};
	                if(item==undefined || item == '' || item == null){
	                    result.state = false;
	                    result.info = '输入不能为空！';
	                }else{
	                    var reg = new RegExp("^\\d+(\\.\\d+)?$");
	                    if(!reg.test(item)){
	                        result.state = false;
	                        result.info = '只能输入不小于0的数字！';
	                    }else{
	                        result.state = true;
	                        result.info = '输入无误！';
	                    }
	                }
	                return result;
	            },

	            /**
	             * 判断输入的值是否为空或正浮点数
	             * @param item
	             * @returns {{state: string, info: string}}
	             */
	            isNumberOrNull : function(item){
	                var result = {state : '', info : ''};
	                if(item!=undefined && item != '' && item != null){
	                    var reg = new RegExp("^\\d+(\\.\\d+)?$");
	                    if(!reg.test(item)){
	                        result.state = false;
	                        result.info = '只能输入不小于0的数字！';
	                    }else{
	                        result.state = true;
	                        result.info = '输入无误！';
	                    }
	                }else{
	                    result.state = true;
	                    result.info = '可以为空！';
	                }
	                return result;
	            },

	            /**
	             * 判断输入的值是否为正确的邮箱格式且不为空
	             * @param item
	             * @returns {{state: string, info: string}}
	             */
	            isEmailAndNotNull : function(item){
	                var result = {state : '', info : ''};
	                if(item==undefined || item == '' || item == null){
	                    result.state = false;
	                    result.info = '输入不能为空！';
	                }else{
	                    var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
	                    if(!reg.test(item)){
	                        result.state = false;
	                        result.info = '输入的邮箱格式不正确！';
	                    }else{
	                        result.state = true;
	                        result.info = '输入无误！';
	                    }
	                }
	                return result;
	            },

	            /**
	             * 判断输入的值不为空时是否为正确的邮箱格式
	             * @param item
	             * @returns {{state: string, info: string}}
	             */
	            isEmailOrNull : function(item){
	                var result = {state : '', info : ''};
	                if(item!=undefined && item != '' && item != null){
	                    var reg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
	                    if(!reg.test(item)){
	                        result.state = false;
	                        result.info = '输入的邮箱格式不正确！';
	                    }else{
	                        result.state = true;
	                        result.info = '输入无误！';
	                    }
	                }else{
	                    result.state = true;
	                    result.info = '可以为空！';
	                }
	                return result;
	            },

	            /**
	             * 判断输入的值是否是手机号或者座机号且不为空
	             * @param item
	             * @returns {{state: string, info: string}}
	             */
	            isPhoneOrTelAndNotNull : function(item){
	                var result = {state : '', info : ''};
	                if(item==undefined || item == '' || item == null){
	                    result.state = false;
	                    result.info = '输入不能为空！';
	                }else{
	                    var reg1 = /^((\d{3}-\d{8}|\d{4}-\d{7,8})|(((13[0-9])|(15[^4,\D])|(18[0,5-9]))\d{8}))$/;
	                    if(!reg1.test(item)){
	                        result.state = false;
	                        result.info = '输入的手机号或电话号码格式不正确！';

	                    }else{
	                        result.state = true;
	                        result.info = '输入无误！';
	                    }
	                }
	                return result;
	            },

	            /**
	             * 判断输入的值不为空时是否为正确的手机号或座机号
	             * @param item
	             * @returns {{state: string, info: string}}
	             */
	            isPhoneOrTelOrNull : function(item){
	                var result = {state : '', info : ''};
	                if(item!=undefined && item != '' && item != null){
	                    var reg1 = /^((\d{3}-\d{8}|\d{4}-\d{7,8})|(((13[0-9])|(15[^4,\D])|(18[0,5-9]))\d{8}))$/;
	                    if(!reg1.test(item)){
	                        result.state = false;
	                        result.info = '输入的手机号或电话号码格式不正确！';

	                    }else{
	                        result.state = true;
	                        result.info = '输入无误！';
	                    }
	                }else{
	                    result.state = true;
	                    result.info = '可以为空！';
	                }
	                return result;
	            },

	            /**
	             * 判断时间时间格式是否正确
	             */
	            isTimerRight: function(item)
	            {
	                var result = {state : '', info : ''};
	                if(item!=undefined&&item != '' && item != null){
	                    item = item.replace(/\//g,'-');
	                    var reg1 = /^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))$/;
	                    if(!reg1.test(item)){
	                        result.state = false;
	                        result.info = '输入的时间格式不正确！';

	                    }else{
	                        result.state = true;
	                        result.info = '输入时间正确！';
	                    }
	                }else{
	                    result.state = true;
	                    result.info = '可以为空！';
	                }
	                return result;
	            },

	            /**
	             * 判断时间时间格式是否正确且不能为空
	             */
	            isTimerRightAndNotNull: function(item)
	            {
	                var result = {state : '', info : ''};
	                if(item!=undefined&&item != '' && item != null){
	                	item = item.replace(/\//g,'-');
	                    var reg1 = /^([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))$/;
	                    if(!reg1.test(item)){
	                        result.state = false;
	                        result.info = '输入的时间格式不正确！';

	                    }else{
	                        result.state = true;
	                        result.info = '输入时间正确！';
	                    }
	                }else{
	                    result.state = false;
	                    result.info = '时间不能为空！';
	                }
	                return result;
	            },
	            /**
	             * 判断结束时间是否大于开始时间且判断时间格式是否正确且不能为空
	             */
	            isrightTime: function(startTime,endTime)
	            {
	                var result = {state : '', info : ''};
	                var start = this.isTimerRightAndNotNull(startTime);
	                var end = this.isTimerRightAndNotNull(endTime);
	                if(start.state&&end.state)
	                {
	                    startTime = startTime.replace(/\//g,'-');
	                    endTime = endTime.replace(/\//g,'-');
	                    if(endTime<=startTime)
	                    {
	                        result.state = false;
	                        result.info = '开始时间不能大于结束时间！';
	                    }else
	                    {
	                        result.state = true;
	                        result.info = '输入正确！';
	                    }
	                }else if(start.state==false)
	                {
	                	if(start.info=='输入的时间格式不正确！'){
                			result.state = false;
	                    	result.info = '开始时间输入的时间格式不正确！';
	                	}else{
                			result.state = false;
	                    	result.info = '开始时间不能为空！';
	                	}
	                }else if(end.state==false)
	                {
	                	if(end.info=='输入的时间格式不正确！'){
                			result.state = false;
	                    	result.info = '结束输入的时间格式不正确！';
	                	}else{
                			result.state = false;
	                    	result.info = '结束时间不能为空！';
	                	}
	                }
	                return result;
	            },
	            /**
	             * 判断对象是否为空
	             *  @param obj
	             */
	            isObjNull: function(item){
	                for(var i in item){
						if(item.hasOwnProperty(i))
						{
							return false;
						}
	                }
	                return true;
	             },

	            /**
	             * 判断是否是一个数组
	             */
	            isArrayNull: function(item)
	            {
	                var result = {state : '', info : ''};
	                if(item instanceof Array)
	                {
	                    if(item.length!=0)
	                    {
	                        result.state = true;
	                        result.info = '非空数组';
	                    }else
	                    {
	                        result.state = true;
	                        result.info = '空数组';
	                    }
	                }else
	                {
	                    result.state = false;
	                    result.info = '不是一个数组';
	                }
	                return result;
	            },
	            /**
	             * 生成UUID
	             * @returns UUID
	             */
	            uuid:function() {
	                function _p8(s) {
	                    var p = (Math.random().toString(16)+"000000000").substr(2,8);
	                    return s ? "-" + p.substr(0,4) + "-" + p.substr(4,4) : p ;
	                }
	                return _p8() + _p8(true) + _p8(true) + _p8();
	            },
	            /**
	             * 去除两边空格
	             */
				 removeTrim:function(attr)
				 {
					if(attr!=undefined)
					{
						return attr.replace(/(^\s*)|(\s*$)/g,'');
					}
				 },
				 /**
				  * 加提示为空判断
				  */
				 isNullOrEmpty:function(obj,tipName){
					 var fullTip=arguments[2];
					 return (obj==null||obj=='')?((function (){fullTip!=null? layer.msg(''+fullTip,{icon:0,time:1000}):layer.msg('请输入'+tipName,{icon:0,time:1000}); return true})()):false;
				 }
	 };
	 
 }