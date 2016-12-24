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
                    subjects: {},
                    customControls: []
                };
                storage.set('modelerSettings', modelerSettings);
            } else {
                $log.debug(TAG + 'modelerSettings already defined');
            }
        }

        init();

        service.getCurrentView = function () {
            return storage.get('modelerSettings').currentView;
        };

        service.setCurrentView = function (currentView) {
            var modelerSettings = storage.get('modelerSettings');
            modelerSettings.currentView = currentView;
            storage.set('modelerSettings', modelerSettings);
        };

        service.clear = function () {
            $log.debug(TAG + 'clear modelerSettings');
            storage.clear();
        };

        service.addCustomControl = function (subjectId, customControlId) {
            $log.debug(TAG + 'addCustomControl()');
            var modelerSettings = storage.get('modelerSettings');
            modelerSettings.customControls.push({
                subjectId: subjectId,
                customControlId: customControlId
            });
            storage.set('modelerSettings', modelerSettings);
        };

        service.getCustomControlId = function (subjectId) {
            $log.debug(TAG + 'getCustomControl()');
            var modelerSettings = storage.get('modelerSettings');
            var result = _.find(modelerSettings.customControls, function (r) {
                return r.subjectId === subjectId;
            });
            return result.customControlId;
        };

        return service;

    }

})();
