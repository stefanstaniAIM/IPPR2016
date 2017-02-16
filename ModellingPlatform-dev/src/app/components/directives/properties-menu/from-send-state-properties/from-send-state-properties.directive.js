(function () {
    'use strict';

    angular
        .module('components.directives')
        .directive('fromSendStateProperties', fromSendStateProperties);

    /** @ngInject */
    function fromSendStateProperties() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/directives/properties-menu/from-send-state-properties/from-send-state-properties.template.html',
            controller: fromSendStatePropertiesController,
            controllerAs: 'fssp',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function fromSendStatePropertiesController($log, $scope, $rootScope, joint, modelerStorage) {
            var self = this;

            self.paper = null;
            self.graph = null;
            self.fromSendState = {};

            self.availableSubjects = [];
            self.availableMessageTypes = [];

            self.selectedSubjectName = '';
            self.selectedMessageName = '';

            self.init = function () {

                self.paper = joint.getPaper();
                self.graph = joint.getGraph();

                //Listen for joint 'cell:pointerdown' event
                self.paper.on('cell:pointerdown', function (cellView, evt, x, y) {
                    if (cellView.model.attributes.type === 'sbd.FromSendStateConnectorElement') {
                        self.fromSendState = cellView.model.attributes;
                        self.clearAvailableData();
                        self.getAvailableData();
                    }
                });

                //Listen for joint 'add'
                self.graph.on('add', function (cell) {
                    if (cell.attributes.type === 'sbd.FromSendStateConnectorElement') {
                        self.fromSendState = cell.attributes;
                        self.clearAvailableData();
                        self.getAvailableData();
                    }
                });

                self.graph.on('remove', function (cell) {
                    if (cell.attributes.type === 'sbd.FromSendStateConnectorElement') {
                        self.clearAvailableData();
                    }
                })
            };

            $scope.$on('businessObject:created', function () {
                self.clearAvailableData();
                self.getAvailableData();
            });

            self.saveStateTransitionProperties = function () {
                $rootScope.$broadcast('stateTransition:changed', self.fromSendState);
            };

            self.getAvailableData = function () {
                self.availableSubjects = modelerStorage.getSubjectDisplayItems();
                self.availableMessageTypes = modelerStorage.getBusinessObjects();

                if (!_.isEmpty(self.fromSendState)) {
                    self.selectedSubjectName = self.fromSendState.customAttrs.toSubject.display;
                    self.selectedMessageName = self.fromSendState.customAttrs.toMessage.name;
                }

            };

            self.clearAvailableData = function () {
                self.availableSubjects = [];
                self.availableMessageTypes = [];
                self.selectedSubjectName = '';
                self.selectedMessageName = '';
            };

            self.changeSubject = function (sb) {
                self.fromSendState.customAttrs.toSubject = {
                    id: sb.id,
                    value: sb.value,
                    display: sb.display
                };
            };

            self.changeMessage = function (msg) {
                self.fromSendState.customAttrs.toMessage = {
                    id: msg.id,
                    name: msg.name
                };
            };

            self.init();
        }
    }

})();
