(function() {
    'use strict';

    angular.module('metaForm')
        .config(RouteConfig);

    /* @ngInject */
    function RouteConfig($stateProvider, $urlRouterProvider) {
        $urlRouterProvider.otherwise("/list?modelCode=1");

        $stateProvider
            .state('form', {
                url: '',
                abstract: true
            })

            // 列表
            .state('form.list', {
                url: '/list?modelCode',
                views: {
                    'main@': {
                        templateUrl: 'pages/list.html',
                        controller: 'ListController as vm'
                    }
                }

            })

            // 详情
            .state('form.info', {
                url: '/info?id&modelCode&operation',
                views: {
                    'main@': {
                        templateUrl: 'pages/info.html',
                        controller: 'InfoController as vm'
                    }
                }

            })
    }
})();
