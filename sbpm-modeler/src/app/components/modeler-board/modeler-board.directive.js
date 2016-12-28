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
            self.activeObject = null;

            self.subjectElementDefaults = angular.copy(fabricConfig.getSubjectElementDefaults());;

            self.init = function () {

                $log.debug(TAG + 'init()');

                self.canvas = fabric.getCanvas();

                /*
                 * Listen for fabric 'object:selected' event
                 */
                self.canvas.on('object:selected', function (element) {

                    $log.debug(element.target);

                    $log.debug(TAG + 'object:selected');

                    fabric.objectSelectedListener(element);

                    /*
                     * Show custom control of active object
                     * Hide custom control of previously selected subject
                     */
                    if (self.activeObject === null) {
                        self.activeObject = element.target;
                        fabricCustomControl.setCustomControlVisibility(self.activeObject, true);
                    } else {
                        fabricCustomControl.setCustomControlVisibility(self.activeObject, false);

                        self.activeObject = element.target;
                        fabricCustomControl.setCustomControlVisibility(self.activeObject, true);
                    }

                });

                /*
                 * Listen for fabric 'selection:cleared' event
                 */
                self.canvas.on('selection:cleared', function (element) {

                    $log.debug(TAG + 'selection:cleared');
                    $log.debug(element.target);
                    /*
                     * Hide custom control of previously selected subject
                     */
                    if (self.activeObject !== null) {
                        fabricCustomControl.setCustomControlVisibility(self.activeObject, false);
                    }
                });
            };

            $scope.$on('canvas:created', self.init);

            $scope.$on('subject:removed', function () {
                self.activeObject = null;
            });

            self.onDrop = function (target, source, ev) {

                $log.debug(TAG + 'onDrop()');

                /*
                 * Add SubjectElement or StateElement to canvas
                 */
                if (source === 'subject-element') {
                    self.subjectElementDefaults.top = ev.originalEvent.y;
                    self.subjectElementDefaults.left = ev.originalEvent.x;

                    fabric.addSubjectElement(self.subjectElementDefaults, true);
                } else { //source === 'state-element'
                    //TODO: Add SubjectElement to canvas
                }
            }
        }
    }

})();
