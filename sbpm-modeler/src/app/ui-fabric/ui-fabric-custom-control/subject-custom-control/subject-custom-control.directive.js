(function () {
    'use strict';

    angular
        .module('ui.fabric')
        .directive('subjectCustomControl', subjectCustomControl);

    /** @ngInject */
    function subjectCustomControl() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/ui-fabric/ui-fabric-custom-control/subject-custom-control/subject-custom-control.template.html',
            controller: SubjectCustomControlController,
            controllerAs: 'cc',
            bindToController: true,
            scope: {
                customControlId: '@customControlId'
            }
        };

        return directive;

        /** @ngInject */
        function SubjectCustomControlController($log, fabric) {
            var TAG = "ui-fabric-custom-control.directive: ";

            var self = this;

            $log.debug(TAG + self.customControlId);

            self.deleteSubjectElement = function () {
                $log.debug(TAG + 'deleteSubjectElement()');
                fabric.removeActiveObjectFromCanvas();
            }

        }
    }
})();
