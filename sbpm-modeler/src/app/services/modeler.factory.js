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
            activeObjectId: '',
            sidViewObjects: {},
            sbdViewObjects: {},
            customControls: {
                sidViewCustomControls: [],
                sbdViewCustomControls: []
            }
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

            if (modelerSettings.currentView === 'SID') {
                modelerSettings.customControls.sidViewCustomControls.push({
                    objectId: objectId,
                    customControlId: customControlId
                });
            } else {
                //TODO: SBD view
            }

            storage.set('modelerSettings', modelerSettings);
        };

        service.removeCustomControl = function (objectId) {

            $log.debug(TAG + 'removeCustomControl()');

            var modelerSettings = storage.get('modelerSettings');

            if (modelerSettings.currentView === 'SID') {
                var sidViewCustomControls = modelerSettings.customControls.sidViewCustomControls;
                modelerSettings.customControls.sidViewCustomControls = _.without(sidViewCustomControls, _.findWhere(sidViewCustomControls, {
                    objectId: objectId
                }));
            } else {
                //TODO: SBD view
            }

            storage.set('modelerSettings', modelerSettings);
        };

        service.removeCustomControls = function () {

            $log.debug(TAG + 'removeCustomControl()');

            var modelerSettings = storage.get('modelerSettings');

            if (modelerSettings.currentView === 'SID') {
                modelerSettings.customControls.sidViewCustomControls = [];
            } else {
                //TODO: SBD view
            }

            storage.set('modelerSettings', modelerSettings);
        };

        service.getCustomControlId = function (objectId) {

            $log.debug(TAG + 'getCustomControl()' + ' - ' + objectId);

            var modelerSettings = storage.get('modelerSettings');

            if (modelerSettings.currentView === 'SID') {
                var sidViewCustomControls = modelerSettings.customControls.sidViewCustomControls;

                var result = _.find(sidViewCustomControls, function (r) {
                    return r.objectId === objectId;
                });
            }

            return result.customControlId;
        };

        service.getCustomControlIds = function () {
            $log.debug(TAG + 'getCustomControlIds()');

            var modelerSettings = storage.get('modelerSettings');

            var result = [];

            if (modelerSettings.currentView === 'SID') {
                var sidViewCustomControls = modelerSettings.customControls.sidViewCustomControls;
                var result = sidViewCustomControls.map(function (r) {
                   return r.customControlId;
                });
            }

            return result;
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

        service.setActiveObjectId = function (activeObjectId) {
            $log.debug(TAG + 'setActiveObjectId()');

            var modelerSettings = storage.get('modelerSettings');
            modelerSettings.activeObjectId = activeObjectId;
            storage.set('modelerSettings', modelerSettings);
        };

        service.getActiveObjectId = function () {
            $log.debug(TAG + 'getActiveObjectId()');

            return storage.get('modelerSettings').activeObjectId;
        };

        return service;

    }

})();
