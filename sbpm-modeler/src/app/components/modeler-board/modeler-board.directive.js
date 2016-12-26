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
        function ModelerBoardController($scope, $log, fabric, fabricConfig, fabricCustomControl) {
            var TAG = 'modeler-board.directive: ';

            var self = this;

            self.canvas = null;
            self.selectedObject = null;

            self.onDrop = onDrop;

            self.nodeDefaults = angular.copy(fabricConfig.getSubjectElementDefaults());

            self.init = function () {
                $log.debug(TAG + 'init()');
                self.canvas = fabric.getCanvas();

                /*
                 * Listen for Fabric 'object:selected' event
                 */

                self.canvas.on('object:selected', function(element) {

                    $log.debug(TAG + 'object:selected');
                    $log.debug(TAG + 'Element: ' + element.target.get('id'));
                    $log.debug(TAG + 'ActiveObject: ' + self.selectedObject);

                    if (self.selectedObject === null) {
                        self.selectedObject = element.target;
                        fabricCustomControl.setCustomControlVisibility(self.selectedObject, false);
                    } else {
                        fabricCustomControl.setCustomControlVisibility(self.selectedObject, true);

                        self.selectedObject = element.target;
                        fabricCustomControl.setCustomControlVisibility(self.selectedObject, false);
                    }

                });

                /*
                 * Listen for Fabric 'selection:cleared' event
                 */

                self.canvas.on('selection:cleared', function(element) {

                    $log.debug(TAG + 'selection:cleared');

                    fabricCustomControl.setCustomControlVisibility(self.selectedObject, true);
                });
            };

            $scope.$on('canvas:created', self.init);

            function onDrop(target, source, ev) {
                $log.debug("dropped " + source + " on " + target);
                $log.debug(ev.originalEvent.x + " " + ev.originalEvent.y);
                self.nodeDefaults.top = ev.originalEvent.y;
                self.nodeDefaults.left = ev.originalEvent.x;
                fabric.addSubjectElement(self.nodeDefaults);
            }

        }
    }

})();
