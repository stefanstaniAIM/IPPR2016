(function () {
    'use strict';

    angular
        .module('components.directives')
        .directive('stateProperties', stateProperties);

    /** @ngInject */
    function stateProperties() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/directives/properties-menu/state-properties/state-properties.template.html',
            controller: StatePropertiesController,
            controllerAs: 'stp',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function StatePropertiesController($log, $rootScope, joint) {
            var self = this;

            self.paper = null;
            self.graph = null;
            self.state = {};

            self.init = function () {

                self.paper = joint.getPaper();
                self.graph = joint.getGraph();

                //Listen for joint 'cell:pointerdown' event
                self.paper.on('cell:pointerdown', function (cellView, evt, x, y) {
                    if (cellView.model.attributes.type === 'sbd.SendStateElement' ||
                        cellView.model.attributes.type === 'sbd.ReceiveStateElement' ||
                        cellView.model.attributes.type === 'sbd.FunctionStateElement') {
                        self.state = cellView.model.attributes;
                    }
                });

                //Listen for joint 'add'
                self.graph.on('add', function (cell) {
                    if (cell.attributes.type === 'sbd.SendStateElement' ||
                        cell.attributes.type === 'sbd.ReceiveStateElement' ||
                        cell.attributes.type === 'sbd.FunctionStateElement') {
                    self.state = cell.attributes;
                    }
                })
            };

            self.setStartStateToFalse = function () {
                self.state.customAttrs.startState = false;
            };

            self.setEndStateToFalse = function () {
                self.state.customAttrs.endState = false;
            };

            self.saveStateProperties = function () {
                $rootScope.$broadcast('state:changed', self.state);
            };

            self.init();
        }
    }

})();
