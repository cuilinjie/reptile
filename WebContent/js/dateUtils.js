Date.prototype.format = function(format){
    var o = {   
        "M+"   :   this.getMonth()+1,   //month   
        "d+"   :   this.getDate(),         //day   
        "h+"   :   this.getHours(),       //hour   
        "m+"   :   this.getMinutes(),   //minute   
        "s+"   :   this.getSeconds(),   //second   
        "q+"   :   Math.floor((this.getMonth()+3)/3),     //quarter   
        "S"   :   this.getMilliseconds()   //millisecond   
    }   
	if (/(y+)/.test(format)) format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
		for(var k in o){
			if(new RegExp("(" + k + ")").test(format)){   
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ( "00" + o[k]).substr((""+ o[k]).length));
			}
		}  
	return format;   
} 

function getTimeFrame(timeFrame){
	var count=1;
	if (timeFrame == "Monthly" ){
		count = 1;
    }
    if (timeFrame == "Quarterly"){
    	count = 3;
    }
    if (timeFrame == "Halfyear"){
        count = 6;
    }
    if (timeFrame == "Oneyear"){
        count = 12;
    }
    return count;
}
function getOneYear(billingMonth){
	var currentDate = new Date();
	if(billingMonth!=null&&billingMonth!=''){
		var regEx = new RegExp("\\-","gi");
		var billingMss=billingMonth.replace(regEx,"/");
		var millM=Date.parse(billingMss);
		currentDate=new Date(millM);
	}
	var year =currentDate.getFullYear();
	var fmonth=1;
	return year+'-'+fmonth+'-15';
}
function getHalfyear(billingMonth){
	var currentDate = new Date();
	if(billingMonth!=null&&billingMonth!=''){
		var regEx = new RegExp("\\-","gi");
		var billingMss=billingMonth.replace(regEx,"/");
		var millM=Date.parse(billingMss);
		currentDate=new Date(millM);
	}
	var year =currentDate.getFullYear();
	var month = currentDate.getMonth()+1;
	if(month==null||month=='')
		return 1;
	var fmonth=parseInt(month);
	var firstH =[1,2,3,4,5,6];
	var secondH=[7,8,9,10,11,12];
	for(var i=0;i<firstH.length;i++){
		if(fmonth==firstH[i]){
			fmonth=firstH[0];
		}
		if(fmonth==secondH[i]){
			fmonth=secondH[0];
		}
	}
	return year+'-'+fmonth+'-15';
}
function getQuarterly(billingMonth){
	var currentDate = new Date();
	if(billingMonth!=null&&billingMonth!=''){
		var regEx = new RegExp("\\-","gi");
		var billingMss=billingMonth.replace(regEx,"/");
		var millM=Date.parse(billingMss);
		currentDate=new Date(millM);
	}
	var year =currentDate.getFullYear();
	var month = currentDate.getMonth()+1;
	if(month==null||month=='')
		return 1;
	var fmonth=parseInt(month);
	
	var firstq=[1,2,3];
	var secondq=[4,5,6];
	var thirdq=[7,8,9];
	var fourthq=[10,11,12];
	for(var i=0;i<firstq.length;i++){
		if(fmonth==firstq[i]){
			fmonth=firstq[0];
			break;
		}
		if(fmonth==secondq[i]){
			fmonth=secondq[0];
			break;
		}
		if(fmonth==thirdq[i]){
			fmonth=thirdq[0];
			break;
		}
		if(fmonth==fourthq[i]){
			fmonth=fourthq[0];
			break;
		}
	}
	return year+'-'+fmonth+'-15';
}

function getMonthly(billingMonth){
	var currentDate = new Date();
	if(billingMonth!=null&&billingMonth!=''){
		var regEx = new RegExp("\\-","gi");
		var billingMss=billingMonth.replace(regEx,"/");
		var millM=Date.parse(billingMss);
		currentDate=new Date(millM);
	}
	var year =currentDate.getFullYear();
	var month = currentDate.getMonth()+1;
	if(month==null||month=='')
		return 1;
	var fmonth=parseInt(month);
	
	return year+'-'+fmonth+'-15';
}

function setPeriodTime(){
	var timeFrame = document.getElementById("timeFrame").value;
	var currentDate = new Date();
	var billingMonth=currentDate.format("yyyy-MM-dd");
	 var count=1;
	 
	 if (timeFrame == "select" ){
			billingMonth=getMonthly();
			count=0;
	    }
	if(timeFrame == "Daily"){
		document.getElementById("startTime").value=currentDate.format("yyyy-MM-dd");
		document.getElementById("endTime").value=currentDate.format("yyyy-MM-dd");
		return false;
	} 
	if(timeFrame == "Weekly"){
		if(!isNullStr(billingMonth)){
			var regEx = new RegExp("\\-","gi");
			var billingMss=billingMonth.replace(regEx,"/");
			var millM=Date.parse(billingMss);
			currentDate=new Date(millM);
		}
		currentDate.setDate(currentDate.getDate() - currentDate.getDay() + 1);
		document.getElementById("startTime").value=currentDate.format("yyyy-MM-dd");   //本周一的日期
		currentDate.setDate(currentDate.getDate() + 6);
		document.getElementById("endTime").value=currentDate.format("yyyy-MM-dd"); //本周日的日期
		return false;
	}
	if (timeFrame == "Monthly" ){
		billingMonth=getMonthly(billingMonth);
		count=0;
    }
    if (timeFrame == "Quarterly"){
    	billingMonth=getQuarterly(billingMonth);
    	count=2;
    }
    if (timeFrame == "Halfyear"){
    	billingMonth=getHalfyear(billingMonth);
    	count=5;
    }
    if (timeFrame == "Oneyear"){
    	billingMonth=getOneYear(billingMonth);
    	count=11;
    }
    
    
	if(!isNullStr(billingMonth)){
		var regEx = new RegExp("\\-","gi");
		var billingMss=billingMonth.replace(regEx,"/");
		var millM=Date.parse(billingMss);
		currentDate=new Date(millM);
	}else{
		currentDate = new Date(currentDate.getTime()-parseInt("9a7ec800", 16));
	}
    var yesterDate = new Date();
    yesterDate = new Date(currentDate.getTime()+(parseInt("9a7ec800", 16)*parseInt(count)));
   
    var startingYear = currentDate.getFullYear();
    var startingMonth =currentDate.getMonth();
    var startingDate = currentDate.getDate();
    var startingHour = currentDate.getHours();
    var startingMinute = currentDate.getMinutes();
    
    var endingYear = yesterDate.getFullYear();
    var endingMonth = yesterDate.getMonth()+1;
    var endingDate = yesterDate.getDate();
    var endingHour = yesterDate.getHours();
    var endingMinute = yesterDate.getMinutes();
    
    var ssDate=new Date(startingYear, startingMonth,1);//开始日期
    var eeDate=new Date(endingYear, endingMonth,0);
    
    var  eDate = new Date(endingYear, endingMonth, endingDate,
                          endingHour, endingMinute);
	var endTimeFull = eDate.getTime();
	var startingTime = ssDate.format("yyyy-MM-dd");
    var endingTime = eeDate.format("yyyy-MM-dd");
    document.getElementById("startTime").value=startingTime;
	document.getElementById("endTime").value=endingTime;
}