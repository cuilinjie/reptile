var operName = '';
var operOrgId = '';
var isTreeList = false;
var isOneTree = false;
var isPageList = false;
var objName = '';
var tplName = '';
var baseUrl = '';
var mainUrl = '';
var backUrl = '';
var impGoUrl = '';
var importUrl = '';
var impMdlName = '';
var expGoUrl = '';
var exportUrl = '';
var expColNames = '';
var expFilters = '';
var filtParam = '';
var editDlgWidth = 750;
var editDlgHeight = 420;
var clearDlgWidth = 750;
var clearDlgHeight = 300;
var searchDlgWidth = 750;
var searchDlgHeight = 350;
var moveDlgWidth = 750;
var moveDlgHeight = 480;
var isViewDlgExist = false;
var isDbClickEdit = false;
var viewDlgWidth = 750;
var viewDlgHeight = 420;
var impDlgWidth = 750;
var impDlgHeight = 400;
var expDlgWidth = 750;
var expDlgHeight = 400;
var gridMain = null;
var dataMain = {};
var selectRowData = {}; 
var dlgSearch = null;

function SHOWMESSAGE(title,message,icon,callBack){
	if(callBack){
		window.parent.$.ligerDialog.confirm(title||"操作提示", message||'',callBack);
	}else{
		if(icon == "error"){
			window.parent.$.ligerDialog.error(title||'异常提醒',message||'',icon||'error');
		}else{
			window.parent.$.ligerDialog.error(title||'消息提示',message||'',icon||'info');
		}
	}
}

function f_onSelectRow(data, index, dom)
{
	selectRowData = data;
}

function f_onDblClickRow(data, rowindex, rowobj)
{
	if( isViewDlgExist ){
		viewRow(selectRowData.id);
	}
	else if( isDbClickEdit ){
		editRow(selectRowData.id);
	}
}

function f_isChecked(rowdata)
{
	if( rowdata == null || selectRowData == null ){
		return false;
	}
    if (rowdata.id == selectRowData.id){
        return true;
    } 
    return false;
}

function f_onAdd()
{
	var url = baseUrl + '/add.do';
	if( filtParam != '' ){
		url = joinURL(url, filtParam);
	}
	if( isTreeList ){
		var rows = gridMain.getSelectedRows();
		if(rows.length>0 && isOneTree) { 
	    	window.parent.$.ligerDialog.error('请选择上级'+objName+'!'); 
	    	return; 
	    }
		if(rows.length>1){ 
	    	window.parent.$.ligerDialog.error('只能选择一条上级'+objName+'记录!'); 
	    	return; 
	    }
		if( rows.length>0 ){
			url = joinURL(url, 'parentId='+rows[0].id);
		}
	}
	window.parent.parent.showDlg('添加'+objName, editDlgWidth, editDlgHeight, timeURL(url) );     
	return false;
}

function getSingleSelectId(action)
{
	var rows = gridMain.getSelectedRows();
	if(rows.length==0){ 
    	window.parent.$.ligerDialog.error('请选择一条记录！'); 
    	return ''; 
    }
	if(rows.length>1){ 
    	window.parent.$.ligerDialog.error('只能'+action+'一条记录！'); 
    	return ''; 
    }
	return rows[0].id;
}

function getAllSelectId(action)
{
	var rows = gridMain.getSelectedRows();
	if(rows.length==0){ 
    	window.parent.$.ligerDialog.error('至少选择一条记录！'); 
    	return ''; 
    }
	var id = rows[0].id;
	for( var i=1; i<rows.length; i++ ){
		id += ";"+rows[i].id;
	}
	return id;
}

function f_onEdit()
{
	var id = getSingleSelectId('修改');
	if( id != null && id != '' ){
		editRow(id);
	}else{
		window.parent.$.ligerDialog.error('不能修改该条记录！');
		return false;
	}
}

function editRow(id)
{
	if(!id||id=='null'){
		window.parent.$.ligerDialog.error('不能修改该条记录！');
		return false;
	}
	var url = timeURL(baseUrl + '/edit.do?id=' + id);
	window.parent.parent.showDlg('修改'+objName, editDlgWidth, editDlgHeight, url);     
}

function f_onDelete()
{
	var id = getAllSelectId();
	if( id != null && id != '' ){
		deleteRow(id);
	}else{
		window.parent.$.ligerDialog.error('不能删除该条记录！');
		return false;
	}
	return false;
}

function deleteRow(id)
{
	if(!id||id=='null'){
		window.parent.$.ligerDialog.error('不能删除该条记录！');
		return false;
	}
	window.parent.$.ligerDialog.confirm('是否删除所选记录？', function (yes){
		if(yes) {
			$.getJSON(timeURL(baseUrl + '/delete.do?id=' + id), doResult);
		}
	});
}

function saveOK(action, id)
{
	selectRowData.id = id;
	loadData();
}

function loadData()
{
	if( isPageList ){
		gridMain.loadData();
	}
	else{
		if( dlgSearch!=null ){
			var form = dlgSearch.dialog[0].ownerDocument.forms[0];
			resetForm(form);
		}
		var url = baseUrl + '/getData.do';
		if( filtParam != '' ){
			url = joinURL(url, filtParam);
		}
		$.getJSON( timeURL(url), function(data){
			dataMain.Rows = data;
			gridMain.loadData();
		});        					
	}
}

function f_onView()
{
	var id = getSingleSelectId('查看');
	if( id != null && id != '' ){
		viewRow(id);
	}
	return false;
}

function viewRow(id)
{
	var url = timeURL(baseUrl + '/view.do?id=' +id);
	window.parent.parent.showDlg('查看'+objName, viewDlgWidth, viewDlgHeight, url);     
}

function f_onMove()
{
	var id = getSingleSelectId('移动');
	if( id != null && id != '' ){
		moveRow(id);
	}
	return false;
}

function moveRow(id)
{
	var url = timeURL(baseUrl + '/moveGo.do?id=' + id);
	window.parent.parent.showDlg('移动'+objName, moveDlgWidth, moveDlgHeight, url);     
}

function f_onMoveUp()
{
	var id = getSingleSelectId('移动');
	if( id != null && id != '' ){
		moveUpRow(id);
	}
	return false;
}

function moveUpRow(id)
{
	if( dataMain.Rows[0].id == id){
		window.parent.$.ligerDialog.error('该条记录已经在最上方！'); 
		return;
	}
	var url = timeURL(baseUrl + '/moveUp.do?id=' + id);
	$.getJSON(url, doResult);              	
}

function f_onMoveDown()
{
	var id = getSingleSelectId('移动');
	if( id != null && id != '' ){
		moveDownRow(id);
	}
	return false;
}

function moveDownRow(id)
{
	if( dataMain.Rows[dataMain.Rows.length-1].id == id){
		window.parent.$.ligerDialog.error('该条记录已经在最下方！'); 
		return;
	}
	var url = timeURL(baseUrl + '/moveDown.do?id=' + id);
	$.getJSON(url, doResult);              	
}

function f_onClear()
{
	var url = timeURL(baseUrl + '/clearGo.do');
	window.parent.parent.showDlg('清除'+objName, clearDlgWidth, clearDlgHeight, url);     
	return false;
}

function doResult(data)
{
	if(data.result=='success'){
		loadData();
	}
	else{
		window.parent.$.ligerDialog.error(data.message);
	}
}

function f_onSearch()
{
	if( dlgSearch==null ){
		$("#formSearch input[ltype='text']").ligerTextBox({inputWidth:200}); 
		$("#formSearch input").filter(".ip2").ligerTextBox({width:553});
		/*$("#formSearch select").ligerComboBox({width:196, selectBoxWidth:196, selectBoxHeight:150, absolute:false, resize:false});*/
		$("#formSearch input[ltype='date']").ligerDateEditor({showTime:true, format:"yyyy-MM-dd hh:mm:ss", width:196, absolute:false, resize:false});
    	dlgSearch = window.parent.$.ligerDialog.open({ title:'查询'+objName, width:searchDlgWidth, height:searchDlgHeight, target:$("#searchDlg"), show:false });
	}
	dlgSearch.show();
	
	var firstTag = null;
	var children = dlgSearch.element.getElementsByTagName('*');
	for (var i=0; i<children.length; i++){
		var child = children[i];
		var classNames = child.className.split(' ');
		for (var j=0; j<classNames.length; j++){
			if (classNames[j] == 'ip1' || classNames[j] == 'ip2' ){ 
				child.focus();
	            return false;
	        }
	    }
	} 

	return false;
}

function f_searchOK(data)
{ 
	dlgSearch.hide();
	dataMain.Rows = data;
	gridMain.loadData();
}

function closeSearchDlg()
{ 
	if(dlgSearch != null){
		dlgSearch.close();
		dlgSearch = null;
	}
}

function f_onRefresh()
{
	window.location.reload();
	return false;
}

function gotoURL(url)
{
	closeSearchDlg();
	window.location.href = url;
}

function f_onBack()
{
	var url = backUrl;
	if( url == '' ){
		var n = baseUrl.lastIndexOf('/');
		if( n>=0 ){
			url = baseUrl.substr(0,n+1)+'main.do';
		}
	}
	if( url != '' ){
		gotoURL(timeURL(url));
	}
	return false;
}

function f_bnSearch()
{
	dlgSearch.hide();
   	var form = $(this).closest("form")[0];
   	var url = mainUrl;
   	if( url == '' ){
   		url = timeURL(baseUrl + '/search.do');
   	}
  	for (var i=0;i<form.length;i++){
  		if( form.elements[i].name.indexOf('filter_')==0 ){
  			url = joinURL(url, form.elements[i].name+'='+encodeURIComponent(form.elements[i].value));
  		}
    }
	gridMain.options.url = url;
	gridMain.loadData();
	return false;
}

function f_bnReset()
{
   	var form = $(this).closest("form")[0];
   	resetForm(form);
   	$(form).find("input[ltype='text']:first").focus();
	return false;
}

function resetForm(form)
{
	if( form == null ) {
		return;
	}
	
	$(form).find("[ltype!='hidden'][name^='filter_']").val('');
	
	$(form).find("input[name~='_txt_val']").val('');
	$(form).find("input[class~='l-text-field']").val('');
	$(form).find("[class~='l-selected']").removeClass('l-selected');
}

function f_onImport()
{
	var url = impGoUrl;
	if( url == '' ){
		var n = baseUrl.indexOf('/', 1);
		if( n>=0 ){
			url = baseUrl.substr(0,n+1)+'importGo.do';
		}
	}
	if( impMdlName == '' ){
		impMdlName = '导入'+objName+'模板';
	}
	url = joinURL(url, 'name='+encodeURIComponent(impMdlName) );
	url = joinURL(url, 'tplName='+encodeURIComponent(tplName) );
	if( importUrl == '' ){
		importUrl = baseUrl + '/importExcel.do';
	}
	url = joinURL(url, 'url='+encodeURIComponent(importUrl) );
	window.parent.parent.showDlg('导入'+objName, impDlgWidth, impDlgHeight, timeURL(url));   
	return false;
}

function f_onExport()
{
	if( expColNames == '' ){
		window.parent.$.ligerDialog.error('expColNames为空！');
		return false;
	}
	var url = expGoUrl;
	if( url == '' ){
		var n = baseUrl.indexOf('/', 1);
		if( n>=0 ){
			url = baseUrl.substr(0,n+1)+'exportGo.do';
		}
	}
	url = joinURL(url, 'colNames='+encodeURIComponent(expColNames) );
	var expUrl = exportUrl;
	if( exportUrl == '' ){
		expUrl = baseUrl + '/exportExcel.do';
	}
	if( dlgSearch!=null ){
		var form = dlgSearch.dialog[0].ownerDocument.forms[0];
		if( form != null ){
		  	for (var i=0;i<form.length;i++){
		  		if( form.elements[i].name.indexOf('filter_')==0 ){
		  			expUrl = joinURL(expUrl, form.elements[i].name+'='+encodeURIComponent(form.elements[i].value));
		  		}
		    }
		}
	}
	url = joinURL(url, 'url='+encodeURIComponent(expUrl) );
	window.parent.parent.showDlg('导出'+objName, expDlgWidth, expDlgHeight, timeURL(url));   
	return false;
}
