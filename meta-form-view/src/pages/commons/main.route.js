(function() {
  angular.module('metaForm')
    .run(appRun);

  /* @ngInject */
  function appRun(routerHelper) {
    routerHelper.configureStates(getStates(), 'metaform/dashboard');

    function getStates() {
      return [
        {
          state: 'form',
          config: {
            url: '/metaform',
            abstract: true,
            views: {
              'header': {
                templateUrl: 'pages/commons/header.html',
                controller: 'HeaderController as vm'
              }
            },
            data: {
              requireLogin: false
            }
          }
        }
      ];

    }
  }
})();
