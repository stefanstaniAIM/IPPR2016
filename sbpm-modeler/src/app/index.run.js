(function() {
    'use strict';

    angular
        .module('sbpm-modeler')
        .run(runBlock);

    /** @ngInject */
    function runBlock($log) {

        $log.debug('runBlock end');
    }

})();
