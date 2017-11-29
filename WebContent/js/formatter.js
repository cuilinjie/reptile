function decimal(value,num){
    if (isNullValue(value)) return "";
    var tmpval = Number(value.toString().replace(/\$|\,/g, ''));
    tmpval=tmpval;
    var val = Number(tmpval.toFixed(num));//先保留2位，然后转换成Number
    if (isNaN(val))
    	val = "";
    return val+" %";
}

function percent(value,num){
    if (isNullValue(value)) return "";
    var tmpval = Number(value.toString().replace(/\$|\,/g, ''));
    tmpval=tmpval*100;
    var val = Number(tmpval.toFixed(num));//先保留2位，然后转换成Number
    if (isNaN(val))
    	val = "";
    return val+" %";
}
//扩展currency类型的格式化函数
$.ligerDefaults.Grid.formatters['decimal[2]'] = function (num, column) {
    //num 当前的值
    //column 列信息
    if (isNullValue(num)) return "";
    var num1 = Number(num.toString().replace(/\$|\,/g, ''));
    num1=num1;
    var n = Number(num1.toFixed(2));//先保留2位，然后转换成Number
    if (isNaN(n))
        n = "";
    return n+" %";
};

/**               
* 时间戳转换日期               
* @param <int> unixTime    待时间戳(秒)               
* @param <bool> isFull    返回完整时间(Y-m-d 或者 Y-m-d H:i:s)               
* @param <int>  timeZone   时区               
*/ 
$.ligerDefaults.Grid.formatters['dataFormat[Y-m-d H:i:s:sss]'] = function (num, column) {
		if(isNullValue(num)) return "";
		var unixTime = parseInt(num) + 8 * 60 * 60*1000;                  
		var time = new Date(unixTime);                  
		var ymdhis = ""; 
		ymdhis += time.getUTCFullYear() + "-";                  
		ymdhis += time.getUTCMonth()+1 + "-";                  
		ymdhis += time.getUTCDate(); 
		ymdhis += " " + time.getUTCHours() + ":";                      
		ymdhis += time.getUTCMinutes() + ":";                      
		ymdhis += time.getUTCSeconds()+".";     
		ymdhis += time.getUTCMilliseconds();
	return ymdhis;              
}

/**               
* 时间戳转换日期               
* @param <int> unixTime    待时间戳(秒)               
* @param <bool> isFull    返回完整时间(Y-m-d 或者 Y-m-d H:i:s)               
* @param <int>  timeZone   时区               
*/ 
$.ligerDefaults.Grid.formatters['dateFormat[yy-MM-dd hh:mm:ss]'] = function (num, column) {
		if(isNullValue(num)) return "";
		var unixTime = parseInt(num) + 8 * 60 * 60*1000;                  
		var date = new Date(unixTime);
        var pattern = "yyyy-MM-dd hh:mm:ss";
	return formatDate(date,pattern);              
}

/**               
* 时间戳转换日期               
* @param <int> unixTime    待时间戳(秒)               
* @param <bool> isFull    返回完整时间(Y-m-d 或者 Y-m-d H:i:s)               
* @param <int>  timeZone   时区               
*/ 
$.ligerDefaults.Grid.formatters['dateFormat[yy-MM-dd hh:mm:ss]-8'] = function (num, column) {
		if(isNullValue(num)) return "";
		var unixTime = parseInt(num);                  
		var date = new Date(unixTime);
        var pattern = "yyyy-MM-dd hh:mm:ss";
	return formatDate(date,pattern);              
}

$.ligerDefaults.Grid.formatters['dateFormat[yy-MM-dd hh:mm]'] = function (num, column) {
	if(isNullValue(num)) return "";
	var unixTime = parseInt(num) + 8 * 60 * 60*1000;                  
	var date = new Date(unixTime);
    var pattern = "yyyy-MM-dd hh:mm";
return formatDate(date,pattern);              
}

//扩展currency类型的格式化函数
$.ligerDefaults.Grid.formatters['decimal[3]'] = function (num, column) {
    //num 当前的值
    //column 列信息
    if (isNullValue(num)) return "";
    var num1 = Number(num.toString().replace(/\$|\,/g, ''));
    num1=num1;
    var n = Number(num1.toFixed(3));//先保留2位，然后转换成Number
    if (isNaN(n))
        n = "";
    return n +" %";
};

//扩展currency类型的格式化函数
$.ligerDefaults.Grid.formatters['percent[2]'] = function (num, column) {
    //num 当前的值
    //column 列信息
    if (isNullValue(num)) return "";
    var num1 = Number(num.toString().replace(/\$|\,/g, ''));
    num1=num1*100;
    var n = Number(num1.toFixed(2));//先保留2位，然后转换成Number
    if (isNaN(n))
        n = "";
    return n+" %";
};

//扩展currency类型的格式化函数
$.ligerDefaults.Grid.formatters['percent[3]'] = function (num, column) {
    //num 当前的值
    //column 列信息
    if (isNullValue(num)) return "";
    var num1 = Number(num.toString().replace(/\$|\,/g, ''));
    num1=num1*100;
    var n = Number(num1.toFixed(3));//先保留2位，然后转换成Number
    if (isNaN(n))
        n = "";
    return n +" %";
};

$.ligerDefaults.Grid.formatters['netbandwidth'] = function (num, column) {
	var factor="";
	if(isNullValue(num)) return "";
	var tmp = num/1000;
	if(tmp>0&&tmp<1)
		factor=num+" Bps";
	if(tmp>=1&&tmp<1000)
		factor=tmp.toFixed(2)+" Kbps";
	if(tmp>=1000&&tmp<1000000)
		factor=(tmp/1000).toFixed(2)+" Mbps";
	if(tmp>=1000000)
		factor=(tmp/1000000).toFixed(2)+" Gbps";
	return factor;
};

$.ligerDefaults.Grid.formatters['netband'] = function (num, column) {
	var factor="";
	if(isNullValue(num)) return "";
	var tmp = num/1024;
	if(tmp>0&&tmp<1)
		factor=num+" Byte";
	if(tmp>=1&&tmp<1024)
		factor=tmp.toFixed(2)+" KB";
	if(tmp>=1024&&tmp<1024*1024)
		factor=(tmp/1024).toFixed(2)+" MB";
	if(tmp>=1024*1024)
		factor=(tmp/1024*1024).toFixed(2)+" GB";
	return factor;
};


$.ligerDefaults.Grid.formatters['timeFormat'] = function (num, column) {
	var factor=" 分钟";
	if(isNullValue(num))
		return "";
	var time = num/60+factor;
	return time;
};

function isNullValue(value){
	if(value==null||value==""||parseFloat(value)<0||value=="null")
		return true;
	return false;
}

function formatDate(date,format){
	var o = {
            "M+": date.getMonth() + 1,
            "d+": date.getDate(),
            "h+": date.getHours(),
            "m+": date.getMinutes(),
            "s+": date.getSeconds(),
            "q+": Math.floor((date.getMonth() + 3) / 3),
            "S": date.getMilliseconds()
        }
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;	
}

function strDateToDate(strDate){
	if(isNullValue(strDate))
		return new Date();
	var strYear=strDate.substring(0,4); 
	var strMonth=strDate.substring(5,7); 
	var strDay=strDate.substring(8,10); 
	var strHours=strDate.substring(11,13); 
	var strMinutes=strDate.substring(14,16); 
	var strSecond=strDate.substring(17,19); 
	return new Date(strYear,parseInt(strMonth)-1,strDay,strHours,strMinutes,strSecond); 
}

function long2StrDate(longtmp){
	var format='yyyy-MM-dd hh:mm:ss';
	var date =new Date();
	if(!isNullValue(longtmp)){
		var dateNum=Number(longtmp);
		date=new Date(dateNum);
	}
	var o = {
            "M+": date.getMonth() + 1,
            "d+": date.getDate(),
            "h+": date.getHours(),
            "m+": date.getMinutes(),
            "s+": date.getSeconds(),
            "q+": Math.floor((date.getMonth() + 3) / 3),
            "S": date.getMilliseconds()
        }
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;	
}

function long2StrDateymd(longtmp){
	var format='yyyy-MM-dd';
	var date =new Date();
	if(!isNullValue(longtmp)){
		var dateNum=Number(longtmp);
		date=new Date(dateNum);
	}
	var o = {
            "M+": date.getMonth() + 1,
            "d+": date.getDate(),
            "q+": Math.floor((date.getMonth() + 3) / 3),
            "S": date.getMilliseconds()
        }
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;	
}


function longToStrDate(longtmp,format){
	var date =new Date();
	if(!isNullValue(longtmp)){
		var dateNum=Number(longtmp);
		date=new Date(dateNum);
	}
	var o = {
            "M+": date.getMonth() + 1,
            "d+": date.getDate(),
            "h+": date.getHours(),
            "m+": date.getMinutes(),
            "s+": date.getSeconds(),
            "q+": Math.floor((date.getMonth() + 3) / 3),
            "S": date.getMilliseconds()
        }
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;	
}