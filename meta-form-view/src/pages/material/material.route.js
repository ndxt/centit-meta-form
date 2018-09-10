(function() {
  angular.module('metaForm')
    .run(appRun);

  /* @ngInject */
  function appRun(routerHelper) {
    routerHelper.configureStates(getStates());

    function getStates() {
      return [
        {
          state: 'form.material',
          config: {
            url: '/material',
            views: {
              'main@': {
                templateUrl: 'pages/material/material.html',
                controller: 'MaterialController as vm'
              }
            },
            data: {
              title: '新页面',
              _class: 'material'
            }
          }
        },
        {
          state: 'form.material.list',
          config: {
            url: '/ll/list',
            views: {
              'main@': {
                templateUrl: 'pages/access/access.list.html',
                controller: 'AccessListController as vm'
              }
            },
            data: {
              title: '表单实例列表',
              _class: 'form-access-list',
              isColumn: false
            }
          }
        }
      ];

    }
  }
})();
