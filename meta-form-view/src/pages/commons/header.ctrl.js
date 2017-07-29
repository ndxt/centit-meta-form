(function() {
  'use strict';

  angular.module('metaForm')
    .controller('HeaderController', HeaderController);

  /* @ngInject */
  function HeaderController(MainFrameAPI) {
    var vm = this;

    init();

    //////////////////

    /**
     * 初始化
     */
    function init() {

      // 拉取菜单
      MainFrameAPI.one('submenu')
        .customGET(null, {
          optid: 'OPTNMANU'
        })
        .then(function(data) {
          vm.menus = data;
        });
    }
  }
})();
