requirejs.config({
    baseUrl: window.ContextPath + 'ui',
    
    paths: {
		jquery: 'js/jquery/jquery-1.11.2.min',
		
		'socket.io': 'js/plugins/socket.io-1.3.7',
		
		underscore: 'js/plugins/underscore/underscore-min',
		
		easyUI: 'js/easyui/1.4.3/jquery.easyui.min',

		codeMirror: 'js/plugins/codemirror/codemirror',
		
		yepnope: 'js/plugins/yepnope/yepnope-1.5.4.min',
		
		fullcalendar: 'js/plugins/fullcalendar/zh-cn',
		
		moment: 'js/plugins/moment.min',

		// 用户自定义参数文件路径
		custom: '../custom',
		
		modules: '../modules',
		
		centit: 'js/centit',
		
		loaders: 'js/loaders',
		
		plugins: 'js/plugins',
		
		websocket: 'js/websocket',
		
        style: 'css',
        
        angular:"js/plugins/angularjs-1.2.6/angular"

    },
    
    shim: {
    	easyUI : {
    		deps: ['jquery', 'css!style/icon.css','angular'],

    		init: function($) {
    			$.parser.auto = false;
    		}
    	},

		codeMirror: {
			deps: [
				'js/plugins/codemirror/mode/xml/xml',
				'js/plugins/codemirror/mode/css/css',
				'js/plugins/codemirror/mode/javascript/javascript',
				'css!plugins/codemirror/codemirror.css'
			]
		},
		
		fullcalendar: {
			deps: [
			     'js/plugins/fullcalendar/fullcalendar',
			     'css!js/plugins/fullcalendar/fullcalendar.css'
			]
		},
		
		angular:{
            exports:"angular",
        }
    },
    
    map: {
		'*' : {
			'css' : 'js/css.min',
			'text' : 'js/text'
		}
	}
});

requirejs(['main']);