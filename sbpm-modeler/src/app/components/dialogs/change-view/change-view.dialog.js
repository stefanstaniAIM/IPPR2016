(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .factory('changeViewDialog', changeViewDialog);

    /** @ngInject */
    function changeViewDialog($log, $mdDialog, modeler, $rootScope) {

        return {
            showDialog: function () {
                $mdDialog.show({
                    controller: ChangeViewDialogController,
                    controllerAs: 'cvd',
                    templateUrl: 'app/components/dialogs/change-view/change-view.template.html',
                    parent: angular.element(document.body),
                    clickOutsideToClose: true
                });
            }
        };

        function ChangeViewDialogController() {

            var TAG = 'change-view.dialog: ';

            var self = this;

            self.currentView = null;

            self.init = function() {

                $log.debug(TAG + "init()");

                self.currentView = modeler.getCurrentView();
            };

            $rootScope.$on('currentView:changed', function () {

                $log.debug(TAG + "currentView:changed");

                $mdDialog.hide();
                self.init();
            });

            self.cancel = function() {

                $log.debug(TAG + "cancel()");

                $mdDialog.cancel();
            };

            self.changeCurrentView = function() {

                $log.debug(TAG + "changeCurrentView()");

                modeler.setCurrentView(modeler.getCurrentView() === 'SID' ? 'SBD' : 'SID');
                $rootScope.$emit('currentView:changed');
                self.init();
            };

            self.isCurrentViewSID = function() {
                return self.currentView === 'SID' ? true : false;
            };

            self.init();
        }

    }

})();
