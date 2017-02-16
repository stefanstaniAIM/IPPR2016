(function () {
    'use strict';

    angular
        .module('components.directives')
        .directive('fastNavigationMenu', fastNavigationMenu);

    /** @ngInject */
    function fastNavigationMenu() {

        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/directives/fast-navigation-menu/fast-navigation-menu.template.html',
            controller: FastNavigationMenuController,
            controllerAs: 'fnmc',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function FastNavigationMenuController($log, $scope, $rootScope, modelerStorage) {
            var self = this;

            self.selectedView = '';
            self.selectedSubject = {};
            self.subjectDisplayItem = {};
            self.subjectDisplayItems = [];

            self.init = function () {
                self.selectedView = modelerStorage.getSelectedView();
                self.selectedSubject = modelerStorage.getSelectedSubject();
                self.subjectDisplayItems = modelerStorage.getSubjectDisplayItems();

                if (!self.selectedSubjectEmpty()) {
                    self.subjectDisplayItem = {
                        id: self.selectedSubject.id,
                        value: self.selectedSubject.customAttrs.name.toLowerCase(),
                        display: self.selectedSubject.customAttrs.name
                    };
                }
            };

            $rootScope.$on('selectedView:changed', self.init);
            $rootScope.$on('selectedSubject:changed', function () {
                //self.init();
                self.safeApply(self.init);
            });

            self.safeApply = function(fn) {
                var phase = $scope.$root.$$phase;
                if(phase == '$apply' || phase == '$digest') {
                    if(fn && (typeof(fn) === 'function')) {
                        fn();
                    }
                } else {
                    $scope.$apply(fn);
                }
            };

            self.changeSelectedView = function () {
                modelerStorage.setSelectedView(modelerStorage.getSelectedView() === 'SID' ? 'SBD' : 'SID');
                $log.info('fastNavigationMenu - selectedView:changed');
                $rootScope.$broadcast('selectedView:changed');
            };

            self.selectedViewSID = function () {
                return self.selectedView === 'SID' ? true : false;
            };

            self.selectedSubjectEmpty = function () {
                return _.isEmpty(self.selectedSubject);
            };

            self.selectedItemChange = function (item) {

                if (typeof item !== 'undefined' && self.selectedSubject.id !== item.id) {
                    $log.info('fastNavigationMenu - selectedItemChange()');

                    $rootScope.$broadcast('selectedSubject:reselect', {id: item.id});
                }
            };

            self.querySearch = function (query) {
                var results = query ? self.subjectDisplayItems.filter(createFilterFor(query)) : self.subjectDisplayItems;
                return results;

            };

            var createFilterFor = function (query) {
                var lowercaseQuery = angular.lowercase(query);

                return function filterFn(state) {
                    return (state.value.indexOf(lowercaseQuery) === 0);
                };

            };

            self.init();
        }
    }

})();
