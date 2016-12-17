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

            var currentView;

            self.cancel = cancel;
            self.changeCurrentView = changeCurrentView;
            self.isCurrentViewSID = isCurrentViewSID;

            function init() {
                currentView = modeler.getCurrentView();
                $log.debug(TAG + "successfully initiated");
            }

            function changeCurrentView() {
                modeler.setCurrentView(modeler.getCurrentView() === 'SID' ? 'SBD' : 'SID');
                $rootScope.$emit('currentView-changed');
                init();
            }

            $rootScope.$on('currentView-changed', function () {
                $log.debug(TAG + "currentView was changed");
                $log.debug(TAG + "update view");
                init();
            });

            function cancel() {
                $mdDialog.cancel();
            }

            function isCurrentViewSID() {
                return currentView === 'SID' ? true : false;
            }

            init();
        }

    }

})();
