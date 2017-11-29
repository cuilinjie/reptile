var operName = '';
var operOrgId = '';
var editResult = '';
var editMessage = '';
var editAction = '';
var editObjId = '';


function checkEditSuccess()
{
	if( editResult == 'success' ){
		window.parent.parent.saveOK(editAction, editObjId);     
		window.parent.parent.closeDlg();
	}
}

function baseInit()
{
	$("#formSave").ligerForm({inputWidth: 200});
	$("#formSave input").filter(".ip2").ligerTextBox({width: 553});
	//$("#formSave").validationEngine({promptPosition: "centerRight"});	
	$("#formSave input[ltype='text']:first").focus();
}

function checkEditError()
{
	if( editResult == 'error' ){
		$.ligerDialog.error(editMessage);
	}
}
