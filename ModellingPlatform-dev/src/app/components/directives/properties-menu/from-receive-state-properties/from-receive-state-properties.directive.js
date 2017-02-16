(function () {
    'use strict';

    angular
        .module('components.directives')
        .directive('fromReceiveStateProperties', fromReceiveStateProperties);

    /** @ngInject */
    function fromReceiveStateProperties() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/directives/properties-menu/from-receive-state-properties/from-receive-state-properties.template.html',
            controller: FromReceiveStatePropertiesController,
            controllerAs: 'frsp',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function FromReceiveStatePropertiesController($log, $scope, $rootScope, joint, modelerStorage) {
            var self = this;

            self.paper = null;
            self.graph = null;
            self.fromReceiveState = {};

            self.availableSubjects = [];
            self.availableMessageTypes = [];

            self.selectedSubjectName = '';
            self.selectedMessageName = '';

            self.init = function () {

                self.paper = joint.getPaper();
                self.graph = joint.getGraph();

                //Listen for joint 'cell:pointerdown' event
                self.paper.on('cell:pointerdown', function (cellView, evt, x, y) {
                    if (cellView.model.attributes.type === 'sbd.FromReceiveStateConnectorElement') {
                        self.fromReceiveState = cellView.model.attributes;
                        self.clearAvailableData();
                        self.getAvailableData();
                    }
                });

                //Listen for joint 'add'
                self.graph.on('add', function (cell) {
                    if (cell.attributes.type === 'sbd.FromReceiveStateConnectorElement') {
                        self.fromReceiveState = cell.attributes;
                        self.clearAvailableData();
                        self.getAvailableData();
                    }
                });

                self.graph.on('remove', function (cell) {
                    if (cell.attributes.type === 'sbd.FromReceiveStateConnectorElement') {
                        self.clearAvailableData();
                    }
                })
            };

            $scope.$on('businessObject:created', function () {
                self.clearAvailableData();
                self.getAvailableData();
            });

            self.saveStateTransitionProperties = function () {
                $rootScope.$broadcast('stateTransition:changed', self.fromReceiveState);
            };

            self.getAvailableData = function () {
                self.availableSubjects = modelerStorage.getSubjectDisplayItems();
                self.availableMessageTypes = modelerStorage.getBusinessObjects();

                if (!_.isEmpty(self.fromReceiveState)) {
                    self.selectedSubjectName = self.fromReceiveState.customAttrs.fromSubject.display;
                    self.selectedMessageName = self.fromReceiveState.customAttrs.fromMessage.name;
                }

            };

            self.clearAvailableData = function () {
                self.availableSubjects = [];
                self.availableMessageTypes = [];
                self.selectedSubjectName = '';
                self.selectedMessageName = '';
            };

            self.changeSubject = function (sb) {
                self.fromReceiveState.customAttrs.fromSubject = {
                    id: sb.id,
                    value: sb.value,
                    display: sb.display
                };
            };

            self.changeMessage = function (msg) {
                self.fromReceiveState.customAttrs.fromMessage = {
                    id: msg.id,
                    name: msg.name
                };
            };

            /*self.safeApply = function (fn) {
                var phase = $scope.$root.$$phase;
                if (phase == '$apply' || phase == '$digest') {
                    if (fn && (typeof(fn) === 'function')) {
                        fn();
                    }
                } else {
                    $scope.$apply(fn);
                }
            };*/

            self.init();
        }
    }

})();
