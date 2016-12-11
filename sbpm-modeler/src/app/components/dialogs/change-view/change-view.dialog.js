(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('changeViewDialog', changeViewDialog);

    /** @ngInject */
    function changeViewDialog($log, $mdDialog, modeler, $rootScope) {

        var TAG = 'change-view.dialog: ';

        return {
            showDialog: function () {
                $mdDialog.show({
                    controller: ChangeViewDialogController,
                    controllerAs: 'cvd',
                    templateUrl: 'app/components/dialogs/change-view/change-view.template.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: true
                })
                .then(function (answer) {

                })
                .catch(function () {

                });
            }
        };

        function ChangeViewDialogController() {
            var self = this;

            self.currentView;

            function init() {
                self.currentView = modeler.getCurrentView();
                $log.debug(TAG + "Current view " + self.currentView);
            }

            self.changeCurrentView = function(currentView) {
                $log.debug(TAG + "Change current view to " + self.currentView);
                modeler.setCurrentView(currentView);
                $rootScope.$emit('currentView-changed');
                init();
            };

            self.cancel = function() {
                $mdDialog.cancel();
            };

            init();
        }

    }

})();
