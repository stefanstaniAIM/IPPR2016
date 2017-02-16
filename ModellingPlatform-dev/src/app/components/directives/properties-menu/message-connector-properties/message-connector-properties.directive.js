(function () {
    'use strict';

    angular
        .module('components.directives')
        .directive('messageConnectorProperties', messageConnectorProperties);

    /** @ngInject */
    function messageConnectorProperties() {

        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/directives/properties-menu/message-connector-properties/message-connector-properties.template.html',
            controller: MessageConnectorPropertiesController,
            controllerAs: 'mcp',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function MessageConnectorPropertiesController($log, $rootScope, $scope, joint, $mdDialog, modelerStorage, $mdToast) {
            var self = this;

            self.paper = null;
            self.graph = null;
            self.messageConnectorModel = {};
            self.messageConnector = {};

            self.sourceSubjectName = 'Not defined';
            self.targetSubjectName = 'Not defined';

            self.init = function () {

                self.paper = joint.getPaper();
                self.graph = joint.getGraph();


                //Listen for joint 'cell:pointerdown' event
                self.paper.on('cell:pointerdown', function (cellView, evt, x, y) {
                    if (cellView.model.attributes.type === 'sid.MessageConnectorElement') {
                        self.messageConnectorModel = cellView.model;
                        self.messageConnector = cellView.model.attributes;
                        self.getSourceTargetNames();
                    }
                });

                //Listen for joint 'add'
                self.graph.on('add', function (cell) {
                    if (cell.attributes.type === 'sid.MessageConnectorElement') {
                        self.messageConnectorModel = cell;
                        self.messageConnector = cell.attributes;
                        self.getSourceTargetNames();
                    }
                });
            };

            $scope.$on('messageConnector:created', function () {
                self.safeApply(self.getSourceTargetNames);
            });

            self.isBidirectionalChanged = function () {
                self.paper.findViewByModel(self.messageConnectorModel).updateConnector();

                if (!self.messageConnector.customAttrs.isBidirectional) {
                    self.messageConnector.customAttrs.targetToSource.messageTypes = [];
                }
            };

            self.getSourceTargetNames = function () {
                var sourceSubject = self.messageConnectorModel.getSourceElement();
                var targetSubject = self.messageConnectorModel.getTargetElement();

                self.sourceSubjectName = sourceSubject.get('customAttrs').name;

                if (targetSubject !== null) {
                    self.targetSubjectName = targetSubject.get('customAttrs').name;
                }
            };

            self.saveMessageConnectorProperties = function () {
                $rootScope.$broadcast('messageConnector:changed', self.messageConnector);
            };

            self.safeApply = function (fn) {
                var phase = $scope.$root.$$phase;
                if (phase == '$apply' || phase == '$digest') {
                    if (fn && (typeof(fn) === 'function')) {
                        fn();
                    }
                } else {
                    $scope.$apply(fn);
                }
            };

            self.removeMessageType = function (isSourceToTarget, messageType) {

                if (isSourceToTarget) {
                    var arrayObject = self.messageConnector.customAttrs.sourceToTarget.messageTypes.find(function (el) {
                        return el.id === messageType.id;
                    });
                    if (typeof arrayObject !== 'undefined') {
                        var arrayObjectIndex = self.messageConnector.customAttrs.sourceToTarget.messageTypes.indexOf(arrayObject);
                        self.messageConnector.customAttrs.sourceToTarget.messageTypes.splice(arrayObjectIndex, 1);
                    }
                } else {
                    var arrayObject = self.messageConnector.customAttrs.targetToSource.messageTypes.find(function (el) {
                        return el.id === messageType.id;
                    });
                    if (typeof arrayObject !== 'undefined') {
                        var arrayObjectIndex = self.messageConnector.customAttrs.targetToSource.messageTypes.indexOf(arrayObject);
                        self.messageConnector.customAttrs.targetToSource.messageTypes.splice(arrayObjectIndex, 1);
                    }
                }
            };

            self.showSelectMessageTypeDialog = function (isSourceToTarget) {
                $mdDialog.show({
                    controller: SelectMessageTypeController,
                    controllerAs: 'smt',
                    templateUrl: 'app/components/directives/properties-menu/message-connector-properties/dialog-templates/select-message-type.template.html',
                    clickOutsideToClose: true,
                    locals: {
                        isSourceToTarget: isSourceToTarget
                    }
                });
            };

            var SelectMessageTypeController = function (isSourceToTarget) {
                var smt = this;

                smt.availableMessageTypes = [];

                smt.init = function () {
                    smt.availableMessageTypes = modelerStorage.getBusinessObjects();
                };

                smt.addMessageType = function (messageType) {

                    if (isSourceToTarget) {
                        var arrayObject = self.messageConnector.customAttrs.sourceToTarget.messageTypes.find(function (el) {
                            return el.id === messageType.id;
                        });
                        if (typeof arrayObject === 'undefined') {
                            self.messageConnector.customAttrs.sourceToTarget.messageTypes.push(messageType);
                        } else {
                            smt.showSimpleToast();
                        }
                    } else {
                        var arrayObject = self.messageConnector.customAttrs.targetToSource.messageTypes.find(function (el) {
                            return el.id === messageType.id;
                        });
                        if (typeof arrayObject === 'undefined') {
                            self.messageConnector.customAttrs.targetToSource.messageTypes.push(messageType);
                        } else {
                            smt.showSimpleToast();
                        }
                    }
                };

                smt.cancel = function () {
                    $mdDialog.cancel();
                };

                smt.messageTypesAvailable = function () {
                    return !_.isEmpty(smt.availableMessageTypes);
                };

                smt.showSimpleToast = function () {

                    $mdToast.show(
                        $mdToast.simple()
                            .textContent('This message type is already added!')
                            .hideDelay(2000)
                            .position('top right')
                    );
                };

                smt.init();
            };

            self.init();
        }
    }

})();
