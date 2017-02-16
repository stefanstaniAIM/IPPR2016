(function () {
    'use strict';

    angular
        .module('components.directives')
        .directive('navigationMenu', navigationMenu);

    /** @ngInject */
    function navigationMenu() {

        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/directives/navigation-menu/navigation-menu.template.html',
            controller: NavigationMenuController,
            controllerAs: 'nmc',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function NavigationMenuController($log, $window, $mdDialog, $mdSidenav, $mdToast, $rootScope, modelerStorage, joint, owlService) {
            var self = this;

            self.selectedView = null;

            self.init = function () {
                self.selectedView = modelerStorage.getSelectedView();
            };

            $rootScope.$on('selectedView:changed', self.init);

            self.selectedViewSID = function () {
                return self.selectedView === 'SID' ? true : false;
            };

            self.sideNavOpened = function () {
                return $mdSidenav('navigation-menu').isOpen();
            };

            self.toggleSidenav = function () {
                $mdSidenav('navigation-menu').toggle();
            };

            self.downloadOwlFile = function () {
                if (_.isEmpty(modelerStorage.getSidJson().cells)) {
                    self.showSimpleToast();
                } else {
                    self.showDownloadOwlFileDialog();
                }
            };

            self.showDownloadOwlFileDialog = function() {
                var confirm = $mdDialog.prompt()
                    .title('Enter a process name')
                    .textContent('e.g VacationRequest')
                    .placeholder('Process name')
                    .ariaLabel('Process name')
                    .ok('Download')
                    .cancel('Cancel');

                $mdDialog.show(confirm).then(function(result) {
                    owlService.downloadOwlFile(result);
                });
            };

            self.showSimpleToast = function () {

                $mdToast.show(
                    $mdToast.simple()
                        .textContent('The modeled process needs to contain at least one object!')
                        .hideDelay(2000)
                        .position('top right')
                );
            };

            self.showNewFileDialog = function (ev) {
                var confirm = $mdDialog.confirm()
                    .title('Would you like to create a new file?')
                    .textContent('The changes you made will not be saved.')
                    .clickOutsideToClose(true)
                    .targetEvent(ev)
                    .ok('Confirm')
                    .cancel('Cancel');

                $mdDialog.show(confirm)
                    .then(function () {
                        modelerStorage.clear();
                        $window.location.reload();
                    })
            };

            self.showViewMessageTypesDialog = function (ev) {
                $mdDialog.show({
                    controller: ViewMessageTypesController,
                    controllerAs: 'vmtc',
                    templateUrl: 'app/components/directives/navigation-menu/dialog-templates/view-message-types.template.html',
                    targetEvent: ev,
                    clickOutsideToClose: true
                });
            };

            var ViewMessageTypesController = function () {
                var vmtc = this;

                vmtc.businessObjects = [];

                vmtc.init = function () {
                    vmtc.businessObjects = modelerStorage.getBusinessObjects();
                };

                vmtc.cancel = function () {
                    $mdDialog.cancel();
                };

                vmtc.showCreateNewMessageTypeDialog = function () {
                    self.showCreateNewMessageTypeDialog();
                };

                vmtc.showUpdateMessageTypeDialog = function (bo) {
                    self.showUpdateMessageTypeDialog(bo);
                };

                vmtc.businessObjectsAvailable = function () {
                    return _.isEmpty(vmtc.businessObjects);
                };

                vmtc.removeBusinessObject = function (boId) {
                    modelerStorage.removeBusinessObject(boId);
                    vmtc.init();
                };

                vmtc.init();
            };

            self.showCreateNewMessageTypeDialog = function (ev) {
                $mdDialog.show({
                    controller: CreateNewMessageTypeController,
                    controllerAs: 'cnmt',
                    templateUrl: 'app/components/directives/navigation-menu/dialog-templates/create-new-message-type.template.html',
                    targetEvent: ev,
                    clickOutsideToClose: true
                });
            };

            var CreateNewMessageTypeController = function () {
                var cnmt = this;

                cnmt.newBusinessObject = {};

                cnmt.addBusinessObject = function () {
                    cnmt.newBusinessObject.id = createId();
                    modelerStorage.addBusinessObject(cnmt.newBusinessObject);
                    $rootScope.$broadcast('businessObject:created');
                    self.showViewMessageTypesDialog();
                };

                cnmt.cancel = function () {
                    $mdDialog.cancel();
                };
            };

            var createId = function () {
                return Math.random().toString(36).substr(2, 9)
            };

            self.showUpdateMessageTypeDialog = function (businessObject) {
                $mdDialog.show({
                    controller: UpdateMessageTypeController,
                    controllerAs: 'umt',
                    templateUrl: 'app/components/directives/navigation-menu/dialog-templates/update-message-type.template.html',
                    //targetEvent: ev,
                    clickOutsideToClose: true,
                    locals : {
                        businessObject : businessObject
                    }
                });
            };

            var UpdateMessageTypeController = function (businessObject) {
                var umt = this;

                umt.businessObject = {};

                umt.init = function () {
                    umt.businessObject = {
                        id: businessObject.id,
                        name: businessObject.name
                    };
                };

                umt.updateBusinessObject = function () {
                    modelerStorage.addBusinessObject(umt.businessObject);
                    self.showViewMessageTypesDialog();
                };

                umt.cancel = function () {
                    $mdDialog.cancel();
                    self.showViewMessageTypesDialog();
                };

                umt.init();
            };

            self.init();
        }
    }

})();
