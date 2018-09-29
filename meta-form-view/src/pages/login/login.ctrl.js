(function() {
  'use strict';

  angular.module('metaForm')
    .controller('LoginController', LoginController);

  /* @ngInject */
  function LoginController(LoginAPI, Authenticate, $state, toastr) {
    var vm = this;

    vm.login = login;

    init();

    /////////////

    /**
     * 初始化
     */
    function init() {

      Authenticate.logout();

      vm.params = {
        ajax: true,
        username: 'admin',
        password: '000000'
      }
    }

    /**
     * 登录
     * @param params
     */
    function login(params) {

      return LoginAPI.post(null, params)
        // 跳转
        .then(function() {
          if ($state.prev && $state.prev.state) {
            toastr.success('登录成功，返回' + $state.prev.title);
            return $state.go($state.prev.state, $state.prev.params);
          }
          toastr.success('登录成功！');
          $state.go('form.material');
        });
    }
  }
})();
