(function () {
    'use strict';

    angular.module('ui.fabric')
        .service('fabricCanvas', fabricCanvas);

    /**
     * @name fabricCanvas
     * @desc Creates a Canvas
     * @param {Object} [$log]
     * @param {Object} [$rootScope]
     * @param {Object} [fabricConfig]
     * @param {Object} [fabricWindow]
     * @return {Object} Returns the new fabricCanvas object
     *
     * @fires canvas:created
     *
     */

    /** @ngInject */
    function fabricCanvas($log, $rootScope, fabricConfig, fabricWindow, modeler) {
        var TAG = 'ui-fabric-canvas.service: ';

        var service = this;

        service.canvasId = null;
        service.canvas = null;
        service.element = null;

        service.canvasDefaults = null;

        service.init = function () {

            $log.debug(TAG + 'init()');

            service.canvasDefaults = fabricConfig.getCanvasDefaults();
            // service.canvasDefaults = angular.copy(fabricConfig.getCanvasDefaults());
        };

        var createId = function () {
            return Math.floor(Math.random() * 10000);
        };

        service.setElement = function (element) {
            service.element = element;
            // $rootScope.$broadcast('canvas:element:selected');
        };

        service.createCanvas = function (options) {

            options = options || service.canvasDefaults;

            service.canvasId = 'fabric-canvas-' + createId();
            service.element.attr('id', service.canvasId);
            service.canvas = new fabricWindow.Canvas(service.canvasId, options);
            $rootScope.$broadcast('canvas:created');
            $log.debug(TAG + 'createCanvas() - ' + service.canvas);

            if (modeler.getModelerSettings() !== null && modeler.getCanvasInitStatus()) {
                $log.debug(TAG + 'createCanvas() --> load objects from localStorage');
                service.canvas.loadFromJSON(JSON.stringify(modeler.getSidViewObjects()), function () {
                    service.canvas.renderAll();
                    $log.debug(TAG + 'get all elements');
                });
            } else {
                modeler.setCanvasInitStatus(true);
            }

            return service.canvas;
        };

        service.getCanvas = function () {
            $log.debug(TAG + 'getCanvas() - ' + service.canvas);
            return service.canvas;
        };

        service.init();

        return service;

    }

})();
