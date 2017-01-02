(function () {
    'use strict';

    angular.module('ui.fabric')
        .factory('fabric', fabric);

    /** @ngInject */
    function fabric($log, fabricCanvas, fabricConfig, fabricWindow, fabricShape, fabricUtils) {
        var TAG = 'ui-fabric.factory: ';

        var service = this;

        const DEFAULT_ARROW_FILL = 'BLACK';
        const FROM_ARROW_FILL = 'ORANGE';
        const TO_ARROW_FILL = 'YELLOW';
        const MOUSE_OVER_ARROW_FILL = 'LIME';

        service.canvas = null;

        service.canvasDefaults = null;
        service.controlDefaults = null;
        service.connectorDefaults = null;
        service.arrowDefaults = null;
        service.subjectElementDefaults = null;

        service.connectorMode = false;
        service.activeObject = null;
        service.selectedObject = null;
        service.connectorLine = null;
        service.connectorLineFromPort = null;
        service.connectorLineFromArrow = null;
        service.connectionStarted = false;
        service.fromObject = null;

        // service.formatShape = { show: false};

        service.init = function () {

            $log.debug(TAG + 'init()');

            service.canvasDefaults = fabricConfig.getCanvasDefaults();
            service.controlDefaults = fabricConfig.getControlDefaults();
            service.connectorDefaults = fabricConfig.getConnectorDefaults();
            service.arrowDefaults = fabricConfig.getArrowDefaults();
            service.subjectElementDefaults = fabricConfig.getSubjectElementDefaults();

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

            if (service.canvas !== null) {
                service.configCanvasListeners();
            }
            //service.configCanvasListeners();

            $log.debug(TAG + 'getCanvas()');

            return service.canvas;
        };

        //
        // Shapes
        //

        var addObjectToCanvas = function (object, render) {

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

        service.getActiveObject = function () {

            $log.debug(TAG + 'getActiveObject()');

            return service.canvas.getActiveObject();

        };

        var removeObjectFromCanvas = function (object, render) {

            service.canvas.remove(object);

            $log.debug(TAG + 'removeObjectFromCanvas() - render: ' + render);

            if (render) {
                service.canvas.renderAll();
            }
        };

        service.setActiveObject = function (object) {

            $log.debug('fabric - setActiveObject()');

            if (object) {
                service.canvas.setActiveObject(object);
                // service.canvas.renderAll();
            }
        };

        service.removeActiveObjectFromCanvas = function () {

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
         * @param {Object} [options] A configuration object, defaults to service.subjectElementDefaults
         * @param {Boolean} [render] When true, service.canvas.renderAll() is invoked
         * @return {Object} Returns the new Rect object
         */
        service.addSubjectElement = function (options, render) {

            $log.debug(TAG + 'addSubjectElement()');

            return addObjectToCanvas(fabricShape.subjectElement(options), render);
        };

        //
        // Connector
        //

        /**
         * @name addConnector
         * @desc Creates a new Connector and adds it to the canvas
         * @param {Array} [points] Array of points, e.g., [0, 0, 0, 0]
         * @param {Object} [options] A configuration object, defaults to service.lineDefaults
         * @param {Boolean} [render] When true, service.canvas.renderAll() is invoked
         * @return {Object} Returns the new Connector object
         */
        service.addConnector = function (points, options, render) {

            $log.debug('fabric - addConnector()');

            return addObjectToCanvas(fabricShape.connector(points, options), render);
        };

        //
        // Arrow
        // See: https://en.wikipedia.org/wiki/Atan2 and
        //      http://gamedev.stackexchange.com/questions/14602/what-are-atan-and-atan2-used-for-in-games
        //

        service.createArrow = function (points, options) {

            // $log.debug('createArrow()');

            var x1 = points[0];
            var y1 = points[1];
            var x2 = points[2];
            var y2 = points[3];

            options = options || service.arrowDefaults;

            var dx = x2 - x1;
            var dy = y2 - y1;

            var angle = Math.atan2(dy, dx);
            angle *= 180 / Math.PI;
            angle += 90;

            options.left = x2;
            options.top = y2;
            options.angle = angle;

            $log.debug('createArrow() - left: ' + options.left + ' top: ' + options.top + ' angle: ' + options.angle);

            // TODO - http://fabricjs.com/fabric-intro-part-3/#subclassing

            var object = service.addTriangle(options);
            object.set('type', 'arrow');
            object.index = 0;
            object.object = {};
            object.otherObject = {};
            object.isFromArrow = true;
            object.port = 'mt';
            object.line = {};

            return object;
        };

        //
        // Triangle
        //

        /**
         * @name addTriangle
         * @desc Creates a new Triangle and adds it to the canvas
         * @param {Object} [options] A configuration object, defaults to service.triangleDefaults
         * @param {Boolean} [render] When true, service.canvas.renderAll() is invoked
         * @return {Object} Returns the new Triangle object
         */
        service.addTriangle = function (options, render) {

            $log.debug('fabric - addTriangle()');

            return addObjectToCanvas(fabricShape.triangle(options), render);
        };

        //
        // Listeners
        //

        service.moveFromLineArrows = function (object, portCenter, index) {

            $log.debug('moveFromLineArrows() - index: ' + index);

            var arrowOptions = service.arrowDefaults;

            $log.debug('moveFromLineArrows() - removeObjectFromCanvas: ' + object.connectors.fromArrow[index].text);
            $log.debug('moveFromLineArrows() - removeObjectFromCanvas: ' + object.connectors.toArrow[index].text);

            removeObjectFromCanvas(object.connectors.fromArrow[index], false);
            removeObjectFromCanvas(object.connectors.toArrow[index], false);

            var x1 = portCenter.x1;
            var y1 = portCenter.y1;
            var x2 = object.connectors.fromLine[index].x2;
            var y2 = object.connectors.fromLine[index].y2;

            // object(fromPort) <-- toArrow -- connector(fromLine or toLine) -- fromArrow --> (toPort)otherObject

            var otherObject = object.connectors.otherObject[index];

            $log.debug('moveFromLineArrows() - fromArrow ->');

            arrowOptions.fill = FROM_ARROW_FILL;

            var fromArrow = service.createArrow([x1, y1, x2, y2], arrowOptions);
            fromArrow.index = index;
            fromArrow.object = object;
            fromArrow.otherObject = otherObject;
            fromArrow.isFromArrow = true;
            fromArrow.port = fromArrow.object.connectors.toPort[index];
            fromArrow.line = fromArrow.object.connectors.fromLine[index];
            fromArrow.text = object.text + ' ' + fromArrow.port + ': ->';

            arrowOptions.fill = TO_ARROW_FILL;

            $log.debug('moveFromLineArrows() - toArrow <-');

            var toArrow = service.createArrow([x2, y2, x1, y1], arrowOptions);
            toArrow.index = fromArrow.index;
            toArrow.object = fromArrow.object;
            toArrow.otherObject = fromArrow.otherObject;
            toArrow.isFromArrow = false;
            toArrow.port = fromArrow.object.connectors.fromPort[index];
            toArrow.line = fromArrow.line;
            toArrow.text = object.text + ' ' + toArrow.port + ': <-';

            object.connectors.fromArrow[index] = fromArrow;
            object.connectors.toArrow[index] = toArrow;

            otherObject.connectors.fromArrow[index] = fromArrow;
            otherObject.connectors.toArrow[index] = toArrow;
        };

        service.moveToLineArrows = function (object, portCenter, index) {

            $log.debug('moveToLineArrows() - index: ' + index);

            var arrowOptions = service.arrowDefaults;

            $log.debug('moveToLineArrows() - removeObjectFromCanvas: ' + object.connectors.fromArrow[index].text);
            $log.debug('moveToLineArrows() - removeObjectFromCanvas: ' + object.connectors.toArrow[index].text);

            removeObjectFromCanvas(object.connectors.fromArrow[index], false);
            removeObjectFromCanvas(object.connectors.toArrow[index], false);

            // /*

            var x1 = portCenter.x2;
            var y1 = portCenter.y2;
            var x2 = object.connectors.toLine[index].x1;
            var y2 = object.connectors.toLine[index].y1;

            // object(fromPort) <-- toArrow -- connector(fromLine or toLine) -- fromArrow --> (toPort)otherObject

            $log.debug('moveToLineArrows() - fromArrow ->');

            var otherObject = object.connectors.otherObject[index];

            //
            // object and otherObject are reversed for the toLine
            //

            arrowOptions.fill = FROM_ARROW_FILL;

            var fromArrow = service.createArrow([x2, y2, x1, y1], arrowOptions);
            fromArrow.index = index;
            fromArrow.object = otherObject;
            fromArrow.otherObject = object;
            fromArrow.isFromArrow = true;
            fromArrow.port = object.connectors.toPort[index];
            fromArrow.line = object.connectors.toLine[index];
            fromArrow.text = otherObject.text + ' ' + fromArrow.port + ': ->';

            $log.debug('moveToLineArrows() - toArrow <-');

            arrowOptions.fill = TO_ARROW_FILL;

            var toArrow = service.createArrow([x1, y1, x2, y2], arrowOptions);
            toArrow.index = fromArrow.index;
            toArrow.object = fromArrow.object;
            toArrow.otherObject = fromArrow.otherObject;
            toArrow.isFromArrow = false;
            toArrow.port = object.connectors.fromPort[index];
            toArrow.line = fromArrow.line;
            toArrow.text = otherObject.text + ' ' + toArrow.port + ': <-';

            object.connectors.fromArrow[index] = fromArrow;
            object.connectors.toArrow[index] = toArrow;

            otherObject.connectors.fromArrow[index] = fromArrow;
            otherObject.connectors.toArrow[index] = toArrow;

            // */
        };

        // Object

        service.objectMovingListener = function (options) {

            $log.debug('objectMovingListener()');

            var object = options.target;

            //
            // If the Object being moved has Connectors, we need to update their position.
            //
            if (object.connectors) {

                $log.info(object.text + ' is moving!');

                var portCenter = null;
                var i = null;

                // object(fromPort) <-- toArrow -- connector(fromLine) -- fromArrow --> (toPort)otherObject

                if (object.connectors.fromLine.length) {  // a line drawn 'from' this object

                    $log.debug('objectMovingListener() - object.connectors.fromLine.length: ' + object.connectors.fromLine.length);

                    i = 0;
                    object.connectors.fromLine.forEach(function (line) {

                        // the center of the 'fromPort' is the line's starting point (x1, y1)
                        // the center of the 'toPort' is the line's end point (x2, y2)
                        portCenter = fabricUtils.getPortCenterPoint(object, object.connectors.fromPort[i]);
                        line.set({'x1': portCenter.x1, 'y1': portCenter.y1});

                        service.moveFromLineArrows(object, portCenter, i);
                        i++;
                    });
                }

                // object(fromPort) <-- toArrow -- connector(toLine) -- fromArrow --> (toPort)otherObject

                if (object.connectors.toLine.length) {  // a line drawn 'to' this object

                    $log.debug('objectMovingListener() - object.connectors.toLine.length: ' + object.connectors.toLine.length);

                    i = 0;
                    object.connectors.toLine.forEach(function (line) {

                        // the center of the 'fromPort' is the line's starting point (x1, y1)
                        // the center of the 'toPort' is the line's end point (x2, y2)
                        portCenter = fabricUtils.getPortCenterPoint(object, object.connectors.toPort[i]);
                        line.set({'x2': portCenter.x2, 'y2': portCenter.y2});

                        service.moveToLineArrows(object, portCenter, i);
                        i++;
                    });
                }

                service.canvas.renderAll();
            }
        };

        service.mouseDownListener = function (options) {

            $log.debug(TAG + 'mouseDownListener()');

            var portCenter = null;

            //
            // Connector Mode
            //

            if (service.connectorMode) {

                if (service.connectionStarted !== true) {
                    $log.debug('mouseDownListener() - service.connectionStarted !== true');
                    return;
                }

                // No valid target
                if (options.target === null) {
                    service.selectedObject = null;
                }

                service.connectionStarted = false;

                //
                // If we're over (mouse:over) a Shape that supports connections.
                //
                if (service.selectedObject) {

                    $log.debug('--------------------------------');

                    // We're over a connection port, and the user has finished (mouse:up) drawing the connector (line).
                    // object(fromPort) <-- toArrow -- connector -- fromArrow --> (toPort)otherObject
                    // one arrow faces towards the object <--, the other faces away from --> the object

                    var toPort = 'ml';
                    var arrowOptions = service.arrowDefaults;

                    $log.debug('mouseDownListener() - toPort: ' + toPort);

                    portCenter = fabricUtils.getPortCenterPoint(service.selectedObject, toPort);
                    service.connectorLine.set({'x2': portCenter.x2, 'y2': portCenter.y2});

                    //
                    // Create the 'from ->' arrow
                    //

                    $log.debug('mouseDownListener() - create fromArrow ->');

                    arrowOptions.fill = FROM_ARROW_FILL;
                    var fromArrow = service.createArrow([service.connectorLine.x1, service.connectorLine.y1,
                        portCenter.x2, portCenter.y2], arrowOptions);
                    fromArrow.object = service.fromObject;
                    fromArrow.otherObject = service.selectedObject;
                    fromArrow.isFromArrow = true;
                    fromArrow.port = toPort;
                    fromArrow.line = service.connectorLine;
                    fromArrow.text = service.fromObject.text + ' ' + fromArrow.port + ': ->';

                    //
                    // Create the 'to <-' arrow
                    //

                    $log.debug('mouseDownListener() - create toArrow <-');

                    arrowOptions.fill = TO_ARROW_FILL;
                    var toArrow = service.createArrow([portCenter.x2, portCenter.y2,
                        service.connectorLine.x1, service.connectorLine.y1], arrowOptions);
                    toArrow.object = fromArrow.object;
                    toArrow.otherObject = fromArrow.otherObject;
                    toArrow.isFromArrow = false;
                    toArrow.port = service.connectorLineFromPort;
                    toArrow.line = fromArrow.line;
                    toArrow.text = service.fromObject.text + ' ' + toArrow.port + ': <-';

                    //
                    // Arrays in JavaScript are zero-based.
                    //

                    var index = service.fromObject.connectors.fromPort.length;

                    if (index !== 0) {
                        index = index - 1;
                    }

                    $log.debug('mouseDownListener() - index: ' + index);

                    fromArrow.index = index;
                    toArrow.index = index;

                    //
                    // The 'from' and 'to' objects need to know about each other so that if one of them
                    // is moved it can update the connector's line co-ordinates and embellishments (arrows).
                    //

                    // object(fromPort) <-- toArrow -- connector(fromLine) -- fromArrow --> (toPort)otherObject

                    service.fromObject.connectors.fromPort.push(service.connectorLineFromPort);
                    service.fromObject.connectors.toArrow.push(toArrow);
                    service.fromObject.connectors.fromLine.push(service.connectorLine);
                    service.fromObject.connectors.fromArrow.push(fromArrow);
                    service.fromObject.connectors.toPort.push(toPort);
                    service.fromObject.connectors.otherObject.push(service.selectedObject);

                    // object(fromPort) <-- toArrow -- connector(toLine) -- fromArrow --> (toPort)otherObject

                    service.selectedObject.connectors.fromPort.push(service.connectorLineFromPort);
                    service.selectedObject.connectors.toArrow.push(toArrow);
                    service.selectedObject.connectors.toLine.push(service.connectorLine);
                    service.selectedObject.connectors.fromArrow.push(fromArrow);
                    service.selectedObject.connectors.toPort.push(toPort);
                    service.selectedObject.connectors.otherObject.push(service.fromObject);

                    $log.info(service.fromObject.name + ' and ' + service.selectedObject.name + ' are connected!');

                    // $log.debug('service.fromObject.connectors: ' + JSON.stringify(['e', service.fromObject.connectors], null, '\t'));
                    // $log.debug('service.selectedObject.connectors: ' + JSON.stringify(['e', service.selectedObject.connectors], null, '\t'));

                    arrowOptions.fill = DEFAULT_ARROW_FILL;

                    if (service.connectorLineFromArrow) {
                        $log.debug('mouseDownListener - removeObjectFromCanvas(service.connectorLineFromArrow)');
                        removeObjectFromCanvas(service.connectorLineFromArrow, false);
                    }

                    service.connectorLineFromArrow = null;
                    service.connectorLineFromPort = null;
                    service.connectorLine = null;

                } else {  // if (service.selectedObject)

                    if (service.connectorLine) {
                        $log.debug('mouseDownListener() - removeObjectFromCanvas(service.connectorLine)');
                        removeObjectFromCanvas(service.connectorLine, false);
                        if (service.connectorLineFromArrow) {
                            $log.debug('mouseDownListener() - removeObjectFromCanvas(service.connectorLineFromArrow)');
                            removeObjectFromCanvas(service.connectorLineFromArrow, false);
                        }
                    }

                    service.connectorLineFromArrow = null;
                    service.connectorLineFromPort = null;
                    service.connectorLine = null;
                }

                service.canvas.renderAll();
                return;
            }

        };

        // Mouse

        service.mouseMoveListener = function (options) {

            //$log.debug('mouseMoveListener()');

            if (!service.connectionStarted) return;

            if (service.connectorMode) {

                var pointer = service.canvas.getPointer(options.e);

                service.connectorLine.set({'x2': pointer.x, 'y2': pointer.y});
                service.canvas.renderAll();
            }
        };

        service.objectSelectedListener = function (object) {

            $log.debug(TAG + 'objectSelectedListener()');

            if (service.connectorMode) {

                if (object.type === 'subjectElement') {

                    $log.debug('objectSelectedListener() - element.target.type === subjectElement');

                    service.selectedObject = object;
                    service.activeObject = service.selectedObject;

                    service.canvas.renderAll();
                }
            }
        };

        service.selectionClearedListener = function () {

            $log.debug('selectionClearedListener()');

            service.activeObject = null;
        };

        service.configCanvasListeners = function () {

            $log.debug('configCanvasListeners()');

            service.canvas.on({
                'object:moving': service.objectMovingListener,
                'selection:cleared': service.selectionClearedListener,
                'mouse:move': service.mouseMoveListener,
                'mouse:down': service.mouseDownListener
            });

        };

        // Connections

        service.drawConnection = function () {

            $log.debug('drawConnection()');

            //
            // Connector Mode
            //

            if (service.connectorMode) {

                if (service.selectedObject) {

                    service.fromObject = service.selectedObject;

                    var points = null;

                    //
                    // Fabric "remembers" the last port you we're successfully over.
                    // Which means you will only receive a __corner === 'undefined' once per object (node).
                    //

                    if (service.fromObject.__corner === undefined) {
                        $log.debug('mouseDownListener() - service.fromObject.__corner === undefined');
                        return;
                    }

                    service.connectionStarted = true;

                    //points = fabricUtils.findTargetPort(service.fromObject);
                    points = [
                        service.fromObject.left + service.fromObject.width, service.fromObject.top + (service.fromObject.height / 2),
                        service.fromObject.left + service.fromObject.width, service.fromObject.top + (service.fromObject.height / 2)
                    ];
                    service.connectorLineFromPort = 'mr';

                    $log.debug('mouseDownListener() - points: ' + JSON.stringify(['e', points], null, '\t'));

                    var connectorOptions = service.connectorDefaults;

                    // service.connectorLine = service.addLine(points, connectorOptions);
                    service.connectorLine = service.addConnector(points, connectorOptions);
                }

                return;
            }

        };

        service.init();

        return service;

    }

})();
