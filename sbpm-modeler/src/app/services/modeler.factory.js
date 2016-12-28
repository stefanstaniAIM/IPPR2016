(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('modeler', modeler);

    /** @ngInject */
    function modeler($log, $q, storage) {

        var TAG = 'modeler.service: ';

        var service = {};

        var modelerSettings = {
            initialized: true,
            canvasInitialized: false,
            currentView: 'SID',
            activeObject: '',
            sidViewObjects: {},
            sbdViewObjects: {},
            customControls: []
        };

        function init() {

            $log.debug(TAG + 'init()');

            if (storage.get('modelerSettings') === null) {

                $log.debug(TAG + 'init() - modelerSetting --> save in localStorage');

                storage.set('modelerSettings', modelerSettings);
            } else {
                $log.debug(TAG + 'init() - modelerSetting --> already defined');
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

        service.addCustomControl = function (objectId, customControlId) {
            $log.debug(TAG + 'addCustomControl()');
            var modelerSettings = storage.get('modelerSettings');
            modelerSettings.customControls.push({
                objectId: objectId,
                customControlId: customControlId
            });
            storage.set('modelerSettings', modelerSettings);
        };

        service.removeCustomControl = function (objectId) {
            $log.debug(TAG + 'removeCustomControl()');
            var modelerSettings = storage.get('modelerSettings');
            var customControls = modelerSettings.customControls;
            $log.debug(customControls);
            modelerSettings.customControls = _.without(customControls, _.findWhere(customControls, {
                objectId: objectId
            }));
            $log.debug(modelerSettings.customControls);
            storage.set('modelerSettings', modelerSettings);
        };

        service.clearCustomControls = function () {
            $log.debug(TAG + 'clearCustomControls()');
            var modelerSettings = storage.get('modelerSettings');
            modelerSettings.customControls = [];
            storage.set('modelerSettings', modelerSettings);
        };

        service.getCustomControlId = function (objectId) {
            $log.debug(TAG + 'getCustomControl()' + ' - ' + objectId);
            var modelerSettings = storage.get('modelerSettings');
            var result = _.find(modelerSettings.customControls, function (r) {
                return r.objectId === objectId;
            });
            return result.customControlId;
        };

        service.getModelerSettings = function () {
            return storage.get('modelerSettings');
        };

        service.getInitStatus = function () {
            return storage.get('modelerSettings').initialized;
        };

        service.setCanvasInitStatus = function (value) {
            $log.debug(TAG + 'setCanvasInitStatus()');
            var modelerSettings = storage.get('modelerSettings');
            modelerSettings.canvasInitialized = value;
            storage.set('modelerSettings', modelerSettings);
        };

        service.getCanvasInitStatus = function () {
            $log.debug(TAG + 'getCanvasInitStatus()');
            return storage.get('modelerSettings').canvasInitialized;
        };

        service.setSidViewObjects = function (value) {
            $log.debug(TAG + 'setSidViewObjects()');
            var modelerSettings = storage.get('modelerSettings');
            modelerSettings.sidViewObjects = value;
            storage.set('modelerSettings', modelerSettings);
        };

        service.getSidViewObjects = function () {
            $log.debug(TAG + 'getSidViewObjects()');
            return storage.get('modelerSettings').sidViewObjects;
        };

        service.removeSidViewObjects = function () {
            $log.debug(TAG + 'removeSidViewObjects()');
            var modelerSettings = storage.get('modelerSettings');
            modelerSettings.sidViewObjects = {};
            storage.set('modelerSettings', modelerSettings);
        };

        return service;

    }

})();
