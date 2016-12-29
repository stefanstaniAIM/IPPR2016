(function () {
    'use strict';

    angular.module('ui.fabric')
        .service('fabricStorage', fabricStorage);

    /** @ngInject */
    function fabricStorage($log, modeler, fabric, fabricCustomControl) {
        const TAG = "ui-fabric-storage.service: ";

        var service = this;

        service.canvas = null;

        service.init = function () {

            $log.debug(TAG + 'init()');

            service.canvas = fabric.getCanvas();
        };

        service.saveSidView = function () {

            $log.debug(TAG + 'saveSidView()');

            var canvasJSON = service.canvas.toJSON();
            modeler.setSidViewObjects(canvasJSON);
        };

        service.clearSidView = function () {

            $log.debug(TAG + 'clearSidView()');

            service.saveSidView();
            modeler.setActiveObjectId('');
            service.canvas.clear();
            fabricCustomControl.removeCustomControls();
        };

        service.loadSidView = function () {

            $log.debug(TAG + 'loadSidView()');

            var canvasJSON = JSON.stringify(modeler.getSidViewObjects());
            service.canvas.loadFromJSON(canvasJSON, function () {
                service.canvas.renderAll();
            });
        };

        service.beforeUnloadHandler = function () {

            $log.debug(TAG + 'beforeUnloadHandler()');

            service.init();

            if (modeler.getModelerSettings() !== null && modeler.getCanvasInitStatus()) {

                $log.debug('----------------------------------------------------');
                $log.debug(TAG + 'beforeUnloadHandler() - saving objects');

                if (modeler.getCurrentView() === 'SID') {
                    service.clearSidView();
                } else {
                    //TODO: SBD view
                }
            }
        };

        service.afterUnloadHandler = function () {

            $log.debug(TAG + 'afterUnloadHandler()');

            service.init();

            if (modeler.getModelerSettings() !== null && modeler.getCanvasInitStatus()) {

                $log.debug('----------------------------------------------------');
                $log.debug(TAG + 'afterUnloadHandler() - loading objects');

                if (modeler.getCurrentView() === 'SID') {
                    service.loadSidView();
                } else {
                    //TODO: SBD view
                }
            } else {
                $log.debug('----------------------------------------------------');
                $log.debug(TAG + 'afterUnloadHandler() - setting canvasInitialized');
                modeler.setCanvasInitStatus(true);
            }
        };

        service.init();

        return service;
    }

})();
