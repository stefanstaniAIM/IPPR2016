(function () {
    'use strict';

    angular
        .module('components.directives')
        .directive('propertiesMenu', propertiesMenu);

    /** @ngInject */
    function propertiesMenu() {

        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/directives/properties-menu/properties-menu.template.html',
            controller: PropertiesMenuController,
            controllerAs: 'pm',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function PropertiesMenuController($log, $mdSidenav, $scope, $rootScope, modelerStorage, joint) {
            var self = this;

            self.paper = null;
            self.graph = null;
            self.selectedView = null;
            self.currentlySelectedElement = null;

            self.init = function() {

                self.selectedView = modelerStorage.getSelectedView();
                self.paper = joint.getPaper();
                self.graph = joint.getGraph();

                //Listen for joint 'cell:pointerdown' event
                self.paper.on('cell:pointerdown', function (cellView, evt, x, y) {
                    self.isLoading = true;
                    self.currentlySelectedElement = cellView.model;
                    self.hideProperties();
                    self.showProperties();
                });

                //Listen for joint 'blank:pointerdown' event
                self.paper.on('blank:pointerdown', self.hideProperties);

                //Listen for joint 'add'
                self.graph.on('add', function(cell) {
                    self.isLoading = true;
                    self.currentlySelectedElement = cell;
                    self.hideProperties();
                    self.showProperties();
                });

                //Listen for joint 'remove'
                self.graph.on('remove', function (cell, collection, opt) {
                    self.hideProperties();
                });
            };

            $rootScope.$on('selectedView:changed', function () {
                self.hideProperties();
                self.init();
            });

            $rootScope.$on('link:removed', function () {
                self.hideProperties();
            });

            self.showProperties = function() {
                $mdSidenav('properties-menu').open();
            };

            self.hideProperties = function() {
                $mdSidenav('properties-menu').close();
            };

            self.isSelectedViewSID = function() {
                return self.selectedView === 'SID' ? true : false;
            };

            self.handleSidViewProperties = function() {
                if (self.currentlySelectedElement !== null) {
                    return self.currentlySelectedElement.attributes.type === 'sid.SubjectElement' ? true : false;
                }

                return false;
            };

            self.isState = function () {
                if (self.currentlySelectedElement !== null) {
                    if (self.currentlySelectedElement.attributes.type === 'sbd.SendStateElement' ||
                        self.currentlySelectedElement.attributes.type === 'sbd.ReceiveStateElement' ||
                        self.currentlySelectedElement.attributes.type === 'sbd.FunctionStateElement') {
                        return true;
                    }
                }

                return false;
            };

            self.isFromSendState = function () {
                if (self.currentlySelectedElement !== null) {
                    return self.currentlySelectedElement.attributes.type === 'sbd.FromSendStateConnectorElement' ? true : false;
                }

                return false;
            };

            self.isFromReceiveState = function () {
                if (self.currentlySelectedElement !== null) {
                    return self.currentlySelectedElement.attributes.type === 'sbd.FromReceiveStateConnectorElement' ? true : false;
                }

                return false;
            };

            self.isFromFunctionState = function () {
                if (self.currentlySelectedElement !== null) {
                    return self.currentlySelectedElement.attributes.type === 'sbd.FromFunctionStateConnectorElement' ? true : false;
                }

                return false;
            };

            self.propertiesOpened = function() {
                return $mdSidenav('properties-menu').isOpen();
            };

            self.closeProperties = function() {
                $mdSidenav('properties-menu').close();
            };

            self.init();
        }
    }

})();
