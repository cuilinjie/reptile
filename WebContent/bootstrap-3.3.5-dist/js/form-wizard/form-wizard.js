var FormWizard = function () {

    return {
        //main function to initiate the module
        init: function () {
            if (!jQuery().bootstrapWizard) {
                return;
            }

            function format(state) {
                if (!state.id) return state.text; // optgroup
                return "<img class='flag' src='assets/img/flags/" + state.id.toLowerCase() + ".png'/>&nbsp;&nbsp;" + state.text;
            }
 
            // default form wizard
            $('#form_wizard_1').bootstrapWizard({
                'nextSelector': '.button-next',
                'previousSelector': '.button-previous',
                onTabClick: function (tab, navigation, index) {
                    //alert('on tab click disabled');
                	 return false;
                },
                onNext: function (tab, navigation, index) {
                	
                	var currentIndex = index;
                	if(!$.myformvalidateplug){
                		$.myformvalidateplug = ui.myformvalidateplug;
                	}
                	var id = $(tab[0]).find("a").attr("href");
                	var error = $.myformvalidateplug(id);
        			if(!error){
        				parent.SHOWMESSAGE("存有不合法数据，验证失败","信息提示","error");
        				$(document).scrollTop(0);
        				return false;
        			}
        			if(!(userdefinedValidate && userdefinedValidate())){
        				return false;
        			}
                    //保存
                    var result = save(currentIndex);
                    if(result){
	                    var total = navigation.find('li').length;
	                    var current = index + 1;
	                    // set wizard title
	//                    $('.step-title', $('#form_wizard_1')).text('Step ' + (index + 1) + ' of ' + total);
	                    // set done steps
	                    jQuery('li', $('#form_wizard_1')).removeClass("done");
	                    var li_list = navigation.find('li');
	                    for (var i = 0; i < index; i++) {
	                        jQuery(li_list[i]).addClass("done");
	                    }
	
	                    if (current == 1) {
	                        $('#form_wizard_1').find('.button-previous').hide();
	                    } else {
	                        $('#form_wizard_1').find('.button-previous').show();
	                    }
	
	                    if (current >= total) {
	                        $('#form_wizard_1').find('.button-next').hide();
	                        $('#form_wizard_1').find('.button-submit').show();
	//                        displayConfirm();
	                    } else {
	                        $('#form_wizard_1').find('.button-next').show();
	                        $('#form_wizard_1').find('.button-submit').hide();
	                    }
	                    App.scrollTo($('#form_wizard_1'));
                    }else{
        				return false;
                    }
                },
                onPrevious: function (tab, navigation, index) {
 
                    var total = navigation.find('li').length;
                    var current = index + 1;
                    // set wizard title
                    $('.step-title', $('#form_wizard_1')).text('Step ' + (index + 1) + ' of ' + total);
                    // set done steps
                    jQuery('li', $('#form_wizard_1')).removeClass("done");
                    var li_list = navigation.find('li');
                    for (var i = 0; i < index; i++) {
                        jQuery(li_list[i]).addClass("done");
                    }

                    if (current == 1) {
                        $('#form_wizard_1').find('.button-previous').hide();
                    } else {
                        $('#form_wizard_1').find('.button-previous').show();
                    }

                    if (current >= total) {
                        $('#form_wizard_1').find('.button-next').hide();
                        $('#form_wizard_1').find('.button-submit').show();
                    } else {
                        $('#form_wizard_1').find('.button-next').show();
                        $('#form_wizard_1').find('.button-submit').hide();
                    }

                    App.scrollTo($('#form_wizard_1'));
                },
                onTabShow: function (tab, navigation, index) {
                    var total = navigation.find('li').length;
                    var current = index + 1;
                    var $percent = (current / total) * 100;
                    $('#form_wizard_1').find('.bar').css({
                        width: $percent + '%'
                    });
                }
            });

            $('#form_wizard_1').find('.button-previous').hide();
            $('#form_wizard_1 .button-submit').click(function () {
            	finish();
            }).hide();
        }

    };

}();