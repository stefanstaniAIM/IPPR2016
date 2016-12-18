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

            self.nodeDefaults = angular.copy(fabricConfig.getRectWithTextDefaults());

            self.init = function () {

                $log.debug(TAG + 'init()');

                self.canvas = fabric.getCanvas();

                self.nodeDefaults.left = 500;

                fabric.addRectWithText('test', self.nodeDefaults);
            };

            $scope.$on('canvas:created', self.init);
        }
    }

})();
