(function () {
    'use strict';

    angular
        .module('components.directives')
        .directive('fromFunctionStateProperties', fromFunctionStateProperties);

    /** @ngInject */
    function fromFunctionStateProperties() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/directives/properties-menu/from-function-state-properties/from-function-state-properties.template.html',
            controller: FromFunctionStatePropertiesController,
            controllerAs: 'ffsp',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function FromFunctionStatePropertiesController($log, $rootScope, joint) {
            var self = this;

            self.paper = null;
            self.graph = null;
            self.fromFunctionState = {};

            self.init = function () {

                self.paper = joint.getPaper();
                self.graph = joint.getGraph();

                //Listen for joint 'cell:pointerdown' event
                self.paper.on('cell:pointerdown', function (cellView, evt, x, y) {
                    if (cellView.model.attributes.type === 'sbd.FromFunctionStateConnectorElement') {
                        self.fromFunctionState = cellView.model.attributes;
                    }
                });

                //Listen for joint 'add'
                self.graph.on('add', function (cell) {
                    if (cell.attributes.type === 'sbd.FromFunctionStateConnectorElement') {
                        self.fromFunctionState = cell.attributes;
                    }
                });

                self.saveStateTransitionProperties = function () {
                    $rootScope.$broadcast('stateTransition:changed', self.fromFunctionState);
                };
            };

            self.init();
        }
    }

})();
