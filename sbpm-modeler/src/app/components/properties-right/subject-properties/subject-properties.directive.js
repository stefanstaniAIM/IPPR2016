(function () {
    'use strict';

    angular
        .module('sbpm-modeler')
        .directive('subjectProperties', subjectProperties);

    /** @ngInject */
    function subjectProperties() {
        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/properties-right/subject-properties/subject-properties.template.html',
            controller: SubjectPropertiesController,
            controllerAs: 'sbp',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function SubjectPropertiesController($log) {
            var self = this;

        }
    }

})();
