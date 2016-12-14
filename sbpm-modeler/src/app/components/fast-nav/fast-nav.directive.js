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

            $rootScope.$on('currentView-changed', function () {
                $log.debug(TAG + "currentView was changed");
                init();
            });

            var self = this;

            self.currentView;
            self.selectedItem = {"value":"alabama","display":"Alabama"};

            self.changeCurrentView = changeCurrentView;

            function init() {
                self.currentView = modeler.getCurrentView();
                $log.debug(TAG + "currentView " + self.currentView);
            }

            function changeCurrentView(currentView) {
                $log.debug(TAG + "Change currentView to " + self.currentView);
                modeler.setCurrentView(currentView);
                init();
            }

            self.simulateQuery = false;
            self.isDisabled    = false;
            self.noCache    = false;
            self.selectedItem = {"value":"alabama","display":"Alabama"};

            // list of `state` value/display objects
            self.states        = loadAll();
            self.querySearch   = querySearch;
            self.selectedItemChange = selectedItemChange;
            self.searchTextChange   = searchTextChange;

            self.newState = newState;

            function newState(state) {
                alert("Sorry! You'll need to create a Constitution for " + state + " first!");
            }

            function querySearch (query) {
                var results = query ? self.states.filter( createFilterFor(query) ) : self.states,
                    deferred;
                if (self.simulateQuery) {
                    deferred = $q.defer();
                    $timeout(function () { deferred.resolve( results ); }, Math.random() * 1000, false);
                    return deferred.promise;
                } else {
                    return results;
                }
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

                return allStates.split(/, +/g).map( function (state) {
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
