(function() {
  angular.module('metaForm')
    .run(appRun);

  /* @ngInject */
  function appRun(routerHelper) {
    routerHelper.configureStates(getStates());

    function getStates() {
      return [
        {
          state: 'form.model',
          config: {
            url: '/model',
            views: {
              'main@': {
                templateUrl: 'pages/model/model.list.html',
                controller: 'ModelListController as vm'
              }
            },
            data: {
              title: '表单模板列表',
              _class: 'form-model-list'
            }
          }
        }
      ];

    }
  }
})();
