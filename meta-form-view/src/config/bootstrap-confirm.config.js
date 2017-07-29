(function() {
  'use strict';

  angular.module('metaForm')
    .run(ConfigRun);

  /* @ngInject */
  function ConfigRun($confirmModalDefaults) {
    //$confirmModalDefaults.templateUrl = 'path/to/your/template';
    $confirmModalDefaults.defaultLabels.title = '请确认';
    $confirmModalDefaults.defaultLabels.ok = '确认';
    $confirmModalDefaults.defaultLabels.cancel = '取消';
  }
})();
