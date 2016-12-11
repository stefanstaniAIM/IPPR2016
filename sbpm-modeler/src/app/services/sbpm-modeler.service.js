(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('modeler', modeler);

    /** @ngInject */
    function modeler($log, $q, storage) {

        var TAG = 'sbpm-modeler.service: ';

        var service = {};

        function init() {
            $log.debug(TAG + 'initiating modelerSettings');
            if (storage.get('modelerSettings') === null) {
                $log.debug(TAG + 'initiating modelerSettings done');
                var modelerSettings = {
                    initiated: true,
                    currentView: 'SID',
                    subjects: {}
                };
                storage.set('modelerSettings', modelerSettings);
            } else {
                $log.debug(TAG + 'modelerSettings already defined');
            }
        }

        init();

        service.clear = function () {
            $log.debug(TAG + 'clear modelerSettings');

            var deferred = $q.defer();
            storage.clear();
            deferred.resolve();

            return deferred.promise;
        };

        return service;

    }

})();
