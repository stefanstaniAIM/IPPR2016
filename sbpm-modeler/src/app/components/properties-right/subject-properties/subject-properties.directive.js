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
        function SubjectPropertiesController($log, fabric, $scope) {
            var TAG = 'sbuject-properties.directive: ';

            var self = this;

            self.canvas = null;
            self.activeObject = null;

            self.init = function () {

                $log.debug(TAG + 'init()');

                self.canvas = fabric.getCanvas();

                /*
                 * Listen for fabric 'object:selected' event
                 */
                self.canvas.on('object:selected', function (element) {
                    self.activeObject = {
                        name: element.target.get('name'),
                        startSubject: element.target.get('startSubject'),
                        multiSubject: element.target.get('multiSubject')
                    };
                });

                /*
                 * Listen for fabric 'selection:cleared' event
                 */
                self.canvas.on('selection:cleared', function (element) {
                    //self.activeObject = null;
                });
            };

            //$scope.$on('canvas:created', self.init);

            self.saveSubjectProperties = function () {
                fabric.getActiveObject().setCustomAttributes(self.activeObject);
                self.canvas.renderAll();
            };

            self.init();
        }
    }

})();
