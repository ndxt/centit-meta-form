(function() {
  angular.module('metaForm')
    .run(appRun);

  /* @ngInject */
  function appRun(routerHelper) {
    routerHelper.configureStates(getStates());

    function getStates() {
      return [
        {
          state: 'form.access',
          config: {
            url: '/access',
            abstract: true,
            data: {
              requireLogin: false
            }
          }
        },

        {
          state: 'form.access.list',
          config: {
            url: '/:modelCode/list',
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
        },

        {
          state: 'form.access.view',
          config: {
            url: '/:modelCode/view?primaryKey&primaryValue',
            views: {
              'main@': {
                templateUrl: 'pages/access/access.info.html',
                controller: 'AccessInfoController as vm'
              }
            },
            data: {
              title: '查看表单实例',
              _class: 'form-access-view',
              operation: 'view'
            }
          }
        },

        {
          state: 'form.access.viewlist',
          config: {
            url: '/:modelCode/viewlist?primaryKey&primaryValue',
            views: {
              'main@': {
                templateUrl: 'pages/access/access.info.html',
                controller: 'AccessInfoController as vm'
              }
            },
            data: {
              title: '查看表单实例',
              _class: 'form-access-viewlist',
              operation: 'viewlist'
            }
          }
        },

        {
          state: 'form.access.create',
          config: {
            url: '/:modelCode/create',
            views: {
              'main@': {
                templateUrl: 'pages/access/access.info.html',
                controller: 'AccessInfoController as vm'
              }
            },
            data: {
              title: '创建表单实例',
              _class: 'form-access-create',
              operation: 'create'
            }
          }
        },

        {
          state: 'form.access.edit',
          config: {
            url: '/:modelCode/edit?primaryKey&primaryValue',
            views: {
              'main@': {
                templateUrl: 'pages/access/access.info.html',
                controller: 'AccessInfoController as vm'
              }
            },
            data: {
              title: '修改表单实例',
              _class: 'form-access-edit',
              operation: 'edit'
            }
          }
        }
      ];

    }
  }
})();
