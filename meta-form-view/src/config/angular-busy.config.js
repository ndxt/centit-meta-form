(function() {
  'use strict';

  angular.module('metaForm')
    .value('cgBusyDefaults', {
      message:'',
      backdrop: false,
      //templateUrl: 'my_custom_template.html',
      //delay: 300,
      minDuration: 500,
      wrapperClass: 'centit-meta-form'
    });
})();
