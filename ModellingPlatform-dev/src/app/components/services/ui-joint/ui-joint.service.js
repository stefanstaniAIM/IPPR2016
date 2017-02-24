(function () {
    'use strict';

    angular.module('uiJoint.jointService', [])
        .factory('joint', joint);

    /** @ngInject */
    function joint($log, $rootScope, $window, jointWindow, jointGraph, jointPaper, jointShape, jointConfig, modelerStorage) {
        var service = this;

        service.graph = null;
        service.paper = null;

        service.activeObject = {};
        service.drawConnection = false;
        service.connectorId = null;

        service.messageConnectorElementDefaults = {};
        service.fromSendStateConnectorElementDefaults = {};
        service.fromReceiveStateConnectorElementDefaults = {};
        service.fromFunctionStateConnectorElementDefaults = {};

        service.graphScale = 1;

        service.init = function () {
            $log.info('jointService - init()');

            service.graph = jointGraph.getGraph();
            service.paper = jointPaper.getPaper();

            service.messageConnectorElementDefaults = jointConfig.getMessageConnectorElementDefaults();
            service.fromSendStateConnectorElementDefaults = jointConfig.getFromSendStateConnectorElementDefaults();
            service.fromReceiveStateConnectorElementDefaults = jointConfig.getFromReceiveStateConnectorElementDefaults();
            service.fromFunctionStateConnectorElementDefaults = jointConfig.getFromFunctionStateConnectorElementDefaults();

            service.configJointListeners();
        };

        service.getPaper = function () {
            if (service.paper === null) {
                $log.error('jointService - paper is null');
            }
            return service.paper;
        };

        service.getGraph = function () {
            if (service.graph === null) {
                $log.error('jointService - graph is null');
            }
            return service.graph;
        };

        //Loading graph elements depending on selectedView
        service.handleViewChange = function () {
            service.graph.clear({saveView: false});

            if (modelerStorage.getSelectedView() === 'SID') {

                //Load sidView
                loadSidView();
            } else {

                //Load sbdView for selected subject
                loadSelectedSubject();
            }

            //After load no elements should be selected
            unhighlightAll();
        };

        service.handleSubjectUpdate = function (subject) {
            //Visual update
            service.paper.findViewByModel(service.graph.getCell(subject.id)).updateBox();

            //Save sidView
            saveSidView();

            //Update selectedSubject if necessary
            if (modelerStorage.getSelectedSubject().id === subject.id) {
                modelerStorage.setSelectedSubject(subject);
                $rootScope.$broadcast('selectedSubject:changed');
            }
        };

        service.handleMessageConnectorUpdate = function (messageConnector) {
            //Visual update
            service.paper.findViewByModel(service.graph.getCell(messageConnector.id)).updateConnector();

            //Save sidView
            saveSidView();
        };

        service.handleStateUpdate = function (state) {
            //Visual update
            service.paper.findViewByModel(service.graph.getCell(state.id)).updateBox();

            //Save sidView
            saveSelectedSubject();
        };

        service.handleStateTransitionUpdate = function (stateTransition) {
            //Visual update
            service.paper.findViewByModel(service.graph.getCell(stateTransition.id)).updateConnector();

            //Save sidView
            saveSelectedSubject();
        };

        service.handleSelectedSubjectChange = function (subjectId) {
            //Save selected subject
            saveSelectedSubject();

            //Clear graph
            service.graph.clear({saveView: false});

            //Load target subject sbdView
            if (modelerStorage.subjectContainsSbdJson(subjectId)) {
                //$log.debug('Load target subject');
                service.graph.fromJSON(modelerStorage.getSbdJsonBySubjectId(subjectId));
                service.graph.getElements().forEach(function (el) {
                    configConnectors(el);
                });
            }

            //Set selected subject
            var newSelectedSubjectSbdJson = modelerStorage.getSidJson().cells.find(function (cell) {
                return cell.id === subjectId;
            });
            modelerStorage.setSelectedSubject(newSelectedSubjectSbdJson);

            unhighlightAll();

            $rootScope.$broadcast('selectedSubject:changed');
        };

        var saveSelectedSubject = function () {
            //$log.debug('Save selected subject');
            var jsonGraph = service.graph.toJSON();
            modelerStorage.addToSbdJsonArray({
                id: modelerStorage.getSelectedSubject().id,
                sbdJson: jsonGraph
            });
        };

        var loadSelectedSubject = function () {
            //$log.debug('Load selected subject');
            if (modelerStorage.subjectContainsSbdJson(modelerStorage.getSelectedSubject().id)) {
                service.graph.fromJSON(modelerStorage.getSbdJsonBySubjectId(modelerStorage.getSelectedSubject().id));
            }

            service.graph.getElements().forEach(function (el) {
                configConnectors(el);
            });
        };

        var saveSidView = function () {
            var jsonGraph = service.graph.toJSON();
            modelerStorage.setSidJson(jsonGraph);
        };

        var loadSidView = function () {
            if (!_.isEmpty(modelerStorage.getSidJson())) {
                service.graph.fromJSON(modelerStorage.getSidJson());
            }

            service.graph.getElements().forEach(function (el) {
                configConnectors(el);
            });
        };

        var handleOnChange = function () {
            if (modelerStorage.getSelectedView() === 'SID') {
                saveSidView();
            } else {
                saveSelectedSubject();
            }
        };

        //
        //Shapes
        //

        var addObjectToGraph = function (object) {

            if (service.graph === null) {
                $log.error('jointService - graph is null');
            }

            service.graph.addCell(object);
            object.toFront();

            //Configure connectors
            configConnectors(object);

            return object;
        };

        var configConnectors = function (object) {
            if (object.get('type') === 'sid.SubjectElement') {

                service.paper.findViewByModel(object).$box.find('.sb-connector-btn').mousedown(function (evt) {
                    service.messageConnectorElementDefaults.source = {id: service.activeObject.id};
                    service.messageConnectorElementDefaults.target = {x: evt.originalEvent.x, y: evt.originalEvent.y};
                    //service.messageConnectorElementDefaults.customAttrs.sourceName = service.activeObject.customAttrs.name;
                    service.addMessageConnectorElement(service.messageConnectorElementDefaults);
                });
            } else if (object.get('type') === 'sid.MessageConnectorElement') {
                service.connectorId = object.id;
                service.drawConnection = true;

            } else if (object.get('type') === 'sbd.SendStateElement') {

                service.paper.findViewByModel(object).$box.find('.ss-connector-btn').mousedown(function (evt) {
                    service.fromSendStateConnectorElementDefaults.source = {id: service.activeObject.id};
                    service.fromSendStateConnectorElementDefaults.target = {
                        x: evt.originalEvent.x,
                        y: evt.originalEvent.y
                    };
                    service.addFromSendStateConnectorElement(service.fromSendStateConnectorElementDefaults);
                });
            } else if (object.get('type') === 'sbd.FromSendStateConnectorElement') {
                service.connectorId = object.id;
                service.drawConnection = true;
            } else if (object.get('type') === 'sbd.ReceiveStateElement') {

                service.paper.findViewByModel(object).$box.find('.rs-connector-btn').mousedown(function (evt) {
                    service.fromReceiveStateConnectorElementDefaults.source = {id: service.activeObject.id};
                    service.fromReceiveStateConnectorElementDefaults.target = {
                        x: evt.originalEvent.x,
                        y: evt.originalEvent.y
                    };
                    service.addFromReceiveStateConnectorElement(service.fromReceiveStateConnectorElementDefaults);
                });
            } else if (object.get('type') === 'sbd.FromReceiveStateConnectorElement') {
                service.connectorId = object.id;
                service.drawConnection = true;
            } else if (object.get('type') === 'sbd.FunctionStateElement') {

                service.paper.findViewByModel(object).$box.find('.fs-connector-btn').mousedown(function (evt) {
                    service.fromFunctionStateConnectorElementDefaults.customAttrs.transitionName = service.activeObject.get('customAttrs').name + " done";

                    service.fromFunctionStateConnectorElementDefaults.source = {id: service.activeObject.id};
                    service.fromFunctionStateConnectorElementDefaults.target = {
                        x: evt.originalEvent.x,
                        y: evt.originalEvent.y
                    };
                    service.addFromFunctionStateConnectorElement(service.fromFunctionStateConnectorElementDefaults);
                });
            } else if (object.get('type') === 'sbd.FromFunctionStateConnectorElement') {
                service.connectorId = object.id;
                service.drawConnection = true;
            }
        };

        //SubjectElement
        service.addSubjectElement = function (options) {
            return addObjectToGraph(jointShape.subjectElement(options));
        };

        //MessageConnectorElement
        service.addMessageConnectorElement = function (options) {
            return addObjectToGraph(jointShape.messageConnectorElement(options));
        };

        //SendStateElement
        service.addSendStateElement = function (options) {
            return addObjectToGraph(jointShape.sendStateElement(options));
        };

        //ReceiveStateElement
        service.addReceiveStateElement = function (options) {
            return addObjectToGraph(jointShape.receiveStateElement(options));
        };

        //FunctionStateElement
        service.addFunctionStateElement = function (options) {
            return addObjectToGraph(jointShape.functionStateElement(options));
        };

        //FromSendStateConnectorElement
        service.addFromSendStateConnectorElement = function (options) {
            return addObjectToGraph(jointShape.fromSendStateConnectorElement(options));
        };

        //FromSendStateConnectorElement
        service.addFromReceiveStateConnectorElement = function (options) {
            return addObjectToGraph(jointShape.fromReceiveStateConnectorElement(options));
        };

        //FromFunctionStateConnectorElement
        service.addFromFunctionStateConnectorElement = function (options) {
            return addObjectToGraph(jointShape.fromFunctionStateConnectorElement(options));
        };

        //
        //Events
        //

        service.configJointListeners = function () {
            service.paper.on({
                'cell:pointerdown': service.cellPointerDownListener,
                'cell:pointerup': service.cellPointerUpListener,
                'blank:pointerdown': service.blankPointerDownListener
                //'blank:mousewheel': service.blankMousewheelListener
            });

            service.graph.on({
                'change': service.changeListener,
                'add': service.addListner,
                'remove': service.removeListener
            });

            $('.modeler-board').on({
                'mousemove': service.mousemoveListener,
                'mouseup': service.mouseupListener
            });
        };

        service.cellPointerDownListener = function (cellView, evt, x, y) {
            highlightElement(cellView);
            service.activeObject = cellView.model;
        };

        service.cellPointerUpListener = function (cellView, evt, x, y) {
            if (cellView.model.get('type') === 'sid.MessageConnectorElement') {
                if (cellView.model.getTargetElement() === null) {
                    modelerStorage.removeFromSidJsonById(cellView.model.id);
                    $rootScope.$broadcast('link:removed');
                    cellView.removeBox();
                    cellView.remove();
                } else {
                    saveSidView();
                }
            } else if (cellView.model.get('type') === 'sbd.FromSendStateConnectorElement' ||
                cellView.model.get('type') === 'sbd.FromReceiveStateConnectorElement' ||
                cellView.model.get('type') === 'sbd.FromFunctionStateConnectorElement') {
                if (cellView.model.getTargetElement() === null) {
                    modelerStorage.removeFromSbdJsonArrayBySubjectId(modelerStorage.getSelectedSubject().id, cellView.model.id);
                    $rootScope.$broadcast('link:removed');
                    cellView.removeBox();
                    cellView.remove();
                } else {
                    saveSelectedSubject();
                }
            }
        };

        service.blankPointerDownListener = function (evt, x, y) {
            unhighlightAll();
        };

        var highlightElement = function (cellView) {
            unhighlightAll();
            jointShape.highlightElement(cellView);
        };

        var unhighlightAll = function () {
            jointShape.unhighlightAll(service.graph.getElements().map(function (el) {
                return service.paper.findViewByModel(el);
            }));
            jointShape.unhighlightAll(service.graph.getLinks().map(function (el) {
                return service.paper.findViewByModel(el);
            }));
        };

        //ZoomIn/Out
        service.blankMousewheelListener = function (evt, x, y, delta) {

            var paperScale = function(sx, sy) {
                service.paper.scale(sx, sy);
            };

            var zoomOut = function() {
                service.graphScale -= 0.1;
                paperScale(service.graphScale, service.graphScale);
            };

            var zoomIn = function() {
                service.graphScale += 0.1;
                paperScale(service.graphScale, service.graphScale);
            };

            if (delta === 1) {
                zoomIn();
            } else {
                zoomOut();
            }
        };

        service.changeListener = function () {
            service.graph.getLinks().forEach(function (link) {
                service.paper.findViewByModel(link).updateConnector();
            });

            handleOnChange();
        };

        service.addListner = function (cell) {
            //$log.debug('Cell added');

            if (modelerStorage.getSelectedView() === 'SID') {

                //$log.debug('Cell added - SID');

                saveSidView();

                if (_.isEmpty(modelerStorage.getSelectedSubject())) {
                    //$log.debug('Set selected subject');
                    modelerStorage.setSelectedSubject(cell.attributes);
                    $rootScope.$broadcast('selectedSubject:changed');
                }
            } else {

                //$log.debug('Cell added - SBD');

                saveSelectedSubject();
            }

            //Set active object
            service.activeObject = cell;

            highlightElement(service.paper.findViewByModel(cell));
        };

        service.removeListener = function (cell, graph, args) {
            //$log.debug('Cell removed');

            if (typeof args.saveView !== 'undefined' && !args.saveView) return;

            if (modelerStorage.getSelectedView() === 'SID') {

                //$log.debug('Cell removed - SID');

                saveSidView();

                if (modelerStorage.getSelectedSubject().id === cell.get('id')) {
                    modelerStorage.setSelectedSubject(modelerStorage.getSidJson().cells[0]);
                }

                if (_.isEmpty(modelerStorage.getSidJson().cells)) {
                    //$log.debug('Set selected subject to empty');
                    modelerStorage.setSelectedSubject({});
                    $rootScope.$broadcast('selectedSubject:changed');
                }

                //Remove subject behaviour
                modelerStorage.removeSbdJsonBySubjctId(cell.get('id'));
            } else {

                //$log.debug('Cell removed - SBD');

                saveSelectedSubject();
            }
        };

        service.mousemoveListener = function (evt) {

            if (service.drawConnection) {
                var linkCell = service.graph.getLinks().find(function (link) {
                    return link.id === service.connectorId;
                });
                var linkCellView = service.paper.findViewByModel(linkCell);

                linkCellView.startArrowheadMove('target');

                var p = service.paper.snapToGrid({
                    x: evt.originalEvent.x,
                    y: evt.originalEvent.y
                });

                linkCellView.pointermove(evt, p.x, p.y);
            }
        };

        service.mouseupListener = function (evt) {

            if (service.drawConnection) {
                service.drawConnection = false;
                var linkCell = service.graph.getLinks().find(function (link) {
                    return link.id === service.connectorId;
                });

                var linkCellView = service.paper.findViewByModel(linkCell);
                linkCellView.pointerup(evt);

                if (linkCell.getTargetElement() !== null) {
                    if (modelerStorage.getSelectedView() === 'SID') {
                        $rootScope.$broadcast('messageConnector:created');
                    }
                }

                /*if (linkCell.getTargetElement() === null) {
                    if (modelerStorage.getSelectedView() === 'SID') {
                        modelerStorage.removeFromSidJsonById(linkCellView.model.id);
                    } else {
                        modelerStorage.removeFromSbdJsonArrayBySubjectId(modelerStorage.getSelectedSubject().id, linkCellView.model.id);
                    }

                    linkCellView.removeBox();
                    linkCellView.remove();

                    $rootScope.$broadcast('link:removed');

                } else {
                    if (modelerStorage.getSelectedView() === 'SID') {
                        $rootScope.$broadcast('messageConnector:created');
                    }
                }*/

                service.connectorId = null;
            }
        };

        $window.onload = function () {
            $log.info('Page loaded');
            service.handleViewChange();
        };

        $window.onbeforeunload = function () {
            //modelerStorage.clear();
            if (modelerStorage.getSelectedView() === 'SID') {
                saveSidView();
            } else {
                saveSelectedSubject();
            }
        };

        return service;
    }

})();
