(function($){
$.fn.extend({
		datagrid : function(settings) {
			if (!settings.url || !settings.columns) {
				return;
			}
			var defaultConfig = {
				serverSide : settings.serverSide || true,// 分页，取数据等等的都放到服务端去
				processing : settings.processing || true,// 载入数据的时候是否显示“载入中”
				pageLength : settings.pageLength || 10,// 首次加载的数据条数
				ordering :  settings.ordering===undefined?true:settings.ordering,// 排序操作在服务端进行，所以可以关了。
				bPaginate: settings.bPaginate===undefined?true:settings.bPaginate,
				bFilter: settings.bFilter===undefined?true:settings.bFilter,
				ajax : {// 类似jquery的ajax参数，基本都可以用。
					type : settings.type || "post",// 后台指定了方式，默认get，外加datatable默认构造的参数很长，有可能超过get的最大长度。
					url : settings.url,
					dataSrc : "jsonData",// 默认data，也可以写其他的，格式化table的时候取里面的数据
					data : function(d) {// d 是原始的发送给服务器的数据，默认很长。
						var param = {};// 因为服务端排序，可以新建一个参数对象
						param.start = d.start;// 开始的序号
						param.length = d.length;// 要取的数据的
						return jQuery.extend({}, param, settings.data);
					},
				},
				columns : settings.columns,
				initComplete : settings.initComplete
						|| function(setting, json) {
						},
				language :{
					lengthMenu : '<select class="form-control input-xsmall">'
							+ '<option value="5">5</option>'
							+ '<option value="10">10</option>'
							+ '<option value="20">20</option>'
							+ '<option value="30">30</option>'
							+ '<option value="40">40</option>'
							+ '<option value="50">50</option>'
							+ '</select>条记录',// 左上角的分页大小显示。
					processing : "载入中",// 处理页面数据的时候的显示
					paginate : {// 分页的样式文本内容。
						previous : "上一页",
						next : "下一页",
						first : "第一页",
						last : "最后一页"
					},
					zeroRecords : "没有内容",// table tbody内容为空时，tbody的内容。
					info : "总共_PAGES_ 页，显示第_START_ 到第 _END_ ，筛选之后得到 _TOTAL_ 条，初始_MAX_ 条 ",// 左下角的信息显示，大写的词为关键字。
					infoEmpty : "",// 筛选为空时左下角的显示。
					infoFiltered : ""// 筛选之后的左下角筛选提示(另一个是分页信息显示，在上面的info中已经设置，所以可以不显示)，
				}
			};
	
			$(this).dataTable(defaultConfig);
		}
	})
})(jQuery);