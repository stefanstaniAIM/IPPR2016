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
        function SubjectCustomControlController($log, fabric, fabricCustomControl, $rootScope, modeler) {
            var TAG = "subject-custom-control.directive: ";

            var self = this;

            self.removeSubjectElement = function () {

                $log.debug(TAG + 'deleteSubjectElement()');

                var activeObjectId = fabric.getActiveObject().get('id');

                fabric.removeActiveObjectFromCanvas();
                modeler.setActiveObjectId('');
                fabricCustomControl.removeCustomControl(activeObjectId);
            };

            self.addMessageConnector = function () {

                $log.debug(TAG + 'addMessageConnector()');

                //fabric.setConnectorMode(false);

                fabric.drawConnection();
            };
        }
    }
})();
