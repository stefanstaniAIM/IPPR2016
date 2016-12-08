(function() {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('modeler', modeler);

    /** @ngInject */
    function modeler($log, modelerStorage) {

        var TAG = 'sbpm-modeler.service: ';

        var service = {};

        function init() {
            $log.debug(TAG + 'initiating modelerSettings in localStorage');
            if (modelerStorage.getModelerSettings() === null) {
                $log.debug(TAG + 'initiating modelerSettings done');
                modelerStorage.initModelerSettings();
            } else {
                $log.debug(TAG + 'modelerSettings already defined');
            }
        }

        init();

        return service;

    }

})();
