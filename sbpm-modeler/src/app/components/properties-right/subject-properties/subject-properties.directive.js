(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('subjectProperties', subjectProperties);

    /** @ngInject */
    function subjectProperties() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/properties-right/subject-properties/subject-properties.template.html',
            controller: SubjectPropertiesController,
            controllerAs: 'sbp',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function SubjectPropertiesController($log, fabric) {
            var TAG = 'sbuject-properties.directive: ';

            var self = this;

            self.canvas = null;
            self.activeObject = null;

            var init = function () {

                $log.debug(TAG + 'init()');

                self.canvas = fabric.getCanvas();

                /*
                 * Listen for fabric 'object:selected' event
                 */
                self.canvas.on('object:selected', function (element) {
                    self.activeObject = element.target;
                    $log.debug(self.activeObject);
                });

                /*
                 * Listen for fabric 'selection:cleared' event
                 */
                self.canvas.on('selection:cleared', function (element) {
                    self.activeObject = null;
                });
            };

            init();

            self.saveSubjectProperties = function () {
                $log.debug(self.activeObject);
            };
        }
    }

})();
