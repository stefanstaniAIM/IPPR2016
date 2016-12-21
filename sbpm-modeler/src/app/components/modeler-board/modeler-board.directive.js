(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('modelerBoard', modelerBoard);

    /** @ngInject */
    function modelerBoard() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/modeler-board/modeler-board.template.html',
            controller: ModelerBoardController,
            controllerAs: 'mb',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function ModelerBoardController($scope, $log, fabric, fabricConfig) {
            var TAG = 'modeler-board.directive: ';

            var self = this;

            self.canvas = null;

            self.onDrop = onDrop;

            self.nodeDefaults = angular.copy(fabricConfig.getRectWithTextDefaults());

            self.init = function () {

                $log.debug(TAG + 'init()');

                self.canvas = fabric.getCanvas();

                self.nodeDefaults.left = 500;

                fabric.addRectWithText('test', self.nodeDefaults);
            };

            $scope.$on('canvas:created', self.init);

            function onDrop(target, source, ev) {
                $log.debug("dropped " + source + " on " + target);
                $log.debug(ev.originalEvent.x + " " + ev.originalEvent.y);
                self.nodeDefaults.top = ev.originalEvent.y;
                self.nodeDefaults.left = ev.originalEvent.x;
                fabric.addRectWithText('test', self.nodeDefaults);
            }

        }
    }

})();
