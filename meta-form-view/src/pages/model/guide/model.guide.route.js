(function() {
  angular.module('metaForm')
    .run(appRun);

  /* @ngInject */
  function appRun(routerHelper) {
    routerHelper.configureStates(getStates());

    function getStates() {
      return [
        {
          state: 'form.model.guide',
          config: {
            url: '/guide',
            views: {
              'main@': {
                templateUrl: 'pages/model/guide/model.guide.html',
                controller: 'ModelGuideController as vm'
              }
            },
            data: {
              title: '表单模块创建向导',
              _class: 'form-model-guide'
            }
          }
        }
      ];

    }
  }
})();
