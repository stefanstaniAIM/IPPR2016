(function () {
    'use strict';

    angular
        .module('components.directives')
        .directive('subjectProperties', subjectProperties);

    /** @ngInject */
    function subjectProperties() {

        var directive = {
            restrict: 'E',
            templateUrl: 'app/components/directives/properties-menu/subject-properties/subject-properties.template.html',
            controller: SubjectPropertiesController,
            controllerAs: 'sbp',
            bindToController: true
        };

        return directive;

        /** @ngInject */
        function SubjectPropertiesController($log, $rootScope, joint) {
            var self = this;

            self.paper = null;
            self.graph = null;
            self.subject = {};

            self.init = function () {

                self.paper = joint.getPaper();
                self.graph = joint.getGraph();

                //Listen for joint 'cell:pointerdown' event
                self.paper.on('cell:pointerdown', function (cellView, evt, x, y) {
                    if (cellView.model.attributes.type === 'sid.SubjectElement') {
                        self.subject = cellView.model.attributes;
                    }
                });

                //Listen for joint 'add'
                self.graph.on('add', function (cell) {
                    if (cell.attributes.type === 'sid.SubjectElement') {
                        self.subject = cell.attributes;
                    }
                })
            };

            self.saveSubjectProperties = function () {
                $rootScope.$broadcast('subject:changed', self.subject);
            };

            self.init();
        }
    }

})();
