var dlg = null;

function showDlg(title, width, height, url)
{
	dlg = $.ligerDialog.open({ title:title, width:width, height:height, url:url, isHidden:false, showMax: true, isResize: true });
}

function closeDlg()
{
	if( dlg ){
		dlg.close();
	}
}

function saveOK(action, id)
{
	getActWin().saveOK(action, id); 
}

function doResult(data)
{
	getActWin().doResult(data); 
}

function loadData()
{
	getActWin().loadData(); 
}

function onAbout()
{
	var url = timeURL('about.do');
	window.showDlg('关于', 600, 400, url);
}

function onlineCheck() {
	$.ajax({
		url: timeURL("online.do"),
		dataType: 'json',
		success: function(data) {
			$("#onlineNum").text('在线用户数：'+data.onlineNum);			
		}
	});        			
}
