(function() {
  angular.module('metaForm')
    .run(appRun);

  /* @ngInject */
  function appRun(routerHelper) {
    routerHelper.configureStates(getStates());

    function getStates() {
      return [
        {
          state: 'login',
          config: {
            url: '/login',
            views: {
              'main': {
                templateUrl: 'pages/login/login.html',
                controller: 'LoginController as vm'
              }
            },
            data: {
              requireLogin: false,
              title: '用户登录',
              _class: 'login'
            }
          }
        }
      ];

    }
  }
})();
