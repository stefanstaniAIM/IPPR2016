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
        function ModelerBoardController($scope, $log, fabric, fabricConfig, fabricCustomControl, modeler) {
            var TAG = 'modeler-board.directive: ';

            var self = this;

            self.canvas = null;

            self.subjectElementDefaults = angular.copy(fabricConfig.getSubjectElementDefaults());

            self.init = function () {

                $log.debug(TAG + 'init()');

                self.canvas = fabric.getCanvas();

                /*
                 * Listen for fabric 'object:selected' event
                 */
                self.canvas.on('object:selected', function (element) {

                    $log.debug(TAG + 'object:selected');

                    $log.debug(element.target);

                    fabric.objectSelectedListener(element.target);

                    /*
                     * Show custom control of active object
                     * Hide custom control of previously selected subject
                     */
                    if (modeler.getActiveObjectId() === '') {
                        modeler.setActiveObjectId(element.target.id);
                        fabricCustomControl.setCustomControlVisibility(modeler.getActiveObjectId(), true);
                    } else {
                        fabricCustomControl.setCustomControlVisibility(modeler.getActiveObjectId(), false);

                        modeler.setActiveObjectId(element.target.id);
                        fabricCustomControl.setCustomControlVisibility(modeler.getActiveObjectId(), true);
                    }

                });

                /*
                 * Listen for fabric 'selection:cleared' event
                 */
                self.canvas.on('selection:cleared', function (element) {

                    $log.debug(TAG + 'selection:cleared');

                    fabric.selectionClearedListener();

                    /*
                     * Hide custom control of previously selected subject
                     */
                    if (modeler.getActiveObjectId() !== '') {
                        fabricCustomControl.setCustomControlVisibility(modeler.getActiveObjectId(), false);
                    }
                });
            };

            $scope.$on('canvas:created', self.init);

            /*
             * Add SubjectElement or StateElement to canvas
             */
            self.onDrop = function (target, source, ev) {

                $log.debug(TAG + 'onDrop()');

                if (source === 'subject-element') {

                    self.newSubjectElement(ev);

                } else { //source === 'state-element'
                    //TODO: Add SubjectElement to canvas
                }
            };

            /*
             * SubjectElement
             */
            self.newSubjectElement = function (ev) {

                $log.debug(TAG + 'newSubjectElement()');

                fabric.setConnectorMode(true);

                self.subjectElementDefaults.top = ev.originalEvent.y;
                self.subjectElementDefaults.left = ev.originalEvent.x;

                var object = fabric.addSubjectElement(self.subjectElementDefaults, true);

                //fabric.setActiveObject(object);
            };
        }
    }

})();
