// Help configure the state-base ui.router
(function () {
  'use strict';

  angular.module('metaForm')
    .provider('routerHelper', routerHelperProvider);

  /* @ngInject */
  function routerHelperProvider ($locationProvider, $stateProvider, $urlRouterProvider) {
    /* jshint validthis:true */
    var config = {
      mainTitle: '自定义表单',
      resolveAlways: {}
    };

    $locationProvider.html5Mode(true);

    this.configure = function (cfg) {
      angular.extend(config, cfg);
    };

    this.$get = RouterHelper;
    /* @ngInject */
    function RouterHelper ($rootScope, toastr, $state, Authenticate) {
      var handlingStateChangeError = false;
      var hasOtherwise = false;
      var stateCounts = {
        errors: 0,
        changes: 0
      };

      var service = {
        configureStates: configureStates,
        getStates: getStates,
        stateCounts: stateCounts
      };

      init();

      return service;

      ///////////////

      function configureStates (states, otherwisePath) {
        states.forEach(function (state) {
          state.config.data = state.config.data || {};

          // add login check if requireLogin is true
          var data = state.config.data;
          if (data && data.requireLogin !== false) {
            state.config.resolve = angular.extend(
              state.config.resolve || {},
              {'loginResolve': Authenticate.login}
            );
          }
          state.config.resolve =
            angular.extend(state.config.resolve || {}, config.resolveAlways);
          $stateProvider.state(state.state, state.config);
        });
        if (otherwisePath && !hasOtherwise) {
          hasOtherwise = true;
          $urlRouterProvider.otherwise(otherwisePath);
        }
      }

      function handleRoutingErrors () {
        // Route cancellation:
        // On routing error, go to the dashboard.
        // Provide an exit clause if it tries to do it twice.
        $rootScope.$on('$stateChangeError',
          function (event, toState, toParams, fromState, fromParams, error) {
            if (handlingStateChangeError) {
              return;
            }
            stateCounts.errors++;
            handlingStateChangeError = true;

            // handle requireLogin issue
            if (error === 'requireLogin') {
              var data = toState.data, title = data ? data.title || '' : '';
              $state.prev = {
                state: toState.name,
                params: toParams,
                title: title
              };

              toastr.info('请先登录！');
              handlingStateChangeError = false;
              return $state.go('login');
            }

            console.error(error, toState, toParams);
            handlingStateChangeError = false;
            return;
          }
        );
      }

      function init () {
        handleRoutingErrors();
        updateDocTitle();
      }

      function getStates () {
        return $state.get();
      }

      function updateDocTitle () {
        $rootScope.$on('$stateChangeSuccess',
          function (event, toState, toParams, fromState, fromParams) {
            stateCounts.changes++;
            handlingStateChangeError = false;

            var title = (toState.data.title ? toState.data.title + ' - ' : '') + config.mainTitle;
            $rootScope.title = title; // data bind to <title>
            document.title = title;
            $rootScope._class = toState.data._class; // data bind to <body>
            $rootScope.isFluid = toState.data.isFluid === false ? false : true; // is fluid container
            $rootScope.isColumn = toState.data.isColumn === false ? false : true; // flex direction
          }
        );
      }
    }
  }
})();
