define(function(require) {

	return {
		/*主题名*/
		theme: 'qui',
		
		/*上下文路径*/
		ContextPath: '/product-stat/',
		
		/*首次启动加载缓冲*/
		loading: false,
		
		/*菜单数据过滤器*/
		menuLoader: function(data) {
			data = data[0][0].children;
			
			data = [{
				id: 'DASHBOARD',
				text: '我的首页',
				url: "modules/frame/demo/dome.html",
				isDashboard: true,
				collapsible: false
			}].concat(data);
			
			return data;
		},
		
		/*菜单图标*/
		menuIcons: {
			"DASHBOARD": 'icon-base icon-base-home',
			"SYSCONF": 'icon-base icon-base-gear',
			"SYS_CONFIG" : 'icon-base icon-base-gear',
			"ORGMAG": "icon-base icon-base-user",
			"MSGMAG": 'icon-base icon-base-message',
			"FLOWOPT": 'icon-base icon-base-www'
		},
		
		/*菜单大图标（一级菜单顶部显示）*/
		menuLargeIcons: null,
		
		/*修改密码确定回调函数*/
		modifyPasswordOkCallback: function(doc, dialog) {
			// 关闭
			dialog.dialog('close');
		},
		
		events: {
			/*初始化前回调*/
			onbeforeinit: function(require) {
			},
			
			/*初始化后回调*/
			onafterinit: function(require) {
			}
		},
		
		Url: {
			MenuUrl: (GLOBAL_IDENTIFY.IS_DEVELOP ? '/admin' : '/service') + "/sys/mainframe"
		}
	};
});