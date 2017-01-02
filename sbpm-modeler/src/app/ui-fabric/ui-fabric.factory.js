(function() {
    'use strict';

    angular.module('ui.fabric')
        .factory('fabric', fabric);

    /** @ngInject */
    function fabric($log, $rootScope, fabricCanvas, fabricConfig, fabricWindow, fabricShape) {
        var TAG = 'ui-fabric.factory: ';

        var service = this;

        service.canvas = null;

        service.canvasDefaults = null;
        service.controlDefaults = null;
        service.rectDefaults = null;
        service.connectorDefaults = null;
        service.arrowDefaults = null;

        service.connectorMode = false;
        service.activeObject = null;
        service.selectedObject = null;
        service.connectorLine = null;
        service.connectorLineFromPort = null;
        service.connectorLineFromArrow = null;
        service.isMouseDown = false;
        service.fromObject = null;

        // service.formatShape = { show: false};

        service.init = function () {

            $log.debug(TAG + 'init()');

            service.canvasDefaults = fabricConfig.getCanvasDefaults();
            service.controlDefaults = fabricConfig.getControlDefaults();
            service.rectDefaults = fabricConfig.getRectDefaults();
            service.connectorDefaults = fabricConfig.getConnectorDefaults();
            service.arrowDefaults = fabricConfig.getArrowDefaults();

            service.initCustomShapes();
        };

        service.initCustomShapes = function () {

            $log.debug(TAG + 'initCustomShapes()');

            fabricWindow.SubjectElement.fromObject = function (object) {
                return new fabricWindow.SubjectElement(object);
            };
        };

        service.setConnectorMode = function (mode) {

            $log.debug(TAG + 'setConnectorMode(): ' + mode);

            service.connectorMode = mode;
        };

        //
        // Canvas
        //

        service.getCanvas = function () {

            $log.debug(TAG + 'getCanvas()');

            service.canvas = fabricCanvas.getCanvas();
            //service.configCanvasListeners();

            $log.debug(TAG + 'getCanvas()');

            return service.canvas;
        };

        //
        // Shapes
        //

        var addObjectToCanvas = function(object, render) {

            render = render || false;

            if (service.canvas === null) {
                $log.error(TAG + 'You must call getCanvas() before you try to add shapes to a canvas.');
                service.getCanvas();
            }

            service.canvas.add(object);
            object.bringToFront();

            if (render !== false) {
                $log.debug(TAG + 'addObjectToCanvas() - renderAll');
                service.canvas.renderAll();
            }

            return object;
        };

        service.getActiveObject = function() {

            $log.debug(TAG + 'getActiveObject()');

            return service.canvas.getActiveObject();

        };

        var removeObjectFromCanvas = function(object, render) {

            service.canvas.remove(object);

            $log.debug(TAG + 'removeObjectFromCanvas() - render: ' + render);

            if (render) {
                service.canvas.renderAll();
            }
        };

        service.setActiveObject = function(object) {

            $log.debug('fabric - setActiveObject()');

            if (object) {
                service.canvas.setActiveObject(object);
                // service.canvas.renderAll();
            }
        };

        service.removeActiveObjectFromCanvas = function() {

            $log.debug(TAG + 'removeActiveObjectFromCanvas()');

            var object = service.canvas.getActiveObject();
            if (object) {
                service.canvas.remove(object);
                service.canvas.renderAll();
            }
        };

        //
        // SubjectElement
        //

        /**
         * @name addSubjectElement
         * @desc Creates a new SubjectElement and adds it to the canvas
         * @param {Object} [options] A configuration object, defaults to service.rectDefaults
         * @param {Boolean} [render] When true, service.canvas.renderAll() is invoked
         * @return {Object} Returns the new Rect object
         */
        service.addSubjectElement = function(options, render) {

            $log.debug(TAG + 'addSubjectElement()');

            return addObjectToCanvas(fabricShape.subjectElement(options), render);
        };

        //
        // Listeners
        //

        service.objectSelectedListener = function(element) {

            $log.debug(TAG + 'objectSelectedListener()');

            if (service.connectorMode) {

                if (element.target.type === 'subjectElement') {

                    $log.debug('objectSelectedListener() - element.target.type === subjectElement');

                    service.selectedObject = element.target;
                    service.activeObject = service.selectedObject;
                    /*service.selectedObject.set('selectable', true);
                    service.selectedObject.set('hasRotatingPoint', true);
                    service.selectedObject.set('hasBorders', service.rectDefaults.hasBorders);
                    service.selectedObject.set('cornerSize', service.rectDefaults.cornerSize);
                    service.selectedObject.set('transparentCorners', service.rectDefaults.transparentCorners);
                    service.selectedObject.setControlsVisibility({ 'tl': true, 'tr': true, 'br': true, 'bl': true });*/

                    service.canvas.renderAll();
                }
            }
        };

        service.selectionClearedListener = function(element) {

            $log.debug('selectionClearedListener()');

            service.activeObject = null;
        };

        service.configCanvasListeners = function() {

            $log.debug('configCanvasListeners()');

            service.canvas.on({
                'selection:cleared': service.selectionClearedListener
            });

        };

        service.init();

        return service;

    }

})();
