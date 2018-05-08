(function() {
  angular.module('metaForm')
    .run(appRun);

  /* @ngInject */
  function appRun(routerHelper) {
    routerHelper.configureStates(getStates());

    function getStates() {
      return [
        {
          state: 'form.dashboard',
          config: {
            url: '/dashboard',
            views: {
              'main@': {
                templateUrl: 'pages/model/model.list.html',
                controller: 'ModelListController as vm'
              }
            },
            data: {
              title: '我的首页',
              _class: 'dashboard'
            }
          }
        }
      ];

    }
  }
})();
