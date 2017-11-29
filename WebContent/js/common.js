function sleep(d){
  for(var t = Date.now();Date.now() - t <= d;);
}

/* 回车判断，跳转到指定焦点 */
function next_ctrl_onfocus(next_ctrl, evt) {
	var value = evt.keyCode;
	if (value == 13) {
		next_ctrl.focus();
		return false;
	}
	return true;
}

function joinURL(url, param)
{
	if(url.indexOf("?")>=0){
		url += "&" + param;
	}else{
		url += "?" + param;
	}
	return url;
}

//给url加时间戳方法    
function timeURL(url){    
	//获取时间戳    
	var timestamp=(new Date()).valueOf();
	//将时间戳附加到url上    
	url = joinURL(url, 't='+timestamp);
	return url;
}

function linkMouseOver(link)
{
  if( link.className!="linkActive" )
  {
	  link.className = "linkActive";
  }
}

function linkMouseOut(link)
{
  if( link.className!="linkNormal" )
  {
	  link.className = "linkNormal";
  }
}

function bnMouseOver(bn)
{
	$(bn).addClass("l-bar-button-over");
}

function bnMouseOut(bn)
{
	$(bn).removeClass("l-bar-button-over");
}

function ltrim(s){
	return s.replace( /^\s*/, "");
}

function rtrim(s){
	return s.replace( /\s*$/, "");
}
	
function trim(s){
	return rtrim(ltrim(s));
}
function trimall(s){
	return s.replace(/[ ]/g,""); 
}

function strFilter(s) 
{ 
	var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）&mdash;—|{}【】‘；：”“'。，、？]")
	var rs = ""; 
	for (var i = 0; i < s.length; i++) { 
		rs = rs+s.substring(i, 1).replace(pattern,''); 
	} 
	return trimall(rs); 
} 

function isNullStr(str){ 
	if(typeof(str) == "undefined") return true;
	if(str==null) return true;
	if ( trim(str) == "" ) return true; 
	if ( trim(str) =="null") return true;
	var regu = "^[ ]+$"; 
	var re = new RegExp(regu); 
	return re.test(str); 
} 

//根据url名称获取值
function getUrlValue(name){
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substring(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
}
//格式化时间
function formatDate(dateString){
	if(dateString != null && dateString != ''){
		dateString = dateString+"";
		dateString = dateString.substr(0,4)+"-"+dateString.substr(4,2)+"-"+dateString.substr(6,2)+" "+dateString.substr(8,2)+":"+dateString.substr(10,2)+":"+dateString.substr(12,2);
	}else{
		dateString = '';
	}
	return dateString;
}

//弹出层
function dialogShow(id,title,url,lock,width,height){
	$.dialog({
		id:id,
	    title:title, 
	    content: "url:"+url,
	    lock:lock,
	    width:width,
	    height:height
	});
}
//关闭层
function closeDialog(){
	var api = frameElement.api, W = api.opener; // api.opener 为载加
	api.close();
}

//日期格式化为 yyyy-MM-dd HH:mm:ss
function formatDate(datetime){
	return new Date(datetime).Format("yyyy-MM-dd hh:mm:ss");
}
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

function formatStr(str){
	if(!str) return "";
	else return str;
}
//全选
function selectAll(field) {
	$("input[name='checkdName']").attr("checked", field.checked);
}
//刷新父页面
function reloadParent(){
	window.parent.location.reload(true);
}
//弹出层(层id,标题,地址,是否锁屏,宽度,高度,是否最大框)
function dialogShow(id,title,url,lock,width,height,ismax){
	$.dialog({
		id:id,
	    title:title, 
	    content: "url:"+url,
	    lock:lock,
	    width:width,
	    height:height,
	    max:ismax
	});
}
//关闭弹出层
function closeDialog(){
	var api = frameElement.api, W = api.opener; // api.opener 为载加
	api.close();
}

//根据url名称获取值
function getUrlValue(name){
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) return unescape(r[2]); return null;
}
//唯一id
getGuidGenerator = function() {   
    var S4 = function() {   
       return (((1+Math.random())*0x10000)|0).toString(16).substring(1);   
    };   
    return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());   
};
//获取浏览器高度
function windowHeight() {
    var de = document.documentElement;
    return self.innerHeight||(de && de.clientHeight)||document.body.clientHeight;
}
//json对象转url
function parseUrlParam(param,key){
    var paramStr="";
    if(param instanceof String||param instanceof Number||param instanceof Boolean){
        paramStr+="&"+key+"="+encodeURIComponent(param);
    }else{
        $.each(param,function(i){
            var k=key==null?i:key+(param instanceof Array?"["+i+"]":"."+i);
            paramStr+="&"+parseUrlParam(this, k);
        });
    }
    return paramStr.substr(1);
};

function getBeforDay(day){
	//获取当前日期前dayNum天日期
    var now = new Date();
	var befor = new Date(now.getTime() - 1000*3600*24*day);
	var befor_month = (befor.getMonth()+1).toString();
	var befor_day = befor.getDate().toString();
	
	if(befor_month.length == 1){
		befor_month = "0" + befor_month;
	}
	if(befor_day.length == 1){
		befor_day = "0" + befor_day;
	}
	var befortime = befor.getFullYear() + befor_month + befor_day;
	return befortime;
}
//处理日期字符串，获取月、日
function getMonthDay(str){
	return str.substr(4,2)+"-"+str.substr(6,2);
}

// 判断有无html标签
function testhtml(str) {
	var a = '<';
	var b = '>';
	if (str.indexOf(a) > -1 && str.indexOf(b) > -1) {
		return true;
	} else {
		return false;
	}
}