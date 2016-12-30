(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('fastNav', fastNav);

    /** @ngInject */
    function fastNav() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/fast-nav/fast-nav.template.html',
            controller: FastNavController,
            controllerAs: 'fn',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function FastNavController($log, modeler, $rootScope) {
            var TAG = 'fast-nav.directive: ';

            var self = this;

            var currentView;

            self.changeCurrentView = changeCurrentView;
            self.isCurrentViewSID = isCurrentViewSID;
            self.selectedItem = {"value": "alabama", "display": "Alabama"};

            function init() {
                currentView = modeler.getCurrentView();
                $log.debug(TAG + "successfully initiated");
            }

            $rootScope.$on('currentView:changed', function () {
                $log.debug(TAG + "currentView was changed");
                $log.debug(TAG + "update view");
                init();
            });

            function changeCurrentView() {
                modeler.setCurrentView(modeler.getCurrentView() === 'SID' ? 'SBD' : 'SID');
                $rootScope.$emit('currentView:changed');
                init();
            }

            function isCurrentViewSID() {
                return currentView === 'SID' ? true : false;
            }

            self.simulateQuery = false;
            self.isDisabled = false;
            self.noCache = false;
            self.selectedItem = {"value": "alabama", "display": "Alabama"};

            // list of `state` value/display objects
            self.states = loadAll();
            self.querySearch = querySearch;
            self.selectedItemChange = selectedItemChange;
            self.searchTextChange = searchTextChange;

            self.newState = newState;

            function newState(state) {
                //
            }

            function querySearch(query) {
                var results = query ? self.states.filter(createFilterFor(query)) : self.states;
                return results;

            }

            function searchTextChange(text) {
                $log.info('Text changed to ' + text);
            }

            function selectedItemChange(item) {
                $log.info('Item changed to ' + JSON.stringify(item));
            }

            /**
             * Build `states` list of key/value pairs
             */
            function loadAll() {
                var allStates = 'Alabama, Alaska, Arizona, Arkansas, California, Colorado, Connecticut, Delaware,\
              Florida, Georgia, Hawaii, Idaho, Illinois, Indiana, Iowa, Kansas, Kentucky, Louisiana,\
              Maine, Maryland, Massachusetts, Michigan, Minnesota, Mississippi, Missouri, Montana,\
              Nebraska, Nevada, New Hampshire, New Jersey, New Mexico, New York, North Carolina,\
              North Dakota, Ohio, Oklahoma, Oregon, Pennsylvania, Rhode Island, South Carolina,\
              South Dakota, Tennessee, Texas, Utah, Vermont, Virginia, Washington, West Virginia,\
              Wisconsin, Wyoming';

                return allStates.split(/, +/g).map(function (state) {
                    return {
                        value: state.toLowerCase(),
                        display: state
                    };
                });
            }

            /**
             * Create filter function for a query string
             */
            function createFilterFor(query) {
                var lowercaseQuery = angular.lowercase(query);

                return function filterFn(state) {
                    return (state.value.indexOf(lowercaseQuery) === 0);
                };

            }

            init();

        }
    }

})();
